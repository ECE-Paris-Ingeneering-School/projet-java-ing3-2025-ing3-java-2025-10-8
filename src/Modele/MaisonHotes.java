package Modele;

import java.math.BigDecimal;
import java.util.List;

/**
 * Représente une maison d'hôtes dans le système de réservation.
 * Une maison d'hôtes est un type d'hébergement qui possède des caractéristiques spécifiques comme le petit-déjeuner
 * et un jardin.
 */
public class MaisonHotes extends Hebergement {

    /**
     * Indique si le petit-déjeuner est proposé dans la maison d'hôtes.
     */
    private boolean petitDejeuner;

    /**
     * Indique si la maison d'hôtes dispose d'un jardin.
     */
    private boolean jardin;

    /**
     * Constructeur de la maison d'hôtes.
     *
     * @param idHebergement L'identifiant de l'hébergement.
     * @param nom Le nom de la maison d'hôtes.
     * @param adresse L'adresse de la maison d'hôtes.
     * @param prixParNuit Le prix par nuit pour cet hébergement.
     * @param description Une description détaillée de la maison d'hôtes.
     * @param specification Des spécifications supplémentaires pour la maison d'hôtes.
     * @param imageUrls Une liste d'URLs des images de la maison d'hôtes.
     * @param petitDejeuner True si le petit-déjeuner est inclus, sinon false.
     * @param jardin True si la maison d'hôtes dispose d'un jardin, sinon false.
     */
    public MaisonHotes(int idHebergement, String nom, String adresse, BigDecimal prixParNuit, String description,
                       String specification, List<String> imageUrls, boolean petitDejeuner, boolean jardin) {
        super(idHebergement, nom, adresse, prixParNuit, description, specification, imageUrls);
        this.petitDejeuner = petitDejeuner;
        this.jardin = jardin;
    }

    // Getters & Setters

    /**
     * Indique si le petit-déjeuner est proposé dans la maison d'hôtes.
     *
     * @return True si le petit-déjeuner est proposé, sinon false.
     */
    public boolean isPetitDejeuner() {
        return petitDejeuner;
    }

    /**
     * Définit si le petit-déjeuner est proposé dans la maison d'hôtes.
     *
     * @param petitDejeuner True si le petit-déjeuner est proposé, sinon false.
     */
    public void setPetitDejeuner(boolean petitDejeuner) {
        this.petitDejeuner = petitDejeuner;
    }

    /**
     * Indique si la maison d'hôtes dispose d'un jardin.
     *
     * @return True si la maison d'hôtes dispose d'un jardin, sinon false.
     */
    public boolean isJardin() {
        return jardin;
    }

    /**
     * Définit si la maison d'hôtes dispose d'un jardin.
     *
     * @param jardin True si la maison d'hôtes dispose d'un jardin, sinon false.
     */
    public void setJardin(boolean jardin) {
        this.jardin = jardin;
    }
}