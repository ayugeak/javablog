<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div id="sidebar_div">
<div class="sidebox" id="toolBox">
	<jsp:include page="toolbar.html"></jsp:include>
</div>
<div class="sidebox" id="searchBox">
	<jsp:include page="searchBox.html"></jsp:include>
</div>
<div class="sidebox">
	<div class="boxtitle">
		Categories
	</div>
	<div class="boxcontent">
		<jsp:include page="categoryList.html"></jsp:include>
	</div>
</div>
<div class="sidebox">
	<div class="boxtitle">
			Recent Posts
		</div>
		<div class="boxcontent">
			<jsp:include page="titleList.html"></jsp:include>
		</div>
</div>
<div class="sidebox">
    <div class="boxtitle">
        Tags
    </div>
    <div class="boxcontent">
        <jsp:include page="tagList.html"></jsp:include>
    </div>
</div>
<div class="sidebox">
	<div class="boxtitle">
		Recent Comments
	</div>
	<div class="boxcontent">
		<jsp:include page="recentComment.html"></jsp:include>
	</div>
</div>
<div class="sidebox">
	<div class="boxtitle">
		Links
	</div>
	<div class="boxcontent">
		<jsp:include page="linkList.html"></jsp:include>
	</div>
</div>
</div>