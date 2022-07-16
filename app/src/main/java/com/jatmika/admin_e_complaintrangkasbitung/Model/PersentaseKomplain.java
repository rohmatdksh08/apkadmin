package com.jatmika.admin_e_complaintrangkasbitung.Model;

public class PersentaseKomplain {

    private String kategori;
    private String jumlah_komplain;
    private String persen;
    private String key;

    public PersentaseKomplain() {

    }

    public PersentaseKomplain(String kategori, String jumlah_komplain, String persen) {
        this.kategori = kategori;
        this.jumlah_komplain = jumlah_komplain;
        this.persen = persen;
    }

    public String getKategori() {
        return kategori;
    }
    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getJumlah_komplain() {
        return jumlah_komplain;
    }
    public void setJumlah_komplain(String jumlah_komplain) {
        this.jumlah_komplain = jumlah_komplain;
    }

    public String getPersen() {
        return persen;
    }
    public void setPersen(String persen) {
        this.persen = persen;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
