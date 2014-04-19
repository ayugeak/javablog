package javablog.bean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import javablog.util.ConfigProperty;

public class ArticleBeanBo extends ModelCtrl {

	public ArticleBeanBo(Connection connection) {
		super(connection);
	}

	/*
	 * 根据文章id获得文章
	 */
	public ArticleBean getArticle(int articleId) {
		ArticleBean ab = null;
		String sql = "SELECT * FROM articles WHERE article_id=" + articleId;
		ArrayList<ArticleBean> ams = this.getArticles(sql);
		if (ams.size() > 0) {
			ab = ams.get(0);
		}
		return ab;
	}

	/*
	 * 设置sql语句中的过滤器
	 */
	// 设置排序方式函数
	public void setFilter(int status, int categoryId, int authorId,
			String attribute, String order) {
		filter = " WHERE status=" + status;
		if (categoryId != 0) {
			filter += " AND category_id=" + categoryId;
		}
		if (authorId != 0) {
			filter += " AND author_id=" + authorId;
		}
		if (attribute != null && attribute.length() > 0) {
			filter += " ORDER BY " + attribute;
			if (order != null && order.equalsIgnoreCase("desc")) {
				filter += " " + order;
			}
		}
	}
	
	//更灵活的设置过滤器函数，其中categoryIds可为多个categoryId（用逗号隔开）或SQL语句
	public void setFilter(String status, String articleIds, String categoryIds, String authorIds,
			String attribute, String order) {
		filter = " WHERE status IN(" + status + ")";
		if (articleIds != null && !articleIds.equals("0")) {
			filter += " AND article_id IN(" + articleIds + ")";
		}
		if (categoryIds != null && !categoryIds.equals("0")) {
			filter += " AND category_id IN(" + categoryIds + ")";
		}
		if (authorIds != null && !authorIds.equals("0")) {
			filter += " AND author_id IN(" + authorIds + ")";
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
		return super.getRowCount("articles");
	}

	/*
	 * 获取最大文章id
	 */
	public int getMaxArticleId() {
		String sql = "SELECT MAX(article_id) FROM articles";
		int articleId = super.getBeanId(sql);
		return articleId;
	}

	/*
	 * 获取最小文章id
	 */
	public int getMinArticleId() {
		String sql = "SELECT MIN(article_id) FROM articles";
		int articleId = super.getBeanId(sql);
		return articleId;
	}

	// 根据文章id获取文章标题
	public String getArticleTitle(int articleId) {
		String sql = "SELECT title FROM articles WHERE article_id=" + articleId
				+ " AND status=" + ConfigProperty.STATUS_NORMAL;
		String articleTitle = super.getBeanMeta(sql);
		return articleTitle;
	}

	/*
	 * 根据sql语句获得所有文章记录
	 */
	public ArrayList<ArticleBean> getArticles(String sql) {
		ResultSet rs = null;
		Statement st = null;
		ArrayList<ArticleBean> ams = new ArrayList<ArticleBean>();
		try {
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				ArticleBean ab = new ArticleBean();
				ab.setArticleId(rs.getInt("article_id"));
				ab.setCategoryId(rs.getInt("category_id"));
				ab.setAuthorId(rs.getInt("author_id"));
				ab.setTitle(rs.getString("title"));
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

	// 获得上一篇文章元数据
	public MetaModel getPreviousArticleMeta(int articleId) {
		MetaModel mm = null;
		String articleTitle = null;
		int buttom = 1;// 文章id下界
		buttom = this.getMinArticleId();
		while (articleId >= buttom) {
			articleTitle = this.getArticleTitle(articleId);
			if (articleTitle != null) {// 找到文章，返回
				mm = new MetaModel();
				mm.setKey(articleId);
				mm.setValue(articleTitle);
				break;
			}
			articleId--;
		}
		return mm;
	}

	// 获得下一篇文章元数据
	public MetaModel getNextArticleMeta(int articleId) {
		MetaModel mm = null;
		String articleTitle = null;
		int top = 1;// 文章id上界
		top = this.getMaxArticleId();
		while (articleId <= top) {
			articleTitle = this.getArticleTitle(articleId);
			if (articleTitle != null) {// 找到文章，返回
				mm = new MetaModel();
				mm.setKey(articleId);
				mm.setValue(articleTitle);
				break;
			}
			articleId++;
		}
		return mm;
	}

	/*
	 * 根据页数获取第pageNow页的所有文章记录 pageNow为0时获取所有文章
	 */
	public ArrayList<ArticleBean> getArticles(int pageNow) {
		ArrayList<ArticleBean> ams = new ArrayList<ArticleBean>();
		String sql = "SELECT * FROM articles " + this.filter;
		if (pageNow > 0) {
			sql += " LIMIT " + (pageNow - 1) * pageSize + "," + pageSize;
		}
		ams = this.getArticles(sql);
		return ams;
	}

	/*
	 * 搜索并返回所有文章记录
	 */
	public ArrayList<ArticleBean> searchArticles(String keywords, int pageNow) {
		ArrayList<ArticleBean> ams = new ArrayList<ArticleBean>();
		keywords = keywords.trim();
		keywords = keywords.replaceAll(" +", "%");
		int index = this.filter.lastIndexOf("ORDER BY");
		String pfilter, sfilter;
		if (index != -1) {
			pfilter = this.filter.substring(0, index);
			sfilter = this.filter.substring(index);
		} else {
			pfilter = this.filter;
			sfilter = "";
		}
		// 把like查询条件加入到filter中
		this.filter = pfilter + " AND (title LIKE '%" + keywords
				+ "%' OR content LIKE '%" + keywords + "%') " + sfilter;

		String sql = "SELECT * FROM articles " + this.filter;
		if (pageNow > 0) {
			sql += " LIMIT " + (pageNow - 1) * pageSize + "," + pageSize;
		}
		ams = this.getArticles(sql);
		return ams;
	}

	/*
	 * 添加一条文章记录
	 */
	public boolean addArticle(ArticleBean articleModel) {
		boolean result = false;
		articleModel.setDate(new Timestamp((new Date()).getTime()));
		String sql = "INSERT INTO articles(category_id, author_id, title, content, date, status) VALUES("
				+ articleModel.getCategoryId()
				+ ","
				+ articleModel.getAuthorId()
				+ ",'"
				+ articleModel.getTitle()
				+ "','"
				+ articleModel.getContent()
				+ "','"
				+ articleModel.getDate()
				+ "',"
				+ articleModel.getStatus()
				+ ")";
		int articleId = this.insert(sql);
		if (articleId > 0) {
			articleModel.setArticleId(articleId);
			result = true;
		}
		return result;
	}

	/*
	 * 修改一条文章记录
	 */
	public boolean editArticle(ArticleBean articleModel) {
		boolean result = false;
		articleModel.setDate(new Timestamp((new Date()).getTime()));
		String sql = "UPDATE articles SET category_id="
				+ articleModel.getCategoryId() + ",author_id="
				+ articleModel.getAuthorId() + ",title='"
				+ articleModel.getTitle() + "',content='"
				+ articleModel.getContent() + "',date='"
				+ articleModel.getDate() + "',status="
				+ articleModel.getStatus() + " WHERE article_id="
				+ articleModel.getArticleId();
		result = this.update(sql);
		return result;
	}

	/*
	 * 根据文章id删除相应文章
	 */
	public boolean deleteArticle(int articleId) {
		boolean result = false;
		String sql = "DELETE FROM articles WHERE article_id=" + articleId;
		result = this.update(sql);
		return result;
	}

	/*
	 * 根据文章id组删除相应文章，各个id用,分隔
	 */
	public boolean deleteArticle(String articleIds) {
		boolean result = false;
		String sql = "DELETE FROM articles WHERE article_id IN(" + articleIds
				+ ")";
		result = this.update(sql);
		return result;
	}

	// 修改文章状态函数
	public boolean changeStatus(int articleId, int status) {
		boolean result = false;
		if (status == 0 || status == 1 || status == 2 || status == 3) {
			String sql = "UPDATE articles SET status=" + status
					+ " WHERE article_id=" + articleId;
			result = this.update(sql);
		}
		return result;
	}

	// 修改文章状态函数
	public boolean changeStatus(String articleIds, int status) {
		boolean result = false;
		if (status == 0 || status == 1 || status == 2 || status == 3) {
			String sql = "UPDATE articles SET status=" + status
					+ " WHERE article_id IN(" + articleIds + ")";
			result = this.update(sql);
		}
		return result;
	}
}
