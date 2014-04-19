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

public class EditLink extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public EditLink() {
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
				String strlinkId = request.getParameter("linkId");
				String name = request.getParameter("name");
				String url = request.getParameter("url");
				StrFilter sf = new StrFilter();// 过滤字符
				int linkId = sf.parseNum(strlinkId);

				Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
				if (connection != null){
					UserBeanBo ubb = new UserBeanBo(connection);
					UserBean ub = ubb.getUser(Integer.parseInt(userIdText));
					if (ub != null && ub.getStatus() == ConfigProperty.STATUS_NORMAL){
						RoleBeanBo ugbb = new RoleBeanBo(connection);
						RoleBean role = ugbb.getRole(ub.getRoleId());
						if (role != null && role.getStatus() == ConfigProperty.STATUS_NORMAL){
	
							LinkBean lb = new LinkBean();
							lb.setLinkId(linkId);
							lb.setName(sf.htmlFilter(name));
							lb.setUrl(url);
							lb.setStatus(ConfigProperty.STATUS_NORMAL);
			
							LinkBeanBo lbb = null;
							String content = null;
							if (linkId == 0 && role.canAddLink()) {// 新建链接
								lbb = new LinkBeanBo(connection);
								if (lbb.addLink(lb)) {
									content = ub.getName() + "(" + ub.getUsername()
											+ ")添加了链接:" + name + "(" + url + ")";
									result = "s";
								}
							} else if (linkId != 0 && role.canEditLink()) {// 修改链接
								lbb = new LinkBeanBo(connection);
								if (lbb.editLink(lb)) {
									content = ub.getName() + "(" + ub.getUsername()
											+ ")修改了链接:" + name + "(" + url + ")";
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
								//update the cache
								String basePath = request.getScheme() + "://"
										+ "localhost" + ":"
										+ request.getServerPort()
										+ request.getContextPath() + "/";
								CacheCtrl cacheCtrl = new CacheCtrl(basePath);
								cacheCtrl.updateLinkCache();
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
