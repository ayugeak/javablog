package javablog.bean;

//元数据模型，只包含键和值
public class MetaModel {
	private int key;
	private String value;

	public int getKey() {
		return this.key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
