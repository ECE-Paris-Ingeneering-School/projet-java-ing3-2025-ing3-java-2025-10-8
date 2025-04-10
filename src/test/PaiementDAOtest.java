package test;

import connexionBdd.ConnexionBdd;
import DAO.PaiementDAO;
import Modele.Paiement;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class PaiementDAOtest {
    public static void main(String[] args) {
        Connection connection = ConnexionBdd.seConnecter();
        Scanner scanner = new Scanner(System.in);

        if (connection != null) {
            PaiementDAO paiementDAO = new PaiementDAO(connection);

            try {
                // Sélection du mode de paiement
                System.out.println("Sélectionnez un mode de paiement : ");
                System.out.println("1. Carte bancaire");
                System.out.println("2. PayPal");
                System.out.println("3. Virement");
                int choix = scanner.nextInt();
                scanner.nextLine(); // Consomme le retour à la ligne

                Paiement.MethodePaiement methode;

                switch (choix) {
                    case 1:
                        methode = Paiement.MethodePaiement.CARTE_BANCAIRE;
                        break;
                    case 2:
                        methode = Paiement.MethodePaiement.PAYPAL;
                        break;
                    case 3:
                        methode = Paiement.MethodePaiement.VIREMENT;
                        break;
                    default:
                        System.out.println("❌ Choix invalide. Paiement annulé.");
                        return;
                }


                // Entrée des infos
                System.out.print("Entrez le montant du paiement : ");
                double montant = scanner.nextDouble();
                scanner.nextLine(); // flush

                System.out.print("Entrez l'ID de la réservation : ");
                int idReservation = scanner.nextInt();
                scanner.nextLine(); // flush

                // Création du paiement EN_ATTENTE
                Paiement paiement = new Paiement(
                        idReservation,
                        montant,
                        methode,
                        Paiement.StatutPaiement.EN_ATTENTE,
                        new Date(System.currentTimeMillis())
                );

                paiementDAO.ajouterPaiement(paiement);
                System.out.println("✅ Paiement ajouté pour la réservation " + paiement.getIdReservation());

                // Récupération de l'ID auto-généré (dernière ligne ajoutée)
                List<Paiement> paiementsForReservation = paiementDAO.getPaiementsByReservation(idReservation);
                Paiement paiementRecupere = paiementsForReservation.get(paiementsForReservation.size() - 1);

                System.out.println("📥 Paiement récupéré : " + paiementRecupere);

                // Confirmation
                System.out.print("Souhaitez-vous valider le paiement ? (oui/non) : ");
                String validation = scanner.nextLine().trim().toLowerCase();

                if (validation.equals("oui")) {
                    paiementRecupere.setStatut(Paiement.StatutPaiement.PAYE);
                    paiementDAO.updatePaiement(paiementRecupere);
                    System.out.println("✅ Paiement validé !");
                } else if (validation.equals("non")) {
                    // Confirmation de l'annulation
                    System.out.print("Voulez-vous vraiment annuler le paiement ? (oui/non) : ");
                    String confirmationAnnulation = scanner.nextLine().trim().toLowerCase();

                    if (confirmationAnnulation.equals("oui")) {
                        paiementRecupere.setStatut(Paiement.StatutPaiement.ANNULE);
                        paiementDAO.updatePaiement(paiementRecupere);
                        System.out.println("❌ Paiement annulé.");
                    } else {
                        System.out.println("ℹ️ Aucun changement effectué. Paiement en attente.");
                    }
                } else {
                    System.out.println("ℹ️ Réponse non reconnue. Paiement laissé en attente.");
                }

                // Affichage final des paiements liés à la réservation
                List<Paiement> paiements = paiementDAO.getPaiementsByReservation(idReservation);
                System.out.println("📋 Paiements pour la réservation " + idReservation + " :");
                for (Paiement p : paiements) {
                    System.out.println("➡️ " + p);
                }

            } catch (SQLException e) {
                System.out.println("💥 Erreur SQL : " + e.getMessage());
            }
        }
    }
}
