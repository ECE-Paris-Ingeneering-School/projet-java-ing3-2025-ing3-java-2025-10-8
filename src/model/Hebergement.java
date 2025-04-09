package model;

public abstract class Hebergement {
    protected int id;
    protected String nom;
    protected String adresse;
    protected double prixParNuit;
    protected String categorie;

    public Hebergement() {}

    public Hebergement(int id, String nom, String adresse, double prixParNuit, String categorie) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.prixParNuit = prixParNuit;
        this.categorie = categorie;
    }
}