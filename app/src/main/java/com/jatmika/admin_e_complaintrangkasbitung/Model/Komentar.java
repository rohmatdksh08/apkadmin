package com.jatmika.admin_e_complaintrangkasbitung.Model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Komentar {

    private String komentarText;
    private String komentarUser;
    private String komentarEmail;
    private String key;

    public Komentar(String komentarText, String komentarUser, String komentarEmail) {
        this.komentarText = komentarText;
        this.komentarUser = komentarUser;
        this.komentarEmail = komentarEmail;
    }

    public Komentar() {

    }

    public String getKey(){
        return key;
    }
    public void setKey(String key){
        this.key = key;
    }

    public String getKomentarText() {
        return komentarText;
    }
    public void setKomentarText(String komentarText) {
        this.komentarText = komentarText;
    }

    public String getKomentarUser() {
        return komentarUser;
    }
    public void setKomentarUser(String komentarUser) {
        this.komentarUser = komentarUser;
    }

    public String getKomentarEmail() {
        return komentarEmail;
    }
    public void setKomentarEmail(String komentarEmail) {
        this.komentarEmail = komentarEmail;
    }

}
