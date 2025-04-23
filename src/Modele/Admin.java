package Modele;

/**
 * Classe Admin héritant de la classe Utilisateur.
 * Représente un utilisateur administrateur de l'application Booking.
 * Permet la gestion spécifique aux administrateurs (ajout d’hébergements, gestion des réductions, etc.).
 * Pattern : MVC - Modèle.
 * @author [TonNom]
 */
public class Admin extends Utilisateur {

    /**
     * Rôle spécifique de l'administrateur (ex : "gestionnaire", "modérateur"...).
     */
    private String role;

    /**
     * Constructeur de la classe Admin.
     * @param id_utilisateur Identifiant unique de l'utilisateur
     * @param nom Nom de l'administrateur
     * @param prenom Prénom de l'administrateur
     * @param email Adresse email utilisée pour la connexion
     * @param mdp Mot de passe de l'administrateur
     * @param roleSpecifique Type ou rôle spécifique attribué à cet administrateur
     */
    public Admin(int id_utilisateur, String nom, String prenom, String email, String mdp, String roleSpecifique) {
        super(id_utilisateur, nom, prenom, email, mdp); // appel au constructeur de la classe Utilisateur
        this.role = roleSpecifique;
    }

    /**
     * Accesseur pour récupérer le rôle de l'administrateur.
     * @return Le rôle spécifique de l'administrateur
     */
    public String getRole() {
        return role;
    }

    /**
     * Mutateur pour définir ou modifier le rôle de l'administrateur.
     * @param role Nouveau rôle à attribuer
     */
    public void setRole(String role) {
        this.role = role;
    }
}
