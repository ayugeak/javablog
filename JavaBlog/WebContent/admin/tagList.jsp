<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="javablog.bean.TagBean,javablog.bean.TagBeanBo
,javablog.util.StrFilter,javablog.util.ConfigProperty,javablog.bean.RoleBean"%>
<%
	String user=(String)session.getAttribute("userId");
	ArrayList<TagBean> tagList = (ArrayList<TagBean>)request.getAttribute("tagList");
	if (user == null || tagList == null) {
		response.sendRedirect("error.jsp");
	} else {
		int pageNow = Integer.parseInt((String)request.getAttribute("pageNow"));
	    int pageCount = Integer.parseInt((String)request.getAttribute("pageCount"));
	    String attribute = (String)request.getAttribute("attribute");
	    String order = (String)request.getAttribute("order");
	    int status = Integer.parseInt((String)request.getAttribute("status"));
	    String pageInfo=pageNow + "/" + pageCount + "页";
	    if(pageNow > 1){
	        pageInfo += "<a href='javascript:void(0)' onclick='tagList.upperPage()'>上一页</a>";
	    }
	    if(pageNow < pageCount){
	        pageInfo += "<a href='javascript:void(0)' onclick='tagList.nextPage()'>下一页</a>";
	    }
	    RoleBean roleBean = (RoleBean)request.getAttribute("role");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<jsp:include page="admin-base.jsp"></jsp:include>
		<title>Tags</title>
		<link rel="stylesheet" type="text/css" href="admin/css/admin.css">
		<script type="text/javascript" src="admin/js/admin.js"></script>
		<script type="text/javascript" src="admin/js/tag.js"></script>
		<script type="text/javascript">
			var tagList=new TagList();
			tagList.changeStatus=function(status){
                changeStatus('select', <%=ConfigProperty.ID_TAG %>, status);
            };
            tagList.deleteData=function(){
                delData('select',<%=ConfigProperty.ID_TAG %>);
            };
			tagList.setPageNow(<%=pageNow%>);
			tagList.setPageCount(<%=pageCount%>);
			tagList.setStatus(<%=status%>);
			tagList.setAttribute("<%=attribute%>");
			tagList.setOrder("<%=order%>");
        </script>
	</head>
	<body onload="tagList.showToolBar()">
		<div class="titlebar">
			<span class="left">
                Tags
            </span>
            <span class="right">
                <input class="button" type="button" value="刷新" onclick="tagList.getTagList()"/>
                <input class="button" type="button" onclick="javascript:history.back()" value="返回"/>
                <input class="button" type="button" onclick="javascript:history.forward()" value="前进"/>
            </span>
		</div>
		<div class="filtermenu">
			<ul>
				<li><a <%if(status==ConfigProperty.STATUS_NORMAL)out.print("class='current'"); %> href="javascript:void(0)" onclick="tagList.filter(<%=ConfigProperty.STATUS_NORMAL%>)">Normal</a></li>
				<li><a <%if(status==ConfigProperty.STATUS_TRASH)out.print("class='current'"); %> href="javascript:void(0)" onclick="tagList.filter(<%=ConfigProperty.STATUS_TRASH%>)">Trash</a></li>
			</ul>
		</div>
		<input type="hidden" value="0" id="tag"/>
		<div class="toolbar">
			<div class="left">
			    <%
                    if (roleBean.canDeleteTag()) {
                %>
				<input class="button" type="button" value="Delete" onclick="tagList.deleteData()"/>
				<%
                    }
                    if (roleBean.canAddTag()) {
                %>
				<input class="button" type="button" onclick="newTag()" value="Add"/>
				<%
                    }
                    if (roleBean.canEditTag()) {
						if(status == ConfigProperty.STATUS_TRASH){
					%>
					<input class="button"type="button" value="Restore"
					    onclick="tagList.changeStatus(<%=ConfigProperty.STATUS_NORMAL %>)"/>
					<%
					 	}else if(status == ConfigProperty.STATUS_NORMAL){
					%>
					<input class="button" type="button" value="Trash"
					    onclick="tagList.changeStatus(<%=ConfigProperty.STATUS_TRASH %>)"/>
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
					<th><a href="javascript:void(0)" onclick="tagList.sort('name')">Name</a></th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody id="datas">
			<%
	            for(int i=0;i<tagList.size();i++){
	                TagBean tb = tagList.get(i);
	        %>
	            <tr>
	                <td><input value="<%=tb.getTagId() %>" name="select" type="checkbox"/></td>
	                <td><%=tb.getName() %></td>
	                <td>
		                <a href="javascript:void(0)" onclick="modiTag(<%=i %>)">Edit</a>
		                <a href="admin/ArticleList?tagId=<%=tb.getTagId() %>">View articles</a>
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
<%}%>