package Vue;

import DAO.HebergementDAO;
import Modele.Appartement;
import Modele.Hotel;
import Modele.MaisonHotes;
import Modele.Hebergement;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class VueConsole {

    private static final DecimalFormat prixFormat = new DecimalFormat("#0.00");

    public void afficherHebergement(Hebergement h) {
        System.out.println("ID : " + h.getIdHebergement());
        System.out.println("Nom : " + h.getNom());
        System.out.println("Adresse : " + h.getAdresse());
        System.out.println("Prix par nuit : " + prixFormat.format(h.getPrixParNuit()) + " €");
        System.out.println("Description : " + h.getDescription());
        System.out.println("Spécification : " + h.getSpecification());

        if (h instanceof Hotel) {
            Hotel hotel = (Hotel) h;
            System.out.println("Type : Hôtel");
            System.out.println("Étoiles : " + hotel.getNombreEtoiles());
            System.out.println("Petit déjeuner : " + (hotel.isPetitDejeuner() ? "Oui" : "Non"));
            System.out.println("Piscine : " + (hotel.isPiscine() ? "Oui" : "Non"));
            System.out.println("Spa : " + (hotel.isSpa() ? "Oui" : "Non"));

        } else if (h instanceof Appartement) {
            Appartement a = (Appartement) h;
            System.out.println("Type : Appartement");
            System.out.println("Nombre de pièces : " + a.getNombrePieces());
            System.out.println("Petit déjeuner : " + (a.isPetitDejeuner() ? "Oui" : "Non"));
            System.out.println("Étage : " + a.getEtage());

        } else if (h instanceof MaisonHotes) {
            MaisonHotes m = (MaisonHotes) h;
            System.out.println("Type : Maison d'hôtes");
            System.out.println("Petit déjeuner : " + (m.isPetitDejeuner() ? "Oui" : "Non"));
            System.out.println("Jardin : " + (m.isJardin() ? "Oui" : "Non"));
        }

        System.out.println("--------------------------------------------");
    }

    public static void main(String[] args) {
        HebergementDAO dao = new HebergementDAO();
        VueConsole vue = new VueConsole();

        // ✅ INSERT : un nouvel hôtel
        Hotel newHotel = new Hotel(
                0,
                "Hôtel du Soleil",
                "10 Rue des Palmiers",
                new BigDecimal("155.50"),
                "Un hôtel chaleureux près de la plage",
                "Wi-Fi, Climatisation, Petit-déjeuner",
                4,
                true,
                false,
                true
        );
        dao.ajouterHotel(newHotel);
        System.out.println("✅ Hôtel inséré");

        // ✅ INSERT : un nouvel appartement
        Appartement newAppart = new Appartement(
                0,
                "Appartement Vue Mer",
                "3 Avenue des Sables",
                new BigDecimal("95.00"),
                "Appartement cosy avec balcon",
                "Cuisine équipée, TV",
                2,
                true,
                3
        );
        dao.ajouterAppartement(newAppart);
        System.out.println("✅ Appartement inséré");

        // ✅ INSERT : une nouvelle maison d'hôtes (adaptée)
        MaisonHotes newMaison = new MaisonHotes(
                0,
                "La Maison des Bois",
                "12 Chemin des Arbres",
                new BigDecimal("130.00"),
                "Maison d'hôtes au cœur de la forêt",
                "Cheminée, Terrasse, Parking",
                true,
                true
        );
        dao.ajouterMaisonHotes(newMaison);
        System.out.println("✅ Maison d'hôtes insérée");

        // 🟢 AFFICHAGE depuis la base
        Hotel hotel = dao.findHotelById(1);
        if (hotel != null) vue.afficherHebergement(hotel);

        Appartement appart = dao.findAppartementById(2);
        if (appart != null) vue.afficherHebergement(appart);

        MaisonHotes maison = dao.findMaisonHotesById(3);
        if (maison != null) vue.afficherHebergement(maison);
    }
}