package fr.iut.appmobprojet.data.model;

import java.util.Date;

public class Product {
    private Date dateAjout;
    private String donneur;
    private String marque;
    private Date peremption;
    private String titre;
    private String typeNourriture;
    private double codePostal;

    public Product(double codePostal, Date dateAjout, String donneur, String marque, Date peremption, String titre, String typeNourriture) {
        this.codePostal = codePostal;
        this.dateAjout = dateAjout;
        this.donneur = donneur;
        this.marque = marque;
        this.peremption = peremption;
        this.titre = titre;
        this.typeNourriture = typeNourriture;
    }

    public double getCodePostal() {
        return codePostal;
    }

    public Date getDateAjout() {
        return dateAjout;
    }

    public String getDonneur() {
        return donneur;
    }

    public String getMarque() {
        return marque;
    }

    public Date getPeremption() {
        return peremption;
    }

    public String getTitre() {
        return titre;
    }

    public String getTypeNourriture() {
        return typeNourriture;
    }
}
