<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="javablog.bean.ArticleBean,javablog.bean.ArticleBeanBo"%>
<%@page import="javablog.util.ConfigProperty,javablog.util.StrFilter"%>
<%@page import="javablog.database.DBConnectionPool,java.sql.Connection"%>
<%
	String attribute = request.getParameter("attribute");
	String countText = request.getParameter("count");
	if (countText != null){
		StrFilter sf = new StrFilter();
		attribute = sf.htmlFilter(attribute).trim();
		int pageSize = sf.parseInt(countText);
		if (pageSize < 4 || pageSize > 16){
			pageSize = Integer.parseInt(ConfigProperty.configCtrl.getConfigValue("front_recent_post_count"));
		}
		if (attribute == null || (!attribute.equalsIgnoreCase("article_id")
				&& !attribute.equalsIgnoreCase("date"))){
			attribute = "article_id";
		}
 %>
<ul>
<%
	Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
	if (connection != null){
		ArticleBeanBo abb = new ArticleBeanBo(connection);
		abb.setPageSize(pageSize);
		abb.setFilter(ConfigProperty.STATUS_NORMAL, 0, 0, attribute, "desc");
		ArrayList<ArticleBean> al = abb.getArticles(1);
		for (int i = 0; i < al.size(); i++) {
			ArticleBean ab = al.get(i);
%>
	<li>
		<a href="article/<%=ab.getArticleId()%>/"><%=ab.getTitle() %></a>
	</li>
<%
		}
		abb.closeConnection();
	}
%>
</ul>
<%
}
%>