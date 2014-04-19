package javablog.servlet.admin.account;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javablog.database.DBConnectionPool;
import javablog.util.ConfigProperty;
import javablog.util.SocketMail;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javablog.bean.*;

public class FindPassword extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public FindPassword() {
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
			String username = request.getParameter("username");
			String email = request.getParameter("email");
			String code = request.getParameter("code");
			String result = "f";
			Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
			if (connection != null){
				UserBeanBo ubb = new UserBeanBo(connection);
				UserBean ub = ubb.getUser(username);// 存在sql注入漏洞，应该先过滤字段
				
				if (ub != null && ub.getEmail().equals(email)) {// 找回密码要同时提供用户名与邮箱
					HttpSession session = request.getSession(true);
					String verifycode = (String) session.getAttribute("code");
					if (verifycode.equals(code)) {
						session.removeAttribute("code");
						// create new password
						String newPassword = ubb.randomPassword();
						ub.setPassword(ubb.getMD5Str(newPassword));
						if (ubb.editUser(ub)) {
							SocketMail socketMail = new SocketMail();
							socketMail.setServer("smtp.gmail.com", 465, "SSL");
							socketMail.setFrom("JavaBlog", "acm0901hust@gmail.com",
									"hustacm0901");
							socketMail.setTo(ub.getName(), ub.getEmail());
							socketMail.setFrom(ConfigProperty.website_name, ConfigProperty.admin_email,
									ConfigProperty.admin_password);
							socketMail.setTo(username, email);
							String subject = ConfigProperty.website_name + ":Password";
							String content = "Hello, " + username
									+ ", your password is: " + newPassword;
							socketMail.setSubject(subject);
							socketMail.setContent(content);
							socketMail.connectServer();
							socketMail.login();
							if (socketMail.sendMail()) {
								result = "s";
							}
							socketMail.close();
						}
					} else {
						result = "w";
					}
				} else {
					result = "n";
				}

				// 关闭数据库连接
				ubb.closeConnection();
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
