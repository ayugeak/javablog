<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat,javablog.bean.ArticleBean
,javablog.bean.CategoryBean,javablog.bean.TagBean
,javablog.bean.UserBean,javablog.bean.MetaModel"%>
<%@page import="javablog.util.ConfigProperty"%>
<%
    ArticleBean ab = (ArticleBean) request.getAttribute("article");
	if (ab == null) {
		response.sendRedirect("error/");
	} else {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String color = "color_" + (int)(Math.random() * 4 + 1);//random date color
		CategoryBean category = (CategoryBean)request.getAttribute("category");
		UserBean author = (UserBean)request.getAttribute("author");
		ArrayList<MetaModel> almm = (ArrayList<MetaModel>)request.getAttribute("relatedArticleList");
		MetaModel pab = (MetaModel)request.getAttribute("previous");
		MetaModel nab = (MetaModel)request.getAttribute("next");
		ArrayList<TagBean> altb = (ArrayList<TagBean>)request.getAttribute("tagList");
%>
<!DOCTYPE HTML>
<html>
	<head>
		<jsp:include page="include/base.jsp"></jsp:include>
		<script type="text/javascript" src="js/main.js"></script>
		<script type="text/javascript" src="js/comment.js"></script>
		<link rel="stylesheet" type="text/css" href="css/main.css" />
		<link rel="stylesheet" type="text/css" href="css/blog.css" />
		<link rel="stylesheet" type="text/css" href="css/comment.css" />
		<title><%=ab.getTitle()%>&nbsp;-&nbsp;<%=ConfigProperty.website_name %></title>
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
					<%=ab.getTitle() %>
				</h2>
				<div class="articlebox">
					<div class="headline">
						<div class="date <%=color %>">
							<p class="year"><%=sdf.format(ab.getDate()).substring(2, 7)%></p>
							<p class="day"><%=sdf.format(ab.getDate()).substring(8, 10)%></p>
						</div>
						<div class="title">
							<h2 class="posttitle">
								<%=ab.getTitle()%>
							</h2>
							<div class="category">
								<div class="category_info left">
								Category:&nbsp;<a href="category/<%=category.getCategoryId() %>/"><%=category.getName()%></a>
								</div>
								<div class="tag_info left">
								<span class="left tag_label">Tags:</span>
								<%
									for (int j = 0; j < altb.size(); j++) {
										TagBean tb = altb.get(j);
								%>
								<span class="tag_link">
									<a href="tag/<%=tb.getTagId()%>/"><%=tb.getName()%></a>
								</span>
								<%
									}
								%>
								</div>
							</div>
						</div>
						<div class="postauthor">
							<div class="left">
								<div class="postby">
									Posted by
								</div>
								<div class="author">
									<a href="author/<%=author.getUserId() %>/"><%=author.getName() %></a>
								</div>
							</div>
							<div class="right">
								<img alt="" class="avatar" src="<%=author.getAvatar()%>" />
							</div>
						</div>
					</div>
					<div class="blogcontent"><%=ab.getContent().replace("[...]","") %></div>
				</div>
				<div class="state">
					转载本站文章，需注明出处
				</div>
				<div id="otherarticle">
					<span class="left">&laquo;
					<%
						if (pab != null){
					%>
						<a href="article/<%=pab.getKey() %>/"><%=pab.getValue() %></a>
					<%
						} else {
					%>
							None
					<%
						}
					%>
					</span>
					<span class="right">
					<%
						if (nab != null){
					%>
						<a href="article/<%=nab.getKey() %>/"><%=nab.getValue() %></a>
					<%
						} else {
					%>
							None
					<%
						}
					%>&raquo;
					</span>
				</div>
				<div id="relatedArticle">
					<div class="headtitle">
						Related articles
					</div>
					<ul>
					<%
						for (int i = 0; i < almm.size(); i++){
							MetaModel mm = almm.get(i);
					%>
							<li><a href="article/<%=mm.getKey() %>/"><%=mm.getValue() %></a></li>
					<%
						}
					%>
					</ul>				
				</div>
				<div class="headtitle">
					Totally <%=ab.getStatus() %> Comments
				</div>
				<div id="commentlist">
					<jsp:include page="include/commentList.jsp"></jsp:include>
				</div>
				<div class="fy">
					<jsp:include page="include/page.jsp"></jsp:include>
				</div>
				<div id="newcomment">
					<jsp:include page="include/newComment.jsp">
						<jsp:param value="<%=ab.getArticleId() %>" name="articleId" />
					</jsp:include>
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
		<!-- JiaThis Button BEGIN -->
		<script type="text/javascript" src="http://v3.jiathis.com/code/jiathis_r.js?uid=1334710971143378&move=0" charset="utf-8"></script>
		<!-- JiaThis Button END -->
	</body>
</html>
<%
	}
%>
