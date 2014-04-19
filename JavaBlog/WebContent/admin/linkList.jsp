<%@page import="javablog.bean.RoleBean"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="javablog.bean.LinkBean,javablog.util.ConfigProperty"%>
<%
	String user=(String)session.getAttribute("userId");
	ArrayList<LinkBean> linkList = (ArrayList<LinkBean>)request.getAttribute("linkList");
	if (user == null || linkList == null) {
		response.sendRedirect("error.jsp");
	} else {
		int pageNow = Integer.parseInt((String)request.getAttribute("pageNow"));
        int pageCount = Integer.parseInt((String)request.getAttribute("pageCount"));
        String attribute = (String)request.getAttribute("attribute");
        String order = (String)request.getAttribute("order");
        int status = Integer.parseInt((String)request.getAttribute("status"));
        String pageInfo=pageNow + "/" + pageCount + "页";
        if(pageNow > 1){
            pageInfo += "<a href='javascript:void(0)' onclick='linkList.upperPage()'>上一页</a>";
        }
        if(pageNow < pageCount){
            pageInfo += "<a href='javascript:void(0)' onclick='linkList.nextPage()'>下一页</a>";
        }
        RoleBean roleBean = (RoleBean)request.getAttribute("role");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<jsp:include page="admin-base.jsp"></jsp:include>
		<title>Links</title>
		<script type="text/javascript" src="admin/js/admin.js"></script>
		<script type="text/javascript" src="admin/js/link.js"></script>
		<script type="text/javascript">
			var linkList=new LinkList();
			linkList.changeStatus=function(status){
                changeStatus('select',<%=ConfigProperty.ID_LINK %>,status);
            };
            linkList.deleteData=function(){
                delData('select',<%=ConfigProperty.ID_LINK %>);
            };
			linkList.setStatus(<%=status%>);
			linkList.setPageNow(<%=pageNow%>);
			linkList.setPageCount(<%=pageCount%>);
			linkList.setAttribute("<%=attribute%>");
			linkList.setOrder("<%=order%>");
        </script>
		<link rel="stylesheet" type="text/css" href="admin/css/admin.css">
	</head>
	<body onload="linkList.showToolBar()">
		<div class="titlebar">
			<span class="left">
                Links
            </span>
            <span class="right">
                <input class="button" type="button" value="刷新" onclick="linkList.getLinkList()"/>
                <input class="button" type="button" onclick="javascript:history.back()" value="返回"/>
                <input class="button" type="button" onclick="javascript:history.forward()" value="前进"/>
            </span>
		</div>
		<div class="filtermenu">
			<ul>
				<li><a <%if(status == ConfigProperty.STATUS_NORMAL)out.print("class='current'"); %> href="javascript:void(0)" onclick="linkList.filter(<%=ConfigProperty.STATUS_NORMAL %>)">Normal</a></li>
				<li><a <%if(status == ConfigProperty.STATUS_TRASH)out.print("class='current'"); %> href="javascript:void(0)" onclick="linkList.filter(<%=ConfigProperty.STATUS_TRASH %>)">Trash</a></li>
			</ul>
		</div>
		<input type="hidden" value="0" id="link"/>
		<div class="toolbar">
			<div class="left">
			    <%
			        if (roleBean.canDeleteLink()) {
			    %>
				<input class="button" type="button" value="Delete" onclick="linkList.deleteData()"/>
				<input class="button" type="button" onclick="newLink()" value="Add"/>
				<%
			        }
			        if (roleBean.canEditLink()) {
						if(status == ConfigProperty.STATUS_TRASH){
					%>
						<input class="button" type="button" value="Restore"
						   onclick="linkList.changeStatus(<%=ConfigProperty.STATUS_NORMAL %>)"/>
					<%
					 	}else if(status == ConfigProperty.STATUS_NORMAL){
					%>
					  	<input class="button" type="button" value="Trash"
					  	 onclick="linkList.changeStatus(<%=ConfigProperty.STATUS_TRASH %>)"/>
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
					<th><input type="checkbox" onclick="selectAll(this.checked,'select')"/></th>
					<th>
						<a href="javascript:void(0)" onclick="linkList.sort('name')">Name</a>
					</th>
					<th>
						<a href="javascript:void(0)" onclick="linkList.sort('url')">Url</a>
					</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody id="datas">
			<%
				for(int i=0; i < linkList.size(); i++){
                    LinkBean lb = linkList.get(i);
            %>
                <tr>
                    <td><input value="<%=lb.getLinkId() %>" name="select" type="checkbox"/></td>
                    <td><%=lb.getName() %></td>
                    <td><%=lb.getUrl() %></td>
                    <td><a href="javascript:void(0)" onclick="modiLink(<%=i %>)">Edit</a></td>
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
<%}%>