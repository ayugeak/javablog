package javablog.servlet.admin.account;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javablog.database.DBConnectionPool;
import javablog.util.StrFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javablog.bean.*;

public class Account extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public Account() {
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
				String password = request.getParameter("password");
				String name = request.getParameter("name");
				String email = request.getParameter("email");
				String url = request.getParameter("url");
				String avatar = request.getParameter("avatar");
				
				Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
				if (connection != null){
					UserBeanBo ubb = new UserBeanBo(connection);
					UserBean ub = ubb.getUser(Integer.parseInt(userIdText));
	
					StrFilter sf = new StrFilter();
					password = sf.htmlFilter(password);
					name = sf.htmlFilter(name);
					email = sf.htmlFilter(email);
					url = sf.htmlFilter(url);
					avatar = sf.htmlFilter(avatar);
					if (password.length() > 0) {//modify password
						password = ubb.getMD5Str(password);
						ub.setPassword(password);
					} else {
						ub.setName(name);
						ub.setEmail(email);
						ub.setUrl(url);
						ub.setAvatar(avatar);
					}
					if (ubb.editUser(ub)) {
						result = "s";
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
