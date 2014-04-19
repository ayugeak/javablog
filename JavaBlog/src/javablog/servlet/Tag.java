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

public class Tag extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public Tag() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		String tagIdText = request.getParameter("tagId");//tag id
		String pageNowText = request.getParameter("pageNow");//page now
		
		StrFilter sf = new StrFilter();
		int tagId = sf.parseInt(tagIdText);
		int pageNow = sf.parseInt(pageNowText);
		if (pageNow == 0) {
			pageNow = 1;
		}

		boolean flag = false;
		if ((pageNow > 0) && (tagId > 0)){
			Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
			if (connection != null){
				TagBeanBo tbb = new TagBeanBo(connection);
				tbb.setFilter(ConfigProperty.STATUS_NORMAL, null, null);
				TagBean tb = tbb.getTag(tagId);
				if (tb != null && tb.getStatus() == ConfigProperty.STATUS_NORMAL) {
					ArticleBeanBo abb = new ArticleBeanBo(connection);
					abb.setPageSize(Integer.parseInt(ConfigProperty.configCtrl.getConfigValue("front_blog_page_size")));
					abb.setFilter(ConfigProperty.STATUS_NORMAL + "",
							"SELECT article_id FROM article_tags WHERE tag_id=" + tagId,
							"SELECT category_id FROM categories WHERE status=" + ConfigProperty.STATUS_NORMAL,
							"SELECT user_id FROM users WHERE status=" + ConfigProperty.STATUS_NORMAL, "article_id", "desc");
		
					ArrayList<ArticleBean> al = abb.getArticles(pageNow);
					CommentBeanBo commentBeanBo = new CommentBeanBo(connection);
					for (int i = 0; i < al.size(); i++) {
						commentBeanBo.setFilter(ConfigProperty.STATUS_NORMAL, al.get(i)
								.getArticleId(), 0, null, null);
						al.get(i).setStatus(commentBeanBo.getRowCount());
					}
					ResultBean result = new ResultBean();
					result.setPageNow(pageNow);
					result.setRowCount(abb.getRowCount());
					result.setPageCount(abb.getPageCount());
					result.setUrl("tag/" + tagId + "/");
					
					request.setAttribute("result", result);
					request.setAttribute("tag", tb.getName());
					request.setAttribute("articleList", al);
					flag = true;
				}
				tbb.closeConnection();
			}
		}

		if (flag){
			request.getRequestDispatcher("tag.jsp").forward(request,
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
