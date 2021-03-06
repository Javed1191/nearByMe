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

import java.util.List;

public class SOServiceHandler
{
	public static int POST = 1;
	public static int GET = 2;
	String responce;
	Context context;
	public SOServiceHandler(Context context)
	{
		this.context = context;
	}
	
	public String makeSeviceCall(String url, int method)
	{
		return this.makeServiceCall(url, method, null);
	}
	public String makeServiceCall(String url, int method, List<NameValuePair> param)
	{
		try 
		{
			DefaultHttpClient httpClient = new SOHttpClient(context);
			//DefaultHttpClient httpClient = new DefaultHttpClient();
			/*TestSo testSo = new TestSo(context);

			DefaultHttpClient httpClient = (DefaultHttpClient) testSo.getNewHttpClient();*/

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
