package Modele;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Classe Modèle représentant un avis utilisateur sur un hébergement.
 * Contient les données liées à un avis : note, commentaire, date, auteur et hébergement visé.
 * Utilisée dans la logique métier et la couche DAO (base de données).
 * Pattern : MVC - Modèle
 */
public class Avis {
    /** Identifiant unique de l'avis (clé primaire) */
    private int idAvis;

    /** Identifiant de l'utilisateur ayant déposé l'avis (clé étrangère vers la table utilisateur) */
    private int idUtilisateur;

    /** Identifiant de l'hébergement concerné par l'avis (clé étrangère vers la table hebergement) */
    private int idHebergement;

    /** Note attribuée à l'hébergement, sur une échelle de 1 à 5 */
    private int note;

    /** Commentaire rédigé par l'utilisateur accompagnant la note */
    private String commentaire;

    /** Date de dépôt de l'avis */
    private Date dateAvis;

    /**
     * Constructeur principal de la classe Avis.
     * @param idAvis Identifiant unique de l'avis
     * @param idUtilisateur Identifiant de l'utilisateur ayant posté l'avis
     * @param idHebergement Identifiant de l'hébergement évalué
     * @param note Note sur 5 attribuée
     * @param commentaire Texte du commentaire associé à l'avis
     * @param dateAvis Date du dépôt de l'avis
     */
    public Avis(int idAvis, int idUtilisateur, int idHebergement, int note, String commentaire, Date dateAvis) {
        this.idAvis = idAvis;
        this.idUtilisateur = idUtilisateur;
        this.idHebergement = idHebergement;
        this.note = note;
        this.commentaire = commentaire;
        this.dateAvis = dateAvis;
    }
    /**
     * Constructeur simplifié sans ID (utilisé avant insertion en BDD).
     * La date d’avis est automatiquement mise à aujourd’hui.
     * @param idUtilisateur ID de l'utilisateur
     * @param idHebergement ID de l'hébergement
     * @param note Note attribuée
     * @param commentaire Commentaire de l'avis
     */
    public Avis(int idUtilisateur, int idHebergement, int note, String commentaire) {
        this(0, idUtilisateur, idHebergement, note, commentaire, new Date(System.currentTimeMillis()));
    }

    // Getters et Setters

    /** @return l'identifiant unique de l'avis */
    public int getIdAvis() { return idAvis; }

    /** @param idAvis Définit l'identifiant unique de l'avis */
    public void setIdAvis(int idAvis) { this.idAvis = idAvis; }

    /** @return l'identifiant de l'utilisateur ayant posté l'avis */
    public int getIdUtilisateur() { return idUtilisateur; }

    /** @param idUtilisateur Définit l'identifiant de l'utilisateur */
    public void setIdUtilisateur(int idUtilisateur) { this.idUtilisateur = idUtilisateur; }

    /** @return l'identifiant de l'hébergement concerné par l'avis */
    public int getIdHebergement() { return idHebergement; }

    /** @param idHebergement Définit l'identifiant de l'hébergement visé */
    public void setIdHebergement(int idHebergement) { this.idHebergement = idHebergement; }

    /** @return la note attribuée */
    public int getNote() { return note; }

    /** @param note Définit la note attribuée (de 1 à 5) */
    public void setNote(int note) { this.note = note; }

    /** @return le commentaire associé à l'avis */
    public String getCommentaire() { return commentaire; }

    /** @param commentaire Définit le contenu du commentaire */
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    /** @return la date de l'avis */
    public Date getDateAvis() { return dateAvis; }

    /** @param dateAvis Définit la date à laquelle l'avis a été posté */
    public void setDateAvis(Date dateAvis) { this.dateAvis = dateAvis; }
}

