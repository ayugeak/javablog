package javablog.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javablog.bean.*;
import javablog.database.DBConnectionPool;
import javablog.util.ConfigProperty;
import javablog.util.StrFilter;

public class ShowArticle extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public ShowArticle() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		String articleIdText = request.getParameter("articleId");//article id
		String pageNowText = request.getParameter("pageNow");//page now
		
		StrFilter sf = new StrFilter();
		int articleId = sf.parseInt(articleIdText);
		int pageNow = sf.parseInt(pageNowText);
		if (pageNow == 0) {
			pageNow = 1;
		}
		
		boolean flag = false;
		if ((pageNow > 0) && (articleId > 0)){
			Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
			if (connection != null){
				ArticleBeanBo abb = new ArticleBeanBo(connection);
				ArticleBean ab = abb.getArticle(articleId);
				if (ab != null && ab.getStatus() == ConfigProperty.STATUS_NORMAL) {
					//确认文章分类和作者正常（status=normal）
					CategoryBeanBo catbb = new CategoryBeanBo(connection);
					CategoryBean cb = catbb.getCategory(ab.getCategoryId());
					UserBeanBo ubb = new UserBeanBo(connection);
					UserBean ub = ubb.getUser(ab.getAuthorId());
					
					if (cb != null && cb.getStatus() == ConfigProperty.STATUS_NORMAL
							&& ub != null && ub.getStatus() == ConfigProperty.STATUS_NORMAL){
						// 获取所有标签
						TagBeanBo tbb = new TagBeanBo(connection);
						tbb.setFilter(ConfigProperty.STATUS_NORMAL + "",
								"SELECT tag_id FROM article_tags WHERE article_id=" + ab.getArticleId(),
								null, null);
						ArrayList<TagBean> tbs = tbb.getTags(0);
						
						// 获取相关文章
						ArticleTagBeanBo atbb = new ArticleTagBeanBo(connection);
						ArrayList<MetaModel> almm = atbb.getRelatedArticles(articleId);
						// 获取所有评论
						CommentBeanBo cbb = new CommentBeanBo(connection);
						cbb.setPageSize(Integer.parseInt(ConfigProperty.configCtrl.getConfigValue("front_comment_page_size")));
						cbb.setFilter(ConfigProperty.STATUS_NORMAL, ab.getArticleId(), 0,
								"comment_id", "desc");
						//将评论总数放在文章的status字段中
						ab.setStatus(cbb.getRowCount());
						ArrayList<CommentBean> al = cbb.getComments(pageNow);
						// 设置评论者头像，把头像地址放在评论的Email属性中
						CommentBean commentBean;
						String defaultAvatar = "images/avatar.png";
						for (int i = 0; i < al.size(); i++) {
							commentBean = al.get(i);
							if (commentBean.getUserId() > 0) {//网站注册者的有自己的专有头像
								ub = ubb.getUser(commentBean.getUserId());
								commentBean.setEmail(ub.getAvatar());
							} else {
								commentBean.setEmail(defaultAvatar);
							}
						}
						
						ResultBean result = new ResultBean();
						result.setPageNow(pageNow);
						result.setRowCount(cbb.getRowCount());
						result.setPageCount(cbb.getPageCount());
						result.setUrl("article/" + articleId + "/");
						
						request.setAttribute("result", result);
						// 找到文章，将文章信息绑定在request中
						request.setAttribute("article", ab);
						request.setAttribute("relatedArticleList", almm);
			
						MetaModel pab = abb.getPreviousArticleMeta(articleId - 1);// 上一篇文章
						MetaModel nab = abb.getNextArticleMeta(articleId + 1);// 下一篇文章
						// 找到前后文章，将文章信息绑定在request中
						request.setAttribute("previous", pab);
						request.setAttribute("next", nab);
						request.setAttribute("tagList", tbs);
						request.setAttribute("commentList", al);
						request.setAttribute("author", ub);
						request.setAttribute("category", cb);
						flag = true;
					}
					abb.closeConnection();
				}
			}
		}
		
		if (flag){
			request.getRequestDispatcher("article.jsp").forward(request,
					response);
		} else {
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

	public void init() throws ServletException {

	}

}
