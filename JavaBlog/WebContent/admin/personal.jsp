<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="javablog.bean.UserBean,javablog.bean.UserBeanBo" %>
<%@page import="javablog.database.DBConnectionPool,java.sql.Connection"%>
<%
	String user = (String) session.getAttribute("userId");
	if (user == null) {
		response.sendRedirect("login.jsp");
	} else {
		Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
		if (connection != null){
			UserBeanBo ubb=new UserBeanBo(connection);
			UserBean ub=ubb.getUser(Integer.parseInt(user));
			ubb.closeConnection();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <jsp:include page="admin-base.jsp"></jsp:include>
    <title>My account</title>
	<link rel="stylesheet" type="text/css" href="admin/css/admin.css">
	<link rel="stylesheet" type="text/css" href="admin/css/profile.css">
	<script type="text/javascript" src="admin/js/admin.js"></script>
	<script type="text/javascript" src="admin/js/account.js"></script>
  </head>
  <body>
   	<div class="titlebar">
		Setting
	</div>
	<div class="tab_panel_bar">
		<a class="tab_panel_link tab_panel_link_current" href="javascript:void(0)" onclick="changeTab(0)">My profile</a>
		<a class="tab_panel_link" href="javascript:void(0)" onclick="changeTab(1)">Login information</a>
	</div>
    <div style="display:block" class="tabPanel">
	    <div>
	    	<label>Avatar:</label>
	    	<img id="avatar" src="<%=ub.getAvatar() %>" title="Click to change" onclick="newAvatar()"/>
	    </div>
	    <div>
		    <label>Name:</label>
		    <input id="name" type="text" value="<%=ub.getName() %>"/>
	    </div>
	    <div>
		    <label>Email:</label>
		    <input id="email" type="text" value="<%=ub.getEmail() %>"/>
	    </div>
	    <div>
	    	<label>Homepage:</label>
	    	<input id="url" type="text" value="<%=ub.getUrl() %>"/>
	    </div>
	    <div>
	    	<input class="button" type="button" value="保存" onclick="modiInfo()"/>
	    </div>
    </div>
    <div class="tabPanel">
	    <div>
		    <label>Login name:</label>
		    <input type="text" id="loginname" value="<%=ub.getUsername() %>" disabled/>
		</div>
	    <div>
	    	<label>Password:</label>
	    	<input type="password" id="password"/>
	    </div>
	    <div>
	    	<label>Confirm password:</label>
	    	<input type="password" id="confirmpassword"/>
	    </div>
	    <div>
	    	<input class="button" type="button" value="保存" onclick="modiLogin()"/>
	    </div>
    </div>
  </body>
</html>
<%
		}
	}
%>