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

public class PaiementVue extends JFrame {

    private JLabel recapReservationLabel;
    private JLabel recapMontantLabel;
    private JLabel recapMontantReductionLabel;

    private JComboBox<String> methodeCombo;
    private JPanel methodeDetailsPanel;
    private CardLayout cardLayout;

    private JTextField carteNumeroField, carteCVVField;
    private JTextField paypalEmailField;
    private JTextField virementIbanField;

    private JButton confirmerBtn;

    private Connection connection;
    private Client client;
    private Reservation reservation;

    private int idUtilisateur;
    private int idReservation;
    private double montant;
    private OffreReduction offreActive;

    public PaiementVue(int idUtilisateur, int idReservation) {
        this.idUtilisateur = idUtilisateur;
        this.idReservation = idReservation;

        setTitle("Paiement Réservation");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        connection = ConnexionBdd.seConnecter();
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données");
            System.exit(1);
        }

        try {
            ClientDAO clientDAO = new ClientDAO();
            this.client = clientDAO.getClientParId(idUtilisateur);

            HebergementDAO hebergementDAO = new HebergementDAO(connection);
            ReservationDAO reservationDAO = new ReservationDAO(connection, hebergementDAO);
            this.reservation = reservationDAO.getReservationById(idReservation);

            if (client == null || reservation == null) {
                JOptionPane.showMessageDialog(this, "Client ou réservation introuvable.");
                return;
            }

            LocalDate debut = reservation.getDateArrivee();
            LocalDate fin = reservation.getDateDepart();
            long nbJours = ChronoUnit.DAYS.between(debut, fin);
            BigDecimal prixNuit = reservation.getHebergement().getPrixParNuit();
            BigDecimal montantTotal = prixNuit.multiply(BigDecimal.valueOf(nbJours));
            this.montant = montantTotal.doubleValue();

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

        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel recapPanel = new JPanel(new GridLayout(3, 1));
        recapPanel.setBorder(BorderFactory.createTitledBorder("Récapitulatif"));

        String recapText = "Réservation n° " + idReservation;
        if (offreActive != null) {
            recapText += " — " + offreActive.getDescription();
        }

        double montantAvantReduction = offreActive != null ?
                montant / (1 - offreActive.getPourcentageReduction() / 100.0) :
                montant;

        recapReservationLabel = new JLabel(recapText);
        recapMontantLabel = new JLabel("Montant avant réduction : " + String.format("%.2f", montantAvantReduction) + " €");

        recapMontantReductionLabel = new JLabel();
        recapMontantReductionLabel.setFont(new Font("SansSerif", Font.ITALIC, 13));
        recapMontantReductionLabel.setForeground(Color.DARK_GRAY);

        if (offreActive != null) {
            double reduction = montantAvantReduction - montant;
            recapMontantReductionLabel.setText("Réduction de " + offreActive.getPourcentageReduction() + "% appliquée : - " + String.format("%.2f", reduction) + " €");
        } else {
            recapMontantReductionLabel.setText("Aucune réduction appliquée.");
        }

        recapPanel.add(recapReservationLabel);
        recapPanel.add(recapMontantLabel);
        recapPanel.add(recapMontantReductionLabel);
        add(recapPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        methodeCombo = new JComboBox<>(new String[]{"Carte bancaire", "PayPal", "Virement"});
        methodeCombo.addActionListener(e -> cardLayout.show(methodeDetailsPanel, (String) methodeCombo.getSelectedItem()));
        centerPanel.add(methodeCombo, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        methodeDetailsPanel = new JPanel(cardLayout);
        methodeDetailsPanel.add(initCartePanel(), "Carte bancaire");
        methodeDetailsPanel.add(initPaypalPanel(), "PayPal");
        methodeDetailsPanel.add(initVirementPanel(), "Virement");

        centerPanel.add(methodeDetailsPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        confirmerBtn = new JButton("Confirmer le paiement");
        confirmerBtn.addActionListener(e -> confirmerPaiement());
        bottomPanel.add(confirmerBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

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

    private JPanel initPaypalPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Email PayPal :"));
        paypalEmailField = new JTextField();
        panel.add(paypalEmailField);
        return panel;
    }

    private JPanel initVirementPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("IBAN :"));
        virementIbanField = new JTextField();
        panel.add(virementIbanField);
        return panel;
    }

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

    private void confirmerPaiement() {
        String methodeStr = (String) methodeCombo.getSelectedItem();
        Paiement.MethodePaiement methode = Paiement.fromSQLMethode(methodeStr);
        Paiement.StatutPaiement statut = Paiement.StatutPaiement.PAYE;

        try {
            Paiement paiement = new Paiement(idReservation, montant, methode, statut, new Date(System.currentTimeMillis()));
            PaiementDAO paiementDAO = new PaiementDAO(connection);
            paiementDAO.ajouterPaiement(paiement);

            HebergementDAO hebergementDAO = new HebergementDAO(connection);
            ReservationDAO reservationDAO = new ReservationDAO(connection, hebergementDAO);
            boolean updated = hebergementDAO.mettreAJourDisponibilite(reservation.getIdHebergement(), false);
            boolean statutUpdated = reservationDAO.mettreAJourStatutReservation(idReservation, Reservation.Statut.PAYE);

            if (updated && statutUpdated) {
                System.out.println("Paiement validé, hébergement et réservation mis à jour.");
            }

            // Appliquer offre de réduction suite à ce paiement (bonus)
            if (offreActive != null) {
                // Ajout éventuel de logique pour désactiver ou marquer l'offre comme utilisée
                System.out.println("Offre fidélité utilisée.");
            }

            // Créer une barre de chargement dans une boîte modale
            JDialog loadingDialog = new JDialog(this, "Traitement du paiement...", true);
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            progressBar.setPreferredSize(new Dimension(250, 30));
            loadingDialog.add(BorderLayout.CENTER, progressBar);
            loadingDialog.pack();
            loadingDialog.setLocationRelativeTo(this);

// Lancer le chargement dans un thread séparé
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    // Simule le temps de traitement (tu peux l’enlever en prod)
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
