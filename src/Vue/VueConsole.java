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

        dao.ajouterHotel(new Hotel(0, "Hôtel Émeraude", "1 Rue de la Liberté", new BigDecimal("120.00"), "Chic et central", "Wi-Fi, Clim, Parking", 3, true, false, false));
        dao.ajouterHotel(new Hotel(0, "Hôtel Montagne", "45 Avenue des Cimes", new BigDecimal("180.00"), "Vue panoramique sur les Alpes", "Sauna, Spa", 4, true, true, true));
        dao.ajouterHotel(new Hotel(0, "Hôtel Océan", "12 Quai de la Mer", new BigDecimal("160.00"), "Face à la plage", "Piscine, Petit Déj", 4, true, true, false));
        dao.ajouterHotel(new Hotel(0, "Hôtel Business", "99 Boulevard Haussmann", new BigDecimal("200.00"), "Idéal pour séminaires", "Salle conf, Bureau", 5, false, false, true));
        dao.ajouterHotel(new Hotel(0, "Hôtel Nature", "Route des Forêts", new BigDecimal("140.00"), "Repos en pleine nature", "Jardin, Calme", 3, true, false, true));
        dao.ajouterHotel(new Hotel(0, "Hôtel Luxe Palace", "Place Royale", new BigDecimal("350.00"), "Prestige 5 étoiles", "Butler, Spa, Limousine", 5, true, true, true));
        dao.ajouterHotel(new Hotel(0, "Hôtel Budget Plus", "18 Rue Éco", new BigDecimal("75.00"), "Simple et efficace", "Wi-Fi", 2, false, false, false));


        dao.ajouterAppartement(new Appartement(0, "Studio cosy centre-ville", "4 Rue Lafayette", new BigDecimal("85.00"), "Petit mais fonctionnel", "Cuisine équipée", 1, true, 2));
        dao.ajouterAppartement(new Appartement(0, "Appartement Vue Tour Eiffel", "7 Quai Branly", new BigDecimal("200.00"), "Vue exceptionnelle", "Balcon, Clim", 2, false, 5));
        dao.ajouterAppartement(new Appartement(0, "Loft Industriel", "10 Rue des Ateliers", new BigDecimal("150.00"), "Style moderne", "TV, Cuisine US", 1, true, 1));
        dao.ajouterAppartement(new Appartement(0, "T3 Familial", "22 Rue du Parc", new BigDecimal("130.00"), "Parfait pour 4 pers.", "2 chambres", 3, true, 3));
        dao.ajouterAppartement(new Appartement(0, "Penthouse Luxueux", "88 Avenue du Ciel", new BigDecimal("320.00"), "Terrasse privée", "Jacuzzi, Salon panoramique", 4, false, 10));
        dao.ajouterAppartement(new Appartement(0, "Appart Budget", "5 Rue Simple", new BigDecimal("65.00"), "Prix mini", "Pas de petit déj", 1, false, 0));
        dao.ajouterAppartement(new Appartement(0, "Appartement avec jardin", "15 Allée Fleurie", new BigDecimal("110.00"), "RDC avec verdure", "Jardin privé", 2, true, 0));


        dao.ajouterMaisonHotes(new MaisonHotes(0, "Maison Lavande", "Chemin des Plantes", new BigDecimal("140.00"), "Charme provençal", "Terrasse, Parfum Lavande", true, true));
        dao.ajouterMaisonHotes(new MaisonHotes(0, "Les Oiseaux", "Route des Champs", new BigDecimal("120.00"), "Repos total", "Calme, Vue campagne", false, true));
        dao.ajouterMaisonHotes(new MaisonHotes(0, "Villa Bella", "Rue des Oliviers", new BigDecimal("190.00"), "Piscine privée", "Style méditerranéen", true, false));
        dao.ajouterMaisonHotes(new MaisonHotes(0, "Le Refuge", "Montagne Verte", new BigDecimal("160.00"), "Randonnées à proximité", "Cheminée", true, true));
        dao.ajouterMaisonHotes(new MaisonHotes(0, "L’Écureuil", "Forêt enchantée", new BigDecimal("100.00"), "Rustique et boisé", "Coin feu, Cabane", false, true));
        dao.ajouterMaisonHotes(new MaisonHotes(0, "Maison du Lac", "Bord du lac", new BigDecimal("175.00"), "Kayak inclus", "Plage privée", true, false));
        dao.ajouterMaisonHotes(new MaisonHotes(0, "Gîte du Silence", "Fin de la route", new BigDecimal("90.00"), "Totalement isolé", "Paix garantie", false, false));


        for (int i = 1; i <= 7; i++) {
            Hotel h = dao.findHotelById(i);
            if (h != null) vue.afficherHebergement(h);

            Appartement a = dao.findAppartementById(i);
            if (a != null) vue.afficherHebergement(a);

            MaisonHotes m = dao.findMaisonHotesById(i);
            if (m != null) vue.afficherHebergement(m);
        }
    }
}