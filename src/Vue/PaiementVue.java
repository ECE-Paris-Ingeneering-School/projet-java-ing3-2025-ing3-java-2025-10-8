package Vue;

import DAO.*;
import Modele.Client;
import Modele.OffreReduction;
import Modele.Paiement;
import Modele.Reservation;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
/**
 * Vue qui permet à un utilisateur de réaliser le paiement de sa réservation.
 * Affiche le montant, applique une réduction (si client a un compte depuis 6mois), et propose différents moyens de paiement.
 */
public class PaiementVue extends JFrame {

    // Labels pour afficher les infos du récapitulatif
    private JLabel recapReservationLabel;
    private JLabel recapMontantLabel;
    private JLabel recapMontantReductionLabel;

    // Composants liés au choix de la méthode de paiement
    private JComboBox<String> methodeCombo;
    private JPanel methodeDetailsPanel;
    private CardLayout cardLayout;

    // Champs spécifiques selon la méthode de paiement choisie
    private JTextField carteNumeroField, carteCVVField;
    private JTextField paypalEmailField;
    private JTextField virementIbanField;

    // Bouton pour confirmer le paiement
    private JButton confirmerBtn;

    // Connexion BDD et informations de la réservation
    private Connection connection;
    private Client client;
    private Reservation reservation;

    private int idUtilisateur;
    private int idReservation;
    private double montant;
    private OffreReduction offreActive;
    /**
     * Constructeur principal.
     * Initialise la fenêtre, les données client/réservation et interface graphique.
     */
    public PaiementVue(int idUtilisateur, int idReservation) {
        this.idUtilisateur = idUtilisateur;
        this.idReservation = idReservation;

        setTitle("Paiement Réservation");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Connexion à la bdd
        connection = ConnexionBdd.seConnecter();
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données");
            System.exit(1);
        }

        try {
            // Recupration du client
            ClientDAO clientDAO = new ClientDAO();
            this.client = clientDAO.getClientParId(idUtilisateur);

            HebergementDAO hebergementDAO = new HebergementDAO(connection);
            ReservationDAO reservationDAO = new ReservationDAO(connection, hebergementDAO);
            this.reservation = reservationDAO.getReservationById(idReservation);

            if (client == null || reservation == null) {
                JOptionPane.showMessageDialog(this, "Client ou réservation introuvable.");
                return;
            }

            // Calcul du montant total
            LocalDate debut = reservation.getDateArrivee();
            LocalDate fin = reservation.getDateDepart();
            long nbJours = ChronoUnit.DAYS.between(debut, fin);
            BigDecimal prixNuit = reservation.getHebergement().getPrixParNuit();
            BigDecimal montantTotal = prixNuit.multiply(BigDecimal.valueOf(nbJours));
            this.montant = montantTotal.doubleValue();

            // Vérifier qu'il existe une offre ou non
            OffreReductionDAO offreDAO = new OffreReductionDAO(connection);
            offreDAO.genererOffreSiAncienUtilisateur(idUtilisateur);
            this.offreActive = offreDAO.getOffreActivePourUtilisateur(idUtilisateur);

            // Appliquer la réduction si disponible
            if (offreActive != null) {
                double reductionMontant = this.montant * offreActive.getPourcentageReduction() / 100.0;
                this.montant -= reductionMontant;
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur d'initialisation : " + e.getMessage());
            return;
        }
        // Initialisation de l'interface graphique
        initUI();
        setVisible(true);
    }
    /**
     * Initialise toute l'interface utilisateur.
     */
    private void initUI() {
        // Panneau récapitulatif
        JPanel recapPanel = new JPanel(new GridLayout(4, 1));
        recapPanel.setBorder(BorderFactory.createTitledBorder("Récapitulatif"));

        String recapText = "Réservation n° " + idReservation;
        if (offreActive != null) {
            recapText += " — " + offreActive.getDescription();
        }
        // Montant avant réduction
        double montantAvantReduction = offreActive != null ?
                montant / (1 - offreActive.getPourcentageReduction() / 100.0) :
                montant;

        recapReservationLabel = new JLabel(recapText);
        recapMontantLabel = new JLabel("Montant avant réduction : " + String.format("%.2f", montantAvantReduction) + " €");

        // Label pour afficher la réduction
        recapMontantReductionLabel = new JLabel();
        recapMontantReductionLabel.setFont(new Font("SansSerif", Font.BOLD, 14)); // Gras pour bien voir
        recapMontantReductionLabel.setForeground(Color.RED); // Rouge directement

        if (offreActive != null) {
            double reduction = montantAvantReduction - montant;
            recapMontantReductionLabel.setText("Réduction de " + offreActive.getPourcentageReduction() + "% appliquée : - " + String.format("%.2f", reduction) + " €");
        } else {
            recapMontantReductionLabel.setText("Aucune réduction appliquée.");
        }

        // Ajouter les labels au panneau
        recapPanel.add(recapReservationLabel);
        recapPanel.add(recapMontantLabel);
        recapPanel.add(recapMontantReductionLabel);
        add(recapPanel, BorderLayout.NORTH);




        // Choix de la méthode de paiement (au centre)
        JPanel centerPanel = new JPanel(new BorderLayout());
        methodeCombo = new JComboBox<>(new String[]{"Carte bancaire", "PayPal", "Virement"});
        methodeCombo.addActionListener(e -> cardLayout.show(methodeDetailsPanel, (String) methodeCombo.getSelectedItem()));
        centerPanel.add(methodeCombo, BorderLayout.NORTH);

        // Détails selon méthode choisie
        cardLayout = new CardLayout();
        methodeDetailsPanel = new JPanel(cardLayout);
        methodeDetailsPanel.add(initCartePanel(), "Carte bancaire");
        methodeDetailsPanel.add(initPaypalPanel(), "PayPal");
        methodeDetailsPanel.add(initVirementPanel(), "Virement");

        centerPanel.add(methodeDetailsPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Bouton de confirmation (tout en bas)
        JPanel bottomPanel = new JPanel(new FlowLayout());
        confirmerBtn = new JButton("Confirmer le paiement");
        confirmerBtn.addActionListener(e -> confirmerPaiement());
        bottomPanel.add(confirmerBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    /**
     * Initiliasation du formulaire pour la carte.
     */
    private JPanel initCartePanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Numéro de carte :"));
        carteNumeroField = new JTextField();
        appliquerFormatCarte(carteNumeroField);
        panel.add(carteNumeroField);

        panel.add(new JLabel("Code CVV :"));
        carteCVVField = new JTextField();
        panel.add(carteCVVField);
        return panel;
    }
    /**
     * Initilisation du formulaire pour paypal.
     */
    private JPanel initPaypalPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Email PayPal :"));
        paypalEmailField = new JTextField();
        panel.add(paypalEmailField);
        return panel;
    }
    /**
     * Initilisation du formulaire pour le virement.
     */
    private JPanel initVirementPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("IBAN :"));
        virementIbanField = new JTextField();
        panel.add(virementIbanField);
        return panel;
    }
    /**
     * Application d'un format de type carte bancaire.
     */
    private void appliquerFormatCarte(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            private String formatCarte(String text) {
                return text.replaceAll("\\D", "").replaceAll("(.{4})", "$1 ").trim();
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null) {
                    String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
                    String formatted = formatCarte(newText);
                    fb.replace(0, fb.getDocument().getLength(), formatted, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null) {
                    String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                    String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
                    String formatted = formatCarte(newText);
                    fb.replace(0, fb.getDocument().getLength(), formatted, attrs);
                }
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = new StringBuilder(currentText).delete(offset, offset + length).toString();
                String formatted = formatCarte(newText);
                fb.replace(0, fb.getDocument().getLength(), formatted, null);
            }
        });
    }
    /**
     * Confirme le paiement après la saisie utilisateur.
     * Insère le paiement, met à jour la disponibilité et affiche un écran de remerciement.
     */
    private void confirmerPaiement() {
        String methodeStr = (String) methodeCombo.getSelectedItem();
        Paiement.MethodePaiement methode = Paiement.fromSQLMethode(methodeStr);
        Paiement.StatutPaiement statut = Paiement.StatutPaiement.PAYE;

        try {
            // Créer le paiement et l'enregistrer dans la base
            Paiement paiement = new Paiement(idReservation, montant, methode, statut, new Date(System.currentTimeMillis()));
            PaiementDAO paiementDAO = new PaiementDAO(connection);
            paiementDAO.ajouterPaiement(paiement);

            // Maj de la disponibilité et du statut de la réservation
            HebergementDAO hebergementDAO = new HebergementDAO(connection);
            ReservationDAO reservationDAO = new ReservationDAO(connection, hebergementDAO);
            boolean updated = hebergementDAO.mettreAJourDisponibilite(reservation.getIdHebergement(), false);
            boolean statutUpdated = reservationDAO.mettreAJourStatutReservation(idReservation, Reservation.Statut.PAYE);

            if (updated && statutUpdated) {
                System.out.println("Paiement validé, hébergement et réservation mis à jour.");
            }

            // Appliquer offre de réduction suite à ce paiement
            if (offreActive != null) {

                System.out.println("Offre fidélité utilisée.");
            }

            // Barre de progression pour simuler attente paiement
            JDialog loadingDialog = new JDialog(this, "Traitement du paiement...", true);
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            progressBar.setPreferredSize(new Dimension(250, 30));
            loadingDialog.add(BorderLayout.CENTER, progressBar);
            loadingDialog.pack();
            loadingDialog.setLocationRelativeTo(this);

            // Simulation de traitement avec SwingWorker
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    Thread.sleep(1500);
                    return null;
                }

                @Override
                protected void done() {
                    loadingDialog.dispose();
                    PaiementVue.this.dispose();
                    new MerciVue(client.getPrenom(), idReservation, montant, methodeStr);
                }
            };

            worker.execute();
            loadingDialog.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du paiement : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
