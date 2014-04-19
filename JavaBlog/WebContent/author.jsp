<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="javablog.util.ConfigProperty"%>
<%
	String author=(String)request.getAttribute("author");
	if (author == null) {
		response.sendRedirect("error/");
	} else {
%>
<!DOCTYPE HTML>
<html>
	<head>
		<jsp:include page="include/base.jsp"></jsp:include>
		<script type="text/javascript" src="js/main.js"></script>
		<link rel="stylesheet" type="text/css" href="css/main.css" />
		<link rel="stylesheet" type="text/css" href="css/blog.css" />
		<title>Articles&nbsp;of&nbsp;<%=author %>&nbsp;-&nbsp;<%=ConfigProperty.website_name %></title>
	</head>
	<body onload="scrollInit();">
		<div id="header">
			<div class="wrapper">
				<jsp:include page="include/header.jsp"></jsp:include>
			</div>
		</div>
		<div id="container">
			<div id="main">
			<div id="main_div">
				<h2 class="headtitle">
					Author:&nbsp;<%=author %>
				</h2>
				<jsp:include page="include/articleList.jsp"></jsp:include>
				<div class="fy">
					<jsp:include page="include/page.jsp"></jsp:include>
				</div>
			</div>
			</div>
			<div id="sidebar">
				<jsp:include page="include/sideBar.jsp"></jsp:include>
			</div>
		</div>
		<div id="footer">
			<div class="wrapper">
				<jsp:include page="include/footer.jsp"></jsp:include>
			</div>
		</div>
	</body>
</html>
<%
}
%>