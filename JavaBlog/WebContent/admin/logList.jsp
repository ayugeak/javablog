<%@page import="javablog.bean.RoleBean"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="javablog.bean.LogBean,javablog.util.ConfigProperty
,java.text.SimpleDateFormat"%>
<%
	String user=(String)session.getAttribute("userId");
	ArrayList<LogBean> logList = (ArrayList<LogBean>)request.getAttribute("logList");
	if (user == null || logList == null) {
		response.sendRedirect("error.jsp");
	} else {
		int pageNow = Integer.parseInt((String)request.getAttribute("pageNow"));
        int pageCount = Integer.parseInt((String)request.getAttribute("pageCount"));
        String attribute = (String)request.getAttribute("attribute");
        String order = (String)request.getAttribute("order");
        int status = Integer.parseInt((String)request.getAttribute("status"));
        String pageInfo=pageNow + "/" + pageCount + "页";
        if(pageNow > 1){
            pageInfo += "<a href='javascript:void(0)' onclick='logList.upperPage()'>上一页</a>";
        }
        if(pageNow < pageCount){
            pageInfo += "<a href='javascript:void(0)' onclick='logList.nextPage()'>下一页</a>";
        }
        RoleBean roleBean = (RoleBean)request.getAttribute("role");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<jsp:include page="admin-base.jsp"></jsp:include>
		<title>Logs</title>
		<script type="text/javascript" src="admin/js/admin.js"></script>
		<script type="text/javascript" src="admin/js/log.js"></script>
		<link rel="stylesheet" type="text/css" href="admin/css/admin.css">
		<script type="text/javascript">
			var logList=new LogList();
			logList.changeStatus=function(status){
                changeStatus('select',<%=ConfigProperty.ID_LOG %>,status);
            };
            logList.deleteData=function(){
                delData('select',<%=ConfigProperty.ID_LOG %>);
            };
			logList.setStatus(<%=status%>);
			logList.setPageNow(<%=pageNow%>);
			logList.setPageCount(<%=pageCount%>);
			logList.setAttribute("<%=attribute%>");
			logList.setOrder("<%=order%>");
		</script>
	</head>
	<body onload="logList.showToolBar()">
		<div class="titlebar">
			<span class="left">
                Logs
            </span>
            <span class="right">
                <input class="button" type="button" value="刷新" onclick="logList.loadData()"/>
                <input class="button" type="button" onclick="javascript:history.back()" value="返回"/>
                <input class="button" type="button" onclick="javascript:history.forward()" value="前进"/>
            </span>
		</div>
		<div class="filtermenu">
			<ul>
				<li><a <%if(status == ConfigProperty.STATUS_NORMAL)out.print("class='current'"); %>href="javascript:void(0)" onclick="logList.filter(<%=ConfigProperty.STATUS_NORMAL%>)">Normal</a></li>
				<li><a <%if(status == ConfigProperty.STATUS_TRASH)out.print("class='current'"); %> href="javascript:void(0)" onclick="logList.filter(<%=ConfigProperty.STATUS_TRASH%>)">Trash</a></li>
			</ul>
		</div>
		<div class="toolbar">
			<div class="left">
			    <%
                    if (roleBean.canDeleteLog()) {
                %>
				<input class="button" type="button" value="Delete" onclick="logList.deleteData()" />
				<%
                    }
			        if (roleBean.canEditLog()) {
						if (status == ConfigProperty.STATUS_TRASH) {
					%>
					<input class="button" type="button" value="Restore"
						onclick="logList.changeStatus(<%=ConfigProperty.STATUS_NORMAL %>)" />
					<%
						}else if(status == ConfigProperty.STATUS_NORMAL){
					%>
					<input class="button" type="button" value="Trash"
						onclick="logList.changeStatus(<%=ConfigProperty.STATUS_TRASH %>)" />
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
						Content
					</th>
					<th>
						<a href="javascript:void(0)" onclick="logList.sort('date')">Date</a>
					</th>
				</tr>
			</thead>
			<tbody id="datas">
			<%
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	            for(int i=0; i < logList.size();i++){
	                LogBean lb = logList.get(i);
	        %>
	            <tr>
	                <td><input value="<%=lb.getLogId() %>" name="select" type="checkbox"/></td>
	                <td><%=lb.getContent() %></td>
	                <td><%=sdf.format(lb.getDate()) %></td>
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
