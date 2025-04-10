package Vue;

import Modele.Hebergement;
import Modele.Hotel;
import Modele.Appartement;
import Modele.MaisonHotes;

public class VueConsole {

    public void afficherHebergement(Hebergement h) {
        System.out.println("Nom : " + h.getNom());
        System.out.println("Adresse : " + h.getAdresse());
        System.out.println("Ville : " + h.getVille());
        System.out.println("Pays : " + h.getPays());
        System.out.println("Description : " + h.getDescription());

        if (h instanceof Hotel) {
            Hotel hotel = (Hotel) h;
            System.out.println("Type : Hôtel");
            System.out.println("Étoiles : " + hotel.getNombreEtoiles());
            System.out.println("Service chambre : " + (hotel.isServiceChambre() ? "Oui" : "Non"));
        } else if (h instanceof Appartement) {
            Appartement a = (Appartement) h;
            System.out.println("Type : Appartement");
            System.out.println("Superficie : " + a.getSuperficie() + " m²");
            System.out.println("Nombre de pièces : " + a.getNombrePieces());
            System.out.println("Balcon : " + (a.isBalcon() ? "Oui" : "Non"));
        } else if (h instanceof MaisonHotes) {
            MaisonHotes m = (MaisonHotes) h;
            System.out.println("Type : Maison d'hôtes");
            System.out.println("Chambres : " + m.getNombreChambres());
            System.out.println("Petit déjeuner inclus : " + (m.isPetitDejeunerInclus() ? "Oui" : "Non"));
        }

        System.out.println("--------------------------------------------");
    }

    public static void main(String[] args) {
        Hotel h = new Hotel(1, "Hôtel Ritz", "12 Rue des Fleurs", "Paris", "75001", "France", "Hôtel 5 étoiles", 5, true);
        Appartement a = new Appartement(2, "Appart de Fabrice", "23 Avenue des Champs", "Lyon", "69000", "France", "Appartement lumineux", 70, 3, true);
        MaisonHotes m = new MaisonHotes(3, "La Maison Douce", "45 Chemin du Calme", "Avignon", "84000", "France", "Charmante maison d'hôtes", 4, true);

        VueConsole vue = new VueConsole();
        vue.afficherHebergement(h);
        vue.afficherHebergement(a);
        vue.afficherHebergement(m);
    }
}
