package services;

import android.content.Context;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.List;

public class ServiceHandler 
{
	public static int POST = 1;
	public static int GET = 2;
	String responce;
	Context context;
	CookieManager cookieManager;
	public ServiceHandler(Context context)
	{
		this.context = context;
		cookieManager = new CookieManager();
	}
	
	public String makeSeviceCall(String url, int method)
	{
		return this.makeServiceCall(url, method, null);
	}
	public String makeServiceCall(String url, int method, List<NameValuePair> param)
	{
		try 
		{

			CookieHandler.setDefault(cookieManager);
			//DefaultHttpClient httpClient = new MyHttpClient(context);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpEntity httpEntity = null;
			HttpResponse httpResponce = null;
			
			if(method==POST)
			{
				HttpPost httpPost = new HttpPost(url);
				if(param!=null)
				{
					httpPost.setEntity(new UrlEncodedFormEntity(param));
				}
				httpResponce = httpClient.execute(httpPost);
				httpEntity = httpResponce.getEntity();
				//responce = EntityUtils.toString(httpEntity);
			}
			else if(method==GET)
			{
				if(param!=null)
				{
					String str_Json = URLEncodedUtils.format(param, "utf-8");
					url += "?"+ str_Json;
				}
				HttpGet httpGet = new HttpGet(url);
				httpResponce = httpClient.execute(httpGet);
				httpEntity = httpResponce.getEntity();
				 
			}
			responce = EntityUtils.toString(httpEntity);
		}
		catch (Exception e) 
		{
			// TODO: handle exception

			e.printStackTrace();
		}
			return responce;
		
	}
}
