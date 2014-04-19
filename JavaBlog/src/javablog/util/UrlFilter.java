package javablog.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UrlFilter implements Filter {

	// The filter configuration object we are associated with. If
	// this value is null, this filter instance is not currently
	// configured.
	private FilterConfig filterConfig = null;

	public UrlFilter() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		//httpRequest.getRequestURL()=http://xxx/JavaBlog/xxx
		//httpRequest.getRequestURI()=/JavaBlog/xxx
		/*String basePath = httpRequest.getScheme() + "://"
					+ httpRequest.getServerName() + ":"
					+ httpRequest.getServerPort()
					+ httpRequest.getContextPath() + "/";
		httpResponse.sendRedirect(basePath + "error/");
		}*/
		//System.out.println(httpRequest.getRemoteAddr());//客户端地址
		//System.out.println(httpRequest.getHeader("user-agent"));//浏览器信息
		if (this.isIE(httpRequest.getHeader("user-agent"))){//we don't support IE
			PrintWriter out = httpResponse.getWriter();
			try{
				out.print("Sorry, this website doesn't support IE!");
			} finally {
				out.flush();
				out.close();
			}
		} else {
			String uri = this.getRealUri(httpRequest.getRequestURI().substring(httpRequest.getContextPath().length()));//remove contextPath
			if (uri == null){//admin
				chain.doFilter(request, response);
			} else {//前台
				httpRequest.getRequestDispatcher(uri).forward(httpRequest,
						httpResponse);
			}
		}
	}

	private Matcher getMatcher(String uri, String regex) {
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(uri);
		return matcher;
	}

	private String getRealUri(String uri) {
		String realUri = null;
		Matcher matcher;
		if ((matcher = this.getMatcher(uri, "/admin[/]?.*")).matches()) {//不对后台管理进行URL转化
			realUri = null;
		} else if ((matcher = this.getMatcher(uri, "/[0-9]*[/]?")).matches()) {//
			realUri = "/Blog?pageNow=";
			if ((matcher = this.getMatcher(uri, "/([0-9]*)[/]?")).matches()) {
				realUri += matcher.group(1);
			}
		} else if ((matcher = this.getMatcher(uri,
				"/category/([0-9]*)[/]?[0-9]*[/]?")).matches()) {//category
			realUri = "/Category?categoryId=" + matcher.group(1) + "&pageNow=";
			if ((matcher = this.getMatcher(uri,
					"/category/([0-9]*)/([0-9]*)[/]?")).matches()) {
				realUri += matcher.group(2);
			}
		} else if ((matcher = this.getMatcher(uri,
				"/tag/([0-9]*)[/]?[0-9]*[/]?")).matches()) {//tag
			realUri = "/Tag?tagId=" + matcher.group(1) + "&pageNow=";
			if ((matcher = this.getMatcher(uri, "/tag/([0-9]*)/([0-9]*)[/]?"))
					.matches()) {
				realUri += matcher.group(2);
			}
		} else if ((matcher = this.getMatcher(uri,
				"/author/([0-9]*)[/]?[0-9]*[/]?")).matches()) {//author
			realUri = "/Author?authorId=" + matcher.group(1) + "&pageNow=";
			if ((matcher = this
					.getMatcher(uri, "/author/([0-9]*)/([0-9]*)[/]?"))
					.matches()) {
				realUri += matcher.group(2);
			}
		} else if ((matcher = this.getMatcher(uri,
				"/search/([^/]*)[/]?([0-9]*)[/]?")).matches()) {//search
			realUri = "/Search?q=" + matcher.group(1) + "&pageNow=";
			if ((matcher = this.getMatcher(uri, "/search/([^/]*)/([0-9]*)[/]?"))
					.matches()) {
				realUri += matcher.group(2);
			}
		} else if ((matcher = this.getMatcher(uri, "/archive[/]?")).matches()) {//archive
			realUri = "/Archive";
		} else if ((matcher = this.getMatcher(uri, "/article/([0-9]*)[/]?[0-9]*[/]?"))
				.matches()) {//article
			realUri = "/ShowArticle?articleId=" + matcher.group(1) + "&pageNow=";
			if ((matcher = this.getMatcher(uri, "/article/([0-9]*)/([0-9]*)[/]?"))
					.matches()) {
				realUri += matcher.group(2);
			}
		} else if ((matcher = this.getMatcher(uri, "/feed[/]?")).matches()) {//feed
			realUri = "/Rss";
		} else if ((matcher = this.getMatcher(uri, "/about[/]?")).matches()) {//about
			realUri = "/about.jsp";
		} else if ((matcher = this.getMatcher(uri, "/project[/]?")).matches()) {//project
			realUri = "/project.jsp";
		} else if ((matcher = this.getMatcher(uri, "/error[/]?")).matches()) {//error
			realUri = "/error.jsp";
		}
		
		return realUri;
	}

	/**
	 * Return the filter configuration object for this filter.
	 */
	public FilterConfig getFilterConfig() {
		return (this.filterConfig);
	}

	/**
	 * Set the filter configuration object for this filter.
	 * 
	 * @param filterConfig
	 *            The filter configuration object
	 */
	public void setFilterConfig(FilterConfig filterConfig) {

		this.filterConfig = filterConfig;
	}

	/**
	 * Destroy method for this filter
	 * 
	 */
	public void destroy() {
	}

	/**
	 * Init method for this filter
	 * 
	 */
	public void init(FilterConfig filterConfig) {

		this.filterConfig = filterConfig;
	}
	
	private boolean isIE(String userAgent){
		boolean result = false;
		if (userAgent.contains("MSIE")){
			result = true;
		}
		return result;
	}

}
