package Modele;

import java.sql.Date;

public class OffreReduction {

    private int idOffre;
    private int idUtilisateur;
    private String description;
    private double pourcentageReduction;
    private Date dateDebut;
    private Date dateFin;

    // Constructeurs

    public OffreReduction(int idUtilisateur, String description, double pourcentageReduction, Date dateDebut, Date dateFin) {
        this.idUtilisateur = idUtilisateur;
        this.description = description;
        this.pourcentageReduction = pourcentageReduction;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public OffreReduction() {
    }


    // Getters et Setters

    public int getIdOffre() {
        return idOffre;
    }

    public void setIdOffre(int idOffre) {
        this.idOffre = idOffre;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPourcentageReduction() {
        return pourcentageReduction;
    }

    public void setPourcentageReduction(double pourcentageReduction) {
        this.pourcentageReduction = pourcentageReduction;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

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
