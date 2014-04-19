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

public class EditMessage extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public EditMessage() {
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
			if (userIdText != null) {// 判断用户是否登录
				String receiverIdText = request.getParameter("receiverId");
				String content = request.getParameter("content");
				StrFilter sf = new StrFilter();// 过滤字符
				int receiverId = sf.parseNum(receiverIdText);

				Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
				if (connection != null){
					UserBeanBo ubb = new UserBeanBo(connection);
					UserBean ub = ubb.getUser(Integer.parseInt(userIdText));
					if (ub != null && ub.getStatus() == ConfigProperty.STATUS_NORMAL){
						RoleBeanBo ugbb = new RoleBeanBo(connection);
						RoleBean role = ugbb.getRole(ub.getRoleId());
						if (role != null && role.getStatus() == ConfigProperty.STATUS_NORMAL){
	
							MessageBean cb = new MessageBean();
							cb.setSenderId(Integer.parseInt(userIdText));
							cb.setReceiverId(receiverId);
							cb.setContent(sf.htmlFilter(content));
							cb.setStatus(ConfigProperty.STATUS_UNREAD);// 消息默认为未读状态
							MessageBeanBo mbb = null;
							if (role.canAddMessage()) {// 判断用户是否具有相应权限
								mbb = new MessageBeanBo(connection);
								if (mbb.addMessage(cb)) {
									result = "s";
								}
							} else {
								result = "n";
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
