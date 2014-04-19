package javablog.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * 数据库初始化类
 */
public class InitDB {
	private Connection connection;
	private String database;

	public InitDB(Connection connection, String database) {
		this.connection = connection;
		this.database = database;
	}

	private void executeSQL(String sql) {
		Statement st = null;
		try {
			st = connection.createStatement();
			st.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				st = null;
			}
		}
	}

	public void createDatabase() {
		String sql = "DROP DATABASE IF EXISTS `" + this.database + "`";
		this.executeSQL(sql);
		sql = "CREATE DATABASE `" + this.database + "` DEFAULT CHARSET=UTF8";
		this.executeSQL(sql);
	}

	public void createTabel(String tableName, String content) {
		String sql = "DROP TABLE IF EXISTS `" + this.database + "`.`"
				+ tableName + "`";
		this.executeSQL(sql);
		sql = "CREATE TABLE `" + this.database + "`.`" + tableName + "`("
				+ content + ")";
		this.executeSQL(sql);
	}

	public void createTabel() {
		// role
		ConfigCtrl configCtrl = new ConfigCtrl("config/sql.properties");
		String name = configCtrl.getConfigValue("role.name");
		String sql = configCtrl.getConfigValue("role.sql");
		this.createTabel(name, sql);

		// user
		name = configCtrl.getConfigValue("user.name");
		sql = configCtrl.getConfigValue("user.sql");
		this.createTabel(name, sql);

		// category
		name = configCtrl.getConfigValue("category.name");
		sql = configCtrl.getConfigValue("category.sql");
		this.createTabel(name, sql);

		// tag
		name = configCtrl.getConfigValue("tag.name");
		sql = configCtrl.getConfigValue("tag.sql");
		this.createTabel(name, sql);

		// article
		name = configCtrl.getConfigValue("article.name");
		sql = configCtrl.getConfigValue("article.sql");
		this.createTabel(name, sql);

		// article_tag
		name = configCtrl.getConfigValue("article_tag.name");
		sql = configCtrl.getConfigValue("article_tag.sql");
		this.createTabel(name, sql);

		// comment
		name = configCtrl.getConfigValue("comment.name");
		sql = configCtrl.getConfigValue("comment.sql");
		this.createTabel(name, sql);

		// message
		name = configCtrl.getConfigValue("message.name");
		sql = configCtrl.getConfigValue("message.sql");
		this.createTabel(name, sql);

		// resource
		name = configCtrl.getConfigValue("resource.name");
		sql = configCtrl.getConfigValue("resource.sql");
		this.createTabel(name, sql);

		// link
		name = configCtrl.getConfigValue("link.name");
		sql = configCtrl.getConfigValue("link.sql");
		this.createTabel(name, sql);

		// logs
		name = configCtrl.getConfigValue("log.name");
		sql = configCtrl.getConfigValue("log.sql");
		this.createTabel(name, sql);
	}

}
