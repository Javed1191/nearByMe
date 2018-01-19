package services;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility 
{
	private Context context;
	
	public Utility(Context context)
	{
		this.context = context;
	}
	
	/**
	 * This is used to check email format
	 * 
	 * @author USER
	 * @param email
	 * @return
	 */
	public boolean checkEmail(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) 
		{
			isValid = true;
		}
		return isValid;
	}
	
	/**
	 * This is used to check weather Internet is on or off
	 * 
	 * @author USER
	
	 * @return
	 */
	
	public boolean checkInternet() {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (connectivity != null) {
				NetworkInfo[] info = connectivity.getAllNetworkInfo();
				if (info != null)
					for (int i = 0; i < info.length; i++)
						// check if network is connected or device is in range
						if (info[i].getState() == NetworkInfo.State.CONNECTED) {
							return true;
						}		
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String getCountryCode()
	{
		
		
		String countryCode = null;
		try {
			TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			  countryCode = tm.getSimCountryIso();
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return countryCode;
		
	}

	public String getDeviceId()
	{
		String imeistring="",imsistring="";
		TelephonyManager telephonyManager;

		try
		{
			int permissionCheck = ContextCompat.checkSelfPermission(context,
					Manifest.permission.READ_PHONE_STATE);

			telephonyManager  = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

		/*
		* getDeviceId() function Returns the unique device ID.
		* for example,the IMEI for GSM and the MEID or ESN for CDMA phones.
		*/
			imeistring = telephonyManager.getDeviceId();

		/*
		* getSubscriberId() function Returns the unique subscriber ID,
		* for example, the IMSI for a GSM phone.
		*/
			//imsistring = telephonyManager.getSubscriberId();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return imeistring;
	}

	public String convertStringToBase64(String toEncodeString) throws UnsupportedEncodingException
	{
		byte[] data = toEncodeString.getBytes("UTF-8");
		String base64 = Base64.encodeToString(data, Base64.DEFAULT);

		return base64;
	}

	public String convertBase64ToString(String toDecodeString) throws UnsupportedEncodingException
	{
		byte[] data = Base64.decode(toDecodeString, Base64.DEFAULT);
		String text = new String(data, "UTF-8");

		return text;
	}


}
