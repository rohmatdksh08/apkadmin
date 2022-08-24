package com.jatmika.admin_e_complaintrangkasbitung.Model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Komentar {

    private String balasan;
    private String nama;
    private String email;
    private String id_balasan;

    public Komentar(String balasan, String nama, String email) {
        this.balasan = balasan;
        this.nama = nama;
        this.email = email;
    }

    public Komentar() {

    }

    public String getKey(){
        return id_balasan;
    }
    public void setKey(String key){
        this.id_balasan = key;
    }

    public String getKomentarText() {
        return balasan;
    }
    public void setKomentarText(String komentarText) {
        this.balasan = komentarText;
    }

    public String getKomentarUser() {
        return nama;
    }
    public void setKomentarUser(String komentarUser) {
        this.nama = komentarUser;
    }

    public String getKomentarEmail() {
        return email;
    }
    public void setKomentarEmail(String komentarEmail) {
        this.email = komentarEmail;
    }

}
