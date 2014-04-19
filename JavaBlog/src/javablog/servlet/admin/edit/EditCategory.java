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

public class EditCategory extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public EditCategory() {
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
			// 判断用户是否登录
			HttpSession session = request.getSession(true);
			String userIdText = (String) session.getAttribute("userId");
			String result = "f";
			if (userIdText != null) {// 已经登录
				// 检查参数
				String categoryIdText = request.getParameter("categoryId");
				String name = request.getParameter("name");
				StrFilter sf = new StrFilter();// 过滤字符
				int categoryId = sf.parseNum(categoryIdText);

				Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
				if (connection != null){
					UserBeanBo ubb = new UserBeanBo(connection);
					UserBean ub = ubb.getUser(Integer.parseInt(userIdText));
					if (ub != null && ub.getStatus() == ConfigProperty.STATUS_NORMAL){
						RoleBeanBo ugbb = new RoleBeanBo(connection);
						RoleBean role = ugbb.getRole(ub.getRoleId());
						if (role != null && role.getStatus() == ConfigProperty.STATUS_NORMAL){
							// 封装bean
							CategoryBean cb = new CategoryBean();
							cb.setCategoryId(categoryId);
							cb.setName(sf.htmlFilter(name));
							cb.setStatus(ConfigProperty.STATUS_NORMAL);
			
							CategoryBeanBo cbb = null;// 分类管理类
							String content = null;// 日志内容
							// 判断用户是否具有相应权限
							if (categoryId == 0 && role.canAddCategory()) {// 新建分类
								cbb = new CategoryBeanBo(connection);
								if (cbb.addCategory(cb)) {
									result = "s";
									content = ub.getName() + "(" + ub.getUsername()
											+ ")添加了分类:" + name;
								}
							} else if (categoryId != 0 && role.canEditCategory()) {// 修改分类
								cbb = new CategoryBeanBo(connection);
								if (cbb.editCategory(cb)) {
									result = "s";
									content = ub.getName() + "(" + ub.getUsername()
											+ ")修改了分类:" + name;
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
								//update the cache
								String basePath = request.getScheme() + "://"
										+ "localhost" + ":"
										+ request.getServerPort()
										+ request.getContextPath() + "/";
								CacheCtrl cacheCtrl = new CacheCtrl(basePath);
								cacheCtrl.updateCategoryCache();
							}
						}
					}
					// 关闭数据库连接
					ubb.closeConnection();
				}
			} else {// 没有登录
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
