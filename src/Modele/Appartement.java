package Modele;

import java.math.BigDecimal;
import java.util.List;

/**
 * Représente un appartement dans le système de réservation.
 * Un appartement est un type d'hébergement qui a des caractéristiques spécifiques comme le nombre de pièces, le petit-déjeuner et l'étage.
 */
public class Appartement extends Hebergement {

    /**
     * Nombre de pièces dans l'appartement.
     */
    private int nombrePieces;

    /**
     * Indique si le petit-déjeuner est proposé.
     */
    private boolean petitDejeuner;

    /**
     * L'étage de l'appartement.
     */
    private int etage;

    /**
     * Constructeur de l'appartement.
     *
     * @param idHebergement L'identifiant de l'hébergement.
     * @param nom Le nom de l'appartement.
     * @param adresse L'adresse de l'appartement.
     * @param prixParNuit Le prix par nuit pour cet appartement.
     * @param description Une description détaillée de l'appartement.
     * @param specification Des spécifications supplémentaires pour l'appartement.
     * @param imageUrls Une liste d'URLs des images de l'appartement.
     * @param nombrePieces Le nombre de pièces dans l'appartement.
     * @param petitDejeuner True si le petit-déjeuner est inclus, false sinon.
     * @param etage L'étage de l'appartement.
     */
    public Appartement(int idHebergement, String nom, String adresse, BigDecimal prixParNuit,
                       String description, String specification, List<String> imageUrls,
                       int nombrePieces, boolean petitDejeuner, int etage) {
        super(idHebergement, nom, adresse, prixParNuit, description, specification, imageUrls);
        this.nombrePieces = nombrePieces;
        this.petitDejeuner = petitDejeuner;
        this.etage = etage;
    }

    // Getters & Setters

    /**
     * Retourne le nombre de pièces dans l'appartement.
     *
     * @return Le nombre de pièces.
     */
    public int getNombrePieces() {
        return nombrePieces;
    }

    /**
     * Définit le nombre de pièces dans l'appartement.
     *
     * @param nombrePieces Le nombre de pièces.
     */
    public void setNombrePieces(int nombrePieces) {
        this.nombrePieces = nombrePieces;
    }

    /**
     * Indique si le petit-déjeuner est proposé.
     *
     * @return True si le petit-déjeuner est proposé, sinon false.
     */
    public boolean isPetitDejeuner() {
        return petitDejeuner;
    }

    /**
     * Définit si le petit-déjeuner est proposé.
     *
     * @param petitDejeuner True si le petit-déjeuner est proposé, sinon false.
     */
    public void setPetitDejeuner(boolean petitDejeuner) {
        this.petitDejeuner = petitDejeuner;
    }

    /**
     * Retourne l'étage de l'appartement.
     *
     * @return L'étage de l'appartement.
     */
    public int getEtage() {
        return etage;
    }

    /**
     * Définit l'étage de l'appartement.
     *
     * @param etage L'étage de l'appartement.
     */
    public void setEtage(int etage) {
        this.etage = etage;
    }
}
