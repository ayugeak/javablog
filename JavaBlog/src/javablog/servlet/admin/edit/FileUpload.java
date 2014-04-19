package javablog.servlet.admin.edit;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
  
import javablog.bean.LogBean;
import javablog.bean.LogBeanBo;
import javablog.bean.ResourceBean;
import javablog.bean.ResourceBeanBo;
import javablog.bean.RoleBean;
import javablog.bean.RoleBeanBo;
import javablog.bean.UserBean;
import javablog.bean.UserBeanBo;
import javablog.database.DBConnectionPool;
import javablog.util.ConfigProperty;
import javablog.util.StrFilter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
  
/** 
 * Servlet implementation class FileUploadServlet 
 */  
@MultipartConfig(  
        location = "/tmp",//文件存放路径，指定的目录必须存在，否则会抛异常  
        maxFileSize = 10485760,//最大上传文件大小,10M  
        fileSizeThreshold = 5242880,//当数据量大于5M时，内容将被写入文件。（specification中的解释的大概意思，不知道是不是指Buffer size），大小也是已字节单位  
        maxRequestSize =  10485760 //最多10M，针对该 multipart/form-data 请求的最大数量，默认值为 -1，表示没有限制。以字节为单位。  
)  
public class FileUpload extends HttpServlet {  
    private static final long serialVersionUID = 1L;  
    private String fileNameExtractorRegex = "filename=\".+\"";
    private String realPath;
	
         
    /** 
     * @see HttpServlet#HttpServlet() 
     */  
    public FileUpload() {  //realPath = this.getServletContext().getRealPath("/").replace("\\", "/");// 网站绝对路径
        super();  
    }  
  
    /** 
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response) 
     */  
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
        doPost(request, response); 
    }  
  
    /** 
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response) 
     */  
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException {  
    	response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			HttpSession session = request.getSession(true);
			String userIdText = (String) session.getAttribute("userId");
			String result = "f";
			if (userIdText != null) {
				Connection connection = DBConnectionPool.getDBConnectionPool().getConnection();
				if (connection != null){
					UserBeanBo ubb = new UserBeanBo(connection);
					UserBean ub = ubb.getUser(Integer.parseInt(userIdText));
					if (ub != null && ub.getStatus() == ConfigProperty.STATUS_NORMAL){
						RoleBeanBo ugbb = new RoleBeanBo(connection);
						RoleBean role = ugbb.getRole(ub.getRoleId());
						if (role != null && role.getStatus() == ConfigProperty.STATUS_NORMAL){
							if (role.canAddResource()) {
								ResourceBeanBo rbb = new ResourceBeanBo(connection);
								LogBeanBo lbb = new LogBeanBo(connection);
								String type = request.getParameter("type");//上传参数
								StrFilter sf = new StrFilter();
								//待上传文件集合
						        Collection<Part> parts = request.getParts();
						        for (Iterator<Part> iterator = parts.iterator(); iterator.hasNext();){
						            Part part = iterator.next();
						            //从Part的content-disposition中提取上传文件的文件名
						            String fileName = getFileName(part);
						            fileName = sf.htmlFilter(fileName);
						            if(fileName != null && fileName.length() > 0){
										ResourceBean rb = new ResourceBean();
										rb.setName(fileName);
										rb.setStatus(ConfigProperty.STATUS_NORMAL);
										if (rbb.addResource(rb)){//记录数据库，获取文件URL
											part.write(realPath + rb.getUrl());//真正写入文件
											// 记录日志
											String content = ub.getName() + "("
													+ ub.getUsername() + ")上传了资源:"
													+ rb.getName();
											LogBean lb = new LogBean();
											lb.setContent(content);
											lb.setStatus(ConfigProperty.STATUS_NORMAL);
											lbb.addLog(lb);
											result = "<script>parent.uploadCallback('"
													+ rb.getName() + "','"
													+ rb.getUrl() + "',"
													+ type + ")</script>";
										} else {
											result = "<script>parent.showInfo('文件:"
													+ fileName + "上传失败')</script>";
										}
						            }//不存在的文件直接跳过
						        }
							} else {
								result = "<script>parent.showInfo('对不起,你没有权限')</script>";
							}
						}
					}
					//关闭数据库连接
					ubb.closeConnection();
				}
			} else {
				result = "<script>parent.showInfo('对不起,你没有权限')</script>";
			}
			out.print(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
    }
    /** 
     * 从Part的Header信息中提取上传文件的文件名 
     * @param part 
     * @return  上传文件的文件名，如果如果没有则返回null 
     */  
    private String getFileName(Part part){
        //获取header信息中的content-disposition，如果为文件，则可以从其中提取出文件名  
        String cotentDesc = part.getHeader("content-disposition");  
        String fileName = null;  
        Pattern pattern = Pattern.compile(fileNameExtractorRegex);  
        Matcher matcher = pattern.matcher(cotentDesc);  
        if(matcher.find()){  
            fileName = matcher.group();  
            fileName = fileName.substring(10, fileName.length()-1);  
        }  
        return fileName;  
    }
    
    public void init() throws ServletException {
		this.realPath = this.getServletContext().getRealPath("/")
				.replace("\\", "/");// 网站绝对路径
	}
}  