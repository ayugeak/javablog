package javablog.servlet.admin.edit;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javablog.database.DBConnectionPool;
import javablog.util.ConfigProperty;
import javablog.util.StrFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javablog.bean.*;

public class EditArticle extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public EditArticle() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();// request.getParameterValues("checkbox");
		try {
			HttpSession session = request.getSession(true);
			String userIdText = (String) session.getAttribute("userId");
			String result = "f";
			if (userIdText != null) {
				String articleIdText = request.getParameter("articleId");
				String title = request.getParameter("title");
				String content = request.getParameter("content");
				String categoryIdText = request.getParameter("categoryId");
				String tagText = request.getParameter("tag");
				String statusText = request.getParameter("status");

				if (categoryIdText == null) {
					categoryIdText = "1";
				}
				if (statusText == null) {
					statusText = "1";
				}

				StrFilter sf = new StrFilter();// 过滤字符
				int articleId = sf.parseNum(articleIdText);
				int categoryId = sf.parseNum(categoryIdText);
				int status = sf.parseNum(statusText);
				title = sf.htmlFilter(title);
				content = sf.contentFilter(content);//不能过滤富文本中的HTML符号
				tagText = sf.htmlFilter(tagText);
				String[] tags = tagText.split(",");// 按逗号切分
				
				Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
				if (connection != null){
					UserBeanBo ubb = new UserBeanBo(connection);
					UserBean ub = ubb.getUser(Integer.parseInt(userIdText));
					if (ub != null && ub.getStatus() == ConfigProperty.STATUS_NORMAL){
						RoleBeanBo ugbb = new RoleBeanBo(connection);
						RoleBean role = ugbb.getRole(ub.getRoleId());
						if (role != null && role.getStatus() == ConfigProperty.STATUS_NORMAL){
							// 封装bean
							ArticleBean ab = new ArticleBean();
							ab.setArticleId(articleId);
							ab.setCategoryId(categoryId);
							ab.setAuthorId(ub.getUserId());
							ab.setTitle(title);
							ab.setContent(content);
							ab.setStatus(status);
							//
							ArticleBeanBo abb = null;
							String logContent = null;
							// 判断权限
							if (articleId == 0 && role.canAddArticle()) {// 新建文章
								abb = new ArticleBeanBo(connection);
								// 该文章是新建的则用户需要有添加文章权限
								if (status == ConfigProperty.STATUS_PENDING) {// 用户是发表文章
									if (abb.addArticle(ab)) {
										logContent = ub.getName() + "(" + ub.getUsername()
												+ ")添加了文章:" + ab.getTitle();
										// 跳转到文章发表成功页面
										result = ab.getArticleId() + "";
									}
								} else if (status == ConfigProperty.STATUS_DRAFT) {// 用户新建了草稿
									if (abb.addArticle(ab)) {
										result = ab.getArticleId() + "";// 新建草稿成功
									}
								}
								if (status == ConfigProperty.STATUS_PENDING
										|| status == ConfigProperty.STATUS_DRAFT) {
									TagBeanBo tbb = new TagBeanBo(connection);
									TagBean tb = new TagBean();
									ArticleTagBeanBo atbb = new ArticleTagBeanBo(connection);
									ArticleTagBean atb = new ArticleTagBean();
									atb.setArticleId(ab.getArticleId());
									for (int i = 0; i < tags.length; i++) {
										tb.setName(tags[i].trim());
										tb.setStatus(ConfigProperty.STATUS_PENDING);
										tbb.addTag(tb);//we only need to add the tag, don't care whether it's already exists
										atb.setTagId(tb.getTagId());
										atbb.addArticleTag(atb);
									}
								}
							} else if (articleId > 0
									&& (ab.getAuthorId() == ub.getUserId() || role
											.canEditArticle())) {// 修改文章，则要么该文章是用户发表的，要么该用户具有编辑权限
								abb = new ArticleBeanBo(connection);
								if (status == ConfigProperty.STATUS_PENDING) {// 用户是修改文章
									if (abb.editArticle(ab)) {// 修改后发表成功
										logContent = ub.getName() + "(" + ub.getUsername()
												+ ")修改了文章:" + ab.getTitle();
										result = ab.getArticleId() + "";
									}
								} else if (status == ConfigProperty.STATUS_DRAFT) {// 用户修改了草稿
									if (abb.editArticle(ab)) {
										result = ab.getArticleId() + "";
									}
								}
								if (status == ConfigProperty.STATUS_PENDING
										|| status == ConfigProperty.STATUS_DRAFT) {
									TagBeanBo tbb = new TagBeanBo(connection);
									TagBean tb = new TagBean();
									ArticleTagBeanBo atbb = new ArticleTagBeanBo(connection);
									atbb.deleteTag(ab.getArticleId());// 首先删除所有tag
									ArticleTagBean atb = new ArticleTagBean();
									atb.setArticleId(ab.getArticleId());
									for (int i = 0; i < tags.length; i++) {
										tb.setName(tags[i].trim());
										tb.setStatus(ConfigProperty.STATUS_PENDING);
										tbb.addTag(tb);
										atb.setTagId(tb.getTagId());
										atbb.addArticleTag(atb);
									}
								}
							} else {// no permission
								result = "n";// 没有权限
							}
							if (logContent != null) {
								LogBean lb = new LogBean();
								lb.setContent(logContent);
								lb.setStatus(ConfigProperty.STATUS_NORMAL);
								LogBeanBo lbb = new LogBeanBo(connection);
								lbb.addLog(lb);
							}
						}
					}
					// 关闭数据库连接
					ubb.closeConnection();
				}
			} else {
				result = "n";// 没有登录
			}
			out.print(result);
		} finally {
			out.flush();
			out.close();
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

	public void init() throws ServletException {

	}

}
