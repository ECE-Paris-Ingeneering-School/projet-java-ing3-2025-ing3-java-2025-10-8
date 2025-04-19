package Vue;

import DAO.HebergementDAO;
import Modele.Hebergement;
import Modele.Client;
import DAO.ReservationDAO;
import Modele.Reservation;
import Vue.MesReservationsFenetre;

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

        // Vérification que le client est valide
        if (client == null) {
            JOptionPane.showMessageDialog(this, "❌ Erreur : Le client est invalide.");
            dispose();  // Ferme la fenêtre si le client est nul
            return;
        }

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
                    JOptionPane.showMessageDialog(this, "Hébergement disponible !");
                } else {
                    JOptionPane.showMessageDialog(this, "Hébergement non disponible à ces dates.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "⚠️ Erreur de format de date. Utilise AAAA-MM-JJ.");
            }
        });

        // Nouveau bouton pour valider la commande (et afficher récapitulatif)
        JButton validerCommandeBtn = new JButton("Valider ma commande");
        validerCommandeBtn.addActionListener(e -> {
            try {
                // Vérification de la validité des informations
                LocalDate dateArriveeLocalDate = LocalDate.parse(dateArrivee.getText());
                LocalDate dateDepartLocalDate = LocalDate.parse(dateDepart.getText());
                int nbAdultesValue = (int) nbAdultes.getSelectedItem();
                int nbEnfantsValue = (int) nbEnfants.getSelectedItem();
                int nbChambresValue = (int) nbChambres.getSelectedItem();

                // Vérification de la disponibilité
                boolean disponible = reservationDAO.estDisponible(
                        (int) hebergement.getIdHebergement(),
                        dateArriveeLocalDate,
                        dateDepartLocalDate
                );
                if (!disponible) {
                    JOptionPane.showMessageDialog(this, "Cet hébergement n'est plus disponible à ces dates.");
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
                        Reservation.Statut.EN_ATTENTE
                );

                // Ajouter la réservation dans la base de données
                boolean success = reservationDAO.ajouterReservation(reservation);

                new HebergementDAO().mettreAJourDisponibilite((int) hebergement.getIdHebergement(), false);


                if (success) {
                    JOptionPane.showMessageDialog(this, "Réservation enregistrée ! Vous pouvez procéder au paiement depuis la page 'Mes réservations'.");

                    // Supposons que le paiement soit effectué ici, maintenant nous mettons à jour le statut
                    boolean paiementReussi = true; // Remplace par la logique réelle de paiement

                    if (paiementReussi) {
                        // Mettre à jour le statut de la réservation dans la base de données
                        boolean updateStatutOk = reservationDAO.mettreAJourStatutReservation(reservation.getIdReservation(), Reservation.Statut.PAYE);

                        if (updateStatutOk) {
                            new HebergementDAO().mettreAJourDisponibilite((int) hebergement.getIdHebergement(), false);
                            JOptionPane.showMessageDialog(this, "Paiement réussi ! Statut mis à jour.");
                        } else {
                            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour du statut.");
                        }
                    }

                    this.dispose(); // Ferme la fenêtre de disponibilité

                } else {
                    JOptionPane.showMessageDialog(this, "Une erreur est survenue lors de l'enregistrement.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la réservation : " + ex.getMessage());
            }
        });

        // Panel pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout());

        // Bouton Vérifier disponibilité
        buttonPanel.add(rechercher);

        // Bouton Valider commande
        buttonPanel.add(validerCommandeBtn);

        // 🔥 Nouveau bouton "Mes Réservations"
        JButton mesReservationsBtn = new JButton("Mes Réservations");
        mesReservationsBtn.addActionListener(e -> {
            MesReservationsFenetre fenetre = new MesReservationsFenetre(client, reservationDAO);
            fenetre.setVisible(true);
        });
        buttonPanel.add(mesReservationsBtn);

        // Ajout du panel boutons à la fenêtre
        add(buttonPanel, BorderLayout.SOUTH);
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
