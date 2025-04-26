package Vue;

import DAO.ReservationDAO;
import Modele.Reservation;
import Modele.Client;
import java.math.BigDecimal;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
/**
 * Fenêtre qui affiche les réservations du client.
 * Cette classe permet à un client de consulter ses réservations, d'effectuer un paiement ou d'annuler une réservation.
 */
public class MesReservationsFenetre extends JFrame {

    private final Client client;
    private final ReservationDAO reservationDAO;
    /**
     * Constructeur de la fenêtre affichant les réservations du client.
     * @param client Le client dont les réservations doivent être affichées
     * @param reservationDAO Le DAO permettant de récupérer les réservations du client
     */
    public MesReservationsFenetre(Client client, ReservationDAO reservationDAO) {
        this.client = client;
        this.reservationDAO = reservationDAO;

        // Paramétrage fenêtre principale
        setTitle("Mes Réservations"); // Titre
        setSize(600, 400);
        setLocationRelativeTo(null); // Centrer la fenêtre
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Fermer la fenêtre sans quitter l'application
        setLayout(new BorderLayout());

        // Récupération des réservations pour ce client
        List<Reservation> reservations = reservationDAO.getReservationsByClient(client.getIdUtilisateur());


        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS)); // Eléments verticals

        if (reservations.isEmpty()) {
            listPanel.add(new JLabel("Aucune réservation trouvée."));
        } else {
            // Pour chaque réservation création panneau
            for (Reservation res : reservations) {
                JPanel resPanel = new JPanel(new BorderLayout());

                // Texte affiche récapitulatif de la réservation
                JTextArea resArea = new JTextArea(genererRecap(res));
                resArea.setEditable(false);
                resArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
                resArea.setBackground(new Color(245, 245, 245)); // Fond
                resArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Espace
                resPanel.add(resArea, BorderLayout.CENTER);

                // Panel pour les boutons d'action
                JPanel boutonPanel = new JPanel();
                boutonPanel.setLayout(new BoxLayout(boutonPanel, BoxLayout.Y_AXIS));

                // Si la réservation est en attente, proposer des actions de paiement et d'annulation
                if (res.getStatut() == Reservation.Statut.EN_ATTENTE) {
                    JButton payerBtn = new RoundedButton("Payer", Color.decode("#003580"), Color.WHITE);
                    payerBtn.setMaximumSize(new Dimension(150, 40)); // Taille du bouton
                    payerBtn.addActionListener(e -> {
                        new PaiementVue(client.getIdUtilisateur(), res.getIdReservation()).setVisible(true);
                    });
                    boutonPanel.add(payerBtn);

                    boutonPanel.add(Box.createVerticalStrut(10));

                    // Bouton pour annuler la réservation
                    JButton annulerBtn = new RoundedButton("Annuler", Color.RED, Color.WHITE);
                    annulerBtn.setMaximumSize(new Dimension(150, 40));
                    annulerBtn.addActionListener(e -> {
                        int confirmation = JOptionPane.showConfirmDialog(this,
                                "Êtes-vous sûr de vouloir annuler cette réservation ?", // Demander confirmation
                                "Confirmer l'annulation",
                                JOptionPane.YES_NO_OPTION);
                        if (confirmation == JOptionPane.YES_OPTION) {
                            reservationDAO.supprimerReservation(res.getIdReservation()); // Supprimer la réservation
                            JOptionPane.showMessageDialog(this, "Réservation annulée avec succès.");
                            dispose(); // Fermer la fenetre
                            new MesReservationsFenetre(client, reservationDAO).setVisible(true);
                        }
                    });
                    boutonPanel.add(annulerBtn);
                } else {
                    // Afficher le reçu ssi paiement effectue
                    JButton recuBtn = new RoundedButton("Reçu", Color.decode("#FF8000"), Color.WHITE);
                    recuBtn.setMaximumSize(new Dimension(150, 40));
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
                resPanel.setBackground(Color.WHITE);
                resPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#003580"), 1));
                resPanel.setPreferredSize(new Dimension(550, 120));

                listPanel.add(Box.createVerticalStrut(10));
                listPanel.add(resPanel);
            }
        }

        JScrollPane scrollPane = new JScrollPane(listPanel); // Faire defiler les réservations
        add(scrollPane, BorderLayout.CENTER);
    }
    /**
     * Génère un récapitulatif des informations de la réservation sous forme de chaîne de caractères.
     * @param res La réservation à récapituler
     * @return Une chaîne de caractères contenant les détails de la réservation
     */
    private String genererRecap(Reservation res) {
        long nuits = java.time.temporal.ChronoUnit.DAYS.between(res.getDateArrivee(), res.getDateDepart());

        // Calcul du prix total
        BigDecimal prixTotal = res.getHebergement().getPrixParNuit().multiply(BigDecimal.valueOf(nuits));

        // Retour du récapitulatif avec la concaténation correcte
        return "Hébergement : " + res.getHebergement().getNom() + "\n" +
                "Du " + res.getDateArrivee() + " au " + res.getDateDepart() + "\n" +
                "Adultes : " + res.getNombreAdultes() + ", Enfants : " + res.getNombreEnfants() + "\n" +
                "Chambres : " + res.getNombreChambres() + "\n" +
                "Statut : " + res.getStatut().getValue() + "\n" +
                "Prix total : " + prixTotal.setScale(2, BigDecimal.ROUND_HALF_UP) + "€";
    }
    /**
     * Classe représentant un bouton avec des coins arrondis.
     * Cette classe personnalise l'apparence des boutons.
     */
    static class RoundedButton extends JButton {
        /**
         * Constructeur pour initialiser le bouton avec un texte et des couleurs.
         * @param text Le texte du bouton
         * @param bg La couleur de fond du bouton
         * @param fg La couleur du texte du bouton
         */
        public RoundedButton(String text, Color bg, Color fg) {
            super(text); // Appel du constructeur
            setContentAreaFilled(false);
            setOpaque(true); // Bouton opaque
            setBackground(bg); // Ccouleur de fond
            setForeground(fg);
            setFont(new Font("Arial", Font.BOLD, 15));
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            setFocusPainted(false);
            setPreferredSize(new Dimension(150, 40));
            setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create(); // Création Graphics2D
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Bords plus lisses
            g2.setColor(getBackground()); // Couleur de fond
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Coins arrondis
            super.paintComponent(g);
            g2.dispose();
        }
        // pour cette methode : Source : https://docs.oracle.com/javase/tutorial/uiswing/painting/

        @Override
        protected void paintBorder(Graphics g) {
        }
    }
}
