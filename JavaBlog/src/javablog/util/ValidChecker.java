package javablog.util;
//常用属性合法性检查类
public class ValidChecker {
	public static boolean isValidOrder(String order){
		boolean result = false;
		if (order == null
				|| order.length() == 0
				|| order.equalsIgnoreCase("asc")
				|| order.equalsIgnoreCase("desc")){
			result = true;
		}
		return result;
	}
	//username consists of letter and number
	public static boolean isValidUsername(String username){
		boolean result = false;
		if (username != null
				&& username.matches("^[a-zA-Z0-9]{4,32}$")){
			result = true;
		}
		return result;
	}
	
	public static boolean isValidPassword(String password){
		boolean result = false;
		if (password != null
				&& password.matches(".{8,64}$")){
			result = true;
		}
		return result;
	}
	
	public static boolean isValidEmail(String email){
		boolean result = false;
		if (email != null
				&& email.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")
				&& email.length() <= 128){
			result = true;
		}
		return result;
	}
}
