package test;

import connexionBdd.ConnexionBdd;
import DAO.PaiementDAO;
import model.Paiement;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class PaiementDAOtest {

    public static void main(String[] args) {
        Connection connection = ConnexionBdd.seConnecter();

        if (connection != null) {
            PaiementDAO paiementDAO = new PaiementDAO(connection);
            Scanner scanner = new Scanner(System.in);

            try {
                // 1. Sélection du mode de paiement
                System.out.println("Sélectionnez un mode de paiement :");
                System.out.println("1. Carte bancaire");
                System.out.println("2. PayPal");
                System.out.println("3. Virement");
                int choixMethode = scanner.nextInt();
                scanner.nextLine(); // consomme le retour à la ligne
                Paiement.MethodePaiement methode = null;

                switch (choixMethode) {
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
                        System.out.println("❌ Méthode de paiement invalide !");
                        return;
                }

                // 2. Saisie des informations de paiement
                System.out.println("Entrez le montant du paiement : ");
                double montant = scanner.nextDouble();
                scanner.nextLine();

                System.out.println("Entrez l'ID de la réservation : ");
                int idReservation = scanner.nextInt();
                scanner.nextLine();

                // 3. Création du paiement avec statut EN_ATTENTE
                Paiement paiement = new Paiement(
                        idReservation,
                        montant,
                        methode,
                        Paiement.StatutPaiement.EN_ATTENTE,
                        new Date(System.currentTimeMillis())
                );

                paiementDAO.ajouterPaiement(paiement);
                System.out.println("✅ Paiement ajouté pour la réservation " + idReservation);

                // 4. Récupération du dernier paiement ajouté (ID auto-incrémenté)
                List<Paiement> paiementsAssocies = paiementDAO.getPaiementsByReservation(idReservation);
                Paiement paiementRecupere = paiementsAssocies.get(paiementsAssocies.size() - 1); // le plus récent

                System.out.println("📥 Paiement récupéré : " + paiementRecupere);

                // 5. Validation ou annulation
                System.out.println("Souhaitez-vous valider le paiement ? (oui/non/vide en attente)");
                String reponse = scanner.nextLine();

                if (reponse.equalsIgnoreCase("oui")) {
                    paiementRecupere.setStatut(Paiement.StatutPaiement.PAYE);
                    paiementDAO.updatePaiement(paiementRecupere);
                    System.out.println("✅ Paiement validé !");
                } else if (reponse.equalsIgnoreCase("non")) {
                    paiementRecupere.setStatut(Paiement.StatutPaiement.ANNULE);
                    paiementDAO.updatePaiement(paiementRecupere);
                    System.out.println("❌ Paiement annulé !");
                } else {
                    System.out.println("⏳ Paiement laissé en attente.");
                }

                // 6. Affichage des paiements liés à la réservation
                System.out.println("📋 Paiements pour la réservation " + idReservation + " :");
                paiementsAssocies = paiementDAO.getPaiementsByReservation(idReservation);
                for (Paiement p : paiementsAssocies) {
                    System.out.println("➡️ " + p);
                }

            } catch (SQLException e) {
                System.out.println("💥 Erreur SQL : " + e.getMessage());
            } catch (Exception e) {
                System.out.println("⚠️ Erreur inattendue : " + e.getMessage());
            }
        }
    }
}
