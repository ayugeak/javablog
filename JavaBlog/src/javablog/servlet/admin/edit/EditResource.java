package javablog.servlet.admin.edit;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javablog.bean.*;
import javablog.database.DBConnectionPool;
import javablog.util.ConfigProperty;
import javablog.util.StrFilter;

public class EditResource extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public EditResource() {
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
				String strresourceId = request.getParameter("resourceId");
				String name = request.getParameter("name");
				StrFilter sf = new StrFilter();// 过滤字符
				int resourceId = sf.parseNum(strresourceId);
				name = sf.htmlFilter(name);

				Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
				if (connection != null){
					UserBeanBo ubb = new UserBeanBo(connection);
					UserBean ub = ubb.getUser(Integer.parseInt(userIdText));
					if (ub != null && ub.getStatus() == ConfigProperty.STATUS_NORMAL){
						RoleBeanBo ugbb = new RoleBeanBo(connection);
						RoleBean role = ugbb.getRole(ub.getRoleId());
						if (role != null && role.getStatus() == ConfigProperty.STATUS_NORMAL){
	
							ResourceBean mb = new ResourceBean();
							mb.setResourceId(resourceId);
							mb.setName(name);
							mb.setStatus(ConfigProperty.STATUS_NORMAL);
							ResourceBeanBo mbb = null;
							String content = null;
							if (role.canEditResource()) {
								mbb = new ResourceBeanBo(connection);
								if (mbb.editResource(mb)) {
									content = ub.getName() + "(" + ub.getUsername()
											+ ")修改了资料:" + mb.getName();
									result = "s";
								}
							} else {
								result = "n";
							}
							if (content != null) {
								LogBean lb = new LogBean();
								lb.setContent(content);
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
