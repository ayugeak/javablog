<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="javablog.bean.ResultBean"%>
<%
    ResultBean result = (ResultBean)request.getAttribute("result");
	if (result != null){
	    int pageNow = result.getPageNow();
	    int rowCount = result.getRowCount();
	    int pageCount = result.getPageCount();
	    String url = result.getUrl();
	    if (rowCount > 0){
%>

    <span class="pageinfo">Page <%=pageNow %> of <%=pageCount %> , <%=rowCount %> records</span>
    <a class="buttonlink" href="<%=url %>">&laquo;</a>
    <%
        if (pageNow > 1) {
    %>
    <a class="buttonlink" href="<%=url %><%=pageNow - 1%>/">&lt;</a>
    <%
        }
            int start = pageNow - 4 > 0? pageNow - 4 : 1;
            int end = start + 8 < pageCount? start + 8 : pageCount;
            for (int i = start; i <= end; i++) {
                if (i == pageNow) {
    %>
    <b class="buttonlink"><%=i%></b>
    <%
        } else {
    %>
    <a class="buttonlink" href="<%=url %><%=i%>/"><%=i%></a>
    <%
        }
            }
            if (pageNow < pageCount) {
    %>
    <a class="buttonlink" href="<%=url %><%=pageNow + 1%>/">&gt;</a>
    <%
        }
    %>
    <a class="buttonlink" href="<%=url %><%=pageCount%>/">&raquo;</a>
<% 
	}
}
%>