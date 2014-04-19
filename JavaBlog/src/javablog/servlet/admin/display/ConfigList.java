package javablog.servlet.admin.display;

import java.io.IOException;
import java.sql.Connection;

import javablog.bean.RoleBean;
import javablog.bean.RoleBeanBo;
import javablog.bean.UserBean;
import javablog.bean.UserBeanBo;
import javablog.database.DBConnectionPool;
import javablog.util.ConfigProperty;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ConfigList
 */

public class ConfigList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConfigList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		boolean flag = false;
		HttpSession session = request.getSession(true);
		String userIdText = (String) session.getAttribute("userId");
		if (userIdText != null) {
			Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
			if (connection != null){//valid connection
				UserBeanBo ubb = new UserBeanBo(connection);
				UserBean ub = ubb.getUser(Integer.parseInt(userIdText));
				if (ub != null && ub.getStatus() == ConfigProperty.STATUS_NORMAL){//valid user
					RoleBeanBo rbb = new RoleBeanBo(connection);
					RoleBean role = rbb.getRole(ub.getRoleId());
					if (role != null && role.getStatus() == 0 && role.canReadConfig()) {//valid permission
						request.setAttribute("role", role);
						flag = true;
					}
				}
			}
		}
		
		if (flag){
			request.getRequestDispatcher("configList.jsp").forward(request,
					response);
		} else {
			request.getRequestDispatcher("error.jsp")
					.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
