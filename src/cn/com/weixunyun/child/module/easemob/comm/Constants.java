package cn.com.weixunyun.child.module.easemob.comm;

/**
 * Constants
 * 
 * @author Lynch 2014-09-15
 *
 */
public interface Constants {

	// API_HTTP_SCHEMA
	public static String API_HTTP_SCHEMA = "https";
	// API_SERVER_HOST
	public static String API_SERVER_HOST = PropertiesUtils.getProperties().getProperty("API_SERVER_HOST");
	// APPKEY
	public static String APPKEY = PropertiesUtils.getProperties().getProperty("APPKEY");
	// APP_CLIENT_ID
	public static String APP_CLIENT_ID = PropertiesUtils.getProperties().getProperty("APP_CLIENT_ID");
	// APP_CLIENT_SECRET
	public static String APP_CLIENT_SECRET = PropertiesUtils.getProperties().getProperty("APP_CLIENT_SECRET");
	// DEFAULT_PASSWORD
	public static String DEFAULT_PASSWORD = PropertiesUtils.getProperties().getProperty("APP_USER_PASS","1357924680");

    public static String SYS_ACCOUNT = PropertiesUtils.getProperties().getProperty("APP_SYS_COUNT","0");
    public static String SYS_PASSWORD = PropertiesUtils.getProperties().getProperty("APP_SYS_PASSWORD","abc123");

}
