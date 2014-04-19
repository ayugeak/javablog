<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="javablog.bean.UserBean,javablog.bean.RoleBean
,javablog.bean.RoleBeanBo,javablog.bean.UserBeanBo,javablog.util.ConfigProperty" %>
<%@page import="javablog.database.DBConnectionPool,java.sql.Connection"%>
<ul class="menu">
<%
	String userIdText = (String) session.getAttribute("userId");
	if (userIdText != null) {
	    Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
        if (connection != null){
			UserBeanBo ubb = new UserBeanBo(connection);
			UserBean ub = ubb.getUser(Integer.parseInt(userIdText));
			if (ub != null && ub.getStatus() == ConfigProperty.STATUS_NORMAL){
				RoleBeanBo ugbb = new RoleBeanBo(connection);
				RoleBean role = ugbb.getRole(ub.getRoleId());
				if (role != null && role.getStatus() == ConfigProperty.STATUS_NORMAL){
					if(role.canReadRole()){
%>
	<li>
		<a href="admin/RoleList?status=<%=ConfigProperty.STATUS_NORMAL %>" target="content">Roles</a>
	</li>
<%
	}
	if(role.canReadUser()){
%>
	<li>
		<a href="admin/UserList?status=<%=ConfigProperty.STATUS_NORMAL %>" target="content">Users</a>
	</li>
<%
	}
	if(role.canReadCategory()){
%>
	<li>
			<a href="admin/CategoryList?status=<%=ConfigProperty.STATUS_NORMAL %>" target="content">Categories</a>
	</li>
<%
	}
	if(role.canReadTag()){
		%>
	<li>
			<a href="admin/TagList?status=<%=ConfigProperty.STATUS_NORMAL %>" target="content">Tags</a>
	</li>
		<%
	}
	if(role.canAddArticle()){
%>
	<li>
			<a href="admin/newArticle.jsp" target="content">New Article</a>
	</li>
		<%
	}
	if(role.canReadArticle()){
%>
	<li>
		<a href="admin/ArticleList?status=<%=ConfigProperty.STATUS_NORMAL %>" target="content">Articles</a>
	</li>
<%
	}
	if(role.canReadComment()){
%>
	<li>
		<a href="admin/CommentList?status=<%=ConfigProperty.STATUS_NORMAL %>" target="content">Comments</a>
	</li>
<%
	}
	if(role.canReadLink()){
%>	
	<li>
		<a href="admin/LinkList?status=<%=ConfigProperty.STATUS_NORMAL %>" target="content">Links</a>
	</li>
<%
	}
	if(role.canReadMessage()){
%>
	<li>
			<a href="admin/MessageList?status=<%=ConfigProperty.STATUS_NORMAL %>" target="content">Messages</a>
	</li>
<%
	}
	if(role.canReadResource()){
%>
    <li>
            <a href="admin/ResourceList?status=<%=ConfigProperty.STATUS_NORMAL %>" target="content">Resources</a>
    </li>
<%
    }
	if(role.canReadLog()){
%>
	<li>
		<a href="admin/LogList?status=<%=ConfigProperty.STATUS_NORMAL %>" target="content">Logs</a>
	</li>
<%
	}
	if(role.canReadConfig()){
%>
	<li>
		<a href="admin/ConfigList" target="content">Setting</a>
	</li>
<%
					}
				}
			}
			ubb.closeConnection();
        }
	}
%>
</ul>