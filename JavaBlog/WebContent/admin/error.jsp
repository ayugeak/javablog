<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String error=(String)request.getAttribute("error");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <jsp:include page="admin-base.jsp"></jsp:include>
    <title>Error</title>
  </head>
  <body>
  		<div class="titlebar">
			Error
		</div>
    	<h3><%=error %></h3>
  </body>
</html>
