package javablog.bean;

/*
 * 分类类
 * status:1为Normal,0为Trash
 * */
public class CategoryBean {
	private int categoryId;// 分类id
	private String name;// 名称
	private int status;// 状态

	public int getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
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
