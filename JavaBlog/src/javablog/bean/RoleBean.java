package javablog.bean;

import javablog.util.ConfigProperty;

//角色类
public class RoleBean {
	private int roleId;// 角色id
	private String name;// 名称
	private String permission;// 权限
	private int status;// 状态

	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPermission() {
		return this.permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	//id为pid的权限字符的index位是否为1
	private boolean getCharBin(int pid, int index){
		char pc = this.permission.charAt(pid);
		return (pc & (0x1 << index)) != 0;
	}
	
	// 角色管理****************************************
	public boolean canReadRole() {
		return this.getCharBin(ConfigProperty.ID_ROLE, 0);
	}

	//
	public boolean canAddRole() {
		return this.getCharBin(ConfigProperty.ID_ROLE, 1);
	}

	//
	public boolean canEditRole() {
		return this.getCharBin(ConfigProperty.ID_ROLE, 2);
	}//

	public boolean canDeleteRole() {
		return this.getCharBin(ConfigProperty.ID_ROLE, 3);
	}

	// 用户管理****************************************
	public boolean canReadUser() {
		return this.getCharBin(ConfigProperty.ID_USER, 0);
	}

	//
	public boolean canAddUser() {
		return this.getCharBin(ConfigProperty.ID_USER, 1);
	}

	//
	public boolean canEditUser() {
		return this.getCharBin(ConfigProperty.ID_USER, 2);
	}//

	public boolean canDeleteUser() {
		return this.getCharBin(ConfigProperty.ID_USER, 3);
	}

	// 分类管理****************************************
	public boolean canReadCategory() {
		return this.getCharBin(ConfigProperty.ID_CATEGORY, 0);
	}

	//
	public boolean canAddCategory() {
		return this.getCharBin(ConfigProperty.ID_CATEGORY, 1);
	}

	//
	public boolean canEditCategory() {
		return this.getCharBin(ConfigProperty.ID_CATEGORY, 2);
	}//

	public boolean canDeleteCategory() {
		return this.getCharBin(ConfigProperty.ID_CATEGORY, 3);
	}

	// 标签管理****************************************
	public boolean canReadTag() {
		return this.getCharBin(ConfigProperty.ID_TAG, 0);
	}

	//
	public boolean canAddTag() {
		return this.getCharBin(ConfigProperty.ID_TAG, 1);
	}

	//
	public boolean canEditTag() {
		return this.getCharBin(ConfigProperty.ID_TAG, 2);
	}

	//
	public boolean canDeleteTag() {
		return this.getCharBin(ConfigProperty.ID_TAG, 3);
	}

	// 文章管理****************************************
	// 读取文章
	public boolean canReadArticle() {
		return this.getCharBin(ConfigProperty.ID_ARTICLE, 0);
	}

	// 添加文章和编辑本人文章内容
	public boolean canAddArticle() {
		return this.getCharBin(ConfigProperty.ID_ARTICLE, 1);
	}

	// 编辑他人文章内容（包括修改状态）
	public boolean canEditArticle() {
		return this.getCharBin(ConfigProperty.ID_ARTICLE, 2);
	}

	// 删除文章
	public boolean canDeleteArticle() {
		return this.getCharBin(ConfigProperty.ID_ARTICLE, 3);
	}

	// 评论管理****************************************
	public boolean canReadComment() {
		return this.getCharBin(ConfigProperty.ID_COMMENT, 0);
	}

	// 添加评论(以管理员模式)
	public boolean canAddComment() {
		return this.getCharBin(ConfigProperty.ID_COMMENT, 1);
	}

	// 编辑评论（审核和修改状态）
	public boolean canEditComment() {
		return this.getCharBin(ConfigProperty.ID_COMMENT, 2);
	}

	// 删除评论
	public boolean canDeleteComment() {
		return this.getCharBin(ConfigProperty.ID_COMMENT, 3);
	}

	// 链接管理****************************************
	public boolean canReadLink() {
		return this.getCharBin(ConfigProperty.ID_LINK, 0);
	}

	//
	public boolean canAddLink() {
		return this.getCharBin(ConfigProperty.ID_LINK, 1);
	}

	//
	public boolean canEditLink() {
		return this.getCharBin(ConfigProperty.ID_LINK, 2);
	}

	//
	public boolean canDeleteLink() {
		return this.getCharBin(ConfigProperty.ID_LINK, 3);
	}

	// 信息管理****************************************
	// 读取信息
	public boolean canReadMessage() {
		return this.getCharBin(ConfigProperty.ID_MESSAGE, 0);
	}

	//
	public boolean canAddMessage() {
		return this.getCharBin(ConfigProperty.ID_MESSAGE, 1);
	}

	//
	public boolean canEditMessage() {
		return this.getCharBin(ConfigProperty.ID_MESSAGE, 2);
	}

	//
	public boolean canDeleteMessage() {
		return this.getCharBin(ConfigProperty.ID_MESSAGE, 3);
	}

	// 资料管理****************************************
	//
	public boolean canReadResource() {
		return this.getCharBin(ConfigProperty.ID_RESOURCE, 0);
	}

	// 上传资料（包括在写文章时上传图片）
	public boolean canAddResource() {
		return this.getCharBin(ConfigProperty.ID_RESOURCE, 1);
	}

	//
	public boolean canEditResource() {
		return this.getCharBin(ConfigProperty.ID_RESOURCE, 2);
	}

	//
	public boolean canDeleteResource() {
		return this.getCharBin(ConfigProperty.ID_RESOURCE, 3);
	}

	// 日志管理****************************************
	public boolean canReadLog() {
		return this.getCharBin(ConfigProperty.ID_LOG, 0);
	}

	//
	public boolean canAddLog() {
		return this.getCharBin(ConfigProperty.ID_LOG, 1);
	}

	//
	public boolean canEditLog() {
		return this.getCharBin(ConfigProperty.ID_LOG, 2);
	}

	//
	public boolean canDeleteLog() {
		return this.getCharBin(ConfigProperty.ID_LOG, 3);
	}

	// 系统管理****************************************
	// 读取系统配置，如网站名，数据库分页大小等
	public boolean canReadConfig() {
		return this.getCharBin(ConfigProperty.ID_SYS, 0);
	}

	// 修改配置
	public boolean canEditConfig() {
		return this.getCharBin(ConfigProperty.ID_SYS, 1);
	}

}
