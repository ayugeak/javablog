<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="javablog.bean.ArticleBean,javablog.bean.ArticleBeanBo"%>
<%@page import="javablog.util.ConfigProperty"%>
<!DOCTYPE HTML>
<html>
	<head>
		<jsp:include page="include/base.jsp"></jsp:include>
		<script type="text/javascript" src="js/main.js"></script>
		<link rel="stylesheet" type="text/css" href="css/main.css"/>
		<link rel="stylesheet" type="text/css" href="css/blog.css" />
		<link rel="stylesheet" type="text/css" href="css/project.css" />
		<title>Project&nbsp;-&nbsp;<%=ConfigProperty.website_name %></title>
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
					Projects
				</h2>
				<div class="project_item">
					<h3 class="project_title">
					JavaBlog
					</h3>
					<div class="project_content">
					JavaBlog是一个基于JAVA的博客系统，也是本网站的源代码，它是一个具有完整功能的博客。
					目前处于测试阶段，等稳定后会放到GitHub上。同时我会在博客中与大家讨论该博客的实现。
					</div>
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