package Vue;

import DAO.ReservationDAO;
import Modele.Reservation;
import Modele.Client;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class MesReservationsFenetre extends JFrame {

    private Client client;
    private ReservationDAO reservationDAO;

    public MesReservationsFenetre(Client client, ReservationDAO reservationDAO) {
        this.client = client;
        this.reservationDAO = reservationDAO;

        setTitle("Mes Réservations");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Récupération des réservations du client
        List<Reservation> reservations = reservationDAO.getReservationsByClient(client.getIdUtilisateur());

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        if (reservations.isEmpty()) {
            listPanel.add(new JLabel("Aucune réservation trouvée."));
        } else {
            for (Reservation res : reservations) {
                JPanel resPanel = new JPanel(new BorderLayout());

                // Texte récapitulatif de la réservation
                JTextArea resArea = new JTextArea(genererRecap(res));
                resArea.setEditable(false);
                resArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
                resArea.setBackground(new Color(245, 245, 245));
                resArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                resPanel.add(resArea, BorderLayout.CENTER);

                JPanel boutonPanel = new JPanel();
                boutonPanel.setLayout(new BoxLayout(boutonPanel, BoxLayout.Y_AXIS));

                // Bouton "Payer" si la réservation est EN_ATTENTE
                if (res.getStatut() == Reservation.Statut.EN_ATTENTE) {
                    JButton payerBtn = new RoundedButton("Payer", Color.decode("#003580"), Color.WHITE);
                    payerBtn.setPreferredSize(new Dimension(120, 40)); // Taille du bouton
                    payerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                    payerBtn.addActionListener(e -> {
                        double montant = calculerMontantTotal(res);
                        new PaiementVue(client.getIdUtilisateur(), res.getIdReservation()).setVisible(true);
                    });
                    boutonPanel.add(payerBtn);
                } else {
                    // Bouton "Reçu" si la réservation est payée
                    JButton recuBtn = new RoundedButton("Reçu", Color.decode("#FF8000"), Color.WHITE);
                    recuBtn.setPreferredSize(new Dimension(120, 40)); // Taille du bouton
                    recuBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                    recuBtn.addActionListener(e -> {
                        File fichierRecu = new File("recus/recu_" + res.getIdReservation() + ".txt");
                        if (fichierRecu.exists()) {
                            try {
                                Desktop.getDesktop().open(fichierRecu);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(this, "Impossible d’ouvrir le reçu.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "Aucun reçu trouvé pour cette réservation.");
                        }
                    });
                    boutonPanel.add(recuBtn);
                }

                resPanel.add(boutonPanel, BorderLayout.EAST);
                resPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // Ajout de bordure autour de la réservation
                resPanel.setPreferredSize(new Dimension(550, 100)); // Taille de chaque panneau de réservation
                resPanel.setBackground(Color.WHITE); // Fond blanc pour chaque réservation
                resPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#003580"), 1)); // Bordure bleue pour chaque réservation
                listPanel.add(Box.createVerticalStrut(10)); // Ajout d'un espacement entre les réservations
                listPanel.add(resPanel);
            }
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private String genererRecap(Reservation res) {
        return "Hébergement : " + res.getHebergement().getNom() + "\n" +
                "Du " + res.getDateArrivee() + " au " + res.getDateDepart() + "\n" +
                "Adultes : " + res.getNombreAdultes() + ", Enfants : " + res.getNombreEnfants() + "\n" +
                "Chambres : " + res.getNombreChambres() + "\n" +
                "Statut : " + res.getStatut().getValue();
    }

    private double calculerMontantTotal(Reservation reservation) {
        double tarifParNuit = 100.0; // À adapter selon ta logique
        long nuits = java.time.temporal.ChronoUnit.DAYS.between(reservation.getDateArrivee(), reservation.getDateDepart());
        return tarifParNuit * nuits * reservation.getNombreChambres();
    }

    // ----- CLASSE INTERNE POUR BOUTONS ARRONDIS -----
    static class RoundedButton extends JButton {
        public RoundedButton(String text, Color bg, Color fg) {
            super(text);
            setContentAreaFilled(false);
            setOpaque(true);
            setBackground(bg);
            setForeground(fg);
            setFont(new Font("Arial", Font.BOLD, 15));
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            setFocusPainted(false);
            setPreferredSize(new Dimension(200, 40));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {
        }
    }
}
