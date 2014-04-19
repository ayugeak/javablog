package javablog.database;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class ConfigCtrl {
	private String filePath;
	private String absoluteFilePath;
	private Properties properties;

	public ConfigCtrl(String filePath) {
		this.filePath = filePath;
		this.properties = new Properties();
		this.initConfig(this.filePath);
	}

	public void initConfig(String filePath) {
		this.filePath = filePath;
		try {
			String url = this.getClass().getResource("").getPath()
					.replaceAll("%20", " ");
			this.absoluteFilePath = url.substring(0, url.indexOf("WEB-INF"))
					+ "WEB-INF/" + filePath;
			InputStream in = new FileInputStream(this.absoluteFilePath);
			this.properties.load(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getConfigValue(String key) {
		String value = properties.getProperty(key);
		return value;
	}

	public void setConfigValue(String key, String value) {
		this.properties.setProperty(key, value);
	}
	
	public boolean saveConfig(){
		boolean result = false;
		try {
			OutputStream out = new FileOutputStream(this.absoluteFilePath);
			this.properties.store(out, null);
			out.close();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
