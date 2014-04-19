function isValidNumber(username){
	var result = false;
	var re = /^[0-9]{1,2}$/g;
	if (username != null && re.test(username)){
		result = true;
	}
	return result;
}

function saveConfig() {
	var website_name = document.getElementById("website_name").value;
	var front_blog_page_size = document.getElementById("front_blog_page_size").value;
	var front_page_count = document.getElementById("front_page_count").value;
	var front_recent_post_count = document.getElementById("front_recent_post_count").value;
	var front_recent_comment_count = document.getElementById("front_recent_comment_count").value;
	var front_comment_page_size = document.getElementById("front_comment_page_size").value;
	var admin_role_page_size = document.getElementById("admin_role_page_size").value;
	var admin_user_page_size = document.getElementById("admin_user_page_size").value;
	var admin_category_page_size = document.getElementById("admin_category_page_size").value;
	var admin_tag_page_size = document.getElementById("admin_tag_page_size").value;
	var admin_article_page_size = document.getElementById("admin_article_page_size").value;
	var admin_comment_page_size = document.getElementById("admin_comment_page_size").value;
	var admin_link_page_size = document.getElementById("admin_link_page_size").value;
	var admin_message_page_size = document.getElementById("admin_message_page_size").value;
	var admin_resource_page_size = document.getElementById("admin_resource_page_size").value;
	var admin_log_page_size = document.getElementById("admin_log_page_size").value;
	var admin_email = document.getElementById("admin_email").value;
	var admin_password = document.getElementById("admin_password").value;

	if (website_name == null){
		return false;
	}
	
	var xmlHttp = createXMLHttpRequest();
	var url = "admin/EditConfig";
	xmlHttp.open("post",url,true);
    xmlHttp.onreadystatechange=callback;
    xmlHttp.setRequestHeader("Content-type","application/x-www-form-urlencoded;charset=UTF-8");
    xmlHttp.send("website_name=" + website_name
    		+"front_blog_page_size=" + front_blog_page_size
			+ "&front_page_count=" + front_page_count
			+ "&front_recent_post_count=" + front_recent_post_count
			+ "&front_recent_comment_count=" + front_recent_comment_count
			+ "&front_comment_page_size=" + front_comment_page_size
			+ "&admin_role_page_size=" + admin_role_page_size
			+ "&admin_user_page_size=" + admin_user_page_size
			+ "&admin_category_page_size=" + admin_category_page_size
			+ "&admin_tag_page_size=" + admin_tag_page_size
			+ "&admin_article_page_size=" + admin_article_page_size
			+ "&admin_comment_page_size=" + admin_comment_page_size
			+ "&admin_link_page_size=" + admin_link_page_size
			+ "&admin_message_page_size=" + admin_message_page_size
			+ "&admin_resource_page_size=" + admin_resource_page_size
			+ "&admin_log_page_size=" + admin_log_page_size
			+ "&admin_email=" + encodeURIComponent(admin_email)
			+ "&admin_password=" + encodeURIComponent(admin_password));
    
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
