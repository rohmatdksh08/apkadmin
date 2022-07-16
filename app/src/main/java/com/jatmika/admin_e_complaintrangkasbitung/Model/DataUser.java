package com.jatmika.admin_e_complaintrangkasbitung.Model;

public class DataUser {

    private String photo, nik, email, password, nama, ttl, jenkel, alamat, nohp, key;
    private int position;

    public DataUser() {

    }

    public DataUser(int position){
        this.position = position;
    }

    public DataUser(String photo, String nik, String email, String password, String nama, String ttl, String jenkel, String alamat, String nohp) {
        this.photo = photo;
        this.nik = nik;
        this.email = email;
        this.password = password;
        this.nama = nama;
        this.ttl = ttl;
        this.jenkel = jenkel;
        this.alamat = alamat;
        this.nohp = nohp;
    }

    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
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

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTtl() {
        return ttl;
    }
    public void setTtl(String ttl) {
        this.ttl = ttl;
    }

    public String getJenkel(){return jenkel;}
    public void setJenkel(String jenkel){
        this.jenkel = jenkel;
    }

    public String getAlamat() {
        return alamat;
    }
    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNohp() {
        return nohp;
    }
    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
