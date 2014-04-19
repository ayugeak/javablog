package javablog.servlet.admin.edit;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javablog.database.DBConnectionPool;
import javablog.util.ConfigProperty;
import javablog.util.StrFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javablog.bean.*;

public class EditRole extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public EditRole() {
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
				String roleIdText = request.getParameter("roleId");
				String name = request.getParameter("name");
				String permission = request.getParameter("permission");
				
				StrFilter sf = new StrFilter();
				int roleId = sf.parseNum(roleIdText);
				name = sf.htmlFilter(name);
				String[] perms = permission.split(",");
				permission = "";
				char c = '\0';
				for (int i = 0; i < perms.length; i++){
					perms[i] = sf.htmlFilter(perms[i]);
					c = (char)Integer.parseInt(perms[i]);
					permission += c;
				}

				Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
				if (connection != null){
					UserBeanBo ubb = new UserBeanBo(connection);
					UserBean ub = ubb.getUser(Integer.parseInt(userIdText));
					if (ub != null && ub.getStatus() == ConfigProperty.STATUS_NORMAL){
						RoleBeanBo ugbb = new RoleBeanBo(connection);
						RoleBean role = ugbb.getRole(ub.getRoleId());
						if (role != null && role.getStatus() == ConfigProperty.STATUS_NORMAL){
	
							RoleBean newUgb = new RoleBean();
							newUgb.setRoleId(roleId);
							newUgb.setName(name);
							newUgb.setPermission(permission);
							newUgb.setStatus(ConfigProperty.STATUS_NORMAL);
			
							String content = null;
							if (roleId == 0 && role.canAddRole()) {
								if (ugbb.addRole(newUgb)) {
									content = ub.getName() + "(" + ub.getUsername()
											+ ")添加了角色:" + newUgb.getName();
									result = "s";
								}
							} else if (roleId != 0 && role.canEditRole()) {
								if (ugbb.editRole(newUgb)) {
									content = ub.getName() + "(" + ub.getUsername()
											+ ")修改了角色:" + newUgb.getName();
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
