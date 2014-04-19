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
		<link rel="stylesheet" type="text/css" href="css/about.css" />
		<title>About&nbsp;-&nbsp;<%=ConfigProperty.website_name %></title>
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
					About FreeCody
				</h2>
				<div class="aboutitem">
					<h3><a href="">Home</a></h3>
					<div>
						主页会显示我最近更新的文章
					</div>
				</div>
				<div class="aboutitem">
					<h3><a href="archive/">Archive</a></h3>
					<div>
						归档页面将所有文章按月份分组，并按时间倒序列出文章标题
					</div>
				</div>
				<div class="aboutitem">
					<h3><a href="project/">Project</a></h3>
					<div>
						项目页面会放一些我自己写的，并且觉得有点意思的项目源文件，可能只是好玩的小程序或开源项目
					</div>
				</div>
				<div class="aboutitem">
					<h3><a href="feed/">RSS</a></h3>
					<div>
						RSS订阅可以让你方便查看博客更新
					</div>
				</div>
				<h2 class="headtitle">
					About me
				</h2>
				<div class="aboutitem">
					<img src="images/avatar.png" alt="" />
					我是华中科技大学计算机学院的大四学生
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