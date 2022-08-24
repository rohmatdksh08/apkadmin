package com.jatmika.admin_e_complaintrangkasbitung.Model;

public class Komplain {
    private String foto, berkas, no_komplain, nik, email, nama, alamat, tanggal, namalokasi, isi, kategori, status,
            jml_lihat, jml_suka, jml_balas, id_komplain;
    private Double latitude;
    private Double longitude;
    private int position;

    public Komplain() {

    }

    public Komplain(int position){
        this.position = position;
    }

    public Komplain(String foto, String berkas, String no_komplain, String nik, String email, String nama, String alamat, String tanggal,
                    String namalokasi, Double latitude, Double longitude, String isi, String kategori, String status, String jml_lihat,
                    String jml_suka, String jml_balas) {
        this.foto = foto;
        this.berkas = berkas;
        this.no_komplain = no_komplain;
        this.nik = nik;
        this.email = email;
        this.nama = nama;
        this.alamat = alamat;
        this.tanggal = tanggal;
        this.namalokasi = namalokasi;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isi = isi;
        this.kategori = kategori;
        this.status = status;
        this.jml_lihat = jml_lihat;
        this.jml_suka = jml_suka;
        this.jml_balas = jml_balas;
    }

    public String getFoto() {
        return foto;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getBerkas() {
        return berkas;
    }
    public void setBerkas(String berkas) {
        this.berkas = berkas;
    }

    public String getNomor() {
        return no_komplain;
    }
    public void setNomor(String nomor) {
        this.no_komplain = nomor;
    }

    public String getNik() {
        return nik;
    }
    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }
    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTanggal() {
        return tanggal;
    }
    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNamalokasi() {
        return namalokasi;
    }
    public void setNamalokasi(String namalokasi) {
        this.namalokasi = namalokasi;
    }

    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getIsi() {
        return isi;
    }
    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getKategori() {
        return kategori;
    }
    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getJml_lihat() {
        return jml_lihat;
    }
    public void setJml_lihat(String jml_lihat) {
        this.jml_lihat = jml_lihat;
    }

    public String getJml_suka() {
        return jml_suka;
    }
    public void setJml_suka(String jml_suka) {
        this.jml_suka = jml_suka;
    }

    public String getJml_balas() {
        return jml_balas;
    }
    public void setJml_balas(String jml_balas) {
        this.jml_balas = jml_balas;
    }

    public String getKey() {
        return id_komplain;
    }
    public void setKey(String key) {
        this.id_komplain = key;
    }
}