package Vue;

import DAO.HebergementDAO;
import Modele.Appartement;
import Modele.Hotel;
import Modele.MaisonHotes;
import Modele.Hebergement;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;

public class VueConsole {

    private static final DecimalFormat prixFormat = new DecimalFormat("#0.00");
    private static Scanner scanner = new Scanner(System.in);
    private static HebergementDAO dao = new HebergementDAO();

    // Affichage des détails d'un hébergement
    public static void afficherHebergement(Hebergement h) {
        System.out.println("===== HÉBERGEMENT =====");
        System.out.println("ID : " + h.getIdHebergement());
        System.out.println("Nom : " + h.getNom());
        System.out.println("Adresse : " + h.getAdresse());
        System.out.println("Prix par nuit : " + h.getPrixParNuit() + " €");
        System.out.println("Description : " + h.getDescription());
        System.out.println("Spécification : " + h.getSpecification());
        System.out.println("Image : " + h.getImageUrl());

        // Vérifie le type réel de l'objet
        if (h instanceof Hotel) {
            Hotel hotel = (Hotel) h;
            System.out.println("→ Type : Hôtel");
            System.out.println("Nombre d'étoiles : " + hotel.getNombreEtoiles());
            System.out.println("Petit déjeuner inclus : " + (hotel.isPetitDejeuner() ? "Oui" : "Non"));
            System.out.println("Piscine : " + (hotel.isPiscine() ? "Oui" : "Non"));
            System.out.println("Spa : " + (hotel.isSpa() ? "Oui" : "Non"));

        } else if (h instanceof Appartement) {
            Appartement app = (Appartement) h;
            System.out.println("→ Type : Appartement");
            System.out.println("Nombre de pièces : " + app.getNombrePieces());
            System.out.println("Petit déjeuner inclus : " + (app.isPetitDejeuner() ? "Oui" : "Non"));
            System.out.println("Étage : " + app.getEtage());

        } else if (h instanceof MaisonHotes) {
            MaisonHotes mh = (MaisonHotes) h;
            System.out.println("→ Type : Maison d’hôtes");
            System.out.println("Petit déjeuner inclus : " + (mh.isPetitDejeuner() ? "Oui" : "Non"));
            System.out.println("Jardin : " + (mh.isJardin() ? "Oui" : "Non"));
        }

        System.out.println("========================\n");
    }

    // Afficher les filtres disponibles
    private static void afficherMenuFiltres() {
        System.out.println("Filtres disponibles :");
        System.out.println("1. Filtrer par type d'hébergement");
        System.out.println("2. Filtrer par prix minimum");
        System.out.println("3. Filtrer par prix maximum");
        System.out.println("4. Filtrer par piscine");
        System.out.println("5. Filtrer par petit déjeuner");
        System.out.println("6. Filtrer par jardin");
        System.out.println("0. Quitter");
        System.out.print("Choisissez un filtre à appliquer : ");
    }

    // Choisir un type d'hébergement
    private static String choisirType() {
        System.out.println("Choisissez un type d'hébergement :");
        System.out.println("1. Hôtel");
        System.out.println("2. Appartement");
        System.out.println("3. Maison d'hôtes");
        System.out.print("Votre choix : ");
        int typeChoisi = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (typeChoisi) {
            case 1: return "Hotel";
            case 2: return "Appartement";
            case 3: return "MaisonHotes";
            default: return null;
        }
    }

    // Demander un prix minimum ou maximum
    private static BigDecimal choisirPrix(String type) {
        System.out.print("Entrez le prix " + type + " (ou appuyez sur Entrée pour ne pas appliquer ce filtre) : ");
        String input = scanner.nextLine();
        if (input.isEmpty()) {
            return null;
        }
        return new BigDecimal(input);
    }

    // Choisir un critère booléen (piscine, petit déjeuner, jardin)
    private static Boolean choisirBool(String critere) {
        System.out.print("Le " + critere + " est-il disponible ? (oui/non) : ");
        String input = scanner.nextLine().toLowerCase();
        if (input.equals("oui")) {
            return true;
        } else if (input.equals("non")) {
            return false;
        }
        return null;
    }

    // Afficher les hébergements filtrés
    private static void afficherHebergements(List<Hebergement> hebergements) {
        if (hebergements.isEmpty()) {
            System.out.println("Aucun hébergement ne correspond à vos critères.");
        } else {
            int i = 1;
            for (Hebergement h : hebergements) {
                System.out.println("\n# Hébergement " + i++);
                afficherHebergement(h);
            }
        }
    }


    public static void afficherRésuméFiltres(String type, BigDecimal prixMin, BigDecimal prixMax,
                                             Boolean piscine, Boolean petitDejeuner, Boolean jardin) {
        System.out.println("\nRésumé des filtres appliqués :");
        if (type != null) System.out.println("Type : " + type);
        if (prixMin != null) System.out.println("Prix minimum : " + prixMin + "€");
        if (prixMax != null) System.out.println("Prix maximum : " + prixMax + "€");
        if (piscine != null) System.out.println("Piscine : " + (piscine ? "Oui" : "Non"));
        if (petitDejeuner != null) System.out.println("Petit déjeuner : " + (petitDejeuner ? "Oui" : "Non"));
        if (jardin != null) System.out.println("Jardin : " + (jardin ? "Oui" : "Non"));
        System.out.println();
    }


    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- MENU DE FILTRES ---");

            // Initialiser les filtres
            String type = null;
            BigDecimal prixMin = null;
            BigDecimal prixMax = null;
            Boolean piscine = null;
            Boolean petitDejeuner = null;
            Boolean jardin = null;

            // Choix de filtres
            boolean filtrageFini = false;
            while (!filtrageFini) {
                afficherMenuFiltres();
                int choix = scanner.nextInt();
                scanner.nextLine(); // consomme le retour à la ligne

                switch (choix) {
                    case 1:
                        type = choisirType();
                        break;
                    case 2:
                        prixMin = choisirPrix("minimum");
                        break;
                    case 3:
                        prixMax = choisirPrix("maximum");
                        break;
                    case 4:
                        piscine = choisirBool("piscine");
                        break;
                    case 5:
                        petitDejeuner = choisirBool("petit déjeuner");
                        break;
                    case 6:
                        jardin = choisirBool("jardin");
                        break;
                    case 0:
                        filtrageFini = true;
                        break;
                    default:
                        System.out.println("Choix invalide.");
                        break;
                }
            }

            // Affiche les filtres sélectionnés
            afficherRésuméFiltres(type, prixMin, prixMax, piscine, petitDejeuner, jardin);

            // Récupère et affiche les résultats
            List<Hebergement> hebergementsFiltres = dao.getHebergementsAvecFiltres(type, prixMin, prixMax, piscine, petitDejeuner, jardin);
            afficherHebergements(hebergementsFiltres);

        }
    }
}
