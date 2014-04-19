package javablog.servlet;

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
import javablog.util.ValidChecker;

public class AddComment extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public AddComment() {
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
			String articleIdText = request.getParameter("articleId");//article id
			String name = request.getParameter("name");//user name
			String email = request.getParameter("email");//email
			String url = request.getParameter("url");//homepage
			String content = request.getParameter("content");//content
			
			StrFilter sf = new StrFilter();
			int articleId = sf.parseInt(articleIdText);
			name = sf.htmlFilter(name).trim();
			email = sf.htmlFilter(email).trim();
			url = sf.htmlFilter(url).trim();
			content = sf.htmlFilter(content).trim();

			String result = "f";
			HttpSession session = request.getSession(true);
			String userIdText = (String) session.getAttribute("userId");
			if ((userIdText != null) || ((articleId > 0)
					&& (name.length() > 0 && name.length() < 32)
					&& ValidChecker.isValidEmail(email)
					&& (url.length() < 256)
					&& (content.length() > 0 && content.length() < 512))){
				CommentBean cb = new CommentBean();
				cb.setArticleId(articleId);
				cb.setContent(content);
				cb.setStatus(ConfigProperty.STATUS_PENDING);
				
				Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
				if (connection != null){
					if (userIdText == null) {//guest
						cb.setUserId(0);
						cb.setName(name);
						cb.setEmail(email);
						cb.setUrl(url);
					} else {//admin
						int userId = Integer.parseInt(userIdText);
						UserBeanBo ubb = new UserBeanBo(connection);
						UserBean ub = ubb.getUser(userId);
						cb.setUserId(userId);
						cb.setName(ub.getName());
						cb.setEmail(ub.getEmail());
						cb.setUrl(ub.getUrl());
					}
					CommentBeanBo cbb = new CommentBeanBo(connection);
					if (cbb.addComment(cb)) {
						result = "s";//OK
					}
					cbb.closeConnection();
				}
			}
			//show result
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
