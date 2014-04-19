package javablog.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javablog.bean.CommentBeanBo;
import javablog.bean.ResultBean;
import javablog.bean.ArticleBean;
import javablog.bean.ArticleBeanBo;
import javablog.database.DBConnectionPool;
import javablog.util.ConfigProperty;
import javablog.util.StrFilter;

public class Blog extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public Blog() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		String pageNowText = request.getParameter("pageNow");//page now
		
		StrFilter sf = new StrFilter();
		int pageNow = sf.parseInt(pageNowText);
		if (pageNow == 0) {
			pageNow = 1;
		}

		boolean flag = false;
		if (pageNow > 0){
			Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
			if (connection != null){
				ArticleBeanBo abb = new ArticleBeanBo(connection);
				abb.setPageSize(Integer.parseInt(ConfigProperty.configCtrl.getConfigValue("front_blog_page_size")));
				abb.setFilter(ConfigProperty.STATUS_NORMAL + "", "0",
						"SELECT category_id FROM categories WHERE status=" + ConfigProperty.STATUS_NORMAL,
						"SELECT user_id FROM users WHERE status=" + ConfigProperty.STATUS_NORMAL, "article_id", "desc");
				ArrayList<ArticleBean> al = abb.getArticles(pageNow);
				CommentBeanBo cbb = new CommentBeanBo(connection);
				for (int i = 0; i < al.size(); i++) {
					cbb.setFilter(ConfigProperty.STATUS_NORMAL, al.get(i).getArticleId(), 0,
							null, null);
					al.get(i).setStatus(cbb.getRowCount());
				}
				ResultBean result = new ResultBean();
				result.setPageNow(pageNow);
				result.setRowCount(abb.getRowCount());
				result.setPageCount(abb.getPageCount());
				result.setUrl("");
				
				request.setAttribute("result", result);
				request.setAttribute("articleList", al);
				flag = true;
				abb.closeConnection();
			}
		}
		
		if (flag){
			request.getRequestDispatcher("index.jsp").forward(request, response);
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
