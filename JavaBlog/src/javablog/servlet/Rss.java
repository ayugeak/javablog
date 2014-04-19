package javablog.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javablog.bean.*;
import javablog.database.DBConnectionPool;
import javablog.util.ConfigProperty;

public class Rss extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public Rss() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/xml;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String basePath = request.getScheme() + "://"
							+ request.getServerName() + ":"
							+ request.getServerPort()
							+ request.getContextPath() + "/";
			Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
			if (connection != null){
				ArticleBeanBo abb = new ArticleBeanBo(connection);
				abb.setPageSize(5);
				abb.setFilter(ConfigProperty.STATUS_NORMAL + "", "0",
						"SELECT category_id FROM categories WHERE status=" + ConfigProperty.STATUS_NORMAL,
						"SELECT user_id FROM users WHERE status=" + ConfigProperty.STATUS_NORMAL, "article_id", "desc");
				
				ArrayList<ArticleBean> al = abb.getArticles(1);
				CategoryBeanBo cbb = new CategoryBeanBo(connection);
				CategoryBean cb;
				UserBeanBo ubb = new UserBeanBo(connection);
				UserBean ub;
				out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				out.println("<rss version=\"2.0\">");
				out.println("<channel>");
				out.println("<title>" + ConfigProperty.website_name + "</title>");
				out.println("<link>" + ConfigProperty.website_name + "</link>");
				out.println("<description>" + ConfigProperty.website_name + " Latest Articles</description>");
				out.println("<language>zh-CN</language>");
				out.println("<copyright>Copyright (C) " + ConfigProperty.website_name + " All rights reserved.</copyright>");
				out.println("<generator>" + ConfigProperty.website_name + " RSS Generator</generator>");
				out.println("<docs>" + basePath + "feed/</docs>");
				int index;
				for (int i = 0; i < al.size(); i++) {
					ArticleBean ab = al.get(i);
					cb = cbb.getCategory(ab.getCategoryId());
					ub = ubb.getUser(ab.getAuthorId());
					out.println("<item>");
					out.println("<title><![CDATA[" + ab.getTitle() + "]]></title>");
					out.println("<link>"+basePath+"article/"
							+ ab.getArticleId() + "/</link>");
					out.println("<category><![CDATA[" + cb.getName() + "]]></category>");
					out.println("<author><![CDATA[" + ub.getName() + "]]></author>");
					out.println("<pubDate>" + ab.getDate().toGMTString() + "</pubDate>");
					out.println("<guid>"+basePath+"article/"
							+ ab.getArticleId() + "/</guid>");
					out.println("<description><![CDATA[");
					index = ab.getContent().indexOf("[...]");
					if(index > 0){
						out.print(ab.getContent().substring(0, index));
					}else{
						out.print(ab.getContent());
					}
					out.println("]]></description>");
					out.println("</item>");
				}
				out.println("</channel>");
				out.println("</rss>");
				abb.closeConnection();
			} else {
				request.getRequestDispatcher(basePath + "error/").forward(request, response);
			}
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
