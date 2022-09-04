package com.jatmika.admin_e_complaintrangkasbitung.API;

public class APIUtility {
    public static final String base_url = "https://api-rohmat.kosanbahari.xyz";

    public static API getAPI(){
        return RetrofitClient.getClient(base_url).create(API.class);
    }
}
