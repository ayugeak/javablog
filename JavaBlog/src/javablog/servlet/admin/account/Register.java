package javablog.servlet.admin.account;

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

public class Register extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public Register() {
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
			StrFilter sf = new StrFilter();
			code = sf.htmlFilter(code);
			
			HttpSession session = request.getSession(true);
			String verifycode = (String) session.getAttribute("code");
			String result = "f";
			if (code.equalsIgnoreCase(verifycode)) {
				session.removeAttribute("code");
				
				Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
				if (connection != null){
					UserBeanBo ubb = new UserBeanBo(connection);
					UserBean ub = new UserBean();
					ub.setUsername(sf.htmlFilter(username));
					
					String newPassword = ubb.randomPassword();
					SocketMail socketMail = new SocketMail();
					socketMail.setServer("smtp.gmail.com", 465, "SSL");
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
					if (socketMail.sendMail()){
						ub.setRoleId(3);//give him a role, role 3 is guest
						ub.setName(username);
						ub.setUsername(username);
						ub.setPassword(ubb.getMD5Str(newPassword));
						ub.setEmail(email);
						ub.setAvatar("images/avatar.png");
						ub.setUrl("");
						ub.setStatus(ConfigProperty.STATUS_NORMAL);
						if (ubb.addUser(ub)) {
							result = "s";
						}
					}
					socketMail.close();
					//close the connection
					ubb.closeConnection();
				}
			} else {
				result = "w";
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
