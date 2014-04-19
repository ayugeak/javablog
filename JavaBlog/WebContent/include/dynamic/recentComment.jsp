<%@page import="javablog.util.StrFilter"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="javablog.util.ConfigProperty"%>
<%@ page import="javablog.bean.CommentBean,javablog.bean.CommentBeanBo
,javablog.bean.ArticleBeanBo,javablog.bean.ArticleBean"%>
<%@page import="javablog.database.DBConnectionPool,java.sql.Connection"%>
<%
	String countText = request.getParameter("count");
	if (countText != null){
		StrFilter sf= new StrFilter();
		int pageSize = sf.parseInt(countText);
		if (pageSize < 4 || pageSize > 16){
			pageSize = Integer.parseInt(ConfigProperty.configCtrl.getConfigValue("front_recent_comment_count"));
		}
%>
<ul>
	<%
	Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
	if (connection != null){
		CommentBeanBo cbb = new CommentBeanBo(connection);
		cbb.setPageSize(pageSize);
		cbb.setFilter(ConfigProperty.STATUS_NORMAL, 0, 0, "comment_id", "desc");
		ArrayList<CommentBean> al = cbb.getComments(1);
		ArticleBeanBo abb = new ArticleBeanBo(connection);
		for (int i = 0; i < al.size(); i++) {
			CommentBean cb = al.get(i);
			ArticleBean ab = abb.getArticle(cb.getArticleId());
	%>
	<li>
		<a href="<%=cb.getUrl()%>" target="blank"><%=cb.getName()%></a> on <a href="article/<%=cb.getArticleId() %>/"><%=ab.getTitle() %></a>
	</li>
	<%
		}
		cbb.closeConnection();
	}
	%>
</ul>
<%
}
%>
