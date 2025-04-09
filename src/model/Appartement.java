package model;

public class Appartement extends Hebergement {
    private int nombrePieces;
    private boolean petitDejeuner;
    private int etage;

    public Appartement() {}

    public Appartement(int id, String nom, String adresse, double prixParNuit, String categorie,
                       int nombrePieces, boolean petitDejeuner, int etage) {
        super(id, nom, adresse, prixParNuit, categorie);
        this.nombrePieces = nombrePieces;
        this.petitDejeuner = petitDejeuner;
        this.etage = etage;
    }
}