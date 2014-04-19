<%@page import="javablog.bean.RoleBean"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.sql.Connection"%>
<%@page import="javablog.database.DBConnectionPool,java.text.SimpleDateFormat"%>
<%@page import="javablog.bean.CommentBean,javablog.bean.ArticleBean
,javablog.bean.ArticleBeanBo,javablog.util.ConfigProperty"%>
<%
	String user=(String)session.getAttribute("userId");
	ArrayList<CommentBean> commentList = (ArrayList<CommentBean>)request.getAttribute("commentList");
	if (user == null || commentList == null) {
		response.sendRedirect("error.jsp");
	} else {
		int articleId = Integer.parseInt((String)request.getAttribute("articleId"));
		int pageNow = Integer.parseInt((String)request.getAttribute("pageNow"));
        int pageCount = Integer.parseInt((String)request.getAttribute("pageCount"));
        String attribute = (String)request.getAttribute("attribute");
        String order = (String)request.getAttribute("order");
        int status = Integer.parseInt((String)request.getAttribute("status"));
        String pageInfo=pageNow + "/" + pageCount + "页";
        if(pageNow > 1){
            pageInfo += "<a href='javascript:void(0)' onclick='commentList.upperPage()'>上一页</a>";
        }
        if(pageNow < pageCount){
            pageInfo += "<a href='javascript:void(0)' onclick='commentList.nextPage()'>下一页</a>";
        }
        Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
		if (connection != null){
	        ArticleBeanBo abb=new ArticleBeanBo(connection);
	        ArticleBean ab = abb.getArticle(articleId);
	        RoleBean roleBean = (RoleBean)request.getAttribute("role");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<jsp:include page="admin-base.jsp"></jsp:include>
		<title>Comments</title>
		<script type="text/javascript" src="admin/js/admin.js"></script>
		<script type="text/javascript" src="admin/js/comment.js"></script>
		<link rel="stylesheet" type="text/css" href="admin/css/admin.css">
		<script type="text/javascript">
			var commentList=new CommentList();
			commentList.changeStatus=function(status){
                changeStatus('select',<%=ConfigProperty.ID_COMMENT %>,status);
            };
            commentList.deleteData=function(){
                delData('select',<%=ConfigProperty.ID_COMMENT %>);
            };
			commentList.setArticleId(<%=articleId%>);
			commentList.setStatus(<%=status%>);
			commentList.setPageNow(<%=pageNow%>);
			commentList.setPageCount(<%=pageCount%>);
			commentList.setAttribute("<%=attribute%>");
			commentList.setOrder("<%=order%>");
		</script>
	</head>
	<body onload="commentList.showToolBar()">
		<div class="titlebar">
		    <span class="left">
                Comments
                <%
                    if (ab != null){
                %>
                    &nbsp;of&nbsp;Article&nbsp;&quot;<a href="admin/showArticle.jsp?articleId=<%=articleId %>"><%=ab.getTitle() %></a>&quot;
                <%
                    }
                %>
		    </span>
			<span class="right">
			    <input class="button" type="button" value="刷新" onclick="commentList.getCommentList()" />
				<input class="button" type="button" onclick="javascript:history.back()" value="返回"/>
				<input class="button" type="button" onclick="javascript:history.forward()" value="前进"/>
		    </span>
		</div>
		<div class="filtermenu">
			<ul>
				<li><a <%if(status==ConfigProperty.STATUS_NORMAL)out.print("class='current'"); %> href="javascript:void(0)" onclick="commentList.filter(<%=ConfigProperty.STATUS_NORMAL%>)">Normal</a></li>
				<li><a <%if(status==ConfigProperty.STATUS_PENDING)out.print("class='current'"); %> href="javascript:void(0)" onclick="commentList.filter(<%=ConfigProperty.STATUS_PENDING%>)">Pending</a></li>
				<li><a <%if(status==ConfigProperty.STATUS_TRASH)out.print("class='current'"); %> href="javascript:void(0)" onclick="commentList.filter(<%=ConfigProperty.STATUS_TRASH%>)">Trash</a></li>
			</ul>
		</div>
		<div class="toolbar">
			<div class="left">
			    <%
                    if (roleBean.canDeleteComment()) {
                %>
				<input class="button" type="button" value="Delete" onclick="commentList.deleteData()" />
				<%
                    }
			        if (roleBean.canEditComment()) {
						if (status == ConfigProperty.STATUS_NORMAL) {
					%>
					<input class="button" type="button" value="Trash"
						onclick="commentList.changeStatus(<%=ConfigProperty.STATUS_TRASH %>)" />
					<%
						}else if(status==ConfigProperty.STATUS_TRASH){
					%>
					<input class="button" type="button" value="Restore"
						onclick="commentList.changeStatus(<%=ConfigProperty.STATUS_NORMAL %>)" />
					<%
						}else{
					 %>
					 <input class="button" type="button" value="Approve"
						onclick="commentList.changeStatus(<%=ConfigProperty.STATUS_NORMAL %>)" />
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
						<a href="javascript:void(0)" onclick="commentList.sort('name')">Name</a>
					</th>
					<th>
						Content
					</th>
					<th>
						<a href="javascript:void(0)" onclick="commentList.sort('article_id')">Article</a>
					</th>
					<th>
						<a href="javascript:void(0)" onclick="commentList.sort('date')">Date</a>
					</th>
				</tr>
			</thead>
			<tbody id="datas">
				<%
		            
		            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		            for(int i=0; i < commentList.size();i++){
		                CommentBean cb = commentList.get(i);
		                ab = abb.getArticle(cb.getArticleId());
		        %>
	            <tr>
	                <td><input value="<%=cb.getCommentId() %>" name="select" type="checkbox"/></td>
	                <td>
	                	<table>
	                		<tbody>
	                			<tr>
		                			<td>
		                				<img class="avatar" src="<%=cb.getEmail() %>" alt="avatar"/>
		                			</td>
		                			<td>
		                				<div><%=cb.getName()%></div>
			                    		<a href="<%=cb.getUrl() %>" target="_blank"><%=cb.getUrl() %></a>
		                			</td>
	                			</tr>
	                		</tbody>
	                	</table>
	                </td>
	                <td><%=cb.getContent() %></td>
	                <td><a href="admin/showArticle.jsp?articleId=<%=cb.getArticleId() %>"><%=ab.getTitle() %></a></td>
	                <td><%=sdf.format(cb.getDate()) %></td>
	            </tr>
		        <%    
		            }
		            abb.closeConnection();
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
