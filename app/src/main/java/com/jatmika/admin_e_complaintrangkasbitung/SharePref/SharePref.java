package com.jatmika.admin_e_complaintrangkasbitung.SharePref;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePref {
    Context mContext;
    SharedPreferences sharedPreferences;
    private String token = "TOKEN_API";
    private String status_login = "STATUS_LOGIN";

    public SharePref(Context context){
        this.mContext = context;
        this.sharedPreferences = mContext.getSharedPreferences("admin_complaint", Context.MODE_PRIVATE);
    }

    public void setTokenApi(String token){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(this.token, token);
        editor.apply();
    }

    public String getTokenApi(){
        return sharedPreferences.getString(this.token, "");
    }

    public void setStatusLogin(Boolean statusLogin){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(this.status_login, statusLogin);
        editor.apply();
    }

    public Boolean getStatusLogin(){
        return sharedPreferences.getBoolean(this.status_login, false);
    }

}
