package com.jatmika.admin_e_complaintrangkasbitung.Model;

public class DataUser {

    private String  nik, email, nama_penduduk, no_telpon, key;
    private int position;

    public DataUser() {

    }

    public DataUser(int position){
        this.position = position;
    }

    public DataUser( String nik, String email, String nama_penduduk, String no_telpon) {
        this.nik = nik;
        this.email = email;
        this.nama_penduduk = nama_penduduk;
        this.no_telpon = no_telpon;
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
        return nama_penduduk;
    }
    public void setNama(String nama) {
        this.nama_penduduk = nama;
    }

    public String getNohp() {
        return no_telpon;
    }
    public void setNohp(String nohp) {
        this.no_telpon = nohp;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
