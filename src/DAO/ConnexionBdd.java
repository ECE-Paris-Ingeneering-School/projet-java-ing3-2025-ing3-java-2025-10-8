package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe qui gère la connexion à la base de données.
 */
public class ConnexionBdd {
    private static final String URL = "jdbc:mysql://localhost:3306/bookingjava"; // Connexion a la bdd
    private static final String UTILISATEUR = "root";
    private static final String MOT_DE_PASSE = "";

    /**
     * Établit une connexion à la base de données.
     *
     * @return Un objet Connection si la connexion est ok, sinon null.
     */
    public static Connection seConnecter() {
        try {
            Connection connexion = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
            System.out.println("Connexion OKAY");
            return connexion;
        } catch (SQLException e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
            return null;
        }
    }
}
