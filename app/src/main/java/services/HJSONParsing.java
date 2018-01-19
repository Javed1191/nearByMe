package services;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import model_classes.Security;
import model_classes.SecurityObj;
import model_classes.Transaction;
import model_classes.TransactionObj;
import model_classes.UserInfo;

/**
 * Created by Akash.Singh on 6/4/2015.
 * There has parse xml string.
 */
public class HJSONParsing
{

    public JWTToken jwtToken;
    private  Context context;

    public HJSONParsing(Context context)
    {
        this.context=context;
        jwtToken = new JWTToken(context);
    }

    public Object ParseLoginDetail(Context context,String Result,String parseType)
    {
        UserInfo userInfo =  new UserInfo(context);
        TransactionObj transactionObj = new TransactionObj();
        SecurityObj securityObj = new SecurityObj();
        Object returnObject=null;
        final ArrayList transactionArrayList = new ArrayList();


        try {
            if (Result != null)
            {

                String  title="",id="",userAccountId="",externalTransferId="",state="",
                        amount="",estimatedFundsAvailableDate="",achRelationshipId="",fees="",
                        FundingReqDate="",disbursementType="",requestedAmount="",rejectReason=""
                        ,GeneratedBy="",trade_line_status="",fundsAvailable="",withdrawVerified="";

                JSONObject jobject = new JSONObject(Result);
                userInfo.status = jobject.getString(StaticConstant.STATUS);
                userInfo.msg = jobject.getString(StaticConstant.MSG);
                userInfo.server_jwt_token = jobject.getString(StaticConstant.JWT_SERVER_TOKEN);
                if (userInfo.status.equalsIgnoreCase("success"))
                {
                    if (userInfo.jwtToken.decryptJWTToken(userInfo.server_jwt_token))
                    {


                        if(parseType.equalsIgnoreCase("transaction"))
                        {

                            JSONArray jArray = new JSONArray();
                            jArray = jobject.getJSONArray(StaticConstant.USER_DATA);

                            for (int i = 0; i < jArray.length(); i++)
                            {
                                JSONObject Obj = jArray.getJSONObject(i);
                                // A category variables


                                if(Obj.has(StaticConstant.TITLE))
                                {
                                    title = Obj.getString(StaticConstant.TITLE);
                                }
                                if(Obj.has(StaticConstant.ID))
                                {
                                    id = Obj.getString(StaticConstant.ID);
                                }

                                if(Obj.has(StaticConstant.USER_ACCOUNT_ID))
                                {
                                    userAccountId = Obj.getString(StaticConstant.USER_ACCOUNT_ID);
                                }
                                if(Obj.has(StaticConstant.EXTERNAL_TRANSFER_ID))
                                {
                                    externalTransferId = Obj.getString(StaticConstant.EXTERNAL_TRANSFER_ID);
                                }
                                if(Obj.has(StaticConstant.STATE))
                                {
                                    state = Obj.getString(StaticConstant.STATE);
                                }
                                if(Obj.has(StaticConstant.AMOUNT))
                                {
                                    amount = Obj.getString(StaticConstant.AMOUNT);
                                    amount = String.format("%.2f", Double.valueOf(amount));
                                }
                                if(Obj.has(StaticConstant.ESTIMATED_FUNDS_AVAILABLE_DATE))
                                {
                                    estimatedFundsAvailableDate = Obj.getString(StaticConstant.ESTIMATED_FUNDS_AVAILABLE_DATE);
                                }
                                if(Obj.has(StaticConstant.ACH_REALATIONSHIP_ID))
                                {
                                    achRelationshipId = Obj.getString(StaticConstant.ACH_REALATIONSHIP_ID);
                                }
                                if(Obj.has(StaticConstant.FEES))
                                {
                                    fees = Obj.getString(StaticConstant.FEES);
                                }
                                if(Obj.has(StaticConstant.FUNDING_REQ_DATE))
                                {
                                    FundingReqDate = Obj.getString(StaticConstant.FUNDING_REQ_DATE);
                                }
                                if(Obj.has(StaticConstant.DISBURSEMENT_TYPE))
                                {
                                    disbursementType = Obj.getString(StaticConstant.DISBURSEMENT_TYPE);
                                }
                                if(Obj.has(StaticConstant.REQUESTED_AMOUNT))
                                {
                                    requestedAmount = Obj.getString(StaticConstant.REQUESTED_AMOUNT);
                                }
                                if(Obj.has(StaticConstant.REJECTION_RESON))
                                {
                                    rejectReason = Obj.getString(StaticConstant.REJECTION_RESON);
                                }
                                if(Obj.has(StaticConstant.GENERATED_BY))
                                {
                                    GeneratedBy = Obj.getString(StaticConstant.GENERATED_BY);
                                }
                                if(Obj.has(StaticConstant.TRADE_LINE_STATUS))
                                {
                                    trade_line_status = Obj.getString(StaticConstant.TRADE_LINE_STATUS);
                                }
                                if(Obj.has(StaticConstant.FUNDS_AVAILABLE))
                                {
                                    fundsAvailable = Obj.getString(StaticConstant.FUNDS_AVAILABLE);
                                }
                                if(Obj.has(StaticConstant.WITHDRAW_VERIFIED))
                                {
                                    withdrawVerified = Obj.getString(StaticConstant.WITHDRAW_VERIFIED);
                                }

                                FundingReqDate = dateFormatChange(FundingReqDate);

                                Transaction feeds = new Transaction(title,FundingReqDate,amount,false);
                                transactionArrayList.add(feeds);

                                transactionObj.setTransactionArrayList(transactionArrayList);

                            }

                            returnObject = transactionObj;
                        }

                        else if(parseType.equalsIgnoreCase("login"))
                            {
                            JSONObject userJson = jobject.getJSONObject(StaticConstant.USER_DATA);


                            if(userJson.has(StaticConstant.SITE_USER_ID)){
                                userInfo.siteUserID = userJson.getString(StaticConstant.SITE_USER_ID);
                            }

                            if(userJson.has(StaticConstant.USER_ACCOUNT_ID)){
                                userInfo.userAccountId = userJson.getString(StaticConstant.USER_ACCOUNT_ID);
                            }
                            if(userJson.has(StaticConstant.USER_FIRST_NAME)){
                                userInfo.userFirstName = userJson.getString(StaticConstant.USER_FIRST_NAME);
                            }
                            if(userJson.has(StaticConstant.USER_LAST_NAME)){

                                userInfo.userLastName = userJson.getString(StaticConstant.USER_LAST_NAME);
                            }
                            if(userJson.has(StaticConstant.MODEL_NAME)){
                                userInfo.ModelName = userJson.getString(StaticConstant.MODEL_NAME);
                            }
                            if(userJson.has(StaticConstant.MY_INT)){
                                userInfo.myint = userJson.getString(StaticConstant.MY_INT);
                            }
                            if(userJson.has(StaticConstant.CUSTOMER_TYPE)){
                                userInfo.CustomerType = userJson.getString(StaticConstant.CUSTOMER_TYPE);
                            }
                            if(userJson.has(StaticConstant.EXPECTED_RETURN)){
                                userInfo.ExpectedReturn = userJson.getString(StaticConstant.EXPECTED_RETURN);
                            }
                            if(userJson.has(StaticConstant.USER_STATUS)){
                                userInfo.user_status = userJson.getString(StaticConstant.USER_STATUS);
                            }
                            if(userJson.has(StaticConstant.USER_EMAIL)){
                                userInfo.userEmailID = userJson.getString(StaticConstant.USER_EMAIL);
                            }
                            if(userJson.has(StaticConstant.MODEL_FK_ID)){
                                userInfo.modelsFkId = userJson.getString(StaticConstant.MODEL_FK_ID);
                            }

                            returnObject = userInfo;

                        }

                        else if(parseType.equalsIgnoreCase("security"))
                        {
                            JSONArray jArray = new JSONArray();
                            jArray = jobject.getJSONArray(StaticConstant.USER_DATA);

                            String  State="",IPAddress="",Country="",LoginTime="",
                                    strDate="",strTime="";

                            for (int i = 0; i < jArray.length(); i++)
                            {
                                JSONObject Obj = jArray.getJSONObject(i);
                                // A category variables

                                if(Obj.has(StaticConstant.IP_ADDRESS))
                                {
                                    IPAddress = Obj.getString(StaticConstant.IP_ADDRESS);
                                }
                                if(Obj.has(StaticConstant.STATE_1))
                                {
                                    State = Obj.getString(StaticConstant.STATE_1);
                                }

                                if(Obj.has(StaticConstant.COUNTRY))
                                {
                                    Country = Obj.getString(StaticConstant.COUNTRY);
                                }
                                if(Obj.has(StaticConstant.LOGIN_TIME))
                                {
                                    LoginTime = Obj.getString(StaticConstant.LOGIN_TIME);

                                    if(!TextUtils.isEmpty(LoginTime))
                                    {
                                        String[] date_time = LoginTime.split("\\s+");
                                        strDate = date_time[0];
                                        strTime = date_time[1];
                                    }
                                }

                            }

                            Security feeds = new Security(IPAddress,State,Country,strDate,strTime);
                            transactionArrayList.add(feeds);

                            securityObj.setSecurityArrayList(transactionArrayList);

                            returnObject = securityObj;

                        }

                        //userInfo.status = userJson.getString("status");

                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return returnObject;
    }

    public ArrayList parseTransaction(Context context,String Result, String parseType)
    {
        ArrayList testArrayList = null;

        final ArrayList transactionArrayList = new ArrayList();
        try {
            if (Result != null) {

                String  title="",id="",userAccountId="",externalTransferId="",state="",
                        amount="",estimatedFundsAvailableDate="",achRelationshipId="",fees="",
                        FundingReqDate="",disbursementType="",requestedAmount="",rejectReason=""
                        ,GeneratedBy="",trade_line_status="",fundsAvailable="",withdrawVerified="";
                String str_json;
                String str_status, str_msg,server_jwt_token;
                JSONObject jobject = new JSONObject(Result);
                str_status = jobject.getString(StaticConstant.STATUS);
                str_msg = jobject.getString(StaticConstant.MSG);
                server_jwt_token = jobject.getString(StaticConstant.JWT_SERVER_TOKEN);
                if (str_status.equalsIgnoreCase("success"))
                {
                    if (jwtToken.decryptJWTToken(server_jwt_token))
                    {

                        JSONArray jArray = new JSONArray();
                        jArray = jobject.getJSONArray(StaticConstant.USER_DATA);

                        if(parseType.equalsIgnoreCase("transaction"))
                        {
                            for (int i = 0; i < jArray.length(); i++)
                            {
                                JSONObject Obj = jArray.getJSONObject(i);
                                // A category variables


                                if(Obj.has(StaticConstant.TITLE))
                                {
                                    title = Obj.getString(StaticConstant.TITLE);
                                }
                                if(Obj.has(StaticConstant.ID))
                                {
                                    id = Obj.getString(StaticConstant.ID);
                                }

                                if(Obj.has(StaticConstant.USER_ACCOUNT_ID))
                                {
                                    userAccountId = Obj.getString(StaticConstant.USER_ACCOUNT_ID);
                                }
                                if(Obj.has(StaticConstant.EXTERNAL_TRANSFER_ID))
                                {
                                    externalTransferId = Obj.getString(StaticConstant.EXTERNAL_TRANSFER_ID);
                                }
                                if(Obj.has(StaticConstant.STATE))
                                {
                                    state = Obj.getString(StaticConstant.STATE);
                                }
                                if(Obj.has(StaticConstant.AMOUNT))
                                {
                                    amount = Obj.getString(StaticConstant.AMOUNT);
                                }
                                if(Obj.has(StaticConstant.ESTIMATED_FUNDS_AVAILABLE_DATE))
                                {
                                    estimatedFundsAvailableDate = Obj.getString(StaticConstant.ESTIMATED_FUNDS_AVAILABLE_DATE);
                                }
                                if(Obj.has(StaticConstant.ACH_REALATIONSHIP_ID))
                                {
                                    achRelationshipId = Obj.getString(StaticConstant.ACH_REALATIONSHIP_ID);
                                }
                                if(Obj.has(StaticConstant.FEES))
                                {
                                    fees = Obj.getString(StaticConstant.FEES);
                                }
                                if(Obj.has(StaticConstant.FUNDING_REQ_DATE))
                                {
                                    FundingReqDate = Obj.getString(StaticConstant.FUNDING_REQ_DATE);
                                }
                                if(Obj.has(StaticConstant.DISBURSEMENT_TYPE))
                                {
                                    disbursementType = Obj.getString(StaticConstant.DISBURSEMENT_TYPE);
                                }
                                if(Obj.has(StaticConstant.REQUESTED_AMOUNT))
                                {
                                    requestedAmount = Obj.getString(StaticConstant.REQUESTED_AMOUNT);
                                }
                                if(Obj.has(StaticConstant.REJECTION_RESON))
                                {
                                    rejectReason = Obj.getString(StaticConstant.REJECTION_RESON);
                                }
                                if(Obj.has(StaticConstant.GENERATED_BY))
                                {
                                    GeneratedBy = Obj.getString(StaticConstant.GENERATED_BY);
                                }
                                if(Obj.has(StaticConstant.TRADE_LINE_STATUS))
                                {
                                    trade_line_status = Obj.getString(StaticConstant.TRADE_LINE_STATUS);
                                }
                                if(Obj.has(StaticConstant.FUNDS_AVAILABLE))
                                {
                                    fundsAvailable = Obj.getString(StaticConstant.FUNDS_AVAILABLE);
                                }
                                if(Obj.has(StaticConstant.WITHDRAW_VERIFIED))
                                {
                                    withdrawVerified = Obj.getString(StaticConstant.WITHDRAW_VERIFIED);
                                }

                                FundingReqDate = dateFormatChange(FundingReqDate);

                                Transaction feeds = new Transaction(title,FundingReqDate,amount,false);
                                transactionArrayList.add(feeds);

                            }

                            testArrayList = transactionArrayList;
                        }
                        else if(parseType.equalsIgnoreCase("security"))
                        {

                            String  State="",IPAddress="",Country="",LoginTime="",
                                    strDate="",strTime="";

                            for (int i = 0; i < jArray.length(); i++)
                            {
                                JSONObject Obj = jArray.getJSONObject(i);
                                // A category variables

                                if(Obj.has(StaticConstant.IP_ADDRESS))
                                {
                                    IPAddress = Obj.getString(StaticConstant.IP_ADDRESS);
                                }
                                if(Obj.has(StaticConstant.STATE_1))
                                {
                                    State = Obj.getString(StaticConstant.STATE_1);
                                }

                                if(Obj.has(StaticConstant.COUNTRY))
                                {
                                    Country = Obj.getString(StaticConstant.COUNTRY);
                                }
                                if(Obj.has(StaticConstant.LOGIN_TIME))
                                {
                                    LoginTime = Obj.getString(StaticConstant.LOGIN_TIME);

                                    if(!TextUtils.isEmpty(LoginTime))
                                    {
                                        String[] date_time = LoginTime.split("\\s+");
                                        strDate = date_time[0];
                                        strTime = date_time[1];
                                    }
                                }

                            }

                            Security feeds = new Security(IPAddress,State,Country,strDate,strTime);
                            transactionArrayList.add(feeds);

                            testArrayList = transactionArrayList;

                        }



                        //userInfo.status = userJson.getString("status");

                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return testArrayList;
    }


    private String dateFormatChange(String strActualDate)
    {
        String dtStart = "2010-10-15T09:27:37Z";
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = format.parse(strActualDate);
            System.out.println(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String strDate = "";
        // Display a date in day, month, year format
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        // Display date with a short day and month name
        formatter = new SimpleDateFormat("EEE, dd MMMM yyyy");
        strDate = formatter.format(date);

        return  strDate;
    }



}
