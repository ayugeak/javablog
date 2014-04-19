<%@page import="javablog.bean.RoleBean,javablog.bean.TagBean,javablog.bean.TagBeanBo"%>
<%@page import="javablog.bean.CategoryBeanBo,javablog.database.DBConnectionPool"%>
<%@page import="javablog.bean.UserBeanBo,java.sql.Connection"%>
<%@page import="javablog.bean.UserBean,java.text.SimpleDateFormat"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="javablog.bean.ArticleBean,javablog.bean.CategoryBean
,javablog.util.StrFilter,javablog.util.ConfigProperty"%>
<%
	String user=(String)session.getAttribute("userId");
    ArrayList<ArticleBean> articleList = (ArrayList<ArticleBean>)request.getAttribute("articleList");
	if (user == null || articleList == null) {
		response.sendRedirect("error.jsp");
	} else {
		int categoryId = Integer.parseInt((String)request.getAttribute("categoryId"));
		int authorId = Integer.parseInt((String)request.getAttribute("authorId"));
		int tagId = Integer.parseInt((String)request.getAttribute("tagId"));
		int pageNow = Integer.parseInt((String)request.getAttribute("pageNow"));
		int pageCount = Integer.parseInt((String)request.getAttribute("pageCount"));
		String attribute = (String)request.getAttribute("attribute");
		String order = (String)request.getAttribute("order");
		int status = Integer.parseInt((String)request.getAttribute("status"));
		String pageInfo=pageNow + "/" + pageCount + "页";
        if(pageNow > 1){
        	pageInfo += "<a href='javascript:void(0)' onclick='articleList.upperPage()'>上一页</a>";
        }
        if(pageNow < pageCount){
        	pageInfo += "<a href='javascript:void(0)' onclick='articleList.nextPage()'>下一页</a>";
        }
        Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
		if (connection != null){
	        UserBeanBo ubb = new UserBeanBo(connection);
	        UserBean ub = ubb.getUser(authorId);
	        CategoryBeanBo cbb = new CategoryBeanBo(connection);
	        CategoryBean cb = cbb.getCategory(categoryId);
	        TagBeanBo tbb = new TagBeanBo(connection);
	        TagBean tb = tbb.getTag(tagId);
	        RoleBean roleBean = (RoleBean)request.getAttribute("role");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<jsp:include page="admin-base.jsp"></jsp:include>
		<title>Articles</title>
		<link rel="stylesheet" type="text/css" href="admin/css/admin.css" />
		<script type="text/javascript" src="admin/js/admin.js"></script>
		<script type="text/javascript" src="admin/js/article.js"></script>
		<script type="text/javascript">
			var articleList=new ArticleList();
			articleList.changeStatus=function(status){
				changeStatus('select',<%=ConfigProperty.ID_ARTICLE %>,status);
			};
			articleList.deleteData=function(){
				delData('select',<%=ConfigProperty.ID_ARTICLE %>);
			};
			articleList.setCategoryId(<%=categoryId %>);
			articleList.setAuthorId(<%=authorId %>);
			articleList.setTagId(<%=tagId %>);
			articleList.setStatus(<%=status %>);
			articleList.setPageNow(<%=pageNow %>);
			articleList.setPageCount(<%=pageCount%>);
			articleList.setAttribute("<%=attribute%>");
			articleList.setOrder("<%=order %>");
		</script>
	</head>
	<body onload="articleList.showToolBar()">
		<div class="titlebar">
			<span class="left">
                Articles
                <%
                    if (ub != null){
                %>
                    &nbsp;of&nbsp;Author&nbsp;&quot;<a href="admin/ArticleList?authorId=<%=authorId %>"><%=ub.getName() %></a>&quot;
                <%
                    }
                    if (cb != null){
                %>
                        &nbsp;of&nbsp;Category&nbsp;&quot;<a href="admin/ArticleList?categoryId=<%=categoryId %>"><%=cb.getName() %></a>&quot;
                <%
                    }
                    if (tb != null){
                %>
                        &nbsp;of&nbsp;Tag&nbsp;&quot;<a href="admin/ArticleList?tagId=<%=tagId %>"><%=tb.getName() %></a>&quot;
                <%
                    }
                %>
            </span>
            <span class="right">
                <input  class="button"type="button" value="刷新" onclick="articleList.getArticleList()" />
                <input class="button" type="button" onclick="javascript:history.back()" value="返回"/>
                <input class="button" type="button" onclick="javascript:history.forward()" value="前进"/>
            </span>
		</div>
		<div class="filtermenu">
			<ul>
				<li><a <%if(status == ConfigProperty.STATUS_NORMAL)out.print("class='current'"); %> href="javascript:void(0)" onclick="articleList.filter(<%=ConfigProperty.STATUS_NORMAL%>)">Normal</a></li>
				<li><a <%if(status == ConfigProperty.STATUS_DRAFT)out.print("class='current'"); %> href="javascript:void(0)" onclick="articleList.filter(<%=ConfigProperty.STATUS_DRAFT%>)">Draft</a></li>
				<li><a <%if(status == ConfigProperty.STATUS_PENDING)out.print("class='current'"); %> href="javascript:void(0)" onclick="articleList.filter(<%=ConfigProperty.STATUS_PENDING%>)">Pending</a></li>
                <li><a <%if(status == ConfigProperty.STATUS_TRASH)out.print("class='current'"); %> href="javascript:void(0)" onclick="articleList.filter(<%=ConfigProperty.STATUS_TRASH%>)">Trash</a></li>
			</ul>
		</div>
		<div class="toolbar">
			<div class="left">
			    <%
			        if (roleBean.canDeleteArticle()){
			    %>
				<input class="button" type="button" value="Delete" onclick="articleList.deleteData()" />
				<%
				    }
			        if (roleBean.canEditArticle()){
			            if (status == ConfigProperty.STATUS_TRASH) {
				%>
					<input class="button" type="button" value="Restore"
						onclick="articleList.changeStatus(<%=ConfigProperty.STATUS_NORMAL %>)" />
					<%
						}else if(status == ConfigProperty.STATUS_NORMAL){
					%>
					<input class="button" type="button" value="Trash"
						onclick="articleList.changeStatus(<%=ConfigProperty.STATUS_TRASH %>)" />
					<%
						}else if(status == ConfigProperty.STATUS_PENDING){
	                %>
	                <input class="button" type="button" value="Approve"
	                    onclick="articleList.changeStatus(<%=ConfigProperty.STATUS_NORMAL %>)" />
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
					<th>
						<input type="checkbox" onclick="selectAll(this.checked,'select')" />
					</th>
					<th>
						<a href="javascript:void(0)" onclick="articleList.sort('author_id')">Author</a>
					</th>
					<th>
						<a href="javascript:void(0)" onclick="articleList.sort('title')">Title</a>
					</th>
					<th>
						<a href="javascript:void(0)" onclick="articleList.sort('category_id')">Category</a>
					</th>
					<th>
						Tags
					</th>
					<th>
						<a href="javascript:void(0)" onclick="articleList.sort('date')">Date</a>
					</th>
					<th>
					   Actions
					</th>
				</tr>
			</thead>
			<tbody id="datas">
            <%
	            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	            for(int i=0;i<articleList.size();i++){
	                ArticleBean ab=articleList.get(i);
	                ub=ubb.getUser(ab.getAuthorId());
	                cb=cbb.getCategory(ab.getCategoryId());
            %>
                <tr>
	                <td><input value="<%=ab.getArticleId() %>" name="select" type="checkbox"/></td>
	                <td><a href="admin/ArticleList?authorId=<%=ab.getAuthorId() %>"><%=ub.getName() %></a></td>
	                <td><a href="admin/showArticle.jsp?articleId=<%=ab.getArticleId() %>"><%=ab.getTitle() %></a></td>
	                <td><a href="admin/ArticleList?categoryId=<%=ab.getCategoryId() %>"><%=cb.getName() %></a></td>
	                <td>
	                <%
	                	tbb.setFilter(ConfigProperty.STATUS_NORMAL + "",
	    					"SELECT tag_id FROM article_tags WHERE article_id=" + ab.getArticleId(),
	    					null, null);
		                ArrayList<TagBean> tbs = tbb.getTags(0);
						for (int j = 0; j < tbs.size(); j++){
							tb = tbs.get(j);
					%>
						<a class="tag_link" href="admin/ArticleList?tagId=<%=tb.getTagId() %>"><%=tb.getName() %></a>
					<%
						}
	                %>
	                </td>
	                <td><%=sdf.format(ab.getDate()) %></td>
	                <td><a href="admin/CommentList?articleId=<%=ab.getArticleId() %>">View comments</a></td>
                </tr>
            <%
	            }
	            ubb.closeConnection();
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
