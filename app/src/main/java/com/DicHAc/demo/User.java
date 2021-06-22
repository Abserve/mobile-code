package com.DicHAc.demo;

/**
 * Chrif made it
 */
public class User {
    String name;
    String embSet;
    String UserId;
    String email,phone,adresse,cin,birthd,qualification,embauche;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getBirthd() {
        return birthd;
    }

    public void setBirthd(String birthd) {
        this.birthd = birthd;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getEmbauche() {
        return embauche;
    }

    public void setEmbauche(String embauche) {
        this.embauche = embauche;
    }

    public User(String email, String phone, String adresse, String cin, String birthd, String qualification, String embauche) {
        this.email = email;
        this.phone = phone;
        this.adresse = adresse;
        this.cin = cin;
        this.birthd = birthd;
        this.qualification = qualification;
        this.embauche = embauche;
    }

    public User(String name, String embSet, String userId) {
        this.name = name;
        this.embSet = embSet;
        this.UserId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmbSet() {
        return embSet;
    }

    public void setEmbSet(String embSet) {
        this.embSet = embSet;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public User() {
    }
}
