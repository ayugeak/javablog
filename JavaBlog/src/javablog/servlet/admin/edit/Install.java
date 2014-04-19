package javablog.servlet.admin.edit;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javablog.database.ConfigCtrl;
import javablog.database.DBConnectionPool;
import javablog.database.DBCtrl;
import javablog.database.InitDB;
import javablog.util.ConfigProperty;
import javablog.util.SocketMail;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javablog.bean.*;

public class Install extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public Install() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String result = "f";

			ConfigCtrl configCtrl = new ConfigCtrl("config/database.properties");
			String installed = configCtrl.getConfigValue("installed");
			//make sure the website is not installed yet
			if (installed == null || installed.equalsIgnoreCase("false")){
				//create database and tables
				//we don't use DBConnectionPool, because we haven't create the database yet
				//DBConnectionPool require a database
				DBCtrl dbCtrl = new DBCtrl();
				Connection connection = dbCtrl.getConnection();
				if (connection != null){
					InitDB initDB = new InitDB(connection, configCtrl.getConfigValue("database"));
					initDB.createDatabase();
					initDB.createTabel();
					//close the connection
					try {
						connection.commit();
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					connection = null;
					//now we can use DBConnectionPool
					connection = DBConnectionPool.getDBConnectionPool().getConnection();
					if (connection != null){
						String username = request.getParameter("username");
						String email = request.getParameter("email");
						//create roles
						RoleBeanBo rbb = new RoleBeanBo(connection);
						RoleBean rootRole = this.generateRootRole();//id=1
						RoleBean editorRole = this.generateEditorRole();//id=2
						RoleBean guestRole = this.generateGuestRole();//id=3
						if (rbb.addRole(rootRole)
								&& rbb.addRole(editorRole)
								&& rbb.addRole(guestRole)) {//the order they add decides their id!
							//create initial roles
							UserBeanBo ubb = new UserBeanBo(connection);
							String password = ubb.randomPassword();
							UserBean ub = new UserBean();
							ub.setRoleId(rootRole.getRoleId());
							ub.setUsername(username);
							ub.setPassword(ubb.getMD5Str(password));
							ub.setName(username);
							ub.setEmail(email);
							ub.setAvatar("images/avatar.png");
							ub.setUrl("");
							ub.setStatus(ConfigProperty.STATUS_NORMAL);
							if (ubb.addUser(ub)) {
								SocketMail socketMail = new SocketMail();
								socketMail.setServer("smtp.gmail.com", 465, "SSL");
								socketMail.setFrom( ConfigProperty.website_name, ConfigProperty.admin_email,
										ConfigProperty.admin_password);
								socketMail.setTo(ub.getName(), ub.getEmail());
								String subject = ConfigProperty.website_name + ":Password";
								String content = "Hello, " + ub.getName()
										+ ", your password is: " + password;
								socketMail.setSubject(subject);
								socketMail.setContent(content);
								socketMail.connectServer();
								socketMail.login();
								if (socketMail.sendMail()) {
									//website is now installed
									configCtrl.setConfigValue("installed", "true");
									if (configCtrl.saveConfig()){
										result = "s";//success!
									}
								}
								socketMail.close();
							}
						}
						rbb.closeConnection();
					}
				}
			} else {
				result = "n";
			}
			out.print(result);
		} finally {
			out.flush();
			out.close();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

	public void init() throws ServletException {

	}
	
	private RoleBean generateRootRole(){
		char c = 0x0f;//00001111
		String permission = "";
		for (int i = 0; i < 11; i++){
			permission += c;
		}
		RoleBean rb = new RoleBean();
		rb.setName("Root");
		rb.setPermission(permission);
		rb.setStatus(ConfigProperty.STATUS_NORMAL);
		return rb;
	}

	private RoleBean generateEditorRole(){
		String permission = "";
		permission += '\0';//role
		permission += '\0';//user
		permission += (char)0x0f;//category
		permission += (char)0x0f;//tag
		permission += (char)0x0f;//article
		permission += (char)0x0f;//comment
		permission += '\0';//link
		permission += '\0';//message
		permission += (char)0x0f;//resource
		permission += '\0';//log
		permission += '\0';//system

		RoleBean rb = new RoleBean();
		rb.setName("Editor");
		rb.setPermission(permission);
		rb.setStatus(ConfigProperty.STATUS_NORMAL);
		return rb;
	}
	
	private RoleBean generateGuestRole(){
		String permission = "";
		permission += '\0';//role
		permission += '\0';//user
		permission += '\0';//category
		permission += '\0';//tag
		permission += (char)0x01;//article:read
		permission += (char)0x01;//comment:read
		permission += '\0';//link
		permission += '\0';//message
		permission += '\0';//resource
		permission += '\0';//log
		permission += '\0';//system
		
		RoleBean rb = new RoleBean();
		rb.setName("Guest");
		rb.setPermission(permission);
		rb.setStatus(ConfigProperty.STATUS_NORMAL);
		return rb;
	}
}
