<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="javablog.bean.TagBean,javablog.bean.TagBeanBo"%>
<%@page import="javablog.util.ConfigProperty"%>
<%@page import="javablog.database.DBConnectionPool,java.sql.Connection"%>
<%
	Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
	if (connection != null){
	    TagBeanBo tbb=new TagBeanBo(connection);
	    tbb.setFilter(ConfigProperty.STATUS_NORMAL, null, null);
	    ArrayList<TagBean> al = tbb.getTags(0);//show all tags
%>
<div id="tagbox">
<%
    	for (int i = 0; i < al.size(); i++) {
            TagBean tb = al.get(i);
%>
    <span class="tag_link">
    	<a href="tag/<%=tb.getTagId() %>/"><%=tb.getName() %></a>
    </span>
<%
	    }
		tbb.closeConnection();
	}
%>
</div>