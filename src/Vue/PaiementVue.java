package Vue;

import DAO.*;
import Modele.Client;
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

    private JButton confirmerBtn, sauvegarderBtn;

    private JProgressBar progressBar;

    private Connection connection;
    private Client client;
    private Reservation reservation;

    private int idUtilisateur;
    private int idReservation;
    private double montant;

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

        recapReservationLabel = new JLabel("Réservation n° " + idReservation);
        recapMontantLabel = new JLabel("Montant à payer : " + String.format("%.2f", montant) + " €");
        recapMontantReductionLabel = new JLabel("Montant après réduction : " + String.format("%.2f", montant) + " €");
        recapMontantReductionLabel.setFont(new Font("SansSerif", Font.ITALIC, 13));
        recapMontantReductionLabel.setForeground(Color.DARK_GRAY);

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
            Reservation reservation = reservationDAO.getReservationById(idReservation);

            if (reservation != null) {
                int idHebergement = reservation.getIdHebergement();
                boolean updated = hebergementDAO.mettreAJourDisponibilite(idHebergement, false);

                if (updated) {
                    System.out.println("Hébergement rendu indisponible après paiement !");
                } else {
                    System.out.println("Échec de mise à jour de la disponibilité.");
                }
            }

            boolean statutOui = reservationDAO.mettreAJourStatutReservation(idReservation, Reservation.Statut.PAYE);
            if (statutOui) {
                System.out.println("Statut de la réservation : PAYÉ ");
            } else {
                System.out.println("Échec de la mise à jour du statut de réservation");
            }

            JOptionPane.showMessageDialog(this, "Paiement confirmé !");
            this.dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du paiement : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void enregistrerEnAttente() {
        JOptionPane.showMessageDialog(this, "Paiement enregistré en attente.");
    }
}
