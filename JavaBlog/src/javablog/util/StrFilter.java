package javablog.util;

//字符串过滤及转换类
public class StrFilter {
	// 判断字符串是否为空
	public boolean isEmpty(String str) {
		return (str == null || str.trim().length() == 0);
	}
	
	//替换数据库中的特殊字符，主要用于过滤富文本内容
	public String contentFilter(String html) {
		if (html == null) {
			html = "";
		}
		html = html.replace("\\", "\\\\");// 替换掉\
		html = html.replace("'", "''");// 处理单引号
		return html;
	}

	// 转换正整数函数
	public int parseNum(String str) {
		if (this.isEmpty(str)) {
			str = "0";
		}
		str = str.trim();// 去掉空格
		if (str.length() > 11) {
			str = str.substring(0, 10);// 整数长度为11
		}
		// 判断字条串是否为整数
		StringBuilder temp = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c >= 48 && c <= 57) {//c is a char
				temp.append(c);
			}
		}
		if (temp.length() == 0) {//set str to 0
			temp.append('0');
		}
		return Integer.parseInt(temp.toString());
	}
	
	//Parse string parameter to integer
	public int parseInt(String number){
		int result = 0;
		if (!this.isEmpty(number)) {
			try{
				result = Integer.parseInt(number);
			} catch (Exception e) {
				result = -1;
			}
		}
		
		return result;
	}

	//过滤字符串中的HTML符号，替换成相应转义字符，主要用于过滤纯文本内容
	public String htmlFilter(String html) {
		if (html == null) {
			html = "";
		}
		html = html.replace("&", "&amp;");// 替换掉&
		html = html.replace("<", "&lt;");// 替换掉<
		html = html.replace(">", "&gt;");// 替换掉>
		html = html.replace("\n", "<br/>");// 替换掉换行符
		html = this.contentFilter(html);//处理数据库特殊字符
		return html;
	}
	
	public boolean isValidOrder(String order){
		boolean result = false;
		if (order == null
				|| order.length() == 0
				|| order.equalsIgnoreCase("asc")
				|| order.equalsIgnoreCase("desc")){
			result = true;
		}
		return result;
	}
}
