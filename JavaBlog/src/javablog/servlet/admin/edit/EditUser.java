package javablog.servlet.admin.edit;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javablog.database.DBConnectionPool;
import javablog.util.ConfigProperty;
import javablog.util.SocketMail;
import javablog.util.StrFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javablog.bean.*;

public class EditUser extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public EditUser() {
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
				String newuserIdText = request.getParameter("userId");
				String roleIdText = request.getParameter("roleId");
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				String name = request.getParameter("name");
				String email = request.getParameter("email");
				StrFilter sf = new StrFilter();
				int userId = sf.parseNum(newuserIdText);
				int roleId = sf.parseNum(roleIdText);
				username = sf.htmlFilter(username);
				password = sf.htmlFilter(password);
				name = sf.htmlFilter(name);

				Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
				if (connection != null){
					UserBeanBo ubb = new UserBeanBo(connection);
					UserBean ub = ubb.getUser(Integer.parseInt(userIdText));
					if (ub != null && ub.getStatus() == ConfigProperty.STATUS_NORMAL){
						RoleBeanBo ugbb = new RoleBeanBo(connection);
						RoleBean role = ugbb.getRole(ub.getRoleId());
						if (role != null && role.getStatus() == ConfigProperty.STATUS_NORMAL){
							UserBean newUb = new UserBean();
							newUb.setUserId(userId);
							newUb.setRoleId(roleId);
							newUb.setUsername(username);
							newUb.setPassword(password);
							newUb.setName(name);
							newUb.setEmail(email);
							newUb.setUrl("");
							newUb.setAvatar("images/avatar.png");
							newUb.setStatus(ConfigProperty.STATUS_NORMAL);
			
							String logContent = null;
							if (userId == 0 && role.canAddUser()) {
								String newPassword = ubb.randomPassword();
								newUb.setPassword(ubb.getMD5Str(newPassword));
								if (ubb.addUser(newUb)) {// 给新添加的用户发送密码邮件
									SocketMail socketMail = new SocketMail();
									socketMail.setServer("smtp.gmail.com", 465, "SSL");
									socketMail.setFrom("JavaBlog", "acm0901hust@gmail.com",
											"hustacm0901");
									socketMail.setTo(newUb.getName(), newUb.getEmail());
									String subject = "JavaBlog:密码";
									String content = "来自JavaBlog的邮件,尊敬的" + newUb.getName()
											+ "你好,您的密码为:" + newPassword;
									socketMail.setSubject(subject);
									socketMail.setContent(content);
									socketMail.connectServer();
									socketMail.login();
									if (socketMail.sendMail()) {
										result = "s";
									}
									socketMail.close();
									logContent = ub.getName() + "(" + ub.getUsername()
											+ ")添加了用户:" + newUb.getName() + "("
											+ newUb.getUsername() + ")";
								}
							} else if (userId != 0 && role.canEditUser()) {
								newUb = ubb.getUser(userId);
								newUb.setRoleId(roleId);
								if (ubb.editUser(newUb)) {
									logContent = ub.getName() + "(" + ub.getUsername()
											+ ")修改了用户:" + newUb.getName() + "("
											+ newUb.getUsername() + ")";
									result = "s";
								}
							} else {
								result = "n";
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

}
