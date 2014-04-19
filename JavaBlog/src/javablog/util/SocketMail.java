package javablog.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

//import sun.misc.BASE64Encoder;//for sun jdk
import org.apache.commons.codec.binary.Base64;

/**
 * 通过socket向smtp协议服务器发送邮件
 * 
 */
public class SocketMail {

	private String lineFeet = "\r\n";
	private String systemEncoding = System.getProperty("file.encoding");
	// server
	private String serverAddress;
	private int port;
	private String security;
	private String serverReply;
	// from
	private String fromName;
	private String fromAddress;
	private String password;
	// to
	private String toName;
	private String toAddress;
	private String subject;
	private String content;

	private Socket mailSocket;
	private BufferedReader inFromServer;
	private DataOutputStream outToServer;

	public SocketMail() {

	}

	// 设置服务器信息
	public void setServer(String serverAddress, int port, String security) {
		this.serverAddress = serverAddress;
		this.port = port;
		this.security = security;
	}

	// 设置发件人信息
	public void setFrom(String fromName, String fromAddress, String password) {
		this.fromName = fromName;
		this.fromAddress = fromAddress;
		this.password = password;
	}

	// 设置收件人信息
	public void setTo(String toName, String toAddress) {
		this.toName = toName;
		this.toAddress = toAddress;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setContent(String content) {
		this.content = content;
	}

	// 连接服务器
	public boolean connectServer() {
		boolean result = false;
		try {
			if (security == null || security.equalsIgnoreCase("STARTTLS")) {
				this.mailSocket = new Socket(this.serverAddress, port);
			} else if (security.equalsIgnoreCase("TLS")
					|| security.equalsIgnoreCase("SSL")) {
				SocketFactory socketFactory = SSLSocketFactory.getDefault();
				this.mailSocket = socketFactory.createSocket(
						this.serverAddress, port);
			}

			this.inFromServer = new BufferedReader(new InputStreamReader(
					mailSocket.getInputStream()));
			this.outToServer = new DataOutputStream(
					mailSocket.getOutputStream());
			serverReply = this.inFromServer.readLine();
			if (serverReply.startsWith("220")) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// STARTTLS模式，将普通的的SOCKET转换为SSLSOCKET
	private void startTLS() {
		SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory
				.getDefault();
		SSLSocket tls = null;
		try {
			serverReply = sendCommand("STARTTLS" + lineFeet);
			tls = (SSLSocket) factory.createSocket(mailSocket, mailSocket
					.getInetAddress().getHostName(), mailSocket.getPort(),
					false);

			tls.startHandshake();
			this.inFromServer = new BufferedReader(new InputStreamReader(
					tls.getInputStream()));
			this.outToServer = new DataOutputStream(tls.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 登录
	public boolean login() {
		boolean result = false;
		try {
			serverReply = sendCommand("HELO " + this.serverAddress + lineFeet);
			if (serverReply.startsWith("250")) {
				if (this.security.equalsIgnoreCase("STARTTLS")) {
					this.startTLS();
				}
				// 验证发信人信息
				serverReply = sendCommand("AUTH LOGIN" + lineFeet);
				if (serverReply.startsWith("334")) {
					String encodedFrom = new String(
							this.encode(this.fromAddress));
					serverReply = sendCommand(encodedFrom + lineFeet);
					if (serverReply.startsWith("334")) {
						String encodedPassword = new String(
								this.encode(this.password));
						serverReply = sendCommand(encodedPassword + lineFeet);
						if (serverReply.startsWith("235")) {
							result = true;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 发送smtp指令 并返回服务器响应信息
	 */
	private String sendCommand(String command) {
		String result = null;
		try {
			this.outToServer.writeBytes(command);
			this.outToServer.flush();
			result = this.inFromServer.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 关闭
	 */
	public void close() {
		try {
			this.outToServer.close();
			this.inFromServer.close();
			mailSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String encode(String content) {
		//BASE64Encoder encoder = new BASE64Encoder();//for sun jdk
		//content = encoder.encode(content.getBytes());
		content = Base64.encodeBase64String(content.getBytes());
		return content;
	}

	public String encodeTitle(String title) {
		String encodeString = "=?" + this.systemEncoding + "?B?"
				+ this.encode(title) + "?=";
		return encodeString;
	}

	/**
	 * 发送邮件
	 * 
	 * @return
	 */
	public boolean sendMail() {
		// 邮件头
		String serverReply = sendCommand("Mail From:<" + this.fromAddress + ">"
				+ lineFeet);
		if (!serverReply.startsWith("250")) {
			return false;
		}
		serverReply = sendCommand("RCPT TO:<" + this.toAddress + ">" + lineFeet);
		if (!serverReply.startsWith("250")) {
			return false;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("From: " + this.encodeTitle(this.fromName) + "<"
				+ this.fromAddress + ">" + lineFeet);
		sb.append("To: \"" + this.encodeTitle(this.toName) + "\" <"
				+ this.toAddress + ">" + lineFeet);
		serverReply = sendCommand("DATA" + lineFeet);
		if (!serverReply.startsWith("354")) {
			return false;
		}
		// 邮件数据
		sb.append("Subject: " + this.encodeTitle(this.subject) + lineFeet);
		sb.append("Date: " + (new Date()).toString() + lineFeet);
		sb.append("MIME-Version: 1.0" + lineFeet);
		sb.append("Content-Type: multipart/mixed;" + lineFeet);
		sb.append("\tboundary=\"----=_JavaBlog_\"" + lineFeet);
		sb.append(lineFeet + "------=_JavaBlog_" + lineFeet);
		sb.append("Content-Type: text/html;charset=\"" + this.systemEncoding
				+ "\"" + lineFeet);
		sb.append("Content-Transfer-Encoding: base64" + lineFeet);
		sb.append(lineFeet + this.encode(this.content) + lineFeet);
		sb.append(lineFeet + "------=_JavaBlog_--" + lineFeet);
		sb.append(lineFeet + "." + lineFeet);// 邮件结束
		// 发送数据
		serverReply = sendCommand(sb.toString());
		if (!serverReply.startsWith("250")) {
			return false;
		}
		serverReply = sendCommand("QUIT" + lineFeet);
		if (!serverReply.startsWith("221")) {
			return false;
		}

		return true;
	}
}
