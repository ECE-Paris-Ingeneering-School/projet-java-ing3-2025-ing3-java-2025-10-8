package DAO;
/**
 * Classe principale pour lancer la connexion à la base de données.
 */
public class main {
    /**
     * Entrée de l'application.
     * Initialise la connexion à la base de données.
     *
     * @param args Arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        ConnexionBdd.seConnecter();
    }
}
