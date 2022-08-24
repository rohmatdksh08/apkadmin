package com.jatmika.admin_e_complaintrangkasbitung.API;

import com.jatmika.admin_e_complaintrangkasbitung.Model.Admin;
import com.jatmika.admin_e_complaintrangkasbitung.Model.Komentar;
import com.jatmika.admin_e_complaintrangkasbitung.Model.Komplain;
import com.jatmika.admin_e_complaintrangkasbitung.Model.TokenApi;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface API {
    @POST("/api/admin/login")
    @FormUrlEncoded
    Call<TokenApi> login(@Field("email") String username, @Field("password") String password, @Field("device_name") String devices_name);
    @GET("/api/komplain/by/{kategori}")
    Call<List<Komplain>> getComplain(@Header("Authorization") String token, @Path("kategori") String kategori);
    @GET("/api/balasan/{idKomplain}")
    Call<List<Komentar>> getComentar(@Header("Authorization") String token, @Path("idKomplain") String idKomplain);
    @POST("/api/balasan")
    @FormUrlEncoded
    Call<Komentar> addComentar(@Header("Authorization") String token, @Field("id_komplain") String id_komplain, @Field("balasan") String balasan);
    @POST("/api/status-komplain")
    @FormUrlEncoded
    Call<ResponseBody> updateStatusComplain(@Header("Authorization") String token, @Field("id_komplain") String id_komplain, @Field("nama_pemeroses") String nama_pemeroses, @Field("pesan") String pesan, @Field("status") String status);
}
