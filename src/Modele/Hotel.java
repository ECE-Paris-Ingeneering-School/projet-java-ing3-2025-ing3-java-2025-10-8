package Modele;

import java.math.BigDecimal;

public class Hotel extends Hebergement {
    private int nombreEtoiles;
    private boolean petitDejeuner;
    private boolean piscine;
    private boolean spa;

    public Hotel(int idHebergement, String nom, String adresse, BigDecimal prixParNuit,
                 String description, String specification, String imageUrl,
                 int nombreEtoiles, boolean petitDejeuner, boolean piscine, boolean spa) {
        super(idHebergement, nom, adresse, prixParNuit, description, specification, imageUrl);
        this.nombreEtoiles = nombreEtoiles;
        this.petitDejeuner = petitDejeuner;
        this.piscine = piscine;
        this.spa = spa;
    }

    // Getters & Setters
    public int getNombreEtoiles() {
        return nombreEtoiles;
    }

    public void setNombreEtoiles(int nombreEtoiles) {
        this.nombreEtoiles = nombreEtoiles;
    }

    public boolean isPetitDejeuner() {
        return petitDejeuner;
    }

    public void setPetitDejeuner(boolean petitDejeuner) {
        this.petitDejeuner = petitDejeuner;
    }

    public boolean isPiscine() {
        return piscine;
    }

    public void setPiscine(boolean piscine) {
        this.piscine = piscine;
    }

    public boolean isSpa() {
        return spa;
    }

    public void setSpa(boolean spa) {
        this.spa = spa;
    }
}
