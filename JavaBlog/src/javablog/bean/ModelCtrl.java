package javablog.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ModelCtrl {
	protected int pageSize = 10;// 每页显示的数据大小
	protected int rowCount = 0;// 查询得到的数据条数
	protected int pageCount = 0;// 查询得到的分页数
	protected String filter = "";
	protected Connection connection;// 数据库连接对象
	public ModelCtrl(Connection connection) {
		this.connection = connection;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	protected void setFilter(String filter) {
		this.filter = filter;
	}

	/*
	 * 获取结果集记录条数
	 */
	public int getRowCount(String tableName) {
		ResultSet rs = null;
		Statement st = null;
		String sql = "SELECT COUNT(*) FROM " + tableName + this.filter;
		try {
			st = this.connection.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				this.rowCount = rs.getInt(1);
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
		return this.rowCount;
	}

	// 获得分页总数
	public int getPageCount() {
		if (rowCount % pageSize == 0) {
			pageCount = rowCount / pageSize;
		} else {
			pageCount = rowCount / pageSize + 1;
		}
		return pageCount;
	}

	// 获取数据库某个表中的最大或最小id
	public int getBeanId(String sql) {
		ResultSet rs = null;
		Statement st = null;
		int beanId = 0;
		try {
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				beanId = rs.getInt(1);
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
		return beanId;
	}

	// 获取数据库某个表中的元数据值，如文章标题、人名等
	public String getBeanMeta(String sql) {
		ResultSet rs = null;
		Statement st = null;
		String beanMeta = null;
		try {
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				beanMeta = rs.getString(1);
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
		return beanMeta;
	}

	/*
	 * 根据sql语句插入数据 返回值为0代表插入失败，大于0表示成功
	 */
	protected int insert(String sql) {
		int metaId = 0;
		// Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// st = connection.createStatement();//old version mysql-connector
			// st.executeUpdate(sql);
			ps = connection.prepareStatement(sql,
					Statement.RETURN_GENERATED_KEYS);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();// 获取auto_increment的id值
			if (rs.next()) {
				metaId = rs.getInt(1);
			}
		} catch (SQLException e) {
			//e.printStackTrace();//DEBUG
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
				rs = null;
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
				ps = null;
			}
		}
		return metaId;
	}

	/*
	 * 根据sql语句更新数据库
	 */
	protected boolean update(String sql) {
		boolean result = true;
		Statement st = null;
		try {
			st = connection.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
					result = false;
				}
				st = null;
			}
		}
		return result;
	}

	// 关闭数据库连接
	public void closeConnection() {
		if (connection != null) {
			try {
				connection.commit();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			connection = null;
		}
	}
}
