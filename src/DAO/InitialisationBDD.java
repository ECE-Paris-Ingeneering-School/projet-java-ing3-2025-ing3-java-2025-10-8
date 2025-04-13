package DAO;

import Modele.Client;
import Modele.Appartement;
import Modele.Hotel;
import Modele.MaisonHotes;
import Modele.Admin;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;



public class InitialisationBDD {

    public static void main(String[] args) {
        System.out.println("Initialisation de la base...");

        // DAO
        HebergementDAO hebergementDAO = new HebergementDAO();
        ClientDAO clientDAO = new ClientDAO();
        AdminDAO adminDAO = new AdminDAO();

        // Clients
        Client client1 = new Client(0, "Martin", "Alice", "alice@gmail.com", "alice123", Client.TypeClient.PARTICULIER);
        Client client2 = new Client(0, "Durand", "Bob", "bob@gmail.com", "bob123", Client.TypeClient.PARTICULIER);
        clientDAO.ajouterClient(client1);
        clientDAO.ajouterClient(client2);

        // Admin
        Admin admin1 = new Admin(0, "Kaufman", "Noa", "noa@gmail.com", "admin123", "ajouter des hebergements");
        Admin admin2 = new Admin(0, "Braida", "Marion", "marion@gmail.com", "admin123", "ajouter des hebergements");
        Admin admin3 = new Admin(0, "Leal", "Clara", "clara@gmail.com", "admin123", "ajouter des hebergements");
        Admin admin4 = new Admin(0, "Lavastre", "Elise", "elise@gmail.com", "admin123", "ajouter des hebergements");
        adminDAO.ajouterAdmin(admin1);
        adminDAO.ajouterAdmin(admin2);
        adminDAO.ajouterAdmin(admin3);
        adminDAO.ajouterAdmin(admin4);

        // Appartement
        Appartement appart = new Appartement(0,
                "Appartement cosy Paris",
                "12 rue Lafayette, Paris",
                new BigDecimal("89.99"),
                "Appartement lumineux avec balcon",
                "appartement",
                2,
                true,
                3
        );
        hebergementDAO.ajouterAppartement(appart);
        int idAppart = getLastInsertedId();
        //ajouterImage(idAppart, "appartement.jpg");

        // Hôtel
        Hotel hotel = new Hotel(0,
                "Hotel Le Majestic",
                "20 avenue Montaigne, Paris",
                new BigDecimal("150.00"),
                "Hôtel 4 étoiles avec spa et petit-déjeuner",
                "hotel",
                4,
                true,
                true,
                true
        );
        hebergementDAO.ajouterHotel(hotel);
        int idHotel = getLastInsertedId();
        //ajouterImage(idHotel, "hotel.jpg");

        // Maison d’hôtes
        MaisonHotes maison = new MaisonHotes(0,
                "Maison de la Plage",
                "35 rue du Littoral, Biarritz",
                new BigDecimal("110.00"),
                "Maison d'hôtes chaleureuse avec jardin",
                "maisonhotes",
                true,
                true
        );
        hebergementDAO.ajouterMaisonHotes(maison);
        int idMaison = getLastInsertedId();
        //ajouterImage(idMaison, "maison.jpg");

        //System.out.println("✅ Base initialisée avec images !");
    }

    // Méthode pour récupérer le dernier id auto-incrémenté inséré
    private static int getLastInsertedId() {
        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement("SELECT LAST_INSERT_ID()")) {
            var rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Méthode pour insérer une image
    /*private static void ajouterImage(int idHebergement, String url) {
        String sql = "INSERT INTO image (id_hebergement, url) VALUES (?, ?)";
        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idHebergement);
            ps.setString(2, url);
            ps.executeUpdate();
            System.out.println("Image ajoutée pour hébergement " + idHebergement + " : " + url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
}
