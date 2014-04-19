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
import javablog.bean.UserBean;
import javablog.bean.UserBeanBo;
import javablog.database.DBConnectionPool;
import javablog.util.ConfigProperty;
import javablog.util.StrFilter;

public class Author extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public Author() {
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
		String authorIdText = request.getParameter("authorId");//author id
		//filter the parameters
		StrFilter sf = new StrFilter();
		int pageNow = sf.parseInt(pageNowText);
		int authorId = sf.parseInt(authorIdText);
		if (pageNow == 0) {
			pageNow = 1;
		}

		boolean flag = false;
		if ((pageNow > 0) && (authorId > 0)){
			Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
			if (connection != null){
				UserBeanBo ubb = new UserBeanBo(connection);
				UserBean ub = ubb.getUser(authorId);
				if (ub != null && ub.getStatus() == ConfigProperty.STATUS_NORMAL) {//the author is valid
					//get articles
					ArticleBeanBo abb = new ArticleBeanBo(connection);
					abb.setPageSize(Integer.parseInt(ConfigProperty.configCtrl.getConfigValue("front_blog_page_size")));
					abb.setFilter(ConfigProperty.STATUS_NORMAL + "", "0",
							"SELECT category_id FROM categories WHERE status=" + ConfigProperty.STATUS_NORMAL,
							authorId + "", "article_id", "desc");
					ArrayList<ArticleBean> al = abb.getArticles(pageNow);
					//get comment count for each article and put it in article's status
					CommentBeanBo cbb = new CommentBeanBo(connection);
					for (int i = 0; i < al.size(); i++) {
						cbb.setFilter(ConfigProperty.STATUS_NORMAL,
								al.get(i).getArticleId(), 0, null, null);
						al.get(i).setStatus(cbb.getRowCount());
					}
					//set page info
					ResultBean result = new ResultBean();
					result.setPageNow(pageNow);
					result.setRowCount(abb.getRowCount());
					result.setPageCount(abb.getPageCount());
					result.setUrl("author/" + authorId + "/");
					//put those data into the request so that we can use them in author.jsp
					request.setAttribute("result", result);
					request.setAttribute("articleList", al);
					request.setAttribute("author", ub.getName());
					flag = true;
				}
				//don't forget close release the DB connection
				ubb.closeConnection();
			}
		}
		
		if (flag){
			request.getRequestDispatcher("author.jsp").forward(request,
					response);
		} else {
			/*//another way to go to error page
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":"
					+ request.getServerPort()
					+ request.getContextPath() + "/";
			response.sendRedirect(basePath + "error/");
			*/
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
