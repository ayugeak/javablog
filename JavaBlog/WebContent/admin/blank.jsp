<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String user = (String) session.getAttribute("userId");
	if (user != null) {
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <jsp:include page="admin-base.jsp"></jsp:include>
    <style type="text/css">
	  body{
		  padding:0px;
		  margin:0px;
		  background:#FFFFFF;
		  cursor:text;
		  word-wrap: break-word;
	  }
    </style>
	</head>
  <body id="textarea" contentEditable="true"
  onchange="document.parent.getElementById('isSaved').value='0'">
    
  </body>
</html>
<%}%>