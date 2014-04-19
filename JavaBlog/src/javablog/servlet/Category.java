package javablog.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javablog.bean.CategoryBean;
import javablog.bean.CategoryBeanBo;
import javablog.bean.CommentBeanBo;
import javablog.bean.ResultBean;
import javablog.bean.ArticleBean;
import javablog.bean.ArticleBeanBo;
import javablog.database.DBConnectionPool;
import javablog.util.ConfigProperty;
import javablog.util.StrFilter;

public class Category extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public Category() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		String categoryIdText = request.getParameter("categoryId");
		String pageNowText = request.getParameter("pageNow");
		
		StrFilter sf = new StrFilter();
		int categoryId = sf.parseNum(categoryIdText);
		int pageNow = sf.parseInt(pageNowText);
		if (pageNow == 0) {
			pageNow = 1;
		}
		
		boolean flag = false;
		if ((pageNow > 0) && (categoryId > 0)){
			Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
			if (connection != null){
				CategoryBeanBo cbb = new CategoryBeanBo(connection);
				CategoryBean cb = cbb.getCategory(categoryId);
				if (cb != null && cb.getStatus() == ConfigProperty.STATUS_NORMAL) {
					ArticleBeanBo abb = new ArticleBeanBo(connection);
					abb.setPageSize(ConfigProperty.front_blog_page_size);
					abb.setFilter(ConfigProperty.STATUS_NORMAL + "", "0", categoryId + "",
							"SELECT user_id FROM users WHERE status=" + ConfigProperty.STATUS_NORMAL, "article_id", "desc");
					
					ArrayList<ArticleBean> al = abb.getArticles(pageNow);
					CommentBeanBo commentBeanBo = new CommentBeanBo(connection);
					for (int i = 0; i < al.size(); i++) {
						commentBeanBo.setFilter(ConfigProperty.STATUS_NORMAL, al.get(i)
								.getArticleId(), 0, null, null);
						al.get(i).setStatus(commentBeanBo.getRowCount());
					}
					ResultBean result = new ResultBean();
					result.setPageNow(pageNow);
					result.setRowCount(abb.getRowCount());
					result.setPageCount(abb.getPageCount());
					result.setUrl("category/" + categoryId + "/");
	
					request.setAttribute("result", result);
					request.setAttribute("articleList", al);
					request.setAttribute("category", cb.getName());
					flag = true;
				}
				cbb.closeConnection();
			}
		}
		
		if (flag){
			request.getRequestDispatcher("category.jsp").forward(request,
					response);
		} else {
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

	public void init() throws ServletException {

	}

}
