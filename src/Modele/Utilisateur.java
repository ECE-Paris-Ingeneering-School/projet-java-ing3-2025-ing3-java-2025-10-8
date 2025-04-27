package Modele;

/**
 * Classe représentant un utilisateur
 * Un utilisateur possède un identifiant, un nom, un prénom, un email et un mot de passe
 */
public class Utilisateur {
    /** Identifiant unique de l'utilisateur */
    protected int id_utilisateur;
    /** Nom de l'utilisateur */
    protected String nom;
    /** Prénom de l'utilisateur */
    protected String prenom;
    /** Adresse email de l'utilisateur */
    protected String email;
    /** Mot de passe de l'utilisateur */
    protected String mdp;

    /**
     * Constructeur pour créer un nouvel utilisateur
     *
     * @param id_utilisateur Identifiant de l'utilisateur
     * @param nom Nom de l'utilisateur
     * @param prenom Prénom de l'utilisateur
     * @param email Adresse email de l'utilisateur
     * @param mdp Mot de passe de l'utilisateur
     */
    public Utilisateur(int id_utilisateur, String nom, String prenom, String email, String mdp) {
        this.id_utilisateur = id_utilisateur;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
    }

    //getter
    /**
     * Retourne l'identifiant de l'utilisateur
     *
     * @return Identifiant de l'utilisateur
     */
    public int getIdUtilisateur() {
        return id_utilisateur;
    }

    /**
     * Retourne le nom de l'utilisateur
     *
     * @return Nom de l'utilisateur
     */
    public String getNom(){
        return nom;
    }

    /**
     * Retourne le prénom de l'utilisateur
     *
     * @return Prénom de l'utilisateur
     */
    public String getPrenom(){
        return prenom;
    }

    /**
     * Retourne l'adresse email de l'utilisateur
     *
     * @return Adresse email de l'utilisateur
     */
    public String getEmail(){
        return email;
    }

    /**
     * Retourne le mot de passe de l'utilisateur
     *
     * @return Mot de passe de l'utilisateur
     */
    public String getMdp(){
        return mdp;
    }

    //setter
    /**
     * Définit l'identifiant de l'utilisateur
     *
     * @param id_utilisateur Nouvel identifiant de l'utilisateur
     */
    public void setIdUtilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    /**
     * Définit le nom de l'utilisateur
     *
     * @param nom Nouveau nom de l'utilisateur
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Définit le prénom de l'utilisateur
     *
     * @param prenom Nouveau prénom de l'utilisateur
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Définit l'adresse email de l'utilisateur
     *
     * @param email Nouvelle adresse email de l'utilisateur
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Définit le mot de passe de l'utilisateur
     *
     * @param mdp Nouveau mot de passe de l'utilisateur
     */
    public void setMdp(String mdp) {
        this.mdp = mdp;
    }
}