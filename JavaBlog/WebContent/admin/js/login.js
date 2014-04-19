function isValidUsername(username){
	var result = false;
	var re = /^[a-zA-Z0-9]{4,32}$/g;
	if (username != null && re.test(username)){
		result = true;
	}
	return result;
}

function isValidPassword(password){
	var result = false;
	var re = /.{8,64}$/g;
	if (password != null && re.test(password)){
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

function isValidCode(code){
	var result = false;
	var re = /^[0-9]{4,4}$/g;
	if (code != null && re.test(code)){
		result = true;
	}
	return result;
}

function checkLogin(){
	var user=document.getElementById("nametext").value;
	var pswd=document.getElementById("pswdtext").value;
	var email=document.getElementById("emailtext").value;
	var cod=document.getElementById("codetext").value;
	var type=document.getElementById("type").value;

	var url = "";
	var flag = false;
	if (isValidUsername(user) && isValidCode(cod)){
		if (type == "login"){
			if (isValidPassword(pswd)){
				url = "admin/Login";
				flag = true;
			}
		} else if (type == "register"){
			if (isValidEmail(email)){
				url = "admin/Register";
				flag = true;
			}
		} else if (type == "findpassword"){
			if (isValidEmail(email)){
				url = "admin/FindPassword";
				flag = true;
			}
		}
	}
	if (!flag){
		return false;
	}
	
	var data = "username=" + user
			+ "&password=" + pswd
			+ "&email=" + email
			+ "&code=" + cod;
	
	var xmlHttp = createXMLHttpRequest();
    xmlHttp.open("post", url, true);
    xmlHttp.onreadystatechange=callback;
    xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
	xmlHttp.send(data);
    function callback()//
    {
        if(xmlHttp.readyState==4&&xmlHttp.status==200){
	        var data= xmlHttp.responseText;
	        if(data=="f"){
	        	changeCode("veryimg"); 
	        	document.getElementById("result").innerHTML="<font color='red'>用户名或密码错误</font>";
	        	 	
	        }else if(data=="w"){
	        	changeCode("veryimg");
	        	document.getElementById("result").innerHTML="<font color='red'>验证码错误</font>";
	        	
	        }else if(data=="s"){
	        	document.getElementById("result").innerHTML="登录成功,页面自动跳转中...";
	        	window.location.href="admin/";
	        }
    	}
    }
	return false;
}
function changeCode(id){
	document.getElementById(id).src="admin/VerifyImage?"+Math.random();
}

function findPassword(){
	var user=document.getElementById("nametext").value;
	var cod=document.getElementById("codetext").value;
	if(user==null||cod==null){
		return false;
	}
	var xmlHttp=createXMLHttpRequest();   //
    var url="admin/SendEmail";
    xmlHttp.open("post",url,true);
    xmlHttp.onreadystatechange=callback;
    xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
	xmlHttp.send("username=" + user
			+ "&code=" + cod);
    document.getElementById("result").innerHTML="正在发送...";
    function callback()//
    {
        if(xmlHttp.readyState==4&&xmlHttp.status==200){
	        var data= xmlHttp.responseText;
	        if(data=="f"){
	        	changeCode("veryimg"); 
	        	document.getElementById("result").innerHTML="<font color='red'>用户名错误</font>";	 	
	        }else if(data=="w"){
	        	changeCode("veryimg");
	        	document.getElementById("result").innerHTML="<font color='red'>验证码错误</font>";
	        	
	        }else if(data=="s"){
	        	document.getElementById("result").innerHTML="发送成功";
	        }
    	}
    }
	return false;
}
function changeTab(obj, type){
	var tabs = document.getElementsByClassName("tab");
	var i;
	for (i = 0; i < tabs.length; i++){
		tabs[i].style.display = "inline";
	}
	if (type == "login"){
		document.getElementById("passwordrow").style.display = "block";
		document.getElementById("emailrow").style.display = "none";
	} else {
		document.getElementById("passwordrow").style.display = "none";
		document.getElementById("emailrow").style.display = "block";
	}
	document.getElementById("type").value=type;
	document.getElementById("logintitle").innerHTML = obj.innerHTML;
	obj.style.display = "none";
	changeCode("veryimg");
}
