function modiInfo() {
	var name=document.getElementById("name").value;
	var email=document.getElementById("email").value;
	var homepage=document.getElementById("url").value;
	var avatar=document.getElementById("avatar").src;
	
	var xmlHttp = createXMLHttpRequest();
	var url = "admin/Account";
	xmlHttp.open("post",url,true);
    xmlHttp.onreadystatechange=callback;
    xmlHttp.setRequestHeader("Content-type","application/x-www-form-urlencoded;charset=UTF-8");
	xmlHttp.send("name=" + encodeURIComponent(name)
			+ "&email=" + encodeURIComponent(email)
			+ "&url=" + encodeURIComponent(homepage)
			+ "&avatar=" + encodeURIComponent(avatar));
	function callback() {
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			var reply = xmlHttp.responseText;
			if (reply == "s") {
				showInfo("修改成功");
				window.location.reload();
			} else if (reply == "n") {
				showInfo("对不起,你没有权限");
			} else {
				showInfo("操作失败");
			}
		}
	}
	return false;
}
function modiLogin() {
	var password=document.getElementById("password").value;
	var confirmpassword=document.getElementById("confirmpassword").value;
	if (password != confirmpassword){
		return false;
	}

	var xmlHttp = createXMLHttpRequest();
	var url = "admin/Account";
	xmlHttp.open("post",url,true);
    xmlHttp.onreadystatechange=callback;
    xmlHttp.setRequestHeader("Content-type","application/x-www-form-urlencoded;charset=UTF-8");
	xmlHttp.send("password=" + encodeURIComponent(password));
	function callback() {
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			var reply = xmlHttp.responseText;
			if (reply == "s") {
				showInfo("修改成功");
				window.location.reload();
			} else if (reply == "n") {
				showInfo("对不起,你没有权限");
			} else {
				showInfo("操作失败");
			}
		}
	}
	return false;
}

function newAvatar(){
	var title="Upload avatar";
	var html="<input type='file' name='file' id='file'><iframe style='display:none;' name='hide'></iframe>";
	inputBox(title,html,'upload(1)');
}
function uploadCallback(name, url, type){
	closeBox();
	document.getElementById("avatar").src=url;
}