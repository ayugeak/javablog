package javablog.bean;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

//资源类，包括上传的图片、文档等资料和添加的友情资源等
public class ResourceBeanBo extends ModelCtrl {
	private String path = "resource/";

	public ResourceBeanBo(Connection connection) {
		super(connection);
	}

	/*
	 * 根据资源id获得资源
	 */
	public ResourceBean getResource(int resourceId) {
		ResourceBean ab = null;
		String sql = "SELECT * FROM resources WHERE resource_id=" + resourceId;
		ArrayList<ResourceBean> ams = this.getResources(sql);
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
		return super.getRowCount("resources");
	}

	/*
	 * 根据sql语句获得所有资源记录
	 */
	public ArrayList<ResourceBean> getResources(String sql) {
		ResultSet rs = null;
		Statement st = null;
		ArrayList<ResourceBean> ams = new ArrayList<ResourceBean>();
		try {
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				ResourceBean ab = new ResourceBean();
				ab.setResourceId(rs.getInt("resource_id"));
				ab.setName(rs.getString("name"));
				ab.setUrl(rs.getString("url"));
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
	 * 根据页数获取第pageNow页的所有资源记录 pageNow为0时获取所有资源
	 */
	public ArrayList<ResourceBean> getResources(int pageNow) {
		ArrayList<ResourceBean> ams = new ArrayList<ResourceBean>();
		String sql = "SELECT * FROM resources " + this.filter;
		if (pageNow > 0) {
			sql += " LIMIT " + (pageNow - 1) * pageSize + "," + pageSize;
		}
		ams = this.getResources(sql);
		return ams;
	}

	/*
	 * 搜索并返回所有资源记录
	 */
	public ArrayList<ResourceBean> searchResources(String keywords, int pageNow) {
		ArrayList<ResourceBean> ams = new ArrayList<ResourceBean>();
		keywords = keywords.trim();
		keywords = keywords.replaceAll(" +", "%");
		String sql = "SELECT * FROM resources WHERE name LIKE '%" + keywords
				+ "%'";
		if (pageNow > 0) {
			sql += " LIMIT " + (pageNow - 1) * pageSize + "," + pageSize;
		}
		ams = this.getResources(sql);
		return ams;
	}

	/*
	 * 添加一条资源记录
	 */
	public boolean addResource(ResourceBean resourceModel) {
		boolean result = false;
		String idText = (new Date()).toString();
		idText = getMD5Str(idText);
		String format = "";
		int index = resourceModel.getName().lastIndexOf('.');
		if (index != -1){
			format = resourceModel.getName().substring(index);
		}
		
		String url = this.path + idText + format;
		resourceModel.setUrl(url);
		resourceModel.setDate(new Timestamp((new Date()).getTime()));
		String sql = "INSERT INTO resources(name, url, date, status) VALUES('"
				+ resourceModel.getName() + "','" + resourceModel.getUrl()
				+ "','" + resourceModel.getDate() + "',"
				+ resourceModel.getStatus() + ")";
		int resourceId = this.insert(sql);
		if (resourceId > 0) {
			resourceModel.setResourceId(resourceId);
			result = true;
		}
		return result;
	}

	/*
	 * 修改一条资源记录
	 */
	public boolean editResource(ResourceBean resourceModel) {
		boolean result = false;
		String sql = "UPDATE resources SET name='" + resourceModel.getName()
				+ "',status=" + resourceModel.getStatus()
				+ " WHERE resource_id=" + resourceModel.getResourceId();
		result = this.update(sql);
		return result;
	}

	/*
	 * 根据资源id删除相应资源
	 */
	public boolean deleteResource(int resourceId) {
		boolean result = false;
		String sql = "DELETE FROM resources WHERE resource_id=" + resourceId;
		result = this.update(sql);
		return result;
	}

	/*
	 * 根据资源id删除相应资源
	 */
	public boolean deleteResource(String resourceIds) {
		boolean result = false;
		String sql = "DELETE FROM resources WHERE resource_id IN("
				+ resourceIds + ")";
		result = this.update(sql);
		return result;
	}

	// 修改资源状态函数
	public boolean changeStatus(int resourceId, int status) {
		boolean result = false;
		if (status == 0 || status == 1 || status == 2 || status == 3) {
			String sql = "UPDATE resources SET status=" + status
					+ " WHERE resource_id=" + resourceId;
			result = this.update(sql);
		}
		return result;
	}

	// 修改资源状态函数
	public boolean changeStatus(String resourceIds, int status) {
		boolean result = false;
		if (status == 0 || status == 1 || status == 2 || status == 3) {
			String sql = "UPDATE resources SET status=" + status
					+ " WHERE resource_id IN(" + resourceIds + ")";
			result = this.update(sql);
		}
		return result;
	}

	// MD5加密算法
	public String getMD5Str(String str) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(str.getBytes("UTF-8"));
		} catch (Exception e) {
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
