<%@page import="javablog.util.ConfigProperty"%>
<%@page import="javablog.bean.RoleBean"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String user = (String)session.getAttribute("userId");
	RoleBean role = (RoleBean)request.getAttribute("role");
	if (user == null || role == null){
		response.sendRedirect("error.jsp");
	} else {
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <jsp:include page="admin-base.jsp"></jsp:include>
    <title>Settings</title>
	<link rel="stylesheet" type="text/css" href="admin/css/admin.css">
	<link rel="stylesheet" type="text/css" href="admin/css/config.css">
	<script type="text/javascript" src="admin/js/admin.js"></script>
	<script type="text/javascript" src="admin/js/config.js"></script>
  </head>
  <body>
    <div class="titlebar">
		Settings
	</div>
	<div>
		<div class="tab_panel_bar">
			<a class="tab_panel_link tab_panel_link_current" href="javascript:void(0)" onclick="changeTab(0)">Front</a>
			<a class="tab_panel_link" href="javascript:void(0)" onclick="changeTab(1)">Admin</a>
		</div>
    	<div style="display:block" class="tabPanel">
    		<div>
				<label>Website name:</label>
				<input type="text" id="website_name"
					value="<%=ConfigProperty.website_name %>"/>
			</div>
			<div>
				<label>Blog page size:</label>
				<input type="text" id="front_blog_page_size"
					value="<%=ConfigProperty.front_blog_page_size %>"/>
			</div>
			<div>
				<label>Page count:</label>
				<input type="text" id="front_page_count"
					value="<%=ConfigProperty.front_page_count %>"/>
			</div>
			<div>
				<label>Recent post count:</label>
				<input type="text" id="front_recent_post_count"
					value="<%=ConfigProperty.front_recent_post_count %>"/>
			</div>
			<div>
				<label>Recent comment count:</label>
				<input type="text" id="front_recent_comment_count"
					value="<%=ConfigProperty.front_recent_comment_count %>"/>
			</div>
			<div>
				<label>Comment page size:</label>
				<input type="text" id="front_comment_page_size"
					value="<%=ConfigProperty.front_comment_page_size %>"/>
			</div>
		</div>
		<div class="tabPanel">
			<div>
				<label>Role page size:</label>
				<input type="text" id="admin_role_page_size"
					value="<%=ConfigProperty.admin_role_page_size %>"/>
			</div>
			<div>
				<label>User page size:</label>
				<input type="text" id="admin_user_page_size"
					value="<%=ConfigProperty.admin_user_page_size %>"/>
			</div>
			<div>
				<label>Category page size:</label>
				<input type="text" id="admin_category_page_size"
					value="<%=ConfigProperty.admin_category_page_size %>"/>
			</div>
			<div>
				<label>Tag page size:</label>
				<input type="text" id="admin_tag_page_size"
					value="<%=ConfigProperty.admin_tag_page_size %>"/>
			</div>
			<div>
				<label>Article page size:</label>
				<input type="text" id="admin_article_page_size"
					value="<%=ConfigProperty.admin_article_page_size %>"/>
			</div>
			<div>
				<label>Comment page size:</label>
				<input type="text" id="admin_comment_page_size"
					value="<%=ConfigProperty.admin_comment_page_size %>"/>
			</div>
			<div>
				<label>Link page size:</label>
				<input type="text" id="admin_link_page_size"
					value="<%=ConfigProperty.admin_link_page_size %>"/>
			</div>
			<div>
				<label>Message page size:</label>
				<input type="text" id="admin_message_page_size"
					value="<%=ConfigProperty.admin_message_page_size %>"/>
			</div>
			<div>
				<label>Resource page size:</label>
				<input type="text" id="admin_resource_page_size"
					value="<%=ConfigProperty.admin_resource_page_size %>"/>
			</div>
			<div>
				<label>Log page size:</label>
				<input type="text" id="admin_log_page_size"
					value="<%=ConfigProperty.admin_log_page_size %>"/>
			</div>
			<div>
				<label>Email:</label>
				<input type="text" id="admin_email"
					value="<%=ConfigProperty.admin_email %>"/>
			</div>
			<div>
				<label>Password:</label>
				<input type="password" id="admin_password"
					value="<%=ConfigProperty.admin_password %>"/>
			</div>
		</div>
		<div class="tab_panel_bottom">
			<input class="button" type="button" value="Save" onclick="saveConfig()"/>
		</div>
	</div>
  </body>
</html>
<%
}
%>