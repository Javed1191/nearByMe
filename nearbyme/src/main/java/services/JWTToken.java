package services;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

/**
 * Created by LENOVO on 10/06/2016.
 */
public class JWTToken
{
    private Context context;
    private Date date;
    static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
    private String strKey = "android";
    private Utility utility;
    public JWTToken(Context context)
    {
        this.context =context;
        utility = new Utility(context);
    }

    public String getJWTToken()
    {
        String strJWTToken="";
        try
        {
           // converting key to base64
           // strKey  =  utility.convertStringToBase64(strKey);

            Long tockenExpTime = System.currentTimeMillis();///1000;
            // String ts = tsLong.toString();

           // String ts1=  context.getDate(tockenExpTime,"dd/MM/yyyy HH:mm:ss.SSS"); // to get 24 hour date format


            //String dtStart = ts1;
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss.SSS"); // to get 12 hour date format
            date = new Date();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.SECOND, 10); // will add 10 minutes to current time

             String strDate =  dateFormat.format(date);
            Date date1MinutsAdded =  calendar.getTime();
            String strDate2 =  dateFormat.format(date1MinutsAdded);
            long long_date = date1MinutsAdded.getTime();

            // We need a signing key, so we'll create one just for this example. Usually
            // the key would be read from your application configuration instead.
           // Key key = MacProvider.generateKey();

            //  String s = Jwts.builder().setSubject("Joe").signWith(SignatureAlgorithm.HS256, key).setExpiration(date).set.compact();

           // String s = Jwts.builder().setSubject("HDFC BSO App").setExpiration(date).signWith(SignatureAlgorithm.HS256, "android").compact();

            strJWTToken = Jwts.builder().setSubject("www.infomanav.com").setExpiration(date1MinutsAdded).setAudience("4fox solutions").signWith(SignatureAlgorithm.HS256, strKey).compact();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return strJWTToken;

    }

    public boolean decryptJWTToken(String str_jwt_token)
    {
        boolean is_valid=false;
        try
        {
            //String dtStart = ts1;
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss"); // to get 12 hour date format
            date = new Date();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, 5); // will add 10 minutes to current time

            String strDate =  dateFormat.format(date);
            Date date1MinutsAdded =  calendar.getTime();
            String strDate2 =  dateFormat.format(date1MinutsAdded);

         //  str_jwt_token = getJWTToken();

           // Jwts.parser().setSigningKey("android").parseClaimsJws("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC93d3cuNGZveC5pbiIsImF1ZCI6Imh0dHA6XC9cL3d3dy40Zm94LmluIiwiaWF0IjoxMzU2OTk5NTI0LCJuYmYiOjEzNTcwMDAwMDB9.fP_7i7psQ2tAyfsARdSw1ha1cHUBn3kCi3mDnTJCIJI");


          //  Jwt jwt = Jwts.parser().setSigningKey("android").parseClaimsJws(str_jwt_token);

            Jwt jwt = Jwts.parser().setSigningKey("android").parseClaimsJws(str_jwt_token);
         //  boolean is_signed = Jwts.parser().setSigningKey("android").isSigned(str_jwt_token);


            //OK, we can trust this JWT
          //  Toast.makeText(SplashScreenActivity.this, "Success", Toast.LENGTH_SHORT).show();
            is_valid = true;

        }
        catch (SignatureException e)
        {
           // Toast.makeText(SplashScreenActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            //don't trust the JWT!
            e.printStackTrace();
            is_valid = false;
        }
        catch(ExpiredJwtException e)
        {
            e.printStackTrace();
            is_valid = false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            is_valid = false;
        }

        return is_valid;

    }
}
