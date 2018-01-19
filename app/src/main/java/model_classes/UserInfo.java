package model_classes;

import android.content.Context;

import services.JWTToken;

/**
 * Created by Javed Shaikh on 24-Mar-17.
 */
public class UserInfo
{

    public JWTToken jwtToken;
    private Context context;

    public  UserInfo(Context context)
    {
        this.context=context;
        jwtToken = new JWTToken(this.context);

    }

    /*public UserInfo(String status, String msg, String server_jwt_token, String siteUserID, String userAccountId, String userFirstName, String userLastName, String modelName, String myint, String customerType, String expectedReturn, String user_status, String userEmailID, String modelsFkId) {
        this.status = status;
        this.msg = msg;
        this.server_jwt_token = server_jwt_token;
        this.siteUserID = siteUserID;
        this.userAccountId = userAccountId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.ModelName = modelName;
        this.myint = myint;
        this.CustomerType = customerType;
        this.ExpectedReturn = expectedReturn;
        this.user_status = user_status;
        this.userEmailID = userEmailID;
        this.modelsFkId = modelsFkId;
    }*/

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getServer_jwt_token() {
        return server_jwt_token;
    }

    public void setServer_jwt_token(String server_jwt_token) {
        this.server_jwt_token = server_jwt_token;
    }

    public String getSiteUserID() {
        return siteUserID;
    }

    public void setSiteUserID(String siteUserID) {
        this.siteUserID = siteUserID;
    }

    public String getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(String userAccountId) {
        this.userAccountId = userAccountId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getModelName() {
        return ModelName;
    }

    public void setModelName(String modelName) {
        ModelName = modelName;
    }

    public String getMyint() {
        return myint;
    }

    public void setMyint(String myint) {
        this.myint = myint;
    }

    public String getCustomerType() {
        return CustomerType;
    }

    public void setCustomerType(String customerType) {
        CustomerType = customerType;
    }

    public String getExpectedReturn() {
        return ExpectedReturn;
    }

    public void setExpectedReturn(String expectedReturn) {
        ExpectedReturn = expectedReturn;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getUserEmailID() {
        return userEmailID;
    }

    public void setUserEmailID(String userEmailID) {
        this.userEmailID = userEmailID;
    }

    public String getModelsFkId() {
        return modelsFkId;
    }

    public void setModelsFkId(String modelsFkId) {
        this.modelsFkId = modelsFkId;
    }

    public String status,msg,server_jwt_token,siteUserID,userAccountId,userFirstName,userLastName,
            ModelName,myint,CustomerType,ExpectedReturn,user_status,userEmailID,modelsFkId;
}
