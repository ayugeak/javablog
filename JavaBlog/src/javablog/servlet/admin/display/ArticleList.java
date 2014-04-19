package javablog.servlet.admin.display;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javablog.database.DBConnectionPool;
import javablog.util.ConfigProperty;
import javablog.util.StrFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javablog.bean.*;

public class ArticleList extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public ArticleList() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		boolean flag = false;
		HttpSession session = request.getSession(true);
		String userIdText = (String) session.getAttribute("userId");
		if (userIdText != null) {
			String categoryIdText = request.getParameter("categoryId");// 获得文章分类
			String authorIdText = request.getParameter("authorId");
			String tagIdText = request.getParameter("tagId");
			String statusText = request.getParameter("status");// 获得文章类型
			String attribute = request.getParameter("attribute");// 排序字段
			String order = request.getParameter("order");// 排序方式
			String pageNowText = request.getParameter("pageNow");// 获得要显示的页数

			StrFilter sf = new StrFilter();
			
			int categoryId = sf.parseInt(categoryIdText);
			int authorId = sf.parseInt(authorIdText);
			int tagId = sf.parseInt(tagIdText);
			int status = sf.parseInt(statusText);
			int pageNow = sf.parseInt(pageNowText);
			if (pageNow == 0) {
				pageNow = 1;
			}
			if (attribute == null){
				attribute = "";
			}
			if (order == null){
				order = "";
			}
			//check:parameters->connection->user->permission(role)
			if ((categoryId >= 0)
					&& (authorId >= 0)
					&& (tagId >= 0)
					&& this.isValidStatus(status)
					&& this.isValidAttribute(attribute)
					&& sf.isValidOrder(order)
					&& pageNow > 0){//valid parameters
				Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
				if (connection != null){//valid connection
					UserBeanBo ubb = new UserBeanBo(connection);
					UserBean ub = ubb.getUser(Integer.parseInt(userIdText));
					if (ub != null && ub.getStatus() == ConfigProperty.STATUS_NORMAL){//valid user
						RoleBeanBo rbb = new RoleBeanBo(connection);
						RoleBean role = rbb.getRole(ub.getRoleId());
						if (role != null && role.getStatus() == 0 && role.canReadArticle()) {//valid permission
							ArticleBeanBo abb = new ArticleBeanBo(connection);
							abb.setPageSize(ConfigProperty.admin_article_page_size);
							if (tagId > 0){
								abb.setFilter(status + "", "SELECT article_id FROM article_tags WHERE tag_id=" + tagId,
										categoryId + "", authorId + "", attribute, order);
							} else {
								abb.setFilter(status, categoryId, authorId, attribute, order);
							}
							
							ArrayList<ArticleBean> al = abb.getArticles(pageNow);
							request.setAttribute("articleList", al);
							request.setAttribute("role", role);
							request.setAttribute("categoryId", categoryId + "");
							request.setAttribute("authorId", authorId + "");
							request.setAttribute("tagId", tagId + "");
							request.setAttribute("pageNow", pageNow + "");
							request.setAttribute("rowCount", abb.getRowCount() + "");
							request.setAttribute("pageCount", abb.getPageCount() + "");
							request.setAttribute("attribute", attribute);
							request.setAttribute("order", order);
							request.setAttribute("status", status + "");
							
							flag = true;
						}
					}
					ubb.closeConnection();
				}
			}
		}
		
		if (flag){
			request.getRequestDispatcher("articleList.jsp").forward(request,
					response);
		} else {
			request.getRequestDispatcher("error.jsp")
					.forward(request, response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void init() throws ServletException {

	}
	
	private boolean isValidAttribute(String attribute){
		boolean result = false;
		if (attribute == null
				|| attribute.length() == 0
				|| attribute.equalsIgnoreCase("article_id")
				|| attribute.equalsIgnoreCase("category_id")
				|| attribute.equalsIgnoreCase("author_id")
				|| attribute.equalsIgnoreCase("title")
				|| attribute.equalsIgnoreCase("date")){
			result = true;
		}
		return result;
	}

	private boolean isValidStatus(int status){
		boolean result = false;
		if (status == ConfigProperty.STATUS_NORMAL
				|| status == ConfigProperty.STATUS_PENDING
				|| status == ConfigProperty.STATUS_DRAFT
				|| status == ConfigProperty.STATUS_TRASH){
			result = true;
		}
		return result;
	}
}
