package Vue;

import DAO.ReservationDAO;
import Modele.Reservation;
import Modele.Client;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MesReservationsFenetre extends JFrame {

    private Client client;
    private ReservationDAO reservationDAO;

    public MesReservationsFenetre(Client client, ReservationDAO reservationDAO) {
        this.client = client;
        this.reservationDAO = reservationDAO;

        setTitle("📋 Mes Réservations");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Récupération des réservations du client
        List<Reservation> reservations = reservationDAO.getReservationsByClient(client.getIdUtilisateur());

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        if (reservations.isEmpty()) {
            listPanel.add(new JLabel("😕 Aucune réservation trouvée."));
        } else {
            for (Reservation res : reservations) {
                JPanel resPanel = new JPanel(new BorderLayout());

                // Utilisation de la méthode de résumé propre
                JTextArea resArea = new JTextArea(genererRecap(res));
                resArea.setEditable(false);
                resArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
                resArea.setBackground(new Color(245, 245, 245));
                resArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                resPanel.add(resArea, BorderLayout.CENTER);

                // Ajout du bouton "Payer" si la réservation est EN_ATTENTE
                if (res.getStatut() == Reservation.Statut.EN_ATTENTE) {
                    JButton payerBtn = new JButton("💳 Payer");
                    payerBtn.addActionListener(e -> {
                        double montant = calculerMontantTotal(res);
                        new PaiementVue(client.getIdUtilisateur(), res.getIdReservation(), montant).setVisible(true);
                    });
                    resPanel.add(payerBtn, BorderLayout.EAST);
                }

                resPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                listPanel.add(Box.createVerticalStrut(10)); // Ajoute un espace entre les réservations
                listPanel.add(resPanel);
            }
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private String genererRecap(Reservation res) {
        return "🏨 Hébergement : " + res.getHebergement().getNom() + "\n" +
                "📅 Du " + res.getDateArrivee() + " au " + res.getDateDepart() + "\n" +
                "👨‍👩‍👧‍👦 Adultes : " + res.getNombreAdultes() + ", Enfants : " + res.getNombreEnfants() + "\n" +
                "🛏️ Chambres : " + res.getNombreChambres() + "\n" +
                "📌 Statut : " + res.getStatut().getValue();
    }

    private double calculerMontantTotal(Reservation reservation) {
        double tarifParNuit = 100.0; // À adapter selon ta logique
        long nuits = java.time.temporal.ChronoUnit.DAYS.between(reservation.getDateArrivee(), reservation.getDateDepart());
        return tarifParNuit * nuits * reservation.getNombreChambres();
    }
}
