package Vue;

import Modele.Reservation;
import DAO.ReservationDAO;
import DAO.ConnexionBdd;
import Modele.Client;
import DAO.HebergementDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
/**
 * Fenêtre d'affichage du récapitulatif d'une réservation NON UTILISEE.
 */
public class RecapitulatifFenetre extends JFrame {

    public RecapitulatifFenetre(Reservation reservation, Client client) {
        // Configuration de la fenêtre
        setTitle("Récapitulatif de la réservation");
        setSize(400, 350);
        setLocationRelativeTo(null); // Centre la fenêtre
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel des détails de la résa
        JPanel detailsPanel = new JPanel(new GridLayout(8, 2, 10, 10)); // Grille 8 lignes x 2 colonnes
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Marge autour

        // Ajout des informations de la réservation
        detailsPanel.add(new JLabel("Nom de l'hébergement :"));
        detailsPanel.add(new JLabel(reservation.getHebergement().getNom()));

        detailsPanel.add(new JLabel("Date d'arrivée :"));
        detailsPanel.add(new JLabel(reservation.getDateArrivee().toString()));

        detailsPanel.add(new JLabel("Date de départ :"));
        detailsPanel.add(new JLabel(reservation.getDateDepart().toString()));

        detailsPanel.add(new JLabel("Nombre d'adultes :"));
        detailsPanel.add(new JLabel(String.valueOf(reservation.getNombreAdultes())));

        detailsPanel.add(new JLabel("Nombre d'enfants :"));
        detailsPanel.add(new JLabel(String.valueOf(reservation.getNombreEnfants())));

        detailsPanel.add(new JLabel("Nombre de chambres :"));
        detailsPanel.add(new JLabel(String.valueOf(reservation.getNombreChambres())));

        detailsPanel.add(new JLabel("Statut :"));
        detailsPanel.add(new JLabel(reservation.getStatut().getValue()));

        // Panel des boutons d'action
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton confirmerBtn = new JButton("Confirmer");
        JButton annulerBtn = new JButton("Annuler");
        JButton voirReservationsBtn = new JButton("Mes Réservations");

        // Action bouton "Confirmer"
        confirmerBtn.addActionListener(e -> {
            Connection connection = ConnexionBdd.seConnecter(); // Connexion à la bdd
            if (connection != null) {
                HebergementDAO hebergementDAO = new HebergementDAO(connection);
                ReservationDAO reservationDAO = new ReservationDAO(connection, hebergementDAO);

                reservation.setStatut(Reservation.Statut.PAYE); // Modification du statut
                boolean success = reservationDAO.modifierReservation(reservation); // Maj bdd

                if (success) {
                    JOptionPane.showMessageDialog(this, "Réservation confirmée !");
                    this.dispose(); // Ferme la fenêtre
                } else {
                    JOptionPane.showMessageDialog(this, "Échec de la confirmation.");
                }
            }
        });

        // Action bouton "Annuler"
        annulerBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Réservation annulée.");
            this.dispose(); // Ferme la fenêtre sans modification
        });

        // Action bouton "Voir Mes Réservations"
        voirReservationsBtn.addActionListener(e -> {
            this.dispose(); // Ferme la fenêtre actuelle
        });

        // Ajout des boutons au panel
        buttonPanel.add(confirmerBtn);
        buttonPanel.add(annulerBtn);
        buttonPanel.add(voirReservationsBtn);

        // Ajout des panels à la fenêtre
        add(detailsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
