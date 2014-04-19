package javablog.bean;

/*
 * 文章标签类，管理标签与文章的多对多关系
 * */
public class ArticleTagBean {
	private int articleId;
	private int tagId;

	public int getArticleId() {
		return this.articleId;
	}

	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}

	public int getTagId() {
		return this.tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

}
