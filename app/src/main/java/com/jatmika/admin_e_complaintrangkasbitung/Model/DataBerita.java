package com.jatmika.admin_e_complaintrangkasbitung.Model;

public class DataBerita {
    private String judul;
    private String foto;
    private String created_at;
    private String nama;
    private String isi_berita;
    private String id_berita;
    private int position;

    public DataBerita() {

    }

    public DataBerita(int position){
        this.position = position;
    }

    public DataBerita(String judul, String foto, String created_at , String nama , String isi_berita) {
        if (judul.trim().equals("")) {
            judul = "No Name";
        }
        this.judul = judul;
        this.foto = foto;
        this.created_at = created_at;
        this.nama = nama;
        this.isi_berita = isi_berita;
    }

    public String getcreated_at() {
        return created_at;
    }
    public void setcreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getjudul() {
        return judul;
    }
    public void setjudul(String judul) {
        this.judul = judul;
    }

    public String getFoto() {
        return foto;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getnama() {
        return nama;
    }
    public void setnama(String nama) {
        this.nama = nama;
    }
    public String getisi() {
        return isi_berita;
    }
    public void setisi(String isi_berita) {
        this.isi_berita = isi_berita;
    }

    public String getid_berita() {
        return id_berita;
    }
    public void setid_berita(String id_berita) {
        this.id_berita = id_berita;
    }
}