package com.jatmika.admin_e_complaintrangkasbitung.Model;

public class Proses {

    private String photo;
    private String pesan;
    private String oleh;
    private String key;

    public Proses(String photo, String pesan, String oleh) {
        this.photo = photo;
        this.pesan = pesan;
        this.oleh = oleh;
    }

    public Proses() {

    }

    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPesan() {
        return pesan;
    }
    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public String getOleh() {
        return oleh;
    }
    public void setOleh(String oleh) {
        this.oleh = oleh;
    }

    public String getKey(){
        return key;
    }
    public void setKey(String key){
        this.key = key;
    }

}
