package javablog.bean;

import java.sql.Timestamp;

/*
 * 留言类，用于用户之间相互发信时用，有三种状态,0-read, 1-trash, 2-unread
 */
public class MessageBean {
	private int messageId;// 信息id
	private int senderId;// 发送者id
	private int receiverId;// 接收者id
	private String content;// 内容
	private Timestamp date;// 发送时间
	private int status;// 状态

	public int getMessageId() {
		return this.messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public int getSenderId() {
		return this.senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public int getReceiverId() {
		return this.receiverId;
	}

	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
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
