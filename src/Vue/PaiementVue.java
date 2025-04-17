package Vue;

import DAO.*;
import Modele.Paiement;
import Modele.Reservation;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PaiementVue extends JFrame {

    private JLabel recapReservationLabel;
    private JLabel recapMontantLabel;
    private JLabel recapMontantReductionLabel; // Nouveau label pour afficher la réduction

    private JComboBox<String> methodeCombo;
    private JPanel methodeDetailsPanel;
    private CardLayout cardLayout;

    private JTextField carteNumeroField, carteCVVField;
    private JTextField paypalEmailField;
    private JTextField virementIbanField;

    private JButton confirmerBtn, sauvegarderBtn;
    private PaiementDAO paiementDAO;

    private int idReservation;
    private double montant;

    // Composants pour le traitement avec la barre de progression
    private JProgressBar progressBar;
    private Connection connection;
    private int idUtilisateur = 3; // À remplacer par l'ID réel de l'utilisateur

    public PaiementVue() {
        setTitle("Paiement Réservation");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        this.connection = ConnexionBdd.seConnecter();
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données");
            System.exit(1);
        }
        paiementDAO = new PaiementDAO(connection);

        initUI();
        setVisible(true);
    }

    public PaiementVue(int idUtilisateur, int idReservation, double montant) {
        this(); // appelle le constructeur par défaut pour initialiser l'UI et la connexion

        this.idUtilisateur = idUtilisateur;
        this.idReservation = idReservation;
        this.montant = montant;

        // Met à jour l'affichage du récap avec les vraies valeurs
        recapReservationLabel.setText("Réservation n° " + idReservation);
        recapMontantLabel.setText("Montant à payer : " + montant + " €");
        recapMontantReductionLabel.setText("Montant après réduction : " + montant + " €");
    }

    private void initUI() {
        JPanel recapPanel = new JPanel(new GridLayout(3, 1));
        recapReservationLabel = new JLabel("Réservation n° " + idReservation);
        recapMontantLabel = new JLabel("Montant à payer : " + montant + " €");
        recapMontantReductionLabel = new JLabel("Montant après réduction : " + montant + " €");
        recapMontantReductionLabel.setFont(new Font("SansSerif", Font.ITALIC, 13));
        recapMontantReductionLabel.setForeground(Color.DARK_GRAY);

        recapPanel.setBorder(BorderFactory.createTitledBorder("Récapitulatif"));
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
        sauvegarderBtn = new JButton("Enregistrer en attente");

        confirmerBtn.addActionListener(e -> confirmerPaiement());
        sauvegarderBtn.addActionListener(e -> enregistrerEnAttente());

        bottomPanel.add(sauvegarderBtn);
        bottomPanel.add(confirmerBtn);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel initCartePanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
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
                String newText = currentText.substring(0, offset) + currentText.substring(offset + length);
                String formatted = formatCarte(newText);
                fb.replace(0, fb.getDocument().getLength(), formatted, null);
            }

            private String formatCarte(String input) {
                return input.replaceAll("\\D", "").replaceAll("(.{4})", "$1 ").trim();
            }
        });
    }

    private void afficherTraitement() {
        JPanel panelTraitement = new JPanel();
        panelTraitement.setLayout(new BorderLayout());

        JLabel statusLabel = new JLabel("Traitement en cours... Veuillez patienter.");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panelTraitement.add(statusLabel, BorderLayout.CENTER);

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true); // Barre de progression indéterminée
        panelTraitement.add(progressBar, BorderLayout.SOUTH);

        this.add(panelTraitement, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    private void cacherTraitement() {
        this.remove(progressBar.getParent());
        this.revalidate();
        this.repaint();
    }

    private void confirmerPaiement() {
        afficherTraitement();

        String methodeStr = (String) methodeCombo.getSelectedItem();
        Paiement.MethodePaiement methode = Paiement.fromSQLMethode(methodeStr);

        if (!verifierChamps(methode)) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Confirmez-vous ce paiement de " + montant + " € via " + methodeStr + " ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        Paiement.StatutPaiement statut = (confirm == JOptionPane.YES_OPTION) ? Paiement.StatutPaiement.PAYE : Paiement.StatutPaiement.ANNULE;

        try {
            // Vérification de l'éligibilité à la réduction
            PreparedStatement ps = connection.prepareStatement("SELECT date_inscription FROM utilisateur WHERE id_utilisateur = ?");
            ps.setInt(1, idUtilisateur);
            ResultSet rs = ps.executeQuery();

            boolean reductionAppliquee = false;
            double montantAvant = montant;
            double montantReduit = 0;

            if (rs.next()) {
                Date dateInscription = rs.getDate("date_inscription");
                LocalDate dateInscriptionLocal = dateInscription.toLocalDate();
                LocalDate aujourdHui = LocalDate.now();

                if (dateInscriptionLocal.plusMonths(6).isBefore(aujourdHui)) {
                    // Applique une réduction de 10%
                    montantReduit = montant * 0.10;
                    montant -= montantReduit;

                    recapMontantReductionLabel.setText(
                            "<html><span style='color:green;'>Réduction de 10% appliquée ✅</span><br/>" +
                                    "<span style='color:#555;'>Avant : " + String.format("%.2f", montantAvant) + " €</span><br/>" +
                                    "<b>Après : " + String.format("%.2f", montant) + " €</b></html>"
                    );

                    reductionAppliquee = true;
                }
            }

            // Créer le paiement
            Paiement paiement = new Paiement(idReservation, montant, methode, statut, new Date(System.currentTimeMillis()));
            paiementDAO.ajouterPaiement(paiement);

            //Mise à jour de la disponibilité après paiement
            HebergementDAO hebergementDAO = new HebergementDAO(connection);
            ReservationDAO reservationDAO = new ReservationDAO(connection, hebergementDAO);
            Reservation reservation = reservationDAO.getReservationById(idReservation);

            if (reservation != null) {
                int idHebergement = reservation.getIdHebergement();

                boolean updated = hebergementDAO.mettreAJourDisponibilite(idHebergement, false); // false = non dispo

                if (updated) {
                    System.out.println("Hébergement rendu indisponible après paiement !");
                } else {
                    System.out.println("Échec de mise à jour de la disponibilité.");
                }
            }

            //Met à jour le statut de la réservation à PAYE

            boolean statutOui = reservationDAO.mettreAJourStatutReservation(idReservation, Reservation.Statut.PAYE);
            if (statutOui) {
                System.out.println("Statut de la réservation : PAYÉ ");
            } else {
                System.out.println("Échec de la mise à jour du statut de réservation");
            }

            // Ajouter la réduction dans la table `offrereduction`
            OffreReductionDAO offreReductionDAO = new OffreReductionDAO(connection);
            double reduction = reductionAppliquee ? 10.0 : 0.0; // 10% si true, sinon 0%
            offreReductionDAO.ajouterReductionPaiement(paiement.getIdPaiement(), reduction, montantReduit);

            cacherTraitement();

            JOptionPane.showMessageDialog(this,
                    statut == Paiement.StatutPaiement.PAYE ? "Paiement réussi !" : "Paiement annulé.",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            cacherTraitement();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'enregistrement :\n" + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }




    }

    private void enregistrerEnAttente() {
        afficherTraitement();

        String methodeStr = (String) methodeCombo.getSelectedItem();
        Paiement.MethodePaiement methode = Paiement.fromSQLMethode(methodeStr);

        if (!verifierChamps(methode)) return;

        try {
            Thread.sleep(2000); // Simule un délai de traitement

            Paiement paiement = new Paiement(idReservation, montant, methode, Paiement.StatutPaiement.EN_ATTENTE, new Date(System.currentTimeMillis()));
            paiementDAO.ajouterPaiement(paiement);

            cacherTraitement();
            JOptionPane.showMessageDialog(this,
                    "💾 Paiement enregistré en attente.",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            cacherTraitement();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'enregistrement en attente :\n" + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private boolean verifierChamps(Paiement.MethodePaiement methode) {
        if (methode == Paiement.MethodePaiement.CARTE_BANCAIRE &&
                (carteNumeroField.getText().isEmpty() || carteCVVField.getText().isEmpty())) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs de la carte bancaire.");
            return false;
        } else if (methode == Paiement.MethodePaiement.PAYPAL && paypalEmailField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner un email PayPal.");
            return false;
        } else if (methode == Paiement.MethodePaiement.VIREMENT && virementIbanField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner un IBAN pour le virement.");
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PaiementVue(3, 42, 100.0));
    }
}
