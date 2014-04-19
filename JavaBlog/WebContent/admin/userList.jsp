<%@page import="javablog.database.DBConnectionPool"%>
<%@page import="java.sql.Connection"%>
<%@page import="javablog.bean.RoleBean,javablog.bean.RoleBeanBo"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="javablog.bean.UserBean,javablog.util.ConfigProperty"%>
<%
	String user=(String)session.getAttribute("userId");
	ArrayList<UserBean> userList = (ArrayList<UserBean>)request.getAttribute("userList");
	if (user == null || userList == null) {
		response.sendRedirect("error.jsp");
	} else {
		int roleId = Integer.parseInt((String)request.getAttribute("roleId"));
        int pageNow = Integer.parseInt((String)request.getAttribute("pageNow"));
        int pageCount = Integer.parseInt((String)request.getAttribute("pageCount"));
        String attribute = (String)request.getAttribute("attribute");
        String order = (String)request.getAttribute("order");
        int status = Integer.parseInt((String)request.getAttribute("status"));
        String pageInfo=pageNow + "/" + pageCount + "页";
        if(pageNow > 1){
            pageInfo += "<a href='javascript:void(0)' onclick='userList.upperPage()'>上一页</a>";
        }
        if(pageNow < pageCount){
            pageInfo += "<a href='javascript:void(0)' onclick='userList.nextPage()'>下一页</a>";
        }
        Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
		if (connection != null){
	        RoleBeanBo rbb = new RoleBeanBo(connection);
	        RoleBean rb = rbb.getRole(roleId);
	        RoleBean roleBean = (RoleBean)request.getAttribute("role");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<jsp:include page="admin-base.jsp"></jsp:include>
		<title>Users</title>
		<link rel="stylesheet" type="text/css" href="admin/css/admin.css">
		<script type="text/javascript" src="admin/js/admin.js"></script>
		<script type="text/javascript" src="admin/js/user.js"></script>
		<script type="text/javascript">
			var userList=new UserList();
			userList.changeStatus=function(status){
                changeStatus('select',<%=ConfigProperty.ID_USER %>,status);
            };
            userList.deleteData=function(){
                delData('select',<%=ConfigProperty.ID_USER %>);
            };
            userList.setRoleId(<%=roleId %>);
			userList.setStatus(<%=status%>);
			userList.setPageNow(<%=pageNow%>);
			userList.setPageCount(<%=pageCount%>);
			userList.setAttribute("<%=attribute%>");
			userList.setOrder("<%=order%>");
        </script>
	</head>
	<body onload="userList.showToolBar()">
		<div class="titlebar">
			<span class="left">
                Users
                <%
                    if (rb != null){
                %>
                    &nbsp;of&nbsp;Role&nbsp;&quot;<a href="admin/UserList?roleId=<%=roleId %>"><%=rb.getName() %></a>&quot;
                <%
                    }
                %>
            </span>
            <span class="right">
                <input class="button" type="button" value="刷新" onclick="userList.getUserList()" />
                <input class="button" type="button" onclick="javascript:history.back()" value="返回"/>
                <input class="button" type="button" onclick="javascript:history.forward()" value="前进"/>
            </span>
		</div>
		<div class="filtermenu">
			<ul>
				<li><a <%if(status == ConfigProperty.STATUS_NORMAL)out.print("class='current'"); %> href="javascript:void(0)" onclick="userList.filter(<%=ConfigProperty.STATUS_NORMAL%>)">Normal</a></li>
				<li><a <%if(status == ConfigProperty.STATUS_TRASH)out.print("class='current'"); %> href="javascript:void(0)" onclick="userList.filter(<%=ConfigProperty.STATUS_TRASH%>)">Trash</a></li>
			</ul>
		</div>
		<input type="hidden" value="0" id="userId"/>
		<div id="roleDiv" style="display:none">
			<%
				
		        rbb.setFilter(ConfigProperty.STATUS_NORMAL, null, null);
		        ArrayList<RoleBean> roleList = rbb.getRoles(0);
		        
		        for (int i = 0; i < roleList.size(); i++){
		        	rb = roleList.get(i);
		        	out.print("<option value='" + rb.getRoleId() + "'>" + rb.getName() + "</option>");
		        }
			%>
		</div>
		<div class="toolbar">
			<div class="left">
			    <%
			        if (roleBean.canDeleteUser()) {
			    %>
				<input class="button" type="button" value="Delete" onclick="userList.deleteData()" />
				<%
			        }
				    if (roleBean.canAddUser()) {
				%>
				<input class="button" type="button" onclick="newUser()" value="Add"/>
				<%
			        }
			     if (roleBean.canEditUser()) {
					if (status == ConfigProperty.STATUS_TRASH) {
				%>
				<input class="button" type="button" value="Restore"
					onclick="userList.changeStatus(<%=ConfigProperty.STATUS_NORMAL %>)" />
				<%
					}else if(status == ConfigProperty.STATUS_NORMAL){
				%>
				<input class="button" type="button" value="Trash"
					onclick="userList.changeStatus(<%=ConfigProperty.STATUS_TRASH %>)" />
				<%
					}
			     }
				%>
			</div>
			<div class="page right">
			    <%=pageInfo %>
			</div>
		</div>
		<table id="dtable">
			<thead>
				<tr>
					<th><input type="checkbox" onclick="selectAll(this.checked,'select')" /></th>
					<th>
						<a href="javascript:void(0)" onclick="userList.sort('name')">Name</a>
					</th>
					<th>
                        <a href="javascript:void(0)" onclick="userList.sort('username')">Username</a>
                    </th>
					<th>
						<a href="javascript:void(0)" onclick="userList.sort('role_id')">Role</a>
					</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody id="datas">
				<%
		            for(int i=0; i < userList.size(); i++){
		                UserBean ub = userList.get(i);
		                rb = rbb.getRole(ub.getRoleId());
	            %>
	            <tr>
	                <td><input type="checkbox" value="<%=ub.getUserId() %>" name="select"/></td>
                    <td>
						<table>
	                		<tbody>
	                			<tr>
		                			<td>
		                				<img class="avatar" src="<%=ub.getAvatar() %>" alt="avatar"/>
		                			</td>
		                			<td>
		                				<div><%=ub.getName() %></div>
			                    		<a href="<%=ub.getUrl() %>" target="_blank"><%=ub.getUrl() %></a>
		                			</td>
	                			</tr>
	                		</tbody>
	                	</table>
					</td>
                    <td><%=ub.getUsername() %></td>
	                <td><a href="admin/UserList?roleId=<%=ub.getRoleId() %>"><%=rb.getName() %></a></td>
	                <td>
		                <a href="javascript:void(0)" onclick="modiUser(<%=i %>)">Edit</a>
		                <a href="admin/ArticleList?authorId=<%=ub.getUserId() %>">View articles</a>
	                </td>
                </tr>
	            <%
		            }
		            rbb.closeConnection();
	            %>
			</tbody>
		</table>
		<div class="toolbar">
		</div>
	</body>
</html>
<%
		}
	}
%>
