package javablog.servlet.admin.edit;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javablog.database.DBConnectionPool;
import javablog.util.CacheCtrl;
import javablog.util.ConfigProperty;
import javablog.util.StrFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javablog.bean.*;

public class ChangeStatus extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public ChangeStatus() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			HttpSession session = request.getSession(true);
			String userIdText = (String) session.getAttribute("userId");
			String result = "f";
			if (userIdText != null) {
				String idText = request.getParameter("id");
				String typeText = request.getParameter("type");
				String statusText = request.getParameter("status");
				
				StrFilter sf = new StrFilter();
				int type = sf.parseNum(typeText);
				int status = sf.parseNum(statusText);
				String[] ids = idText.split(",");
				int[] id = new int[ids.length];
				for (int i = 0; i < ids.length; i++) {
					id[i] = sf.parseNum(ids[i]);
				}
				
				Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
				if (connection != null){
					UserBeanBo ubb = new UserBeanBo(connection);
					UserBean ub = ubb.getUser(Integer.parseInt(userIdText));
					if (ub != null && ub.getStatus() == ConfigProperty.STATUS_NORMAL){
						RoleBeanBo ugbb = new RoleBeanBo(connection);
						RoleBean role = ugbb.getRole(ub.getRoleId());
						if (role != null && role.getStatus() == ConfigProperty.STATUS_NORMAL){
							String basePath = request.getScheme() + "://"
									+ "localhost" + ":"//+ request.getServerName() + ":"
									+ request.getServerPort()
									+ request.getContextPath() + "/";
							switch (type) {
							case ConfigProperty.ID_ROLE:// 用户组
								if (role.canEditRole()) {
									if (ugbb.changeStatus(idText, status)) {
										result = "s";
									}
								} else {
									result = "n";
								}
								break;
							case ConfigProperty.ID_USER:// 用户
								if (role.canEditUser()) {
									if (ubb.changeStatus(idText, status)) {
										result = "s";
									}
								} else {
									result = "n";
								}
								break;
							case ConfigProperty.ID_CATEGORY:// 分类
								if (role.canEditCategory()) {
									CategoryBeanBo cbb = new CategoryBeanBo(connection);
									if (cbb.changeStatus(idText, status)) {
										result = "s";
										//update the cache
										CacheCtrl cacheCtrl = new CacheCtrl(basePath);
										cacheCtrl.updateCategoryCache();
									}
								} else {
									result = "n";
								}
								break;
							case ConfigProperty.ID_ARTICLE:// 文章
								if (role.canEditArticle()) {
									ArticleBeanBo abb = new ArticleBeanBo(connection);
									if (abb.changeStatus(idText, status)) {
										result = "s";
										//update the cache
										CacheCtrl cacheCtrl = new CacheCtrl(basePath);
										cacheCtrl.updateRecentArticleCache();
									}
									// 文章审核通过后，相当于审核通过了该文章对应的所有标签
									if (status == ConfigProperty.STATUS_NORMAL) {
										ArticleTagBeanBo atbb = new ArticleTagBeanBo(
												connection);
										atbb.changeStatus(idText, status);
										//update the cache
										CacheCtrl cacheCtrl = new CacheCtrl(basePath);
										cacheCtrl.updateTagCache();
									}
								} else {
									result = "n";
								}
								break;
							case ConfigProperty.ID_COMMENT:// 评论
								if (role.canEditComment()) {
									CommentBeanBo cbb = new CommentBeanBo(connection);
									if (cbb.changeStatus(idText, status)) {
										result = "s";
										//update the cache
										CacheCtrl cacheCtrl = new CacheCtrl(basePath);
										cacheCtrl.updateRecentCommentCache();
									}
								} else {
									result = "n";
								}
								break;
							case ConfigProperty.ID_LINK:// 链接
								if (role.canEditLink()) {
									LinkBeanBo lbb = new LinkBeanBo(connection);
									if (lbb.changeStatus(idText, status)) {
										result = "s";
										//update the cache
										CacheCtrl cacheCtrl = new CacheCtrl(basePath);
										cacheCtrl.updateLinkCache();
									}
								} else {
									result = "n";
								}
								break;
							case ConfigProperty.ID_MESSAGE:// 信息
								if (role.canEditMessage()) {
									MessageBeanBo mbb = new MessageBeanBo(connection);
									if (mbb.changeStatus(idText, status)) {
										result = "s";
									}
								} else {
									result = "n";
								}
								break;
							case ConfigProperty.ID_RESOURCE:// 资源
								if (role.canEditResource()) {
									ResourceBeanBo rbb = new ResourceBeanBo(connection);
									if (rbb.changeStatus(idText, status)) {
										result = "s";
									}
								} else {
									result = "n";
								}
								break;
							case ConfigProperty.ID_LOG:// 日志
								if (role.canDeleteLog()) {
									LogBeanBo lbb = new LogBeanBo(connection);
									if (lbb.changeStatus(idText, status)) {
										result = "s";
									}
								} else {
									result = "n";
								}
								break;
							case ConfigProperty.ID_TAG:// 标签
								if (role.canEditTag()) {
									TagBeanBo tbb = new TagBeanBo(connection);
									if (tbb.changeStatus(idText, status)) {
										result = "s";
										//update the cache
										CacheCtrl cacheCtrl = new CacheCtrl(basePath);
										cacheCtrl.updateTagCache();
									}
								} else {
									result = "n";
								}
								break;
							default:
								break;
							}
						}
					}
					// 关闭数据库连接
					ubb.closeConnection();
				}
			} else {
				result = "n";// no permission
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
