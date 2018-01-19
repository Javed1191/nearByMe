package services;

public class Application_Constants {
	public static String Main_URL = "https://hdfcbso.infomanav.com/android_web/action?";
	//public static String Main_SO_URL = "https://soapp.infomanav.com/android_web_so/action?";
	public static String Main_SO_URL = "https://soapp.infomanav.com/android_web_so/action?";


	//public static String Main_SO_URL = "http://192.168.1.19/so/android_web_so/action?";

	// Local link
	//public static String Main_URL = "http://192.168.1.100/so/android_web_so/action?";
	//public static String Main_URL = "http://192.168.1.14/hdfc_bso/android_web/action?";

	//public static String PRODUCT_IMAGE_PATH = "http://192.168.1.3/hdfc_bso/uploads/product/thumb/";
	public static String PRODUCT_IMAGE_PATH = "https://hdfcbso.infomanav.com/uploads/product/thumb/";
	public static String PRODUCT_DETAILS_IMAGE_PATH = "http://hdfcbso.infomanav.com/uploads/product/";
	//public static String APP_IMAGE_PATH = "http://192.168.1.3/hdfc_bso/uploads/app/thumb/";
	public static String APP_IMAGE_PATH = "https://hdfcbso.infomanav.com/file/app_img?";
	public static String NOTIF_IMAGE_PATH = "https://hdfcbso.infomanav.com/file/ann_img?";
	public static String USER_IMAGE_PATH = "https://hdfcbso.infomanav.com/file/profile_img?";
	//public static String PDF_DOWNLOAD_PATH = "https://hdfcbso.infomanav.com/uploads/product/";
	public static String PDF_DOWNLOAD_PATH = "https://hdfcbso.infomanav.com/file?";
	//public static String KNOWLEDGE_HUB_PDF_DOWNLOAD_PATH = "https://hdfcbso.infomanav.com/uploads/know/";
	public static String KNOWLEDGE_HUB_PDF_DOWNLOAD_PATH = "https://hdfcbso.infomanav.com/file/download_know?";
	public static String SEEDVALUE = "rushirajparalkar";
	public static String JWT_TOKEN = "jwtToken";


	// SMS provider identification
	// It should match with your SMS gateway origin
	// You can use  MSGIND, TESTER and ALERTS as sender ID
	// If you want custom sender Id, approve MSG91 to get one
	public static final String SMS_ORIGIN = "ANHIVE";

	// special character to prefix the otp. Make sure this character appears only once in the sms
	public static final String OTP_DELIMITER = ":";
}