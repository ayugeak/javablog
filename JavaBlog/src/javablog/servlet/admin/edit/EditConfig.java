package javablog.servlet.admin.edit;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javablog.database.DBConnectionPool;
import javablog.util.ConfigProperty;
import javablog.util.StrFilter;
import javablog.util.ValidChecker;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javablog.bean.*;

public class EditConfig extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public EditConfig() {
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
				String website_name = request.getParameter("website_name");
				String front_blog_page_size = request.getParameter("front_blog_page_size");
				String front_page_count = request.getParameter("front_page_count");
				String front_recent_post_count = request.getParameter("front_recent_post_count");
				String front_recent_comment_count = request.getParameter("front_recent_comment_count");
				String front_comment_page_size = request.getParameter("front_comment_page_size");
				String admin_role_page_size = request.getParameter("admin_role_page_size");
				String admin_user_page_size = request.getParameter("admin_user_page_size");
				String admin_category_page_size = request.getParameter("admin_category_page_size");
				String admin_tag_page_size = request.getParameter("admin_tag_page_size");
				String admin_article_page_size = request.getParameter("admin_article_page_size");
				String admin_comment_page_size = request.getParameter("admin_comment_page_size");
				String admin_link_page_size = request.getParameter("admin_link_page_size");
				String admin_message_page_size = request.getParameter("admin_message_page_size");
				String admin_resource_page_size = request.getParameter("admin_resource_page_size");
				String admin_log_page_size = request.getParameter("admin_log_page_size");
				String admin_email = request.getParameter("admin_email");
				String admin_password = request.getParameter("admin_password");
				
				StrFilter sf = new StrFilter();// 过滤字符
				website_name = sf.contentFilter(website_name);
				int front_blog_page_size_int = sf.parseInt(front_blog_page_size);
				int front_page_count_int = sf.parseInt(front_page_count);
				int front_recent_post_count_int = sf.parseInt(front_recent_post_count);
				int front_recent_comment_count_int = sf.parseInt(front_recent_comment_count);
				int front_comment_page_size_int = sf.parseInt(front_comment_page_size);
				int admin_role_page_size_int = sf.parseInt(admin_role_page_size);
				int admin_user_page_size_int = sf.parseInt(admin_user_page_size);
				int admin_category_page_size_int = sf.parseInt(admin_category_page_size);
				int admin_tag_page_size_int = sf.parseInt(admin_tag_page_size);
				int admin_article_page_size_int = sf.parseInt(admin_article_page_size);
				int admin_comment_page_size_int = sf.parseInt(admin_comment_page_size);
				int admin_link_page_size_int = sf.parseInt(admin_link_page_size);
				int admin_message_page_size_int = sf.parseInt(admin_message_page_size);
				int admin_resource_page_size_int = sf.parseInt(admin_resource_page_size);
				int admin_log_page_size_int = sf.parseInt(admin_log_page_size);
				
				if ((website_name.length() > 0 && website_name.length() < 255)
						&& (2 < front_blog_page_size_int && front_blog_page_size_int < 20)
						&& (2 < front_page_count_int && front_page_count_int < 20)
						&& (2 < front_recent_post_count_int && front_recent_post_count_int < 20)
						&& (2 < front_recent_comment_count_int && front_recent_comment_count_int < 20)
						&& (2 < front_comment_page_size_int && front_comment_page_size_int < 20)
						&& (0 < admin_role_page_size_int && admin_role_page_size_int < 30)
						&& (0 < admin_user_page_size_int && admin_user_page_size_int < 30)
						&& (0 < admin_category_page_size_int && admin_category_page_size_int < 30)
						&& (0 < admin_tag_page_size_int && admin_tag_page_size_int < 30)
						&& (0 < admin_article_page_size_int && admin_article_page_size_int < 30)
						&& (0 < admin_comment_page_size_int && admin_comment_page_size_int < 30)
						&& (0 < admin_link_page_size_int && admin_link_page_size_int < 30)
						&& (0 < admin_message_page_size_int && admin_message_page_size_int < 30)
						&& (0 < admin_resource_page_size_int && admin_resource_page_size_int < 30)
						&& (0 < admin_log_page_size_int && admin_log_page_size_int < 30)
						&& ValidChecker.isValidEmail(admin_email)
						&& ValidChecker.isValidPassword(admin_password)){
				
					Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
					if (connection != null){
						UserBeanBo ubb = new UserBeanBo(connection);
						UserBean ub = ubb.getUser(Integer.parseInt(userIdText));
						if (ub != null && ub.getStatus() == ConfigProperty.STATUS_NORMAL){
							RoleBeanBo ugbb = new RoleBeanBo(connection);
							RoleBean role = ugbb.getRole(ub.getRoleId());
							if (role != null && role.getStatus() == ConfigProperty.STATUS_NORMAL){
								String content = null;
								if (role.canEditConfig()) {
									ConfigProperty.configCtrl.setConfigValue("website_name", website_name);
									ConfigProperty.configCtrl.setConfigValue("front_blog_page_size", front_blog_page_size);
									ConfigProperty.configCtrl.setConfigValue("front_page_count", front_page_count);
									ConfigProperty.configCtrl.setConfigValue("front_recent_post_count", front_recent_post_count);
									ConfigProperty.configCtrl.setConfigValue("front_recent_comment_count", front_recent_comment_count);
									ConfigProperty.configCtrl.setConfigValue("front_comment_page_size", front_comment_page_size);
									ConfigProperty.configCtrl.setConfigValue("admin_role_page_size", admin_role_page_size);
									ConfigProperty.configCtrl.setConfigValue("admin_user_page_size", admin_user_page_size);
									ConfigProperty.configCtrl.setConfigValue("admin_category_page_size", admin_category_page_size);
									ConfigProperty.configCtrl.setConfigValue("admin_tag_page_size", admin_tag_page_size);
									ConfigProperty.configCtrl.setConfigValue("admin_article_page_size", admin_article_page_size);
									ConfigProperty.configCtrl.setConfigValue("admin_comment_page_size", admin_comment_page_size);
									ConfigProperty.configCtrl.setConfigValue("admin_link_page_size", admin_link_page_size);
									ConfigProperty.configCtrl.setConfigValue("admin_message_page_size", admin_message_page_size);
									ConfigProperty.configCtrl.setConfigValue("admin_resource_page_size", admin_resource_page_size);
									ConfigProperty.configCtrl.setConfigValue("admin_log_page_size", admin_log_page_size);
									ConfigProperty.configCtrl.setConfigValue("admin_email", admin_email);
									ConfigProperty.configCtrl.setConfigValue("admin_password", admin_password);
									if (ConfigProperty.configCtrl.saveConfig()){
										this.updateConfig();
										content = ub.getName() + "(" + ub.getUsername() + ")修改了网站配置";
										result = "s";
									}
								} else {
									result = "n";
								}
								if (content != null) {
									LogBean lob = new LogBean();
									lob.setContent(content);
									lob.setStatus(ConfigProperty.STATUS_NORMAL);
									LogBeanBo lobb = new LogBeanBo(connection);
									lobb.addLog(lob);
								}
							}
						}
						// 关闭数据库连接
						ubb.closeConnection();
					}
				}
			} else {
				result = "n";
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
	
	private void updateConfig(){
		ConfigProperty.website_name = ConfigProperty.configCtrl.getConfigValue("website_name");
		ConfigProperty.front_blog_page_size = Integer.parseInt(ConfigProperty.configCtrl.getConfigValue("front_blog_page_size"));
		ConfigProperty.front_page_count = Integer.parseInt(ConfigProperty.configCtrl.getConfigValue("front_page_count"));
		ConfigProperty.front_recent_post_count = Integer.parseInt(ConfigProperty.configCtrl.getConfigValue("front_recent_post_count"));
		ConfigProperty.front_recent_comment_count = Integer.parseInt(ConfigProperty.configCtrl.getConfigValue("front_recent_comment_count"));
		ConfigProperty.front_comment_page_size = Integer.parseInt(ConfigProperty.configCtrl.getConfigValue("front_comment_page_size"));
		ConfigProperty.admin_role_page_size = Integer.parseInt(ConfigProperty.configCtrl.getConfigValue("admin_role_page_size"));
		ConfigProperty.admin_user_page_size = Integer.parseInt(ConfigProperty.configCtrl.getConfigValue("admin_user_page_size"));
		ConfigProperty.admin_category_page_size = Integer.parseInt(ConfigProperty.configCtrl.getConfigValue("admin_category_page_size"));
		ConfigProperty.admin_tag_page_size = Integer.parseInt(ConfigProperty.configCtrl.getConfigValue("admin_tag_page_size"));
		ConfigProperty.admin_article_page_size = Integer.parseInt(ConfigProperty.configCtrl.getConfigValue("admin_article_page_size"));
		ConfigProperty.admin_comment_page_size = Integer.parseInt(ConfigProperty.configCtrl.getConfigValue("admin_comment_page_size"));
		ConfigProperty.admin_link_page_size = Integer.parseInt(ConfigProperty.configCtrl.getConfigValue("admin_link_page_size"));
		ConfigProperty.admin_message_page_size = Integer.parseInt(ConfigProperty.configCtrl.getConfigValue("admin_message_page_size"));
		ConfigProperty.admin_resource_page_size = Integer.parseInt(ConfigProperty.configCtrl.getConfigValue("admin_resource_page_size"));
		ConfigProperty.admin_log_page_size = Integer.parseInt(ConfigProperty.configCtrl.getConfigValue("admin_log_page_size"));
		ConfigProperty.admin_email = ConfigProperty.configCtrl.getConfigValue("admin_email");
		ConfigProperty.admin_password = ConfigProperty.configCtrl.getConfigValue("admin_password");
	}

}
