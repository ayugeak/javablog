package javablog.database;

import java.sql.Connection;
import java.sql.DriverManager;
//used only when install the website, later we can use the connection pool
public class DBCtrl {
	private String driver;
	private String url;
	private String username;
	private String password;

	public DBCtrl() {
		ConfigCtrl configCtrl = new ConfigCtrl("config/database.properties");
		this.driver = configCtrl.getConfigValue("driver");
		this.url = configCtrl.getConfigValue("url");
		this.username = configCtrl.getConfigValue("username");
		this.password = configCtrl.getConfigValue("password");
	}

	public Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName(this.driver).newInstance();
			connection = DriverManager.getConnection(this.url,
					this.username, this.password);
			connection.setAutoCommit(false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return connection;
	}
}
