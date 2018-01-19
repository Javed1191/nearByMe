package services;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Shared_Preferences_Class 
{
	public static final String PREF_NAME = "TALENT_SPRINT_PREFERENCES";
	public static final int MODE = Context.MODE_PRIVATE;
	
	public static final String APP_USER_ID = "app_user_id";
	
	public static final String EMPLOYEE_CODE = "employee_code";
	public static final String EMPLOYEE_NAME = "employee_name";
	public static final String EMPLOYEE_EMAIL = "employee_email";
	public static final String EMPLOYEE_MOBILE = "employee_mobile";

	public static final String DEVICE_TOKEN = "device_token";
	public static final String LAST_SYNC_DATE = "last_sync_time";
	public static final String DATE_OF_JOINING = "date_of_joining";
	public static final String IS_BSO_VERIFIED = "is_bso_varified";
	//public static final String USER_INTEREST_ID = "user_interest_id";

	// HDFC SO USER SESSION
	public static final String SO_EMPLOYEE_CODE = "so_employee_code";
	public static final String IS_SO_VERIFIED = "no";
	public static final String SO_EMPLOYEE_NAME = "so_employee_name";
	public static final String SO_EMPLOYEE_EMAIL_ID = "so_employee_email_id";
	public static final String SO_EMPLOYEE_MOBILE_NO = "so_employee_mobile_no";
	public static final String SO_EMPLOYEE_BRANCH_CODE = "so_employee_breanch_code";
	public static final String SO_EMPLOYEE_PROFILE_PIC= "so_employee_profile_pic";



	public static final String USER_EMAIL = "user_email";
	public static final String USER_NAME = "user_name";
	public static final String USER_PASS = "user_pass";
	public static final String USER_PIC = "user_pic";
	//public static final String USER_PROFILE_IMAGE = "profile_image";
	public static String USER_CHANGE_NAME = "";
	public static String IS_APP_INSTALLED = "";
	public static String IS_WELCOME_POPUP = "";
	public static final String IS_USER_VERIFIED = "is_user_verified";
	public static final String USER_CITY = "";
	public static final String USER_CITY_ID = "1";
	public static final String USER_CITY_ONE = "user_city_one";
	public static final String USER_CITY_TWO = "user_city_two";
	public static final String USER_SOCIAL_IMAGE = "user_social_image";

	public static final String LOYALTY_POITS = "loyalty_points";
	
	public static final String FACEBOOK_ID = "facebook_id";
	public static final String FB_USER_ID = "fb_user_id";
	public static final String FB_EMAIL_ID = "fb_email_id";
	public static final String WEBSERVICE_URL = "web_url";
	public static final String FACEBOOK_USER_NAME = "facebook_user_name";
	public static final String FACEBOOK_USER_EMAIL = "facebook_user_email";
	public static final String FACEBOOK_USER_FIRST_NAME = "facebook_user_first_name";
	public static final String FACEBOOK_USER_LAST_NAME = "facebook_user_last_email";
	public static final String USER_FB_STATUS = "fb_user_status";
	
	public static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	public static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	public static final String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin";
	public static final String TWITTER_USER_NAME = "twitter_user_name";
	public static final String TWITTER_USER_ID = "twitter_user_id";
	public static final String USER_TWITTER_STATUS = "user_twitter_status";
	
	public static final String GOOGLE_ID = "google_id";
	public static final String GOOGLE_USER_ID = "google_user_id";
	public static final String GOOGLE_EMAIL_ID = "google_email_id";
	public static final String GOOGLE_USER_NAME = "google_user_name";
	
	
	public static final String REG_STEP = "reg_step";
	
	public static final String REG_FIRST = "reg_first";
	public static final String REG_LAST = "reg_last";
	public static final String REG_USER_NAME = "reg_user_name";
	public static final String REG_COUNTRY_ID = "reg_country_id";
	public static final String REG_STATE_ID = "reg_state_id";
	public static final String REG_CITY_ID = "reg_city_id";
	public static final String REG_COUNTRY_NAME = "reg_country_name";
	public static final String REG_STATE_NAME = "reg_state_name";
	public static final String REG_CITY_NAME = "reg_city_name";
	public static final String REG_DAY = "reg_day";
	public static final String REG_PROMO_CODE = "reg_promo_code";
	public static final String REG_MONTH = "reg_month";
	public static final String REG_YEAR = "reg_year";
	public static final String REG_MOB_NO = "reg_mob_no";
	public static final String REG_EMAIL = "reg_email";
	public static final String REG_PASS = "reg_pass";
	
	public static final String REG_STEP_1 = "reg_step_1";
	public static final String REG_MARITIAL = "reg_maritial";
	public static final String REG_MARITIAL_ID = "reg_maritial_id";
	public static final String REG_KIDS = "reg_kids";
	public static final String REG_KIDS_ID = "reg_kids_id";
	public static final String REG_EDU = "reg_edu";
	public static final String REG_EDU_ID = "reg_edu_id";
	public static final String REG_JOB = "reg_job";
	public static final String REG_JOB_ID = "reg_job_id";
	public static final String REG_INCOME = "reg_income";
	public static final String REG_INCOME_ID = "reg_income_id";
	public static final String REG_RELIGION = "reg_religion";
	public static final String REG_RELIGION_ID = "reg_religion_id";
	public static final String REG_ETHNICITY = "reg_ethnicity";
	public static final String REG_ETHNICITY_ID = "reg_ethnicity_id";
	public static final String REG_POL_VIEW = "reg_pol_view";
	public static final String REG_POL_VIEW_ID = "reg_pol_view_id";
	public static final String REG_HEIGHT = "reg_height";
	public static final String REG_HEIGHT_ID = "reg_height_id";
	public static final String REG_PHYSYCS = "reg_physycs";
	public static final String REG_PHYSYCS_ID = "reg_physycs_id";
	public static final String REG_EYE_COLOR = "reg_eye_color";
	public static final String REG_EYE_COLOR_ID = "reg_eye_color_id";
	public static final String REG_HAIR_COLOR= "reg_hair_color";
	public static final String REG_HAIR_COLOR_ID= "reg_hair_color_id";
	
	public static final String REG_STEP_2 = "reg_step_2";
	public static final String REG_ABOUT = "reg_about";
	public static final String REG_LOOK = "reg_look";
	public static final String REG_IDEAL = "reg_ideal";
	
	public static final String REG_STEP_3 = "reg_step_3";
	public static final String REG_PREF = "reg_pref";
	public static final String REG_OTHER_PREF = "reg_other_pref";

	public static final String GCM_REG_ID = "gcm_reg_id";
	public static final String NOTIFICATION_COUNT = "notification_count";

	public static final String TAG = "HDFC";
	public static final String SEARCH_TYPE = "";


	public static void writeBoolean(Context context, String key, boolean value)
	{
		getEditor(context).putBoolean(key, value).commit();
	}

	public static boolean readBoolean(Context context, String key,
                                      boolean defValue) {
		return getPreferences(context).getBoolean(key, defValue);
	}

	public static void writeInteger(Context context, String key, int value) {
		getEditor(context).putInt(key, value).commit();
	}

	public static int readInteger(Context context, String key, int defValue) {
		return getPreferences(context).getInt(key, defValue);
	}

	public static void writeString(Context context, String key, String value) {
		getEditor(context).putString(key, value).commit();
	}

	public static String readString(Context context, String key, String defValue) {
		return getPreferences(context).getString(key, defValue);
	}


	public static void writeFloat(Context context, String key, float value) {
		getEditor(context).putFloat(key, value).commit();
	}

	public static float readFloat(Context context, String key, float defValue) {
		return getPreferences(context).getFloat(key, defValue);
	}

	public static void writeLong(Context context, String key, long value) {
		getEditor(context).putLong(key, value).commit();
	}

	public static long readLong(Context context, String key, long defValue) {
		return getPreferences(context).getLong(key, defValue);
	}

	public static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences(PREF_NAME, MODE);
	}

	public static Editor getEditor(Context context) {
		return getPreferences(context).edit();
	}

}
