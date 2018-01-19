package services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by USER on 21-01-2016.
 */
public class ServiceHandler
{

    Context context;
    ProgressDialog progressDialog;
    String strResponse;
    Activity activity;
    private KProgressHUD hud;
    LoadingDialog loadingDialog;
    private  Object object;
    private ArrayList objectArrayList;
    private HJSONParsing hjsonParsing;
    public ServiceHandler(Context context)
    {
        this.context = context;
        hjsonParsing = new HJSONParsing(context);
    }




    public void registerUser(Map<String,String> paramets,String web_url, final VolleyCallback callback)
    {

      /*  hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);

        hud.show();*/

        loadingDialog =  new LoadingDialog(context);
        loadingDialog.show();


        final  Map<String,String> params = paramets;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, web_url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        if(loadingDialog!=null && loadingDialog.isShowing())
                            loadingDialog.dismiss();

                        //Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                        strResponse = response;

                       // object = hjsonParsing.ParseLoginDetail(context,strResponse);


                        callback.onSuccess(strResponse);

                    }


                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        if(loadingDialog!=null && loadingDialog.isShowing())
                            loadingDialog.dismiss();
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                    }
                })


        {
            @Override
            protected Map<String,String> getParams()
            {

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

       /* stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
    }




    public void callAPIObj(Map<String,String> paramets,String web_url,final String parseType, final VolleyCallbackObject callback)
    {

      /*  hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);

        hud.show();*/

        loadingDialog =  new LoadingDialog(context);
        loadingDialog.show();


        final  Map<String,String> params = paramets;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, web_url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        if(loadingDialog!=null && loadingDialog.isShowing())
                            loadingDialog.dismiss();

                        //Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                        strResponse = response;
                        object = hjsonParsing.ParseLoginDetail(context,strResponse,parseType);
                        callback.onSuccess(object);

                    }


                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        if(loadingDialog!=null && loadingDialog.isShowing())
                            loadingDialog.dismiss();
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                    }
                })


        {
            @Override
            protected Map<String,String> getParams()
            {

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

       /* stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
    }



    public void callAPIList(Map<String,String> paramets, String web_url, final String parseType, final VolleyCallbackList callback)
    {

      /*  hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);

        hud.show();*/

        loadingDialog =  new LoadingDialog(context);
        loadingDialog.show();


        final  Map<String,String> params = paramets;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, web_url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        if(loadingDialog!=null && loadingDialog.isShowing())
                            loadingDialog.dismiss();

                        //Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                        strResponse = response;

                        objectArrayList = hjsonParsing.parseTransaction(context,strResponse,parseType);


                        callback.onSuccess(objectArrayList);

                    }


                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        if(loadingDialog!=null && loadingDialog.isShowing())
                            loadingDialog.dismiss();
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                    }
                })


        {
            @Override
            protected Map<String,String> getParams()
            {

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

       /* stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
    }



    public interface VolleyCallback{
        void onSuccess(String result);
    }

    public interface VolleyCallbackObject{
        void onSuccess(Object result);
    }
    public interface VolleyCallbackList{
        void onSuccess(ArrayList result);
    }

}
