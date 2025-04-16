package Vue;

import Modele.Reservation;
import DAO.ReservationDAO;
import DAO.ConnexionBdd;
import Modele.Client;
import DAO.HebergementDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class RecapitulatifFenetre extends JFrame {

    public RecapitulatifFenetre(Reservation reservation, Client client) {
        setTitle("Récapitulatif de la réservation");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel des détails
        JPanel detailsPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton confirmerBtn = new JButton("✅ Confirmer");
        JButton annulerBtn = new JButton("❌ Annuler");
        JButton voirReservationsBtn = new JButton("📋 Mes Réservations");

        confirmerBtn.addActionListener(e -> {
            Connection connection = ConnexionBdd.seConnecter();
            if (connection != null) {
                HebergementDAO hebergementDAO = new HebergementDAO(connection);
                ReservationDAO reservationDAO = new ReservationDAO(connection, hebergementDAO);

                reservation.setStatut(Reservation.Statut.PAYE);
                boolean success = reservationDAO.modifierReservation(reservation);
                if (success) {
                    JOptionPane.showMessageDialog(this, "✅ Réservation confirmée !");
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Échec de la confirmation.");
                }
            }
        });

        annulerBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "❌ Réservation annulée.");
            this.dispose();
        });

        voirReservationsBtn.addActionListener(e -> {
            //new MesReservationsFenetre(client, new ReservationDAO(ConnexionBdd.seConnecter())).setVisible(true);
            this.dispose();
        });

        buttonPanel.add(confirmerBtn);
        buttonPanel.add(annulerBtn);
        buttonPanel.add(voirReservationsBtn);

        add(detailsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
