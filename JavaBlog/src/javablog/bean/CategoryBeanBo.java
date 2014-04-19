package javablog.bean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CategoryBeanBo extends ModelCtrl {

	public CategoryBeanBo(Connection connection) {
		super(connection);
	}

	/*
	 * 根据分类id获得分类
	 */
	public CategoryBean getCategory(int categoryId) {
		CategoryBean ab = null;
		String sql = "SELECT * FROM categories WHERE category_id=" + categoryId;
		ArrayList<CategoryBean> ams = this.getCategories(sql);
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
		return super.getRowCount("categories");
	}

	/*
	 * 根据sql语句获得所有分类记录
	 */
	public ArrayList<CategoryBean> getCategories(String sql) {
		ResultSet rs = null;
		Statement st = null;
		ArrayList<CategoryBean> ams = new ArrayList<CategoryBean>();
		try {
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				CategoryBean ab = new CategoryBean();
				ab.setCategoryId(rs.getInt("category_id"));
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
	 * 根据页数获取第pageNow页的所有分类记录 pageNow为0时获取所有分类
	 */
	public ArrayList<CategoryBean> getCategories(int pageNow) {
		ArrayList<CategoryBean> ams = new ArrayList<CategoryBean>();
		String sql = "SELECT * FROM categories " + this.filter;
		if (pageNow > 0) {
			sql += " LIMIT " + (pageNow - 1) * pageSize + "," + pageSize;
		}
		ams = this.getCategories(sql);
		return ams;
	}

	/*
	 * 搜索并返回所有分类记录
	 */
	public ArrayList<CategoryBean> searchCategories(String keywords, int pageNow) {
		ArrayList<CategoryBean> ams = new ArrayList<CategoryBean>();
		keywords = keywords.trim();
		keywords = keywords.replaceAll(" +", "%");
		String sql = "SELECT * FROM categories WHERE name LIKE '%" + keywords
				+ "%'";
		if (pageNow > 0) {
			sql += " LIMIT " + (pageNow - 1) * pageSize + "," + pageSize;
		}
		ams = this.getCategories(sql);
		return ams;
	}

	/*
	 * 添加一条分类记录
	 */
	public boolean addCategory(CategoryBean categoryModel) {
		boolean result = false;

		String sql = "INSERT INTO categories(name, status) VALUES('"
				+ categoryModel.getName() + "'," + categoryModel.getStatus()
				+ ")";
		int categoryId = this.insert(sql);
		if (categoryId > 0) {
			categoryModel.setCategoryId(categoryId);
			result = true;
		}
		return result;
	}

	/*
	 * 修改一条分类记录
	 */
	public boolean editCategory(CategoryBean categoryModel) {
		boolean result = false;
		String sql = "UPDATE categories SET name='" + categoryModel.getName()
				+ "',status=" + categoryModel.getStatus()
				+ " WHERE category_id=" + categoryModel.getCategoryId();
		result = this.update(sql);
		return result;
	}

	/*
	 * 根据分类id删除相应分类
	 */
	public boolean deleteCategory(int categoryId) {
		boolean result = false;
		String sql = "DELETE FROM categories WHERE category_id=" + categoryId;
		result = this.update(sql);
		return result;
	}

	/*
	 * 根据分类id删除相应分类
	 */
	public boolean deleteCategory(String categoryIds) {
		boolean result = false;
		String sql = "DELETE FROM categories WHERE category_id IN("
				+ categoryIds + ")";
		result = this.update(sql);
		return result;
	}

	// 修改分类状态函数
	public boolean changeStatus(int categoryId, int status) {
		boolean result = false;
		if (status == 0 || status == 1 || status == 2) {
			String sql = "UPDATE categories SET status=" + status
					+ " WHERE category_id=" + categoryId;
			result = this.update(sql);
		}
		return result;
	}

	// 修改分类状态函数
	public boolean changeStatus(String categoryIds, int status) {
		boolean result = false;
		if (status == 0 || status == 1 || status == 2) {
			String sql = "UPDATE categories SET status=" + status
					+ " WHERE category_id IN(" + categoryIds + ")";
			result = this.update(sql);
		}
		return result;
	}
}
