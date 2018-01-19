package services;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Shared_Preferences_Class 
{
	public static final String PREF_NAME = "WAHED_PREFERENCES";
	public static final int MODE = Context.MODE_PRIVATE;
	
	public static final String USER_ID = "app_user_id";
	public static final String USER_EMAIL = "user_email";
	public static final String USER_NAME = "user_name";
	public static final String USER_ACCOUNT_ID = "user_account_id";
	public static final String USER_ACCOUNT_TYPE = "user_account_type";
	public static final String USER_RISK_LEVEL = "user_risk_level";
	public static final String EXPECTED_RETURN = "expected_return";
	public static final String ACCOUNT_STATUS = "account_status";
	public static final String MODELS_FK_ID = "models_fk_id";
	public static final String IS_USER_AGREE = "is_user_agree";

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



	public static final String TAG = "WAHED";


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
