package Modele;

import java.math.BigDecimal;
import java.util.List;

/**
 * Représente un hôtel dans le système de réservation.
 * Un hôtel est un type d'hébergement qui possède des caractéristiques spécifiques comme le nombre d'étoiles,
 * la disponibilité du petit-déjeuner, de la piscine et du spa.
 */
public class Hotel extends Hebergement {

    /**
     * Le nombre d'étoiles de l'hôtel.
     */
    private int nombreEtoiles;

    /**
     * Indique si le petit-déjeuner est proposé dans l'hôtel.
     */
    private boolean petitDejeuner;

    /**
     * Indique si l'hôtel dispose d'une piscine.
     */
    private boolean piscine;

    /**
     * Indique si l'hôtel dispose d'un spa.
     */
    private boolean spa;

    /**
     * Constructeur de l'hôtel.
     *
     * @param idHebergement L'identifiant de l'hébergement.
     * @param nom Le nom de l'hôtel.
     * @param adresse L'adresse de l'hôtel.
     * @param prixParNuit Le prix par nuit pour cet hôtel.
     * @param description Une description détaillée de l'hôtel.
     * @param specification Des spécifications supplémentaires pour l'hôtel.
     * @param imageUrls Une liste d'URLs des images de l'hôtel.
     * @param nombreEtoiles Le nombre d'étoiles de l'hôtel.
     * @param petitDejeuner True si le petit-déjeuner est inclus, sinon false.
     * @param piscine True si l'hôtel dispose d'une piscine, sinon false.
     * @param spa True si l'hôtel dispose d'un spa, sinon false.
     */
    public Hotel(int idHebergement, String nom, String adresse, BigDecimal prixParNuit,
                 String description, String specification, List<String> imageUrls,
                 int nombreEtoiles, boolean petitDejeuner, boolean piscine, boolean spa) {
        super(idHebergement, nom, adresse, prixParNuit, description, specification, imageUrls);
        this.nombreEtoiles = nombreEtoiles;
        this.petitDejeuner = petitDejeuner;
        this.piscine = piscine;
        this.spa = spa;
    }


    /**
     * Retourne le nombre d'étoiles de l'hôtel.
     *
     * @return Le nombre d'étoiles de l'hôtel.
     */
    public int getNombreEtoiles() {
        return nombreEtoiles;
    }

    /**
     * Définit le nombre d'étoiles de l'hôtel.
     *
     * @param nombreEtoiles Le nombre d'étoiles de l'hôtel.
     */
    public void setNombreEtoiles(int nombreEtoiles) {
        this.nombreEtoiles = nombreEtoiles;
    }

    /**
     * Indique si le petit-déjeuner est proposé dans l'hôtel.
     *
     * @return True si le petit-déjeuner est proposé, sinon false.
     */
    public boolean isPetitDejeuner() {
        return petitDejeuner;
    }

    /**
     * Définit si le petit-déjeuner est proposé dans l'hôtel.
     *
     * @param petitDejeuner True si le petit-déjeuner est proposé, sinon false.
     */
    public void setPetitDejeuner(boolean petitDejeuner) {
        this.petitDejeuner = petitDejeuner;
    }

    /**
     * Indique si l'hôtel dispose d'une piscine.
     *
     * @return True si l'hôtel dispose d'une piscine, sinon false.
     */
    public boolean isPiscine() {
        return piscine;
    }

    /**
     * Définit si l'hôtel dispose d'une piscine.
     *
     * @param piscine True si l'hôtel dispose d'une piscine, sinon false.
     */
    public void setPiscine(boolean piscine) {
        this.piscine = piscine;
    }

    /**
     * Indique si l'hôtel dispose d'un spa.
     *
     * @return True si l'hôtel dispose d'un spa, sinon false.
     */
    public boolean isSpa() {
        return spa;
    }

    /**
     * Définit si l'hôtel dispose d'un spa.
     *
     * @param spa True si l'hôtel dispose d'un spa, sinon false.
     */
    public void setSpa(boolean spa) {
        this.spa = spa;
    }
}