package gui;

import DAO.PaiementDAO;
import DAO.ConnexionBdd;
import Modele.Paiement;

import javax.swing.*;
import javax.swing.text.*;
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

    // Composants pour le traitement avec la barre de progression
    private JProgressBar progressBar;

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

    // Affichage de l'écran de traitement avec la barre de progression
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

    // Cacher l'écran de traitement
    private void cacherTraitement() {
        this.remove(progressBar.getParent());
        this.revalidate();
        this.repaint();
    }

    private void confirmerPaiement() {
        // Afficher la barre de progression et le message de traitement
        afficherTraitement();

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
            // Simuler un délai de traitement
            Thread.sleep(2000); // 2 secondes de délai pour simuler le traitement

            Paiement paiement = new Paiement(
                    idReservation,
                    montant,
                    methode,
                    statut,
                    new Date(System.currentTimeMillis())
            );

            paiementDAO.ajouterPaiement(paiement);

            // Cacher la barre de progression
            cacherTraitement();

            // Afficher un message de succès ou d'échec
            JOptionPane.showMessageDialog(this,
                    statut == Paiement.StatutPaiement.PAYE ?
                            "🎉 Paiement réussi !" :
                            "❌ Paiement annulé.",
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
            // Simuler un délai de traitement
            Thread.sleep(2000); // 2 secondes de délai pour simuler le traitement

            Paiement paiement = new Paiement(
                    idReservation,
                    montant,
                    methode,
                    Paiement.StatutPaiement.EN_ATTENTE,
                    new Date(System.currentTimeMillis())
            );

            paiementDAO.ajouterPaiement(paiement);

            // Cacher la barre de progression et afficher un message d'attente
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
