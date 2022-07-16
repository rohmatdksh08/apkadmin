package com.jatmika.admin_e_complaintrangkasbitung.Model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Suka {

    private String email;
    private String namaPengguna;
    private String key;

    public Suka(String email, String namaPengguna) {
        this.email = email;
        this.namaPengguna = namaPengguna;
    }

    public Suka() {

    }

    public String getKey(){
        return key;
    }
    public void setKey(String key){
        this.key = key;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getNamaPengguna() {
        return namaPengguna;
    }
    public void setNamaPengguna(String namaPengguna) {
        this.namaPengguna = namaPengguna;
    }

    @Override
    public String toString(){
        return " "+email+"\n" +
                " "+namaPengguna;
    }

}
