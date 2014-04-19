<%@page import="javablog.bean.RoleBean"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="javablog.bean.CategoryBean,javablog.util.StrFilter
,javablog.util.ConfigProperty"%>
<%
	String user=(String)session.getAttribute("userId");
	ArrayList<CategoryBean> categoryList = (ArrayList<CategoryBean>)request.getAttribute("categoryList");
	if (user == null || categoryList == null) {
		response.sendRedirect("error.jsp");
	} else {
		int pageNow = Integer.parseInt((String)request.getAttribute("pageNow"));
        int pageCount = Integer.parseInt((String)request.getAttribute("pageCount"));
        String attribute = (String)request.getAttribute("attribute");
        String order = (String)request.getAttribute("order");
        int status = Integer.parseInt((String)request.getAttribute("status"));
        String pageInfo=pageNow + "/" + pageCount + "页";
        if(pageNow > 1){
            pageInfo += "<a href='javascript:void(0)' onclick='categoryList.upperPage()'>上一页</a>";
        }
        if(pageNow < pageCount){
            pageInfo += "<a href='javascript:void(0)' onclick='categoryList.nextPage()'>下一页</a>";
        }
        RoleBean roleBean = (RoleBean)request.getAttribute("role");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<jsp:include page="admin-base.jsp"></jsp:include>
		<title>Categories</title>
		<link rel="stylesheet" type="text/css" href="admin/css/admin.css">
		<script type="text/javascript" src="admin/js/admin.js"></script>
		<script type="text/javascript" src="admin/js/category.js"></script>
		<script type="text/javascript">
			var categoryList=new CategoryList();
			categoryList.changeStatus=function(status){
                changeStatus('select',<%=ConfigProperty.ID_CATEGORY %>,status);
            };
            categoryList.deleteData=function(){
                delData('select',<%=ConfigProperty.ID_CATEGORY %>);
            };
			categoryList.setPageNow(<%=pageNow%>);
			categoryList.setPageCount(<%=pageCount%>);
			categoryList.setStatus(<%=status%>);
			categoryList.setAttribute("<%=attribute%>");
			categoryList.setOrder("<%=order%>");
        </script>
	</head>
	<body onload="categoryList.showToolBar()">
		<div class="titlebar">
			<span class="left">
                Categories
            </span>
            <span class="right">
                <input class="button" type="button" value="刷新" onclick="categoryList.getCategoryList()"/>
                <input class="button" type="button" onclick="javascript:history.back()" value="返回"/>
                <input class="button" type="button" onclick="javascript:history.forward()" value="前进"/>
            </span>
		</div>
		<div class="filtermenu">
			<ul>
				<li><a <%if(status==ConfigProperty.STATUS_NORMAL)out.print("class='current'"); %> href="javascript:void(0)" onclick="categoryList.filter(<%=ConfigProperty.STATUS_NORMAL%>)">Normal</a></li>
				<li><a <%if(status==ConfigProperty.STATUS_TRASH)out.print("class='current'"); %> href="javascript:void(0)" onclick="categoryList.filter(<%=ConfigProperty.STATUS_TRASH%>)">Trash</a></li>
			</ul>
		</div>
		<input type="hidden" value="0" id="category"/>
		<div class="toolbar">
			<div class="left">
			    <%
                    if (roleBean.canDeleteCategory()) {
                %>
				<input class="button" type="button" value="Delete" onclick="categoryList.deleteData()"/>
				<%
                    }
                    if (roleBean.canAddCategory()) {
                %>
				<input class="button" type="button" onclick="newCategory()" value="Add"/>
				<%
                    }
                    if (roleBean.canEditCategory()) {
						if(status == ConfigProperty.STATUS_TRASH){
					%>
					<input class="button"type="button" value="Restore"
					    onclick="categoryList.changeStatus(<%=ConfigProperty.STATUS_NORMAL %>)"/>
					<%
					 	}else if(status == ConfigProperty.STATUS_NORMAL){
					%>
					<input class="button" type="button" value="Trash"
					    onclick="categoryList.changeStatus(<%=ConfigProperty.STATUS_TRASH %>)"/>
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
					<th><a href="javascript:void(0)" onclick="categoryList.sort('name')">Name</a></th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody id="datas">
			<%
	            for(int i=0;i<categoryList.size();i++){
	                CategoryBean cb = categoryList.get(i);
	        %>
	            <tr>
	                <td><input value="<%=cb.getCategoryId() %>" name="select" type="checkbox"/></td>
	                <td><%=cb.getName() %></td>
	                <td>
		                <a href="javascript:void(0)" onclick="modiCategory(<%=i %>)">Edit</a>
		                <a href="admin/ArticleList?categoryId=<%=cb.getCategoryId() %>">View articles</a>
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