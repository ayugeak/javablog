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

public class Delete extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public Delete() {
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
				StrFilter sf = new StrFilter();
				int type = sf.parseNum(typeText);
				String[] ids = idText.split(",");
				int[] id = new int[ids.length];
				for (int i = 0; i < ids.length; i++) {
					id[i] = sf.parseNum(ids[i]);
				}
				//we need check the id
				
				Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
				if (connection != null){
					UserBeanBo ubb = new UserBeanBo(connection);
					UserBean ub = ubb.getUser(Integer.parseInt(userIdText));
					if (ub != null && ub.getStatus() == ConfigProperty.STATUS_NORMAL){
						RoleBeanBo ugbb = new RoleBeanBo(connection);
						RoleBean role = ugbb.getRole(ub.getRoleId());
						
						String content = "";
						if (role != null && role.getStatus() == ConfigProperty.STATUS_NORMAL){
							//update the cache
							String basePath = request.getScheme() + "://"
									+ "localhost" + ":"
									+ request.getServerPort()
									+ request.getContextPath() + "/";
							switch (type) {
							case ConfigProperty.ID_ROLE:// 删除角色
								if (role.canDeleteRole()) {
									boolean valid = true;
									//you can't delete these roles:root, editor, guest
									for (int i = 0; i < id.length; i++){
										if (id[i] == 1 || id[i] == 2 || id[i] == 3){
											valid = false;
											break;
										}
									}
									if (valid && ugbb.deleteRole(idText)) {
										result = "s";
									}
								} else {
									result = "n";
								}
								break;
							case ConfigProperty.ID_USER:// 删除用户
								if (role.canDeleteUser()) {
									if (ubb.deleteUser(idText)) {
										result = "s";
									}
								} else {
									result = "n";
								}
								break;
							case ConfigProperty.ID_CATEGORY:// 删除分类
								if (role.canDeleteCategory()) {
									CategoryBeanBo cbb = new CategoryBeanBo(connection);
									if (cbb.deleteCategory(idText)) {
										result = "s";
										CacheCtrl cacheCtrl = new CacheCtrl(basePath);
										cacheCtrl.updateCategoryCache();
									}
								} else {
									result = "n";
								}
								break;
							case ConfigProperty.ID_ARTICLE:// 删除文章
								if (role.canDeleteArticle()) {
									ArticleBeanBo abb = new ArticleBeanBo(connection);
									if (abb.deleteArticle(idText)) {
										result = "s";
										CacheCtrl cacheCtrl = new CacheCtrl(basePath);
										cacheCtrl.updateRecentArticleCache();
									}
								} else {
									result = "n";
								}
								break;
							case ConfigProperty.ID_COMMENT:// 删除评论
								if (role.canDeleteComment()) {
									CommentBeanBo tbb = new CommentBeanBo(connection);
									if (tbb.deleteComment(idText)) {
										result = "s";
										CacheCtrl cacheCtrl = new CacheCtrl(basePath);
										cacheCtrl.updateRecentCommentCache();
									}
								} else {
									result = "n";
								}
								break;
							case ConfigProperty.ID_LINK:// 删除链接
								if (role.canDeleteLink()) {
									LinkBeanBo lbb = new LinkBeanBo(connection);
									if (lbb.deleteLink(idText)) {
										result = "s";
										CacheCtrl cacheCtrl = new CacheCtrl(basePath);
										cacheCtrl.updateLinkCache();
									}
								} else {
									result = "n";
								}
								break;
							case ConfigProperty.ID_MESSAGE:// 删除信息
								if (role.canDeleteMessage()) {
									MessageBeanBo mbb = new MessageBeanBo(connection);
									if (mbb.deleteMessage(idText)) {
										result = "s";
									}
								} else {
									result = "n";
								}
								break;
							case ConfigProperty.ID_RESOURCE:// 删除资源
								if (role.canDeleteResource()) {
									ResourceBeanBo rbb = new ResourceBeanBo(connection);
									if (rbb.deleteResource(idText)) {
										result = "s";
									}
								} else {
									result = "n";
								}
								break;
							case ConfigProperty.ID_LOG:// 删除日志
								if (role.canDeleteLog()) {
									LogBeanBo lbb = new LogBeanBo(connection);
									if (lbb.deleteLog(idText)) {
										result = "s";
									}
								} else {
									result = "n";
								}
								break;
							case ConfigProperty.ID_TAG:// 删除标签
								if (role.canDeleteTag()) {
									TagBeanBo tbb = new TagBeanBo(connection);
									/*
									 * for (int j = 0; j < id.length; j++) { TagBean tb =
									 * tbb.getTag(id[j]); content = "删除标签:" + tb.getName();
									 * }
									 */
									if (tbb.deleteTag(idText)) {
										result = "s";
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
						if (content.length() > 0 && result.equals("s")) {// 记录日志
							LogBean lb = new LogBean();
							lb.setContent(content);
							lb.setStatus(ConfigProperty.STATUS_NORMAL);
							LogBeanBo lbb = new LogBeanBo(connection);
							lbb.addLog(lb);
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
