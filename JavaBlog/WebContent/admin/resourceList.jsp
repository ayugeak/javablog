<%@page import="javablog.bean.RoleBean"%>
<%@page import="javablog.util.ConfigProperty"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="javablog.bean.ResourceBean,java.text.SimpleDateFormat"%>
<%
	String user=(String)session.getAttribute("userId");
	ArrayList<ResourceBean> resourceList = (ArrayList<ResourceBean>)request.getAttribute("resourceList");
	if (user == null || resourceList == null) {
		response.sendRedirect("error.jsp");
	} else {
		int pageNow = Integer.parseInt((String)request.getAttribute("pageNow"));
        int pageCount = Integer.parseInt((String)request.getAttribute("pageCount"));
        String attribute = (String)request.getAttribute("attribute");
        String order = (String)request.getAttribute("order");
        int status = Integer.parseInt((String)request.getAttribute("status"));
        String pageInfo=pageNow + "/" + pageCount + "页";
        if(pageNow > 1){
            pageInfo += "<a href='javascript:void(0)' onclick='resourceList.upperPage()'>上一页</a>";
        }
        if(pageNow < pageCount){
            pageInfo += "<a href='javascript:void(0)' onclick='resourceList.nextPage()'>下一页</a>";
        }
        RoleBean roleBean = (RoleBean)request.getAttribute("role");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<jsp:include page="admin-base.jsp"></jsp:include>
		<title>Resources</title>
		<script type="text/javascript" src="admin/js/admin.js"></script>
		<script type="text/javascript" src="admin/js/resource.js"></script>
		<link rel="stylesheet" type="text/css" href="admin/css/admin.css">
		<script type="text/javascript">
			var resourceList=new ResourceList();
			resourceList.changeStatus=function(status){
                changeStatus('select',<%=ConfigProperty.ID_RESOURCE %>,status);
            };
            resourceList.deleteData=function(){
                delData('select',<%=ConfigProperty.ID_RESOURCE %>);
            };
			resourceList.setStatus(<%=status%>);
			resourceList.setPageNow(<%=pageNow%>);
			resourceList.setPageCount(<%=pageCount%>);
			resourceList.setAttribute("<%=attribute%>");
			resourceList.setOrder("<%=order%>");
		</script>
	</head>
	<body onload="resourceList.showToolBar()">
		<div class="titlebar">
			<span class="left">
                Resources
            </span>
            <span class="right">
                <input class="button" type="button" value="刷新" onclick="resourceList.getResourceList()"/>
                <input class="button" type="button" onclick="javascript:history.back()" value="返回"/>
                <input class="button" type="button" onclick="javascript:history.forward()" value="前进"/>
            </span>
		</div>
		<div class="filtermenu">
			<ul>
				<li><a <%if(status == ConfigProperty.STATUS_NORMAL)out.print("class='current'"); %> href="javascript:void(0)" onclick="resourceList.filter(<%=ConfigProperty.STATUS_NORMAL%>)">Normal</a></li>
				<li><a <%if(status == ConfigProperty.STATUS_TRASH)out.print("class='current'"); %> href="javascript:void(0)" onclick="resourceList.filter(<%=ConfigProperty.STATUS_TRASH%>)">Trash</a></li>
			</ul>
		</div>
		<input type="hidden" value="0" id="resourceId"/>
		<div class="toolbar">
			<div class="left">
			    <%
			        if (roleBean.canDeleteResource()) {
			    %>
				<input class="button" type="button" value="Delete" onclick="resourceList.deleteData()" />
				<%
			        }
				    if (roleBean.canAddResource()) {
				%>
				<input class="button" type="button" value="Add" onclick="newUpload()"/>
				<%
				    }
				    if (roleBean.canEditResource()) {
						if (status == ConfigProperty.STATUS_NORMAL) {
					%>
					<input class="button" type="button" value="Trash"
						onclick="resourceList.changeStatus(<%=ConfigProperty.STATUS_TRASH %>)" />
					<%
						}else if(status == ConfigProperty.STATUS_TRASH){
					%>
					<input class="button" type="button" value="Restore"
						onclick="resourceList.changeStatus(<%=ConfigProperty.STATUS_NORMAL %>)" />
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
					<th><input type="checkbox" id="checkall" onclick="selectAll(this.checked,'select')"/></th>
					<th>
						<a href="javascript:void(0)" onclick="resourceList.sort('name')">Name</a>
					</th>
					<th>
						<a href="javascript:void(0)" onclick="resourceList.sort('date')">Date</a>
					</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody id="datas">
			<%
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	            for(int i=0; i < resourceList.size();i++){
	                ResourceBean rb = resourceList.get(i);
	        %>
	            <tr>
	                <td><input value="<%=rb.getResourceId() %>" name="select" type="checkbox"/></td>
	                <td><a href="<%=rb.getUrl() %>"><%=rb.getName() %></a></td>
	                <td><%=sdf.format(rb.getDate()) %></td>
	                <td><a href="javascript:void(0)" onclick="modiResource(<%=i %>)">Edit</a></td>
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
