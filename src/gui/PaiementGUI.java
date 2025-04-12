package gui;

import DAO.PaiementDAO;
import DAO.ConnexionBdd;
import Modele.Paiement;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.Date;

public class PaiementGUI extends JFrame {

    private JLabel recapReservationLabel;
    private JLabel recapMontantLabel;

    private JComboBox<String> methodeCombo;
    private JPanel methodeDetailsPanel;
    private CardLayout cardLayout;

    private JTextField carteNumeroField, carteCVVField;
    private JTextField paypalEmailField;
    private JTextField virementIbanField;

    private JButton confirmerBtn, sauvegarderBtn;
    private PaiementDAO paiementDAO;

    private int idReservation = 1234;
    private double montant = 150.0;

    public PaiementGUI() {
        setTitle("Paiement Réservation");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Connection connection = ConnexionBdd.seConnecter();
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données");
            System.exit(1);
        }
        paiementDAO = new PaiementDAO(connection);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        JPanel recapPanel = new JPanel(new GridLayout(2, 1));
        recapReservationLabel = new JLabel("Réservation n° " + idReservation);
        recapMontantLabel = new JLabel("Montant à payer : " + montant + " €");

        recapReservationLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        recapMontantLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        recapPanel.setBorder(BorderFactory.createTitledBorder("Récapitulatif"));
        recapPanel.add(recapReservationLabel);
        recapPanel.add(recapMontantLabel);

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

    private void confirmerPaiement() {
        String methodeStr = (String) methodeCombo.getSelectedItem();
        Paiement.MethodePaiement methode = Paiement.fromSQLMethode(methodeStr);

        if (!verifierChamps(methode)) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Confirmez-vous ce paiement de " + montant + " € via " + methodeStr + " ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        Paiement.StatutPaiement statut = (confirm == JOptionPane.YES_OPTION) ?
                Paiement.StatutPaiement.PAYE :
                Paiement.StatutPaiement.ANNULE;

        try {
            Paiement paiement = new Paiement(
                    idReservation,
                    montant,
                    methode,
                    statut,
                    new Date(System.currentTimeMillis())
            );

            paiementDAO.ajouterPaiement(paiement);

            JOptionPane.showMessageDialog(this,
                    statut == Paiement.StatutPaiement.PAYE ?
                            "🎉 Paiement enregistré avec succès !" :
                            "❌ Paiement annulé.",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);

            resetForm();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'enregistrement :\n" + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void enregistrerEnAttente() {
        String methodeStr = (String) methodeCombo.getSelectedItem();
        Paiement.MethodePaiement methode = Paiement.fromSQLMethode(methodeStr);

        if (!verifierChamps(methode)) return;

        try {
            Paiement paiement = new Paiement(
                    idReservation,
                    montant,
                    methode,
                    Paiement.StatutPaiement.EN_ATTENTE,
                    new Date(System.currentTimeMillis())
            );

            paiementDAO.ajouterPaiement(paiement);

            JOptionPane.showMessageDialog(this,
                    "💾 Paiement enregistré en attente.",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);

            resetForm();

        } catch (Exception e) {
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

    private void resetForm() {
        carteNumeroField.setText("");
        carteCVVField.setText("");
        paypalEmailField.setText("");
        virementIbanField.setText("");
        methodeCombo.setSelectedIndex(0);
        cardLayout.show(methodeDetailsPanel, "Carte bancaire");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PaiementGUI::new);
    }
}
