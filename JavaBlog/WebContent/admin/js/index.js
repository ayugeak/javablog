function exit() {
	var xmlHttp = createXMLHttpRequest(); //
	var url = "admin/Logout";
	xmlHttp.open("post", url, true); //
	xmlHttp.onreadystatechange = exitcallback; //
	xmlHttp.send();
	function exitcallback()//
	{
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			var data= xmlHttp.responseText;
			if(data=="s"){
				window.location.href="admin/";
			}
		}
	}
	return false;
}
function select(id) {
	var submenus = document.getElementsByClassName("submenu");
	for(var i=0;i<submenus.length;i++){
		submenus[i].style.display="none";
	}
	var submenu=document.getElementById(id);
	if (submenu.style.display == "block") {
		submenu.style.display = "none";
	} else {
		submenu.style.display = "block";
	}
}
function showInfo(message){
	var msg=document.getElementById("info");
	msg.innerHTML=message;
	msg.style.display="block";
	setTimeout("document.getElementById('info').style.display='none'",3000);
}

function showAccountMenu(){
	var accountMenu = document.getElementById("accountMenu");
	var accountMenuSwitch = document.getElementById("accountMenuSwitch");
	if (accountMenu.style.display != "block"){
		accountMenu.style.display = "block";
		accountMenuSwitch.setAttribute("class", "account-menu-onhover");
	}
}

function hideAccountMenu(){
	var accountMenu = document.getElementById("accountMenu");
	var accountMenuSwitch = document.getElementById("accountMenuSwitch");
	if (accountMenu.style.display != "none"){
		accountMenu.style.display = "none";
		accountMenuSwitch.setAttribute("class", "account-menu-normal");
	}
}