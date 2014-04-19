package javablog.util;

import java.io.IOException;

public class CacheCtrl {
	private String localPath;
	private String webPath;
	
	public CacheCtrl(String webPath){
		this.webPath = webPath + "include/dynamic/";
		String url = this.getClass().getResource("").getPath().replaceAll("%20", " ");
		this.localPath = url.substring(0, url.indexOf("WEB-INF")) + "include/static/";
	}
	
	public boolean updateCache(String dynamicPage, String staticPage){
		boolean result = false;
		Runtime rt = Runtime.getRuntime();
		try {
			String command = "wget " + dynamicPage + " -O " + staticPage;
			rt.exec(command);
			result = true;
		} catch (IOException e){
			e.printStackTrace();
		}
		return result;
	}

	public boolean updateBaseCache(){
		String dynamicPage = this.webPath + "base.jsp";
		String staticPage = this.localPath + "base.html";
		return this.updateCache(dynamicPage, staticPage);
	}
	
	public boolean updateHeaderCache(){
		String dynamicPage = this.webPath + "header.jsp";
		String staticPage = this.localPath + "header.html";
		return this.updateCache(dynamicPage, staticPage);
	}
	
	public boolean updateFooterCache(){
		String dynamicPage = this.webPath + "footer.jsp";
		String staticPage = this.localPath + "footer.html";
		return this.updateCache(dynamicPage, staticPage);
	}
	
	public boolean updateCategoryCache(){
		String dynamicPage = this.webPath + "categoryList.jsp";
		String staticPage = this.localPath + "categoryList.html";
		return this.updateCache(dynamicPage, staticPage);
	}
	
	public boolean updateTagCache(){
		String dynamicPage = this.webPath + "tagList.jsp";
		String staticPage = this.localPath + "tagList.html";
		return this.updateCache(dynamicPage, staticPage);
	}

	public boolean updateLinkCache(){
		String dynamicPage = this.webPath + "linkList.jsp";
		String staticPage = this.localPath + "linkList.html";
		return this.updateCache(dynamicPage, staticPage);
	}

	public boolean updateRecentArticleCache(){
		String dynamicPage = this.webPath
				+ "titleList.jsp?attribute=article_id&count="
				+ ConfigProperty.configCtrl.getConfigValue("front_recent_post_count");
		String staticPage = this.localPath + "titleList.html";
		return this.updateCache(dynamicPage, staticPage);
	}
	
	public boolean updateRecentCommentCache(){
		String dynamicPage = this.webPath + "recentComment.jsp?count="
				+ ConfigProperty.configCtrl.getConfigValue("front_recent_comment_count");
		String staticPage = this.localPath + "recentComment.html";
		return this.updateCache(dynamicPage, staticPage);
	}
}
