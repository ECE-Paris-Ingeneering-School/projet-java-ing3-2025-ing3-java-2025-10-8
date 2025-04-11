package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionBdd {
    private static final String URL = "jdbc:mysql://localhost:3306/bookingjava";//connexion a la bdd
    private static final String UTILISATEUR = "root";
    private static final String MOT_DE_PASSE = "";

    //methode connexion à la bdd
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
