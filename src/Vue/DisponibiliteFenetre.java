package Vue;

import Modele.Hebergement;
import Modele.Client;
import DAO.ReservationDAO;
import Modele.Reservation;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class DisponibiliteFenetre extends JFrame {

    private Hebergement hebergement;
    private Client client;
    private ReservationDAO reservationDAO;

    public DisponibiliteFenetre(Hebergement hebergement, Client client, ReservationDAO reservationDAO) {
        this.hebergement = hebergement;
        this.client = client;
        this.reservationDAO = reservationDAO;

        setTitle("Disponibilités - " + hebergement.getNom());
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField dateArrivee = new JTextField("2025-05-01");
        JTextField dateDepart = new JTextField("2025-05-03");
        JComboBox<Integer> nbAdultes = new JComboBox<>(new Integer[]{1, 2, 3, 4});
        JComboBox<Integer> nbEnfants = new JComboBox<>(new Integer[]{0, 1, 2, 3});
        JComboBox<Integer> nbChambres = new JComboBox<>(new Integer[]{1, 2, 3});

        formPanel.add(new JLabel("Date d'arrivée :"));
        formPanel.add(dateArrivee);
        formPanel.add(new JLabel("Date de départ :"));
        formPanel.add(dateDepart);
        formPanel.add(new JLabel("Nombre d'adultes :"));
        formPanel.add(nbAdultes);
        formPanel.add(new JLabel("Nombre d'enfants :"));
        formPanel.add(nbEnfants);
        formPanel.add(new JLabel("Nombre de chambres :"));
        formPanel.add(nbChambres);

        // Bouton pour vérifier la disponibilité
        JButton rechercher = new JButton("Vérifier la disponibilité");
        rechercher.addActionListener(e -> {
            try {
                LocalDate dateArriveeLocalDate = LocalDate.parse(dateArrivee.getText());
                LocalDate dateDepartLocalDate = LocalDate.parse(dateDepart.getText());

                boolean disponible = reservationDAO.estDisponible(
                        (int) hebergement.getIdHebergement(),
                        dateArriveeLocalDate,
                        dateDepartLocalDate
                );

                if (disponible) {
                    JOptionPane.showMessageDialog(this, "✅ Hébergement disponible !");
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Hébergement non disponible à ces dates.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "⚠️ Erreur de format de date. Utilise AAAA-MM-JJ.");
            }
        });

        // Nouveau bouton pour valider la commande (et afficher récapitulatif)

        // Nouveau bouton pour valider la commande (et afficher récapitulatif)

        // Nouveau bouton pour valider la commande (et afficher récapitulatif)
        JButton validerCommandeBtn = new JButton("Valider ma commande");
        validerCommandeBtn.addActionListener(e -> {
            try {
                LocalDate dateArriveeLocalDate = LocalDate.parse(dateArrivee.getText());
                LocalDate dateDepartLocalDate = LocalDate.parse(dateDepart.getText());
                int nbAdultesValue = (int) nbAdultes.getSelectedItem();
                int nbEnfantsValue = (int) nbEnfants.getSelectedItem();
                int nbChambresValue = (int) nbChambres.getSelectedItem();

                boolean disponible = reservationDAO.estDisponible(
                        (int) hebergement.getIdHebergement(),
                        dateArriveeLocalDate,
                        dateDepartLocalDate
                );
                if (!disponible) {
                    JOptionPane.showMessageDialog(this, "❌ Cet hébergement n'est plus disponible à ces dates.");
                    return;
                }

                // Créer l'objet réservation
                Reservation reservation = new Reservation(
                        0, // ID auto-généré
                        client.getIdUtilisateur(),
                        (int) hebergement.getIdHebergement(),
                        dateArriveeLocalDate,
                        dateDepartLocalDate,
                        nbAdultesValue,
                        nbEnfantsValue,
                        nbChambresValue,
                        Reservation.Statut.CONFIRMEE
                );

                // Calculer le montant total de la réservation (par exemple, vous pouvez multiplier le nombre de chambres, adultes, etc. par un tarif)
                double montantTotal = calculerMontantTotal(reservation); // Implémentez cette méthode pour calculer le montant total

                // Créer une instance de PaiementVue en passant les bons paramètres
                new PaiementVue(client.getIdUtilisateur(), reservation.getIdReservation(), montantTotal).setVisible(true);

                // Fermer la fenêtre de disponibilité
                this.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "⚠️ Erreur lors de la réservation : " + ex.getMessage());
            }

        });





        // Panel pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(rechercher);
        buttonPanel.add(validerCommandeBtn);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    private double calculerMontantTotal(Reservation reservation) {
        // Exemple simple pour calculer le montant, cela dépend de la façon dont vous souhaitez calculer le prix
        double tarifParNuit = 100.0; // Remplacez par le tarif réel de l'hébergement
        long nuits = java.time.temporal.ChronoUnit.DAYS.between(reservation.getDateArrivee(), reservation.getDateDepart());
        return tarifParNuit * nuits * reservation.getNombreChambres();
    }

}
