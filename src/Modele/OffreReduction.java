package Modele;

import java.sql.Date;

/**
 * Représente une offre de réduction appliquée à un utilisateur.
 * Une offre contient un pourcentage de réduction ainsi qu'une période de validité.
 */
public class OffreReduction {

    private int idOffre;
    private int idUtilisateur;
    private String description;
    private double pourcentageReduction;
    private Date dateDebut;
    private Date dateFin;
    /**
     * Crée une offre de réduction avec les infos principales.
     *
     * @param idUtilisateur L'identifiant de l'utilisateur lié à l'offre.
     * @param description Le détail de l'offre.
     * @param pourcentageReduction Le pourcentage de réduction appliqué.
     * @param dateDebut La date de début de validité de l'offre.
     * @param dateFin La date de fin de validité de l'offre.
     */
    public OffreReduction(int idUtilisateur, String description, double pourcentageReduction, Date dateDebut, Date dateFin) {
        this.idUtilisateur = idUtilisateur;
        this.description = description;
        this.pourcentageReduction = pourcentageReduction;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }
    /**
     * Constructeur sans paramètre.
     * Pour les remplissages manuel des attributs.
     */
    public OffreReduction() {
    }
    /**
     * Retourne l'identifiant de l'offre.
     *
     * @return L'id de l'offre.
     */
    public int getIdOffre() {
        return idOffre;
    }
    /**
     * Définit l'identifiant de l'offre.
     *
     * @param idOffre L'id à affecter.
     */
    public void setIdOffre(int idOffre) {
        this.idOffre = idOffre;
    }
    /**
     * Retourne l'identifiant de l'utilisateur concerné par l'offre.
     *
     * @return L'id de l'utilisateur.
     */
    public int getIdUtilisateur() {
        return idUtilisateur;
    }
    /**
     * Définit l'identifiant de l'utilisateur.
     *
     * @param idUtilisateur L'id de l'utilisateur à affecter.
     */
    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }
    /**
     * Retourne la description de l'offre.
     *
     * @return Le texte de description.
     */
    public String getDescription() {
        return description;
    }
    /**
     * Met à jour la description de l'offre.
     *
     * @param description Le nouveau texte de description.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * Retourne le pourcentage de réduction proposé.
     *
     * @return Le pourcentage sous forme décimale.
     */
    public double getPourcentageReduction() {
        return pourcentageReduction;
    }
    /**
     * Modifie le pourcentage de réduction.
     *
     * @param pourcentageReduction Le nouveau pourcentage.
     */
    public void setPourcentageReduction(double pourcentageReduction) {
        this.pourcentageReduction = pourcentageReduction;
    }
    /**
     * Retourne la date de début de validité de l'offre.
     *
     * @return La date de début.
     */
    public Date getDateDebut() {
        return dateDebut;
    }
    /**
     * Modifie la date de début de validité.
     *
     * @param dateDebut La nouvelle date de début.
     */
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }
    /**
     * Retourne la date de fin de validité de l'offre.
     *
     * @return La date de fin.
     */
    public Date getDateFin() {
        return dateFin;
    }
    /**
     * Modifie la date de fin de validité de l'offre.
     *
     * @param dateFin La nouvelle date de fin.
     */
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }
    /**
     * Retourne une représentation textuelle de l'offre.
     *
     * @return Les informations principales de l'offre sous forme de texte.
     */
    @Override
    public String toString() {
        return "OffreReduction{" +
                "idOffre=" + idOffre +
                ", idUtilisateur=" + idUtilisateur +
                ", description='" + description + '\'' +
                ", pourcentageReduction=" + pourcentageReduction +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                '}';
    }
}
