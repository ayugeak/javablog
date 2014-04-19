package javablog.bean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * 文章与标签的关系类
 */
public class ArticleTagBeanBo extends ModelCtrl {

	public ArticleTagBeanBo(Connection connection) {
		super(connection);
	}

	/*
	 * 获取结果集记录条数
	 */
	public int getRowCount() {
		return super.getRowCount("article_tags");
	}
	/*
	 * 设置sql语句中的过滤器
	 */
	/*public void setFilter(int status, String attribute, String order) {
		filter = " status=" + status;
		if (attribute != null && attribute.length() > 0) {
			filter += " ORDER BY " + attribute;
			if (order != null && order.equalsIgnoreCase("desc")) {
				filter += " " + order;
			}
		}
	}*/

	/*
	 * 添加一条文章标签记录
	 */
	public boolean addArticleTag(ArticleTagBean articleTagModel) {
		boolean result = false;
		String sql = "INSERT INTO article_tags VALUES("
				+ articleTagModel.getArticleId() + ","
				+ articleTagModel.getTagId() + ")";
		result = this.update(sql);
		return result;
	}

	// 获得某标签对应的所有文章
	/*public ArrayList<ArticleBean> getArticles(int tagId, int pageNow) {
		ArrayList<ArticleBean> al;
		ArticleBeanBo abb = new ArticleBeanBo(this.connection);
		String sql = "SELECT * FROM articles WHERE article_id IN"
				+ "(SELECT article_id FROM article_tags WHERE tag_id=" + tagId
				+ ")";
		if (this.filter != null && this.filter.length() > 0) {
			sql += " AND " + this.filter;
		}
		if (pageNow > 0) {
			sql += " limit " + (pageNow - 1) * pageSize + "," + pageSize;
		}
		al = abb.getArticles(sql);
		return al;
	}*/

	// 根据文章id获取与其相关文章
	public ArrayList<MetaModel> getRelatedArticles(int articleId) {
		ArrayList<MetaModel> al = new ArrayList<MetaModel>();
		// MYSQL子查询中不支持limit
		// String sql =
		// "SELECT article_id, title FROM articles WHERE article_id" +
		// " IN(SELECT article_id FROM article_tags WHERE tag_id IN "
		// + "(SELECT tag_id FROM article_tags WHERE article_id=" + articleId
		// +") GROUP BY article_id ORDER BY COUNT(*) DESC) LIMIT 0, 5";
		String sql = "SELECT article_tags.article_id, articles.title"
				+ " FROM article_tags LEFT JOIN articles"
				+ " USING(article_id) WHERE article_tags.article_id<>"
				+ articleId + " AND article_tags.tag_id"
				+ " IN (SELECT tag_id FROM article_tags WHERE article_id="
				+ articleId + ") GROUP BY article_tags.article_id"
				+ " ORDER BY COUNT(*) DESC LIMIT 0, 5";
		ResultSet rs = null;
		Statement st = null;
		try {
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				MetaModel mm = new MetaModel();
				mm.setKey(rs.getInt("article_id"));
				mm.setValue(rs.getString("title"));
				al.add(mm);
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
		return al;
	}

	// 获得某文章对应的所有标签
	/*public ArrayList<TagBean> getTags(int articleId, int pageNow) {
		ArrayList<TagBean> al;
		TagBeanBo abb = new TagBeanBo(this.connection);
		String sql = "SELECT * FROM tags WHERE tag_id IN(SELECT tag_id FROM article_tags WHERE article_id="
				+ articleId + ")";
		if (this.filter != null && this.filter.length() > 0) {
			sql += " AND " + this.filter;
		}
		if (pageNow > 0) {
			sql += " LIMIT " + (pageNow - 1) * pageSize + "," + pageSize;
		}
		al = abb.getTags(sql);
		return al;
	}*/

	// 删除某篇文章的某个标签
	public boolean deleteTag(int tagId, int articleId) {
		boolean result = false;
		String sql = "DELETE FROM article_tags WHERE tag_id=" + tagId
				+ " AND article_id=" + articleId;
		result = this.update(sql);
		return result;
	}

	// 删除某篇文章的所有标签
	public boolean deleteTag(int articleId) {
		boolean result = false;
		String sql = "DELETE FROM article_tags WHERE article_id=" + articleId;
		result = this.update(sql);
		return result;
	}

	// 更新某篇文章下所有标签的状态
	public boolean changeStatus(int articleId, int status) {
		boolean result = false;
		if (status == 0 || status == 1 || status == 2 || status == 3) {
			TagBeanBo tbb = new TagBeanBo(this.connection);
			String sql = "UPDATE tags SET status="
					+ status
					+ " WHERE tag_id IN(SELECT tag_id FROM article_tags WHERE article_id="
					+ articleId + ")";
			result = tbb.updateTag(sql);
		}
		return result;
	}

	// 更新某篇文章下所有标签的状态
	public boolean changeStatus(String articleIds, int status) {
		boolean result = false;
		if (status == 0 || status == 1 || status == 2) {
			TagBeanBo tbb = new TagBeanBo(this.connection);
			String sql = "UPDATE tags SET status="
					+ status
					+ " WHERE tag_id IN(SELECT tag_id FROM article_tags WHERE article_id IN("
					+ articleIds + "))";
			result = tbb.updateTag(sql);
		}
		return result;
	}

}
