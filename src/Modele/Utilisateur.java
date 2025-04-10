package Modele;

public class Utilisateur {
    protected int id_utilisateur;
    protected String nom;
    protected String prenom;
    protected String email;
    protected String mdp;

    public Utilisateur() {

    }
    //constructeur
    public Utilisateur(int id_utilisateur, String nom, String prenom, String email, String mdp) {
        this.id_utilisateur = id_utilisateur;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
    }

    //getter
    public int getId_utilisateur() {
        return id_utilisateur;
    }
    public String getNom(){
        return nom;
    }
    public String getPrenom(){
        return prenom;
    }
    public String getEmail(){
        return email;
    }
    public String getMdp(){
        return mdp;
    }

    //setter
    public void setId_utilisateur(int id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setMdp(String mdp) {
        this.mdp = mdp;
    }
}