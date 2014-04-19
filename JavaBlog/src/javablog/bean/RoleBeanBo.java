package javablog.bean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RoleBeanBo extends ModelCtrl {

	public RoleBeanBo(Connection connection) {
		super(connection);
	}

	/*
	 * 根据角色id获得角色
	 */
	public RoleBean getRole(int roleId) {
		RoleBean ab = null;
		String sql = "SELECT * FROM roles WHERE role_id=" + roleId;
		ArrayList<RoleBean> ams = this.getRoles(sql);
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
		return super.getRowCount("roles");
	}

	/*
	 * 根据sql语句获得所有角色记录
	 */
	public ArrayList<RoleBean> getRoles(String sql) {
		ResultSet rs = null;
		Statement st = null;
		ArrayList<RoleBean> ams = new ArrayList<RoleBean>();
		try {
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				RoleBean ab = new RoleBean();
				ab.setRoleId(rs.getInt("role_id"));
				ab.setName(rs.getString("name"));
				ab.setPermission(rs.getString("permission"));
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
	 * 根据页数获取第pageNow页的所有角色记录 pageNow为0时获取所有角色
	 */
	public ArrayList<RoleBean> getRoles(int pageNow) {
		ArrayList<RoleBean> ams = new ArrayList<RoleBean>();
		String sql = "SELECT * FROM roles " + this.filter;
		if (pageNow > 0) {
			sql += " LIMIT " + (pageNow - 1) * pageSize + "," + pageSize;
		}
		ams = this.getRoles(sql);
		return ams;
	}

	/*
	 * 搜索并返回所有角色记录
	 */
	public ArrayList<RoleBean> searchRoles(String keywords, int pageNow) {
		ArrayList<RoleBean> ams = new ArrayList<RoleBean>();
		keywords = keywords.trim();
		keywords = keywords.replaceAll(" +", "%");
		String sql = "SELECT * FROM roles WHERE name LIKE '%" + keywords + "%'";
		if (pageNow > 0) {
			sql += " LIMIT " + (pageNow - 1) * pageSize + "," + pageSize;
		}
		ams = this.getRoles(sql);
		return ams;
	}

	/*
	 * 添加一条角色记录
	 */
	public boolean addRole(RoleBean roleModel) {
		boolean result = false;
		String sql = "INSERT INTO roles(name, permission, status) VALUES('"
				+ roleModel.getName() + "','" + roleModel.getPermission()
				+ "'," + roleModel.getStatus() + ")";
		int roleId = this.insert(sql);
		if (roleId > 0) {
			roleModel.setRoleId(roleId);
			result = true;
		}
		return result;
	}

	/*
	 * 修改一条角色记录
	 */
	public boolean editRole(RoleBean roleModel) {
		boolean result = false;
		String sql = "UPDATE roles SET name='" + roleModel.getName()
				+ "',permission='" + roleModel.getPermission() + "',status="
				+ roleModel.getStatus() + " WHERE role_id="
				+ roleModel.getRoleId();
		result = this.update(sql);
		return result;
	}

	/*
	 * 根据角色id删除相应角色
	 */
	public boolean deleteRole(int roleId) {
		boolean result = false;
		String sql = "DELETE FROM roles WHERE role_id=" + roleId;
		result = this.update(sql);
		return result;
	}

	/*
	 * 根据角色id删除相应角色
	 */
	public boolean deleteRole(String roleIds) {
		boolean result = false;
		String sql = "DELETE FROM roles WHERE role_id IN(" + roleIds + ")";
		result = this.update(sql);
		return result;
	}

	// 修改角色状态函数
	public boolean changeStatus(int roleId, int status) {
		boolean result = false;
		if (status == 0 || status == 1 || status == 2) {
			String sql = "UPDATE roles SET status=" + status
					+ " WHERE role_id=" + roleId;
			result = this.update(sql);
		}
		return result;
	}

	// 修改角色状态函数
	public boolean changeStatus(String roleIds, int status) {
		boolean result = false;
		if (status == 0 || status == 1 || status == 2) {
			String sql = "UPDATE roles SET status=" + status
					+ " WHERE role_id IN(" + roleIds + ")";
			result = this.update(sql);
		}
		return result;
	}
}
