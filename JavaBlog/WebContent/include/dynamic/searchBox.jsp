<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div id="searchdiv">
	<form action="search" method="get" onsubmit="return search()">
		<input name="q" type="text" id="searchText"
			autocomplete="off" spellcheck="false"
			placeholder="Search articles..." />
		<input id="go" type="image" src="images/search.png" />
	</form>
</div>