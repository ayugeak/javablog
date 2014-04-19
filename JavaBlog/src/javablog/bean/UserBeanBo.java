package javablog.bean;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javablog.util.ConfigProperty;

public class UserBeanBo extends ModelCtrl {

	public UserBeanBo(Connection connection) {
		super(connection);
	}

	/*
	 * 根据用户id获得用户
	 */
	public UserBean getUser(int userId) {
		UserBean ab = null;
		String sql = "SELECT * FROM users WHERE user_id=" + userId;
		ArrayList<UserBean> ams = this.getUsers(sql);
		if (ams.size() > 0) {
			ab = ams.get(0);
		}
		return ab;
	}

	/*
	 * 根据用户名获得用户
	 */
	public UserBean getUser(String username) {
		UserBean ab = null;
		String sql = "SELECT * FROM users WHERE username='" + username + "'";
		ArrayList<UserBean> ams = this.getUsers(sql);
		if (ams.size() > 0) {
			ab = ams.get(0);
		}
		return ab;
	}

	/*
	 * 设置sql语句中的过滤器
	 */
	// 设置排序方式函数
	public void setFilter(int status, int roleId, String attribute, String order) {
		filter = " WHERE status=" + status;
		if (roleId != 0) {
			filter += " AND role_id=" + roleId;
		}
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
		return super.getRowCount("users");
	}

	/*
	 * 根据sql语句获得所有用户记录
	 */
	public ArrayList<UserBean> getUsers(String sql) {
		ResultSet rs = null;
		Statement st = null;
		ArrayList<UserBean> ams = new ArrayList<UserBean>();
		try {
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				UserBean ab = new UserBean();
				ab.setUserId(rs.getInt("user_id"));
				ab.setRoleId(rs.getInt("role_id"));
				ab.setUsername(rs.getString("username"));
				ab.setPassword(rs.getString("password"));
				ab.setName(rs.getString("name"));
				ab.setEmail(rs.getString("email"));
				ab.setUrl(rs.getString("url"));
				ab.setAvatar(rs.getString("avatar"));
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
	 * 根据页数获取第pageNow页的所有用户记录 pageNow为0时获取所有用户
	 */
	public ArrayList<UserBean> getUsers(int pageNow) {
		ArrayList<UserBean> ams = new ArrayList<UserBean>();
		String sql = "SELECT * FROM users " + this.filter;
		if (pageNow > 0) {
			sql += " LIMIT " + (pageNow - 1) * pageSize + "," + pageSize;
		}
		ams = this.getUsers(sql);
		return ams;
	}

	// 用户登录判断
	public boolean checkLogin(UserBean ub) {
		boolean result = false;
		ResultSet rs = null;
		Statement st = null;
		try {
			st = connection.createStatement();
			String sql = "select * from users where username='"
					+ ub.getUsername() + "'";
			rs = st.executeQuery(sql);
			if (rs.next()) {
				ub.setUserId(rs.getInt("user_id"));
				String password = rs.getString("password");
				int status = rs.getInt("status");
				if (password.equals(ub.getPassword())
						&& status == ConfigProperty.STATUS_NORMAL) {
					result = true;
				}
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
		return result;
	}

	/*
	 * 搜索并返回所有用户记录
	 */
	public ArrayList<UserBean> searchUsers(String keywords, int pageNow) {
		ArrayList<UserBean> ams = new ArrayList<UserBean>();
		keywords = keywords.trim();
		keywords = keywords.replaceAll(" +", "%");
		String sql = "SELECT * FROM users WHERE name LIKE '%" + keywords + "%'";
		if (pageNow > 0) {
			sql += " LIMIT " + (pageNow - 1) * pageSize + "," + pageSize;
		}
		ams = this.getUsers(sql);
		return ams;
	}

	/*
	 * 添加一条用户记录
	 */
	public boolean addUser(UserBean userModel) {
		boolean result = false;
		String sql = "INSERT INTO users(role_id, username, password, name, email, url, avatar, status) VALUES("
				+ userModel.getRoleId()
				+ ",'"
				+ userModel.getUsername()
				+ "','"
				+ userModel.getPassword()
				+ "','"
				+ userModel.getName()
				+ "','"
				+ userModel.getEmail()
				+ "','"
				+ userModel.getUrl()
				+ "','"
				+ userModel.getAvatar()
				+ "',"
				+ userModel.getStatus()
				+ ")";
		int userId = this.insert(sql);
		if (userId > 0) {
			userModel.setUserId(userId);
			result = true;
		}
		return result;
	}

	/*
	 * 修改一条用户记录
	 */
	public boolean editUser(UserBean userModel) {
		boolean result = false;
		String sql = "UPDATE users SET role_id=" + userModel.getRoleId()
				+ ",password='" + userModel.getPassword() + "',name='"
				+ userModel.getName() + "',email='" + userModel.getEmail()
				+ "',url='" + userModel.getUrl() + "',avatar='"
				+ userModel.getAvatar() + "',status=" + userModel.getStatus()
				+ " WHERE user_id=" + userModel.getUserId();
		result = this.update(sql);
		return result;
	}

	/*
	 * 根据用户id删除相应用户
	 */
	public boolean deleteUser(int userId) {
		boolean result = false;
		String sql = "DELETE FROM users WHERE user_id=" + userId;
		result = this.update(sql);
		return result;
	}

	/*
	 * 根据用户id删除相应用户
	 */
	public boolean deleteUser(String userIds) {
		boolean result = false;
		String sql = "DELETE FROM users WHERE user_id IN(" + userIds + ")";
		result = this.update(sql);
		return result;
	}

	// 修改用户状态函数
	public boolean changeStatus(int userId, int status) {
		boolean result = false;
		if (status == 0 || status == 1 || status == 2 || status == 3) {
			String sql = "UPDATE users SET status=" + status
					+ " WHERE user_id=" + userId;
			result = this.update(sql);
		}
		return result;
	}

	// 修改用户状态函数
	public boolean changeStatus(String userIds, int status) {
		boolean result = false;
		if (status == 0 || status == 1 || status == 2 || status == 3) {
			String sql = "UPDATE users SET status=" + status
					+ " WHERE user_id IN(" + userIds + ")";
			result = this.update(sql);
		}
		return result;
	}

	public String randomPassword() {
		String password = "";
		String dictionary = "abcdefghijklmnopqrstuvwxyz";
		int length = (int) (Math.random() * 10) + 6;// 6-16之间的位数
		for (int i = 0; i < length; i++) {
			int c = (int) (Math.random() * 2);
			int index = (int) (Math.random() * 26);
			if (c == 0) {
				password += dictionary.charAt(index);
			} else {
				password += dictionary.toUpperCase().charAt(index);
			}
		}
		return password;
	}

	// MD5加密算法
	public String getMD5Str(String str) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}
}
