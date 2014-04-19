package javablog.bean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class CommentBeanBo extends ModelCtrl {

	public CommentBeanBo(Connection connection) {
		super(connection);
	}

	/*
	 * 根据评论id获得评论
	 */
	public CommentBean getComment(int commentId) {
		CommentBean ab = null;
		String sql = "SELECT * FROM comments WHERE comment_id=" + commentId;
		ArrayList<CommentBean> ams = this.getComments(sql);
		if (ams.size() > 0) {
			ab = ams.get(0);
		}
		return ab;
	}

	/*
	 * 设置sql语句中的过滤器
	 */
	// 设置排序方式函数
	public void setFilter(int status, int articleId, int userId,
			String attribute, String order) {
		filter = " WHERE status=" + status;
		if (articleId != 0) {
			filter += " AND article_id=" + articleId;
		}
		if (userId != 0) {
			filter += " AND user_id=" + userId;
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
		return super.getRowCount("comments");
	}

	/*
	 * 根据sql语句获得所有评论记录
	 */
	public ArrayList<CommentBean> getComments(String sql) {
		ResultSet rs = null;
		Statement st = null;
		ArrayList<CommentBean> ams = new ArrayList<CommentBean>();
		try {
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				CommentBean ab = new CommentBean();
				ab.setCommentId(rs.getInt("comment_id"));
				ab.setArticleId(rs.getInt("article_id"));
				ab.setUserId(rs.getInt("user_id"));
				ab.setName(rs.getString("name"));
				ab.setEmail(rs.getString("email"));
				ab.setUrl(rs.getString("url"));
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
	 * 根据页数获取第pageNow页的所有评论记录 pageNow为0时获取所有评论
	 */
	public ArrayList<CommentBean> getComments(int pageNow) {
		ArrayList<CommentBean> ams = new ArrayList<CommentBean>();
		String sql = "SELECT * FROM comments " + this.filter;
		if (pageNow > 0) {
			sql += " LIMIT " + (pageNow - 1) * pageSize + "," + pageSize;
		}
		ams = this.getComments(sql);
		return ams;
	}

	/*
	 * 搜索并返回所有评论记录
	 */
	public ArrayList<CommentBean> searchComments(String keywords, int pageNow) {
		ArrayList<CommentBean> ams = new ArrayList<CommentBean>();
		keywords = keywords.trim();
		keywords = keywords.replaceAll(" +", "%");
		String sql = "SELECT * FROM comments WHERE content LIKE '%" + keywords
				+ "%'";
		if (pageNow > 0) {
			sql += " LIMIT " + (pageNow - 1) * pageSize + "," + pageSize;
		}
		ams = this.getComments(sql);
		return ams;
	}

	/*
	 * 添加一条评论记录
	 */
	public boolean addComment(CommentBean commentModel) {
		boolean result = false;
		commentModel.setDate(new Timestamp((new Date()).getTime()));
		String userId;
		if (commentModel.getUserId() == 0) {
			userId = "NULL";
		} else {
			userId = commentModel.getUserId() + "";
		}
		String sql = "INSERT INTO comments(article_id, user_id, name, email, url, content, date, status) VALUES("
				+ commentModel.getArticleId()
				+ ","
				+ userId
				+ ",'"
				+ commentModel.getName()
				+ "','"
				+ commentModel.getEmail()
				+ "','"
				+ commentModel.getUrl()
				+ "','"
				+ commentModel.getContent()
				+ "','"
				+ commentModel.getDate()
				+ "'," + commentModel.getStatus() + ")";
		int commentId = this.insert(sql);
		if (commentId > 0) {
			commentModel.setCommentId(commentId);
			result = true;
		}
		return result;
	}

	/*
	 * 根据评论id删除相应评论
	 */
	public boolean deleteComment(int commentId) {
		boolean result = false;
		String sql = "DELETE FROM comments WHERE comment_id=" + commentId;
		result = this.update(sql);
		return result;
	}

	/*
	 * 根据评论id删除相应评论
	 */
	public boolean deleteComment(String commentIds) {
		boolean result = false;
		String sql = "DELETE FROM comments WHERE comment_id IN(" + commentIds
				+ ")";
		result = this.update(sql);
		return result;
	}

	// 修改评论状态函数
	public boolean changeStatus(int commentId, int status) {
		boolean result = false;
		if (status == 0 || status == 1 || status == 2 || status == 3) {
			String sql = "UPDATE comments SET status=" + status
					+ " WHERE comment_id=" + commentId;
			result = this.update(sql);
		}
		return result;
	}

	// 修改评论状态函数
	public boolean changeStatus(String commentIds, int status) {
		boolean result = false;
		if (status == 0 || status == 1 || status == 2 || status == 3) {
			String sql = "UPDATE comments SET status=" + status
					+ " WHERE comment_id IN(" + commentIds + ")";
			result = this.update(sql);
		}
		return result;
	}

}
