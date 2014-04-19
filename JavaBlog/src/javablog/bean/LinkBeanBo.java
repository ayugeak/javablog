package javablog.bean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

//链接类，包括上传的图片、文档等资料和添加的友情链接等
public class LinkBeanBo extends ModelCtrl {

	public LinkBeanBo(Connection connection) {
		super(connection);
	}

	/*
	 * 根据链接id获得链接
	 */
	public LinkBean getLink(int linkId) {
		LinkBean ab = null;
		String sql = "SELECT * FROM links WHERE link_id=" + linkId;
		ArrayList<LinkBean> ams = this.getLinks(sql);
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
		return super.getRowCount("links");
	}

	/*
	 * 根据sql语句获得所有链接记录
	 */
	public ArrayList<LinkBean> getLinks(String sql) {
		ResultSet rs = null;
		Statement st = null;
		ArrayList<LinkBean> ams = new ArrayList<LinkBean>();
		try {
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				LinkBean ab = new LinkBean();
				ab.setLinkId(rs.getInt("link_id"));
				ab.setName(rs.getString("name"));
				ab.setUrl(rs.getString("url"));
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
	 * 根据页数获取第pageNow页的所有链接记录 pageNow为0时获取所有链接
	 */
	public ArrayList<LinkBean> getLinks(int pageNow) {
		ArrayList<LinkBean> ams = new ArrayList<LinkBean>();
		String sql = "SELECT * FROM links " + this.filter;
		if (pageNow > 0) {
			sql += " LIMIT " + (pageNow - 1) * pageSize + "," + pageSize;
		}
		ams = this.getLinks(sql);
		return ams;
	}

	/*
	 * 搜索并返回所有链接记录
	 */
	public ArrayList<LinkBean> searchLinks(String keywords, int pageNow) {
		ArrayList<LinkBean> ams = new ArrayList<LinkBean>();
		keywords = keywords.trim();
		keywords = keywords.replaceAll(" +", "%");
		String sql = "SELECT * FROM links WHERE name LIKE '%" + keywords + "%'";
		if (pageNow > 0) {
			sql += " LIMIT " + (pageNow - 1) * pageSize + "," + pageSize;
		}
		ams = this.getLinks(sql);
		return ams;
	}

	/*
	 * 添加一条链接记录
	 */
	public boolean addLink(LinkBean linkModel) {
		boolean result = false;
		String sql = "INSERT INTO links(name, url, status) VALUES('"
				+ linkModel.getName() + "','" + linkModel.getUrl() + "',"
				+ linkModel.getStatus() + ")";
		int linkId = this.insert(sql);
		if (linkId > 0) {
			linkModel.setLinkId(linkId);
			result = true;
		}
		return result;
	}

	/*
	 * 修改一条链接记录
	 */
	public boolean editLink(LinkBean linkModel) {
		boolean result = false;
		String sql = "UPDATE links SET name='" + linkModel.getName()
				+ "',url='" + linkModel.getUrl() + "',status="
				+ linkModel.getStatus() + " WHERE link_id="
				+ linkModel.getLinkId();
		result = this.update(sql);
		return result;
	}

	/*
	 * 根据链接id删除相应链接
	 */
	public boolean deleteLink(int linkId) {
		boolean result = false;
		String sql = "DELETE FROM links WHERE link_id=" + linkId;
		result = this.update(sql);
		return result;
	}

	/*
	 * 根据链接id删除相应链接
	 */
	public boolean deleteLink(String linkIds) {
		boolean result = false;
		String sql = "DELETE FROM links WHERE link_id IN(" + linkIds + ")";
		result = this.update(sql);
		return result;
	}

	// 修改链接状态函数
	public boolean changeStatus(int linkId, int status) {
		boolean result = false;
		if (status == 0 || status == 1 || status == 2 || status == 3) {
			String sql = "UPDATE links SET status=" + status
					+ " WHERE link_id=" + linkId;
			result = this.update(sql);
		}
		return result;
	}

	// 修改链接状态函数
	public boolean changeStatus(String linkIds, int status) {
		boolean result = false;
		if (status == 0 || status == 1 || status == 2 || status == 3) {
			String sql = "UPDATE links SET status=" + status
					+ " WHERE link_id IN(" + linkIds + ")";
			result = this.update(sql);
		}
		return result;
	}
}
