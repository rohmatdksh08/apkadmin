package com.jatmika.admin_e_complaintrangkasbitung.API;

import com.jatmika.admin_e_complaintrangkasbitung.Model.Admin;
import com.jatmika.admin_e_complaintrangkasbitung.Model.DataBerita;
import com.jatmika.admin_e_complaintrangkasbitung.Model.DataUser;
import com.jatmika.admin_e_complaintrangkasbitung.Model.Komentar;
import com.jatmika.admin_e_complaintrangkasbitung.Model.Komplain;
import com.jatmika.admin_e_complaintrangkasbitung.Model.Penduduk;
import com.jatmika.admin_e_complaintrangkasbitung.Model.PersentaseKomplain;
import com.jatmika.admin_e_complaintrangkasbitung.Model.TokenApi;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    @POST("/api/penduduk")
    @FormUrlEncoded
    Call<ResponseBody> addPenduduk(@Header("Authorization") String token, @Field("nik") String nik, @Field("nama_penduduk") String nama_penduduk, @Field("jenis_kelamin") String jk, @Field("tempat_lahir") String tempat_lahir, @Field("tanggal_lahir") String tanggal_lahir, @Field("alamat") String alamat);
    @GET("/api/penduduk")
    Call<List<Penduduk>> getPenduduk(@Header("Authorization") String token);
    @DELETE("/api/komplain/delete/{id}")
    Call<ResponseBody> deleteKomplain(@Header("Authorization") String token, @Path("id") String id);

    @Multipart
    @POST("/api/berita")
    Call<ResponseBody> addBerita(@Header("Authorization") String token, @Part("isi_berita") RequestBody isi_berita, @Part("judul") RequestBody judul, @Part MultipartBody.Part foto);
    @GET("/api/berita")
    Call<List<DataBerita>> getBerita(@Header("Authorization") String token);
    @POST("/api/berita-balasan")
    @FormUrlEncoded
    Call<ResponseBody> addComentarBerita(@Header("Authorization") String token, @Field("id_berita") String id_berita, @Field("balasan") String balasan);
    @GET("/api/berita-balasan/{id}")
    Call<List<Komentar>> getComentarBerita(@Header("Authorization") String token, @Path("id") String id);
    @DELETE("/api/berita/delete/{id}")
    Call<ResponseBody> deleteBerita(@Header("Authorization") String token, @Path("id") String id);
    @POST("/api/logout")
    Call<ResponseBody> logout(@Header("Authorization") String token);

    @DELETE("/api/penduduk/delete/{id}")
    Call<ResponseBody> deletePenduduk(@Header("Authorization") String token, @Path("id") String id);

    @GET("/api/pengguna")
    Call<List<DataUser>> getPengguna(@Header("Authorization") String token);

    @GET("/api/chart-komplain")
    Call<List<PersentaseKomplain>> getPersentase(@Header("Authorization") String token);
}
