<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="javablog.bean.UserBean,javablog.bean.UserBeanBo" %>
<%@page import="javablog.database.DBConnectionPool,java.sql.Connection"%>
<%
	String articleId = request.getParameter("articleId");
	String user = (String) session.getAttribute("userId");
	String avatarUrl = "images/avatar.png";
	String display = "block";
	String name = "Guest";
	if (user != null) {//admin
		Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
		if (connection != null){
			UserBeanBo ubb = new UserBeanBo(connection);
			UserBean ub = ubb.getUser(Integer.parseInt(user));
			if (ub != null){
				name = ub.getName();
				avatarUrl = ub.getAvatar();
				display = "none";
			}
			ubb.closeConnection();
		}
	}
%>
<a id="comment"></a>
<div>
	Hello, <%=name %>, feel free to comment
</div>
<form>
	<div class="left">
		<div style="display:<%=display %>" id="newinfo">
			<div>
				<input type="text" class="left input" id="name" placeholder="Name"/>
				<label class="right label"><font class="strong" title="Can't be empty">*</font>&nbsp;(1&le;length&lt;32)</label>
			</div>
			<div>
				<input type="text" class="left input" id="email" placeholder="Email"/>
				<label class="right label"><font class="strong" title="Can't be empty">*</font>&nbsp;(1&le;length&lt;256)</label>
			</div>
			<div>
				<input type="text" class="left input" id="url" placeholder="Homepage"/>
				<label class="right label">&nbsp;&nbsp;(0&le;length&lt;256)</label>
			</div>
		</div>
		<div id="contentbox">
			<textarea id="content" placeholder="Comment"></textarea>
		</div>
		<div id="submitbox">
			<span>
				<input class="button" id="submit" type="button" onclick="addComment(<%=articleId %>)" value="Post Comment" />
			</span>
			<span id="submitmsg">(1&le;length&lt;512)</span>
		</div>
	</div>
	<div class="right">
		<img class="avatar" src="<%=avatarUrl %>" alt="Avatar" />
	</div>
</form>