package com.jatmika.admin_e_complaintrangkasbitung.Model;

public class DataBerita {
    private String judulberita;
    private String foto;
    private String tanggalposting;
    private String penulis;
    private String isiberita;
    private String key;
    private int position;

    public DataBerita() {

    }

    public DataBerita(int position){
        this.position = position;
    }

    public DataBerita(String judulberita, String foto, String tanggalposting , String penulis , String isiberita) {
        if (judulberita.trim().equals("")) {
            judulberita = "No Name";
        }
        this.judulberita = judulberita;
        this.foto = foto;
        this.tanggalposting = tanggalposting;
        this.penulis = penulis;
        this.isiberita = isiberita;
    }

    public String getTanggalposting() {
        return tanggalposting;
    }
    public void setTanggalposting(String tanggalposting) {
        this.tanggalposting = tanggalposting;
    }

    public String getJudulberita() {
        return judulberita;
    }
    public void setJudulberita(String judulberita) {
        this.judulberita = judulberita;
    }

    public String getFoto() {
        return foto;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getPenulis() {
        return penulis;
    }
    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }
    public String getIsiberita() {
        return isiberita;
    }
    public void setIsiberita(String isiberita) {
        this.isiberita = isiberita;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}