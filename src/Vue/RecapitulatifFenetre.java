package Vue;

import Modele.Reservation;

import javax.swing.*;
import java.awt.*;

public class RecapitulatifFenetre extends JFrame {

    public RecapitulatifFenetre(Reservation reservation) {
        setTitle("Récapitulatif de la réservation");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel pour afficher les détails
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

        // Panel pour les boutons de confirmation et annulation
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton confirmerBtn = new JButton("Confirmer la réservation");
        JButton annulerBtn = new JButton("Annuler");

        confirmerBtn.addActionListener(e -> {
            // Effectuer l'action de confirmation de la réservation ici
            // Exemple : appel à la méthode pour finaliser la réservation dans la BDD
            JOptionPane.showMessageDialog(this, "✅ Réservation confirmée !");
            this.dispose();
        });

        annulerBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "❌ Réservation annulée.");
            this.dispose();
        });

        buttonPanel.add(confirmerBtn);
        buttonPanel.add(annulerBtn);

        add(detailsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

}
