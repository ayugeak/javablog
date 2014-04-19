package javablog.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javablog.bean.*;
import javablog.database.DBConnectionPool;
import javablog.util.ConfigProperty;

public class Archive extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public Archive() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
		if (connection != null){
			ArticleBeanBo abb = new ArticleBeanBo(connection);
			//给前台用户看的article，其分类和作者都必须是合法的（status=normal）
			abb.setFilter(ConfigProperty.STATUS_NORMAL + "", "0",
					"SELECT category_id FROM categories WHERE status=" + ConfigProperty.STATUS_NORMAL,
					"SELECT user_id FROM users WHERE status=" + ConfigProperty.STATUS_NORMAL, "article_id", "desc");
			ArrayList<ArticleBean> al = abb.getArticles(0);
			
			request.setAttribute("articleList", al);
			abb.closeConnection();
			request.getRequestDispatcher("archive.jsp").forward(request, response);
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
