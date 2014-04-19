package javablog.bean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TagBeanBo extends ModelCtrl {

	public TagBeanBo(Connection connection) {
		super(connection);
	}

	/*
	 * 根据标签id获得标签
	 */
	public TagBean getTag(int tagId) {
		TagBean ab = null;
		String sql = "SELECT * FROM tags WHERE tag_id=" + tagId;
		ArrayList<TagBean> ams = this.getTags(sql);
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
	
	public void setFilter(String status, String tagIds, String attribute, String order) {
		filter = " WHERE status IN(" + status + ")";
		if (tagIds != null && !tagIds.equals("0")) {
			filter += " AND tag_id IN(" + tagIds + ")";
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
		return super.getRowCount("tags");
	}

	/*
	 * 根据sql语句获得所有标签记录
	 */
	public ArrayList<TagBean> getTags(String sql) {
		ResultSet rs = null;
		Statement st = null;
		ArrayList<TagBean> ams = new ArrayList<TagBean>();
		try {
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				TagBean ab = new TagBean();
				ab.setTagId(rs.getInt("tag_id"));
				ab.setName(rs.getString("name"));
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
	 * 根据页数获取第pageNow页的所有标签记录 pageNow为0时获取所有标签
	 */
	public ArrayList<TagBean> getTags(int pageNow) {
		ArrayList<TagBean> ams = new ArrayList<TagBean>();
		String sql = "SELECT * FROM tags " + this.filter;
		if (pageNow > 0) {
			sql += " LIMIT " + (pageNow - 1) * pageSize + "," + pageSize;
		}
		ams = this.getTags(sql);
		return ams;
	}

	/*
	 * 搜索并返回所有标签记录
	 */
	public ArrayList<TagBean> searchTags(String keywords, int pageNow) {
		ArrayList<TagBean> ams = new ArrayList<TagBean>();
		keywords = keywords.trim();
		keywords = keywords.replaceAll(" +", "%");
		String sql = "SELECT * FROM tags WHERE name LIKE '%" + keywords + "%'";
		if (pageNow > 0) {
			sql += " LIMIT " + (pageNow - 1) * pageSize + "," + pageSize;
		}
		ams = this.getTags(sql);
		return ams;
	}

	/*
	 * 根据sql语句更新标签
	 */
	public boolean updateTag(String sql) {
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

	/*
	 * 添加一条标签记录
	 */
	public boolean addTag(TagBean tagModel) {
		boolean result = false;
		String sql = "INSERT INTO tags(name, status) VALUES('"
				+ tagModel.getName() + "'," + tagModel.getStatus() + ")";
		int tagId = this.insert(sql);
		if (tagId > 0) {
			result = true;
		} else {// 该标签已存在，只能通过name查询其id
			sql = "SELECT * FROM tags WHERE name='" + tagModel.getName() + "'";
			String tagIdText = super.getBeanMeta(sql);
			if (tagIdText != null) {
				tagId = Integer.parseInt(tagIdText);
			}
		}
		tagModel.setTagId(tagId);
		return result;
	}

	/*
	 * 修改一条标签记录
	 */
	public boolean editTag(TagBean tagModel) {
		boolean result = false;
		String sql = "UPDATE tags SET name='" + tagModel.getName()
				+ "',status=" + tagModel.getStatus() + " WHERE tag_id="
				+ tagModel.getTagId();
		result = this.update(sql);
		return result;
	}

	/*
	 * 根据标签id删除相应标签
	 */
	public boolean deleteTag(int tagId) {
		boolean result = false;
		String sql = "DELETE FROM tags WHERE tag_id=" + tagId;
		result = this.update(sql);
		return result;
	}

	/*
	 * 根据标签id删除相应标签
	 */
	public boolean deleteTag(String tagIds) {
		boolean result = false;
		String sql = "DELETE FROM tags WHERE tag_id IN(" + tagIds + ")";
		result = this.update(sql);
		return result;
	}

	// 修改标签状态函数
	public boolean changeStatus(int tagId, int status) {
		boolean result = false;
		if (status == 0 || status == 1 || status == 2 || status == 3) {
			String sql = "UPDATE tags SET status=" + status + " WHERE tag_id="
					+ tagId;
			result = this.update(sql);
		}
		return result;
	}

	// 修改标签状态函数
	public boolean changeStatus(String tagIds, int status) {
		boolean result = false;
		if (status == 0 || status == 1 || status == 2) {
			String sql = "UPDATE tags SET status=" + status
					+ " WHERE tag_id IN(" + tagIds + ")";
			result = this.update(sql);
		}
		return result;
	}
}
