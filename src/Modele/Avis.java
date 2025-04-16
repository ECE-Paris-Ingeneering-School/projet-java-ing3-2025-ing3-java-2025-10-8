package Modele;

import java.sql.Date;
import java.time.LocalDate;

public class Avis {
    private int idAvis;
    private int idUtilisateur;
    private int idHebergement;
    private int note; // entre 1 et 5
    private String commentaire;
    private Date dateAvis;

    public Avis(int idAvis, int idUtilisateur, int idHebergement, int note, String commentaire, Date dateAvis) {
        this.idAvis = idAvis;
        this.idUtilisateur = idUtilisateur;
        this.idHebergement = idHebergement;
        this.note = note;
        this.commentaire = commentaire;
        this.dateAvis = dateAvis;
    }

    public Avis(int idUtilisateur, int idHebergement, int note, String commentaire) {
        this(0, idUtilisateur, idHebergement, note, commentaire, new Date(System.currentTimeMillis()));
    }

    public Avis(int idAvis, int idUtilisateur, int idHebergement, int note, String commentaire, LocalDate now) {
    }

    // Getters et Setters
    public int getIdAvis() { return idAvis; }
    public void setIdAvis(int idAvis) { this.idAvis = idAvis; }

    public int getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(int idUtilisateur) { this.idUtilisateur = idUtilisateur; }

    public int getIdHebergement() { return idHebergement; }
    public void setIdHebergement(int idHebergement) { this.idHebergement = idHebergement; }

    public int getNote() { return note; }
    public void setNote(int note) { this.note = note; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public Date getDateAvis() { return dateAvis; }
    public void setDateAvis(Date dateAvis) { this.dateAvis = dateAvis; }
}
