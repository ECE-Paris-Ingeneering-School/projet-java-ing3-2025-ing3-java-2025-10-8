package Modele;

import java.math.BigDecimal;

public class Appartement extends Hebergement {
    private int nombrePieces;
    private boolean petitDejeuner;
    private int etage;

    public Appartement(int idHebergement, String nom, String adresse, BigDecimal prixParNuit,
                       String description, String specification, String imageUrl,
                       int nombrePieces, boolean petitDejeuner, int etage) {
        super(idHebergement, nom, adresse, prixParNuit, description, specification, imageUrl);
        this.nombrePieces = nombrePieces;
        this.petitDejeuner = petitDejeuner;
        this.etage = etage;
    }

    // Getters & Setters
    public int getNombrePieces() {
        return nombrePieces;
    }

    public void setNombrePieces(int nombrePieces) {
        this.nombrePieces = nombrePieces;
    }

    public boolean isPetitDejeuner() {
        return petitDejeuner;
    }

    public void setPetitDejeuner(boolean petitDejeuner) {
        this.petitDejeuner = petitDejeuner;
    }

    public int getEtage() {
        return etage;
    }

    public void setEtage(int etage) {
        this.etage = etage;
    }
}