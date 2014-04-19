<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <jsp:include page="admin-base.jsp"></jsp:include>
        <script type="text/javascript" src="admin/js/admin.js"></script>
		<script type="text/javascript">
		function isValidUsername(username){
			var result = false;
			var re = /^[a-zA-Z0-9]{4,32}$/g;
			if (username != null && re.test(username)){
				result = true;
			}
			return result;
		}
		
		function isValidEmail(email){
			var result = false;
			var re = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/g;
			if (email != null && re.test(email) && email.length <= 128){
				result = true;
			}
			return result;
		}
		
	    function install(){     
	    	var username = document.getElementById("username").value;
	    	var email = document.getElementById("email").value;
	    	
	    	if(!isValidUsername(username) || !isValidEmail(email)){
	    		return false;
	    	}
	    	
	    	var xmlHttp = createXMLHttpRequest();
	        var url = "admin/Install";
	        xmlHttp.open("post", url, true);
	        xmlHttp.onreadystatechange = installCallback;
	        xmlHttp.setRequestHeader("Content-type","application/x-www-form-urlencoded;charset=UTF-8");
	        xmlHttp.send("username=" + username
	        		+ "&email="+encodeURIComponent(email));
	        function installCallback()
	        {
	            if(xmlHttp.readyState == 4 && xmlHttp.status == 200){
	    	        var data = xmlHttp.responseText;
	    	        if (data == "s"){
	    	        	changeTab(3);
	    	        } else if(data == "n"){
	    	        	alert("该网站已安装!");
	    	        } else {
	    	        	alert("失败");
	    	        }
	        	}
	        }
	    	return false;
	    }
	    
	    function finish(){
	    	
	    }
		</script>
		<title>安装JavaBlog</title>
		<style type="text/css">
		html, body{
			margin: 0px;
			padding: 0px;
		}
		body{
			background: #F7F7F7;
		}
		#wrapper{
			margin:0px auto;
			width:540px;
		}
		#container{
			width: 530px;
			height: 320px;
			margin: 150px 0px 0px 0px;
			padding: 0px;
		}
		#process_title{
			color: #FFFFFF;
			background: #333333;
			padding: 5px 0px 5px 5px;
			width: 525px;
			height: 24px;
			font-size: 20px;
			font-weight: bold;
			text-align: center;
			box-shadow: 0px -1px 5px #666666;
		}
		#process_body{
			background: #FFFFFF;
			width: 530px;
			height: 280px;
			overflow: hidden;
			border-radius:0px 0px 5px 5px;
			box-shadow: 0px 0px 5px #666666;
		}
		#process_sidebar{
			width: 150px;
			height: 270px;
			padding: 5px 0px 5px 0px;
			background: #DBDBDB;
			float: left;
			border-right: 1px solid #999999;
		}
		#process_sidebar ul{
			list-style: none;
			display: block;
			margin: 0px;
			padding: 0px;
		}
		#process_main{
			width: 360px;
			height: 270px;
			padding: 5px 0px 5px 0px;
			float: right;
		}
		.process_content{
			height: 240px;
		}
		.process_button{
			height: 30px;
			text-align: right;
		}
		.tab_panel_link{
			text-align: right;
			font-size: 18px;
			list-style: none;
			display: block;
			margin: 0px;
			padding: 5px 10px 5px 0px;
		}
		.tab_panel_link_current{
			color: #04B1F2;
			background: #FFFFFF;
			box-shadow: -3px 0px 3px #666666;
			border-right: 1px solid #FFFFFF;
			margin-right: -1px;
		}
		.tabPanel{
			display: none;
			width: 350px;
			overflow: hidden;
		}
		.form_label{
			font-size: 16px;
			height: 20px;
			line-height: 20px;
			text-align: left;
		}
		.form_input{
			height:25px;
			width: 260px;
		}
		.form_button{
			background: #F5F5F5;
			border: 1px solid rgba(0, 0, 0, 0.1);
			border-radius: 3px;
			cursor: pointer;
			font-size: 12px;
			font-weight: bold;
			height: 24px;
			line-height: 24px;
			margin: 0px;
			min-width: 54px;
			padding: 0px 3px 0px 3px;
			text-align: center;
		}
		.form_button:hover {
			background: #F8F8F8;
			border: 1px solid #C6C6C6;
			text-decoration: none;
			box-shadow: 0 1px 1px rgba(0, 0, 0, 0.1);
			color: #333333;
		}
		.content_title{
			margin: 0px;
			padding: 5px;
		}
		</style>
	</head>
	<body>
		<div id="wrapper">
			<div id="container">
				<div id="process_title">
					JavaBlog installation guide
				</div>
				<div id="process_body">
					<div id="process_sidebar">
						<ul>
						    <li class="tab_panel_link tab_panel_link_current">Welcome</li>
						    <li class="tab_panel_link">Agreement</li>
						    <li class="tab_panel_link">Create account</li>
						    <li class="tab_panel_link">Finish</li>
						</ul>
					</div>
					<div id="process_main">
					    <div style="display:block;" class="tabPanel">
					    	<div class="process_content">
					        	<h2 class="content_title">Welcome to JavaBlog</h2>
					        </div>
					        <div class="process_button">
					        	<input class="form_button" type="button" value="next" onclick="changeTab(1)"/>
					        </div>
					    </div>
					    <div class="tabPanel">
					    	<div class="process_content">
					        	<h2 class="content_title">Agreement</h2>
					        </div>
					        <div class="process_button">
					        	<input class="form_button" type="button" value="next" onclick="changeTab(2)"/>
					        </div>
					    </div>
					    <div class="tabPanel">
					        <form action="" method="post">
					        	<div class="process_content">
					        		<h2 class="content_title">Create account</h2>
						            <div>
						                <div class="form_label">Username</div>
						                <input class="form_input" type="text" id="username" name="username"/>
						            </div>
						            <div>
						                <div class="form_label">Email</div>
						                <input class="form_input" type="text" id="email" name="email"/>
						            </div>
						        </div>
						        <div class="process_button">
						        	<input class="form_button" type="submit" value="next" onclick="return install()"/>
						        </div>
					        </form>
					    </div>
					    <div class="tabPanel">
					    	<div class="process_content">
						        <h2 class="content_title">Finish</h2>
						        <h3>Please check your email for the password and then</h3>
					        </div>
					        <div class="process_button">
					        	<a class="form_button" href="admin/">Go to the background</a>
					    	</div>
					    </div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>