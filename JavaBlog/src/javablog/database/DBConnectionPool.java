package javablog.database;

import java.sql.Connection;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBConnectionPool {
	private static DBConnectionPool dbCtrl = new DBConnectionPool();
	private DataSource dataSource;

	private DBConnectionPool() {
		InitialContext context;
		ConfigCtrl configCtrl = new ConfigCtrl("config/database.properties");
		String database = configCtrl.getConfigValue("database");
		try {
			context = new InitialContext();
			dataSource = (DataSource) context
					.lookup("java:comp/env/jdbc/" + database);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static DBConnectionPool getDBConnectionPool() {
		return dbCtrl;
	}

	public Connection getConnection() {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
		} catch (Exception ex) {
			//ex.printStackTrace();
			connection = null;
		}
		return connection;
	}
}
