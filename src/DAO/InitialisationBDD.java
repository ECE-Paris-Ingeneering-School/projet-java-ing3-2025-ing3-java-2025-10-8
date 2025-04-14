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

        HebergementDAO hebergementDAO = new HebergementDAO();
        ClientDAO clientDAO = new ClientDAO();
        AdminDAO adminDAO = new AdminDAO();

        // Clients
        clientDAO.ajouterClient(new Client(0, "Martin", "Alice", "alice@gmail.com", "alice123", Client.TypeClient.PARTICULIER));
        clientDAO.ajouterClient(new Client(0, "Durand", "Bob", "bob@gmail.com", "bob123", Client.TypeClient.PARTICULIER));

        // Admins
        adminDAO.ajouterAdmin(new Admin(0, "Kaufman", "Noa", "noa@gmail.com", "admin123", "ajouter des hebergements"));
        adminDAO.ajouterAdmin(new Admin(0, "Braida", "Marion", "marion@gmail.com", "admin123", "ajouter des hebergements"));
        adminDAO.ajouterAdmin(new Admin(0, "Leal", "Clara", "clara@gmail.com", "admin123", "ajouter des hebergements"));
        adminDAO.ajouterAdmin(new Admin(0, "Lavastre", "Elise", "elise@gmail.com", "admin123", "ajouter des hebergements"));

        // Appartements
        hebergementDAO.ajouterAppartement(new Appartement(0, "Studio Chic", "1 rue de Rome, Paris", new BigDecimal("75.00"), "Studio moderne et pratique", "appartement", "studio.jpg", 1, true, 2));
        hebergementDAO.ajouterAppartement(new Appartement(0, "Appartement Vue Seine", "3 quai Voltaire, Paris", new BigDecimal("95.00"), "Vue exceptionnelle sur la Seine", "appartement", "seine.jpg", 2, false, 5));
        hebergementDAO.ajouterAppartement(new Appartement(0, "Loft Industriel", "10 rue Oberkampf, Paris", new BigDecimal("120.00"), "Loft tendance au cœur du Marais", "appartement", "loft.jpg", 3, true, 1));
        hebergementDAO.ajouterAppartement(new Appartement(0, "Appartement Design", "22 avenue Victor Hugo, Paris", new BigDecimal("105.00"), "Design contemporain et lumineux", "appartement", "design.jpg", 2, false, 4));
        hebergementDAO.ajouterAppartement(new Appartement(0, "Appartement Cosy Paris", "12 rue Lafayette, Paris", new BigDecimal("89.99"), "Appartement lumineux avec balcon", "appartement", "appartement.jpg", 2, true, 3));

        // Hôtels
        hebergementDAO.ajouterHotel(new Hotel(0, "Hotel Le Majestic", "20 avenue Montaigne, Paris", new BigDecimal("150.00"), "Hôtel 4 étoiles avec spa et petit-déjeuner", "hotel", "majestic.jpg", 4, true, true, true));
        hebergementDAO.ajouterHotel(new Hotel(0, "Hotel Bleu Ciel", "5 place de la République, Paris", new BigDecimal("110.00"), "Chambres modernes et restaurant gastronomique", "hotel", "bleuciel.jpg", 3, true, false, false));
        hebergementDAO.ajouterHotel(new Hotel(0, "Grand Hotel Central", "45 boulevard Haussmann, Paris", new BigDecimal("135.00"), "Au centre de Paris avec centre de remise en forme", "hotel", "central.jpg", 4, true, false, true));
        hebergementDAO.ajouterHotel(new Hotel(0, "Hôtel de la Paix", "8 rue Cler, Paris", new BigDecimal("98.00"), "Ambiance calme proche Tour Eiffel", "hotel", "paix.jpg", 2, false, false, false));
        hebergementDAO.ajouterHotel(new Hotel(0, "Hôtel Panorama", "2 rue des Martyrs, Paris", new BigDecimal("125.00"), "Vue panoramique sur tout Paris", "hotel", "panorama.jpg", 3, true, true, false));

        // Maisons d’hôtes
        hebergementDAO.ajouterMaisonHotes(new MaisonHotes(0, "Maison de la Plage", "35 rue du Littoral, Biarritz", new BigDecimal("110.00"), "Maison d'hôtes chaleureuse avec jardin", "maisonhotes", "maison.jpg", true, true));
        hebergementDAO.ajouterMaisonHotes(new MaisonHotes(0, "Villa Lavande", "18 chemin des Fleurs, Provence", new BigDecimal("130.00"), "Calme et nature en plein cœur de la Provence", "maisonhotes", "lavande.jpg", true, true));
        hebergementDAO.ajouterMaisonHotes(new MaisonHotes(0, "Maison Bord de Mer", "7 promenade des Anglais, Nice", new BigDecimal("145.00"), "Maison d'hôtes en bord de mer avec terrasse", "maisonhotes", "mer.jpg", false, true));
        hebergementDAO.ajouterMaisonHotes(new MaisonHotes(0, "Maison des Arts", "11 rue des Beaux-Arts, Lyon", new BigDecimal("90.00"), "Ambiance artistique et conviviale", "maisonhotes", "arts.jpg", true, false));
        hebergementDAO.ajouterMaisonHotes(new MaisonHotes(0, "Maison Campagne", "2 chemin du Bois, Toulouse", new BigDecimal("85.00"), "Nature et tranquillité à la campagne", "maisonhotes", "campagne.jpg", false, true));

        System.out.println("\u2705 Base initialisée avec 15 hébergements (images incluses) !");
    }

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