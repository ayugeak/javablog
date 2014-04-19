package javablog.bean;

import java.sql.Timestamp;

public class CommentBean {
	private int commentId;// 评论id
	private int articleId;// 文章id
	private int userId;// 用户id
	private String name;// 用户昵称
	private String email;// Email
	private String url;// homepage
	private String content;// 内容
	private Timestamp date;// 发表时间
	private int status;// 状态

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public int getCommentId() {
		return this.commentId;
	}

	public int getArticleId() {
		return this.articleId;
	}

	public void setArticleId(int id) {
		this.articleId = id;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getDate() {
		return this.date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
