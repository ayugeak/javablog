package javablog.bean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class LogBeanBo extends ModelCtrl {

	public LogBeanBo(Connection connection) {
		super(connection);
	}

	/*
	 * 根据文章id获得文章
	 */
	public LogBean getLog(int logId) {
		LogBean ab = null;
		String sql = "SELECT * FROM logs WHERE log_id=" + logId;
		ArrayList<LogBean> ams = this.getLogs(sql);
		if (ams.size() > 0) {
			ab = ams.get(0);
		}
		return ab;
	}

	/*
	 * 设置sql语句中的过滤器
	 */
	// 设置排序方式函数
	public void setFilter(int status, String attribute, String order) {
		filter = " WHERE status=" + status;
		if (attribute != null && attribute.length() > 0) {
			filter += " ORDER BY " + attribute;
			if (order != null && order.equalsIgnoreCase("desc")) {
				filter += " " + order;
			}
		}
	}

	/*
	 * 获取结果集记录条数
	 */
	public int getRowCount() {
		return super.getRowCount("logs");
	}

	/*
	 * 根据sql语句获得所有文章记录
	 */
	public ArrayList<LogBean> getLogs(String sql) {
		ResultSet rs = null;
		Statement st = null;
		ArrayList<LogBean> ams = new ArrayList<LogBean>();
		try {
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				LogBean ab = new LogBean();
				ab.setLogId(rs.getInt("log_id"));
				ab.setContent(rs.getString("content"));
				ab.setDate(rs.getTimestamp("date"));
				ab.setStatus(rs.getInt("status"));

				ams.add(ab);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
				rs = null;
			}
			if (st != null) {
				try {
					st.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				st = null;
			}
		}
		return ams;
	}

	/*
	 * 根据页数获取第pageNow页的所有文章记录 pageNow为0时获取所有文章
	 */
	public ArrayList<LogBean> getLogs(int pageNow) {
		ArrayList<LogBean> ams = new ArrayList<LogBean>();
		String sql = "SELECT * FROM logs " + this.filter;
		if (pageNow > 0) {
			sql += " LIMIT " + (pageNow - 1) * pageSize + "," + pageSize;
		}
		ams = this.getLogs(sql);
		return ams;
	}

	/*
	 * 搜索并返回所有文章记录
	 */
	public ArrayList<LogBean> searchLogs(String keywords, int pageNow) {
		ArrayList<LogBean> ams = new ArrayList<LogBean>();
		keywords = keywords.trim();
		keywords = keywords.replaceAll(" +", "%");
		String sql = "SELECT * FROM logs WHERE content LIKE '%" + keywords
				+ "%'";
		if (pageNow > 0) {
			sql += " LIMIT " + (pageNow - 1) * pageSize + "," + pageSize;
		}
		ams = this.getLogs(sql);
		return ams;
	}

	/*
	 * 添加一条文章记录
	 */
	public boolean addLog(LogBean logModel) {
		boolean result = false;
		logModel.setDate(new Timestamp((new Date()).getTime()));
		String sql = "INSERT INTO logs(content, date, status) VALUES('"
				+ logModel.getContent() + "','" + logModel.getDate() + "',"
				+ logModel.getStatus() + ")";
		int logId = this.insert(sql);
		if (logId > 0) {
			logModel.setLogId(logId);
			result = true;
		}
		return result;
	}

	/*
	 * 修改一条文章记录
	 */
	public boolean editLog(LogBean logModel) {
		boolean result = false;
		logModel.setDate(new Timestamp((new Date()).getTime()));
		String sql = "UPDATE logs SET content='" + logModel.getContent()
				+ "',date='" + logModel.getDate() + "',status="
				+ logModel.getStatus() + " WHERE log_id=" + logModel.getLogId();
		result = this.update(sql);
		return result;
	}

	/*
	 * 根据文章id删除相应文章
	 */
	public boolean deleteLog(int logId) {
		boolean result = false;
		String sql = "DELETE FROM logs WHERE log_id=" + logId;
		result = this.update(sql);
		return result;
	}

	/*
	 * 根据文章id删除相应文章
	 */
	public boolean deleteLog(String logIds) {
		boolean result = false;
		String sql = "DELETE FROM logs WHERE log_id IN(" + logIds + ")";
		result = this.update(sql);
		return result;
	}

	// 修改文章状态函数
	public boolean changeStatus(int logId, int status) {
		boolean result = false;
		if (status == 0 || status == 1 || status == 2 || status == 3) {
			String sql = "UPDATE logs SET status=" + status + " WHERE log_id="
					+ logId;
			result = this.update(sql);
		}
		return result;
	}

	// 修改文章状态函数
	public boolean changeStatus(String logIds, int status) {
		boolean result = false;
		if (status == 0 || status == 1 || status == 2 || status == 3) {
			String sql = "UPDATE logs SET status=" + status
					+ " WHERE log_id IN(" + logIds + ")";
			result = this.update(sql);
		}
		return result;
	}
}
