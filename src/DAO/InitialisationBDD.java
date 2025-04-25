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
import java.util.List;

/**
 * Classe permettant l'initialisation de la base de données avec des données de test.
 * Elle ajoute des clients, des administrateurs et des hébergements avec images.
 */
public class InitialisationBDD {

    /**
     * Point d'entrée principal qui initialise la base de données avec des données par défaut.
     * @param args arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        System.out.println("Initialisation de la base...");

        HebergementDAO hebergementDAO = new HebergementDAO();
        ClientDAO clientDAO = new ClientDAO();
        AdminDAO adminDAO = new AdminDAO();

        // Clients
        clientDAO.ajouterClient(new Client(0, "Martin", "Alice", "alice@gmail.com", "alice123", Client.TypeClient.NOUVEAU));
        clientDAO.ajouterClient(new Client(0, "Durand", "Bob", "bob@gmail.com", "bob123", Client.TypeClient.NOUVEAU));

        // Admins
        adminDAO.ajouterAdmin(new Admin(0, "Kaufman", "Noa", "noa@gmail.com", "admin123", "ajouter des hebergements"));
        adminDAO.ajouterAdmin(new Admin(0, "Braida", "Marion", "marion@gmail.com", "admin123", "ajouter des hebergements"));
        adminDAO.ajouterAdmin(new Admin(0, "Leal", "Clara", "clara@gmail.com", "admin123", "ajouter des hebergements"));
        adminDAO.ajouterAdmin(new Admin(0, "Lavastre", "Elise", "elise@gmail.com", "admin123", "ajouter des hebergements"));

        // Appartements
        hebergementDAO.ajouterAppartement(new Appartement(0, "Studio Chic", "1 rue de Rome, Paris", new BigDecimal("75.00"), "Studio moderne et pratique", "appartement", List.of("images/a1/1.jpg", "images/a1/2.jpg", "images/a1/3.jpg"), 1, true, 2));
        hebergementDAO.ajouterAppartement(new Appartement(0, "Appartement Vue Seine", "3 quai Voltaire, Paris", new BigDecimal("95.00"), "Vue exceptionnelle sur la Seine", "appartement", List.of("images/a2/1.jpg", "images/a2/2.jpg", "images/a2/3.jpg"), 2, false, 5));
        hebergementDAO.ajouterAppartement(new Appartement(0, "Loft Industriel", "10 rue Oberkampf, Paris", new BigDecimal("120.00"), "Loft tendance au cœur du Marais", "appartement", List.of("images/a3/1.jpg", "images/a3/2.jpg", "images/a3/3.jpg"), 3, true, 1));
        hebergementDAO.ajouterAppartement(new Appartement(0, "Appartement Design", "22 avenue Victor Hugo, Paris", new BigDecimal("105.00"), "Design contemporain et lumineux", "appartement", List.of("images/a4/1.jpg", "images/a4/2.jpg", "images/a4/3.jpg"), 2, false, 4));
        hebergementDAO.ajouterAppartement(new Appartement(0, "Appartement Cosy Paris", "12 rue Lafayette, Paris", new BigDecimal("89.99"), "Appartement lumineux avec balcon", "appartement", List.of("images/a5/1.jpg", "images/a5/2.jpg", "images/a5/3.jpg"), 2, true, 3));

        // Hôtels
        hebergementDAO.ajouterHotel(new Hotel(0, "Hotel Le Majestic", "20 avenue Montaigne, Paris", new BigDecimal("150.00"), "Hôtel 4 étoiles avec spa et petit-déjeuner", "hotel", List.of("images/h1/1.jpg", "images/h1/2.jpg", "images/h1/3.jpg"), 4, true, true, true));
        hebergementDAO.ajouterHotel(new Hotel(0, "Hotel Bleu Ciel", "5 place de la République, Paris", new BigDecimal("110.00"), "Chambres modernes et restaurant gastronomique", "hotel", List.of("images/h2/1.jpg", "images/h2/2.jpg", "images/h2/3.jpg"), 3, true, false, false));
        hebergementDAO.ajouterHotel(new Hotel(0, "Grand Hotel Central", "45 boulevard Haussmann, Paris", new BigDecimal("135.00"), "Au centre de Paris avec centre de remise en forme", "hotel", List.of("images/h3/1.jpg", "images/h3/2.jpg", "images/h3/3.jpg"), 4, true, false, true));
        hebergementDAO.ajouterHotel(new Hotel(0, "Hôtel de la Paix", "8 rue Cler, Paris", new BigDecimal("98.00"), "Ambiance calme proche Tour Eiffel", "hotel", List.of("images/h4/1.jpg", "images/h4/2.jpg", "images/h4/3.jpg"), 2, false, false, false));
        hebergementDAO.ajouterHotel(new Hotel(0, "Hôtel Panorama", "2 rue des Martyrs, Paris", new BigDecimal("125.00"), "Vue panoramique sur tout Paris", "hotel", List.of("images/h5/1.jpg", "images/h5/2.jpg", "images/h5/3.jpg"), 3, true, true, false));

        // Maisons d’hôtes
        hebergementDAO.ajouterMaisonHotes(new MaisonHotes(0, "Maison de la Plage", "35 rue du Littoral, Biarritz", new BigDecimal("110.00"), "Maison d'hôtes chaleureuse avec jardin", "maisonhotes", List.of("images/m1/1.jpg", "images/m1/2.jpg", "images/m1/3.jpg"), true, true));
        hebergementDAO.ajouterMaisonHotes(new MaisonHotes(0, "Villa Lavande", "18 chemin des Fleurs, Provence", new BigDecimal("130.00"), "Calme et nature en plein cœur de la Provence", "maisonhotes", List.of("images/m2/1.jpg", "images/m2/2.jpg", "images/m2/3.jpg"), true, true));
        hebergementDAO.ajouterMaisonHotes(new MaisonHotes(0, "Maison Bord de Mer", "7 promenade des Anglais, Nice", new BigDecimal("145.00"), "Maison d'hôtes en bord de mer avec terrasse", "maisonhotes", List.of("images/m3/1.jpg", "images/m3/2.jpg", "images/m3/3.jpg"), false, true));
        hebergementDAO.ajouterMaisonHotes(new MaisonHotes(0, "Maison des Arts", "11 rue des Beaux-Arts, Lyon", new BigDecimal("90.00"), "Ambiance artistique et conviviale", "maisonhotes", List.of("images/m4/1.jpg", "images/m4/2.jpg", "images/m4/3.jpg"), true, false));
        hebergementDAO.ajouterMaisonHotes(new MaisonHotes(0, "Maison Campagne", "2 chemin du Bois, Toulouse", new BigDecimal("85.00"), "Nature et tranquillité à la campagne", "maisonhotes", List.of("images/m5/1.jpg", "images/m5/2.jpg", "images/m5/3.jpg"), false, true));

        System.out.println("\u2705 Base initialisée avec 15 hébergements (images incluses) !");
    }

    /**
     * Récupère l'identifiant de la dernière entrée insérée dans la base de données.
     * @return ID de la dernière entrée ou -1 en cas d'erreur.
     */
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

    /**
     * Ajoute une image liée à un hébergement donné dans la table image.
     * @param idHebergement ID de l'hébergement.
     * @param url URL de l'image.
     */
    private static void ajouterImage(int idHebergement, String url) {
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
    }
}