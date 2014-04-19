<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="javablog.bean.RoleBean,javablog.util.ConfigProperty"%>
<%
	String user=(String)session.getAttribute("userId");
	ArrayList<RoleBean> roleList = (ArrayList<RoleBean>)request.getAttribute("roleList");
	if (user == null || roleList == null) {
		response.sendRedirect("error.jsp");
	} else {
		int pageNow = Integer.parseInt((String)request.getAttribute("pageNow"));
        int pageCount = Integer.parseInt((String)request.getAttribute("pageCount"));
        String attribute = (String)request.getAttribute("attribute");
        String order = (String)request.getAttribute("order");
        int status = Integer.parseInt((String)request.getAttribute("status"));
        String pageInfo=pageNow + "/" + pageCount + "页";
        if(pageNow > 1){
            pageInfo += "<a href='javascript:void(0)' onclick='roleList.upperPage()'>上一页</a>";
        }
        if(pageNow < pageCount){
            pageInfo += "<a href='javascript:void(0)' onclick='roleList.nextPage()'>下一页</a>";
        }
        RoleBean roleBean = (RoleBean)request.getAttribute("role");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<jsp:include page="admin-base.jsp"></jsp:include>
		<title>Users</title>
		<link rel="stylesheet" type="text/css" href="admin/css/admin.css">
		<script type="text/javascript" src="admin/js/admin.js"></script>
		<script type="text/javascript" src="admin/js/role.js"></script>
		<script type="text/javascript">
			var roleList=new RoleList();
			roleList.changeStatus=function(status){
                changeStatus('select',<%=ConfigProperty.ID_ROLE %>,status);
            };
            roleList.deleteData=function(){
                delData('select',<%=ConfigProperty.ID_ROLE %>);
            };
			roleList.setStatus(<%=status%>);
			roleList.setPageNow(<%=pageNow%>);
			roleList.setPageCount(<%=pageCount%>);
			roleList.setAttribute("<%=attribute%>");
			roleList.setOrder("<%=order%>");
        </script>
	</head>
	<body onload="roleList.showToolBar()">
		<div class="titlebar">
			<span class="left">
                Roles
            </span>
            <span class="right">
                <input class="button" type="button" value="刷新" onclick="roleList.getRoleList()"/>
                <input class="button" type="button" onclick="javascript:history.back()" value="返回"/>
                <input class="button" type="button" onclick="javascript:history.forward()" value="前进"/>
            </span>
		</div>
		<div class="filtermenu">
			<ul>
				<li><a <%if(status==ConfigProperty.STATUS_NORMAL)out.print("class='current'"); %> href="javascript:void(0)" onclick="roleList.filter(<%=ConfigProperty.STATUS_NORMAL%>)">Normal</a></li>
				<li><a <%if(status==ConfigProperty.STATUS_TRASH)out.print("class='current'"); %> href="javascript:void(0)" onclick="roleList.filter(<%=ConfigProperty.STATUS_TRASH%>)">Trash</a></li>
			</ul>
		</div>
		<input type="hidden" value="0" id="roleId"/>
		<div id="permissionDiv" style="display:none">
		    <div class="perm_class">
			    <label>角色</label>
			    Read<input type="checkbox" class="perm_item"/>
                Add<input type="checkbox" class="perm_item"/>
                Edit<input type="checkbox" class="perm_item"/>
                Delete<input type="checkbox" class="perm_item"/>
		    </div>
		    <div class="perm_class">
                <label>用户</label>
                Read<input type="checkbox" class="perm_item"/>
                Add<input type="checkbox" class="perm_item"/>
                Edit<input type="checkbox" class="perm_item"/>
                Delete<input type="checkbox" class="perm_item"/>
            </div>
            <div class="perm_class">
                <label>分类</label>
                Read<input type="checkbox" class="perm_item"/>
                Add<input type="checkbox" class="perm_item"/>
                Edit<input type="checkbox" class="perm_item"/>
                Delete<input type="checkbox" class="perm_item"/>
            </div>
            <div class="perm_class">
                <label>标签</label>
                Read<input type="checkbox" class="perm_item"/>
                Add<input type="checkbox" class="perm_item"/>
                Edit<input type="checkbox" class="perm_item"/>
                Delete<input type="checkbox" class="perm_item"/>
            </div>
            <div class="perm_class">
                <label>文章</label>
                Read<input type="checkbox" class="perm_item"/>
                Add<input type="checkbox" class="perm_item"/>
                Edit<input type="checkbox" class="perm_item"/>
                Delete<input type="checkbox" class="perm_item"/>
            </div>
            <div class="perm_class">
                <label>评论</label>
                Read<input type="checkbox" class="perm_item"/>
                Add<input type="checkbox" class="perm_item"/>
                Edit<input type="checkbox" class="perm_item"/>
                Delete<input type="checkbox" class="perm_item"/>
            </div>
            <div class="perm_class">
                <label>链接</label>
                Read<input type="checkbox" class="perm_item"/>
                Add<input type="checkbox" class="perm_item"/>
                Edit<input type="checkbox" class="perm_item"/>
                Delete<input type="checkbox" class="perm_item"/>
            </div>
            <div class="perm_class">
                <label>信息</label>
                Read<input type="checkbox" class="perm_item"/>
                Add<input type="checkbox" class="perm_item"/>
                Edit<input type="checkbox" class="perm_item"/>
                Delete<input type="checkbox" class="perm_item"/>
            </div>
            <div class="perm_class">
                <label>资料</label>
                Read<input type="checkbox" class="perm_item"/>
                Add<input type="checkbox" class="perm_item"/>
                Edit<input type="checkbox" class="perm_item"/>
                Delete<input type="checkbox" class="perm_item"/>
            </div>
            <div class="perm_class">
                <label>日志</label>
                Read<input type="checkbox" class="perm_item"/>
                Add<input type="checkbox" class="perm_item"/>
                Edit<input type="checkbox" class="perm_item"/>
                Delete<input type="checkbox" class="perm_item"/>
            </div>
            <div class="perm_class">
                <label>系统设置</label>
                Read<input type="checkbox" class="perm_item"/>
                Edit<input type="checkbox" class="perm_item"/>
            </div>
		</div>
		<div class="toolbar">
			<div class="left">
			    <%
			        if (roleBean.canDeleteRole()) {
			    %>
				<input class="button" type="button" value="Delete" onclick="roleList.deleteData()" />
				<%
			        }
				    if (roleBean.canAddRole()) {
				%>
				<input class="button" type="button" onclick="newRole()" value="Add"/>
				<%
				    }
				    if (roleBean.canEditRole()) {
						if (status == ConfigProperty.STATUS_TRASH) {
					%>
					<input class="button" type="button" value="Restore"
						onclick="roleList.changeStatus(<%=ConfigProperty.STATUS_NORMAL %>)" />
					<%
						}else if(status == ConfigProperty.STATUS_NORMAL){
					%>
					<input class="button" type="button" value="Trash"
						onclick="roleList.changeStatus(<%=ConfigProperty.STATUS_TRASH %>)" />
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
						<a href="javascript:void(0)" onclick="roleList.sort('name')">Name</a>
					</th>
					<th>
						Permission
					</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody id="datas">
		        <%
			        for(int i=0;i<roleList.size();i++){
	                    RoleBean rb = roleList.get(i);
                %>
                <tr>
                    <td><input type="checkbox" value="<%=rb.getRoleId() %>" name='select'/></td>
                    <td><%=rb.getName() %></td>
                    <td>
                        <div class="perm_class">
			                <label>角色</label>
			                Read<input type="checkbox" class="perm_item" <%if (rb.canReadRole()){out.print("checked=\"checked\"");}%> disabled="disabled"/>
			                Add<input type="checkbox" class="perm_item" <%if (rb.canAddRole()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Edit<input type="checkbox" class="perm_item" <%if (rb.canEditRole()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Delete<input type="checkbox" class="perm_item" <%if (rb.canDeleteRole()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			            </div>
			            <div class="perm_class">
			                <label>用户</label>
			                Read<input type="checkbox" class="perm_item" <%if (rb.canReadUser()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Add<input type="checkbox" class="perm_item" <%if (rb.canAddUser()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Edit<input type="checkbox" class="perm_item" <%if (rb.canEditUser()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Delete<input type="checkbox" class="perm_item" <%if (rb.canDeleteUser()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			            </div>
			            <div class="perm_class">
			                <label>分类</label>
			                Read<input type="checkbox" class="perm_item" <%if (rb.canReadCategory()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Add<input type="checkbox" class="perm_item" <%if (rb.canAddCategory()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Edit<input type="checkbox" class="perm_item" <%if (rb.canEditCategory()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Delete<input type="checkbox" class="perm_item" <%if (rb.canDeleteCategory()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			            </div>
			             <div class="perm_class">
			                <label>标签</label>
			                Read<input type="checkbox" class="perm_item" <%if (rb.canReadTag()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Add<input type="checkbox" class="perm_item" <%if (rb.canAddTag()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Edit<input type="checkbox" class="perm_item" <%if (rb.canEditTag()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Delete<input type="checkbox" class="perm_item" <%if (rb.canDeleteTag()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			            </div>
			            <div class="perm_class">
			                <label>文章</label>
			                Read<input type="checkbox" class="perm_item" <%if (rb.canReadArticle()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Add<input type="checkbox" class="perm_item" <%if (rb.canAddArticle()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Edit<input type="checkbox" class="perm_item" <%if (rb.canEditArticle()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Delete<input type="checkbox" class="perm_item" <%if (rb.canDeleteArticle()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			            </div>
			            <div class="perm_class">
			                <label>评论</label>
			                Read<input type="checkbox" class="perm_item" <%if (rb.canReadComment()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Add<input type="checkbox" class="perm_item" <%if (rb.canAddComment()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Edit<input type="checkbox" class="perm_item" <%if (rb.canEditComment()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Delete<input type="checkbox" class="perm_item" <%if (rb.canDeleteComment()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			            </div>
			            <div class="perm_class">
			                <label>链接</label>
			                Read<input type="checkbox" class="perm_item" <%if (rb.canReadLink()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Add<input type="checkbox" class="perm_item" <%if (rb.canAddLink()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Edit<input type="checkbox" class="perm_item" <%if (rb.canEditLink()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Delete<input type="checkbox" class="perm_item" <%if (rb.canDeleteLink()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			            </div>
			            <div class="perm_class">
			                <label>信息</label>
			                Read<input type="checkbox" class="perm_item" <%if (rb.canReadMessage()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Add<input type="checkbox" class="perm_item" <%if (rb.canAddMessage()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Edit<input type="checkbox" class="perm_item" <%if (rb.canEditMessage()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Delete<input type="checkbox" class="perm_item" <%if (rb.canDeleteMessage()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			            </div>
			            <div class="perm_class">
			                <label>资料</label>
			                Read<input type="checkbox" class="perm_item" <%if (rb.canReadResource()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Add<input type="checkbox" class="perm_item" <%if (rb.canAddResource()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Edit<input type="checkbox" class="perm_item" <%if (rb.canEditResource()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Delete<input type="checkbox" class="perm_item" <%if (rb.canDeleteResource()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			            </div>
			            <div class="perm_class">
			                <label>日志</label>
			                Read<input type="checkbox" class="perm_item" <%if (rb.canReadLog()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Add<input type="checkbox" class="perm_item" <%if (rb.canAddLog()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Edit<input type="checkbox" class="perm_item" <%if (rb.canEditLog()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Delete<input type="checkbox" class="perm_item" <%if (rb.canDeleteLog()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			            </div>
			            <div class="perm_class">
			                <label>系统设置</label>
			                Read<input type="checkbox" class="perm_item" <%if (rb.canReadConfig()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			                Edit<input type="checkbox" class="perm_item" <%if (rb.canEditConfig()){out.print("checked=\"checked\"");} %> disabled="disabled"/>
			            </div>
                    </td>
                    <td>
                    <a href='javascript:void(0)' onclick="modiRole(<%=i %>)">Edit</a>
                    <a href="admin/UserList?roleId=<%=rb.getRoleId() %>">View users</a>
                    </td>
                </tr>
                <%
                    }
		        %>
			</tbody>
		</table>
		<div class="toolbar">
		</div>
	</body>
</html>
<%
	}
%>
