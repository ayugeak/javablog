package javablog.bean;

import java.sql.Timestamp;

/*文章模型类
 * status:0为Trash，1为Normal，2为pending，3为draft
 * */
public class ArticleBean {

	private int articleId;// 文章id
	private int categoryId;// 分类id
	private int authorId;// 作者
	private String title;// 标题
	private String content;// 内容
	private Timestamp date;// 发表时间
	private int status;// 状态

	public int getArticleId() {
		return articleId;
	}

	public void setArticleId(int id) {
		this.articleId = id;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public int getAuthorId() {
		return authorId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
