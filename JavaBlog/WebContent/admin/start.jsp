<%@page import="javablog.bean.CommentBeanBo"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="javablog.bean.CategoryBeanBo,javablog.bean.ArticleBeanBo,javablog.bean.LinkBeanBo" %>
<%@page import="javablog.database.DBConnectionPool,java.sql.Connection"%>
<%
	String user=(String)session.getAttribute("userId");
	if (user == null) {
		response.sendRedirect("error.jsp");
	} else {
		Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
		if (connection != null){
		    CategoryBeanBo cbb=new CategoryBeanBo(connection);
		    ArticleBeanBo abb=new ArticleBeanBo(connection);
		    CommentBeanBo commentBeanBo = new CommentBeanBo(connection);
		    LinkBeanBo lbb=new LinkBeanBo(connection);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <jsp:include page="admin-base.jsp"></jsp:include>
    <title>Starting page</title>
	<link rel="stylesheet" type="text/css" href="admin/css/admin.css">
	<script type="text/javascript" src="admin/js/admin.js"></script>
  </head>
  <body>
    <div class="titlebar">
		Starting page
	</div>
    <div>
     <ul>
     	<li>Articles:<%=abb.getRowCount() %></li>
     	<li>Categories:<%=cbb.getRowCount() %></li>
     	<li>Comments:<%=commentBeanBo.getRowCount() %></li>
     	<li>Links:<%=lbb.getRowCount() %></li>
     </ul>
    </div>
  </body>
</html>
<%
			lbb.closeConnection();
		}
	}
%>