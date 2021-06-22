package com.DicHAc.demo;

public class ListItems {
    private String nom_prenom;
    private String qualification;
    private String ImgUrl;

    public ListItems(String nom_prenom, String qualification, String imgUrl) {
        this.nom_prenom = nom_prenom;
        this.qualification = qualification;
        ImgUrl = imgUrl;
    }

    public String getNom_prenom() {
        return nom_prenom;
    }

    public void setNom_prenom(String nom_prenom) {
        this.nom_prenom = nom_prenom;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }
}
