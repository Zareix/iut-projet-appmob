package fr.iut.appmobprojet.data.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Objet java représentant un produit tel qu'ils sont stockés dans Firestore
 */
public class Product {
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

    private String id;
    private String dateAjout;
    private Date dateAjoutDate;
    private String categorie;
    private String donneur;
    private String marque;
    private String peremption;
    private Date peremptionDate;
    private String titre;
    private String typeNourriture;
    private String codePostal;
    private String reservePar;

    public Product(String id, String codePostal, Date dateAjout, String donneur, String marque, Date peremption, String titre, String typeNourriture, String categorie) {
        this.id = id;
        this.codePostal = codePostal;
        this.dateAjout = dateFormat.format(dateAjout);
        this.dateAjoutDate = dateAjout;
        this.donneur = donneur;
        this.marque = marque;
        this.peremption = dateFormat.format(peremption);
        this.peremptionDate = peremption;
        this.titre = titre;
        this.typeNourriture = typeNourriture;
        this.categorie = categorie;
        this.reservePar = "";
    }

    public Product(String id, String codePostal, Date dateAjout, String donneur, String marque, Date peremption, String titre, String typeNourriture, String categorie, String estReservePar) {
        this.id = id;
        this.codePostal = codePostal;
        this.dateAjout = dateFormat.format(dateAjout);
        this.dateAjoutDate = dateAjout;
        this.donneur = donneur;
        this.marque = marque;
        this.peremption = dateFormat.format(peremption);
        this.peremptionDate = peremption;
        this.titre = titre;
        this.typeNourriture = typeNourriture;
        this.categorie = categorie;
        this.reservePar = estReservePar;
    }

    public String getReservePar() {
        return reservePar;
    }

    public String getId() {
        return id;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public String getDateAjout() {
        return dateAjout;
    }

    public Date getDateAjoutDate() {
        return dateAjoutDate;
    }

    public String getDonneur() {
        return donneur;
    }

    public String getMarque() {
        return marque;
    }

    public String getPeremption() {
        return peremption;
    }

    public Date getPeremptionDate() {
        return peremptionDate;
    }

    public String getTitre() {
        return titre;
    }

    public String getTypeNourriture() {
        return typeNourriture;
    }

    public String getCategorie() {
        return categorie;
    }

    @Override
    public String toString() {
        return titre + " par " + donneur;
    }
}
