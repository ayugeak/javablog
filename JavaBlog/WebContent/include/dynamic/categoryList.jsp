<%@page import="javablog.util.ConfigProperty"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="javablog.bean.CategoryBean,javablog.bean.CategoryBeanBo"%>
<%@page import="javablog.database.DBConnectionPool,java.sql.Connection"%>
<%
	Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
	if (connection != null){
		CategoryBeanBo cbb = new CategoryBeanBo(connection);
		cbb.setFilter(ConfigProperty.STATUS_NORMAL, null, null);
		ArrayList<CategoryBean> al = cbb.getCategories(0);//get all categories
%>
<ul>
	<%
		for (int i = 0; i < al.size(); i++) {
			CategoryBean cb = al.get(i);
	%>
	<li>
		<a href="category/<%=cb.getCategoryId()%>/"><%=cb.getName()%></a>
	</li>
	<%
		}
		cbb.closeConnection();
	}
	%>
</ul>