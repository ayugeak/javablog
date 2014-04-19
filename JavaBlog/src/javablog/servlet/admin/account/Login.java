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

public class Login extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public Login() {
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
			String password = request.getParameter("password");
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
					password = ubb.getMD5Str(sf.htmlFilter(password));
					ub.setPassword(password);
					if (ubb.checkLogin(ub)) {
						session.setAttribute("userId", ub.getUserId() + "");// 将用户id存入session中
						result = "s";// 登录成功
					}
					// 关闭数据库连接
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
