package javablog.bean;

/*
 * 标签类
 * status:0为Trash，1为Normal，2为pending
 * */
public class TagBean {
	private int tagId;// 标签id
	private String name;// 名称
	private int status;// 状态

	public int getTagId() {
		return this.tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
