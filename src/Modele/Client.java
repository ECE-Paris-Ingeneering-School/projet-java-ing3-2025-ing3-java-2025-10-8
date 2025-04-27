package Modele;

/**
 * Classe Modèle représentant un client de l'application Booking.
 * Hérite de la classe Utilisateur et ajoute une typologie de client (Particulier ou Entreprise).
 * Pattern : MVC - Modèle.
 * Utilisée pour les opérations métier et les interactions avec la base via ClientDAO.
 * @see DAO.ClientDAO
 * @see Modele.Utilisateur
 */
public class Client extends Utilisateur {

    /**
     * Enumération des types de clients possibles.
     * PARTICULIER : utilisateur classique
     * ENTREPRISE : client entreprise pouvant avoir des offres spécifiques
     */
    public enum TypeClient {
        NOUVEAU,
        ANCIEN,
    }

    /** Type du client (Particulier ou Entreprise) */
    private TypeClient typeClient;

    /**
     * Constructeur complet de la classe Client.
     * @param id_utilisateur Identifiant unique du client (hérité de Utilisateur)
     * @param nom Nom du client
     * @param prenom Prénom du client
     * @param email Adresse email (utilisée pour la connexion)
     * @param mdp Mot de passe
     * @param typeClient Type du client (Particulier ou Entreprise)
     */
    public Client(int id_utilisateur, String nom, String prenom, String email, String mdp, TypeClient typeClient) {
        super(id_utilisateur, nom, prenom, email, mdp);
        this.typeClient = typeClient;
    }

    /**
     * Accesseur du type de client.
     * @return Le type de client (PARTICULIER ou ENTREPRISE)
     */
    public TypeClient getTypeClient() {
        return typeClient;
    }

    /**
     * Mutateur du type de client.
     * @param typeClient Nouveau type à attribuer (PARTICULIER ou ENTREPRISE)
     */
    public void setTypeClient(TypeClient typeClient) {
        this.typeClient = typeClient;
    }
}
