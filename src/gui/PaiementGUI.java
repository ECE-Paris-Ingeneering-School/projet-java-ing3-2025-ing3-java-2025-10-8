package gui;

import DAO.PaiementDAO;
import connexionBdd.ConnexionBdd;
import model.Paiement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class PaiementGUI extends JFrame {
    private JTextField idReservationField, montantField;
    private JComboBox<String> methodeCombo;
    private JButton ajouterBtn, rafraichirBtn, validerBtn, annulerBtn;
    private JTable paiementTable;
    private DefaultTableModel tableModel;

    private PaiementDAO paiementDAO;

    public PaiementGUI() {
        setTitle("Gestion des Paiements");
        setSize(700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Connexion à la base de données
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
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));

        inputPanel.add(new JLabel("ID Réservation :"));
        idReservationField = new JTextField();
        inputPanel.add(idReservationField);

        inputPanel.add(new JLabel("Montant :"));
        montantField = new JTextField();
        inputPanel.add(montantField);

        inputPanel.add(new JLabel("Méthode de paiement :"));
        methodeCombo = new JComboBox<>(new String[]{"Carte bancaire", "PayPal", "Virement"});
        inputPanel.add(methodeCombo);

        ajouterBtn = new JButton("Ajouter Paiement");
        ajouterBtn.addActionListener(e -> ajouterPaiement());
        inputPanel.add(ajouterBtn);

        rafraichirBtn = new JButton("Afficher Paiements");
        rafraichirBtn.addActionListener(e -> chargerPaiements());
        inputPanel.add(rafraichirBtn);

        // Ajout des boutons Valider et Annuler
        validerBtn = new JButton("Valider Paiement");
        validerBtn.addActionListener(e -> validerPaiement());
        inputPanel.add(validerBtn);

        annulerBtn = new JButton("Annuler Paiement");
        annulerBtn.addActionListener(e -> annulerPaiement());
        inputPanel.add(annulerBtn);

        add(inputPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Réservation", "Montant", "Méthode", "Statut", "Date"}, 0);
        paiementTable = new JTable(tableModel);
        add(new JScrollPane(paiementTable), BorderLayout.CENTER);
    }

    private void ajouterPaiement() {
        try {
            int idRes = Integer.parseInt(idReservationField.getText());
            double montant = Double.parseDouble(montantField.getText());
            String methodeStr = (String) methodeCombo.getSelectedItem();
            Paiement.MethodePaiement methode = Paiement.fromSQLMethode(methodeStr);

            Paiement paiement = new Paiement(
                    idRes,
                    montant,
                    methode,
                    Paiement.StatutPaiement.EN_ATTENTE,
                    new Date(System.currentTimeMillis())
            );
            paiementDAO.ajouterPaiement(paiement);
            JOptionPane.showMessageDialog(this, "✅ Paiement ajouté !");
            chargerPaiements();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }

    private void chargerPaiements() {
        try {
            int idRes = Integer.parseInt(idReservationField.getText());
            List<Paiement> paiements = paiementDAO.getPaiementsByReservation(idRes);

            tableModel.setRowCount(0); // Clear table
            for (Paiement p : paiements) {
                tableModel.addRow(new Object[]{
                        p.getIdPaiement(),
                        p.getIdReservation(),
                        p.getMontant(),
                        Paiement.getSQLMethode(p.getMethodePaiement()),
                        Paiement.getSQLStatut(p.getStatut()),
                        p.getDatePaiement()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur de chargement : " + e.getMessage());
        }
    }

    private void validerPaiement() {
        try {
            int selectedRow = paiementTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un paiement à valider.");
                return;
            }

            int idPaiement = (int) tableModel.getValueAt(selectedRow, 0);
            Paiement paiement = paiementDAO.getPaiementById(idPaiement);
            if (paiement != null) {
                paiement.setStatut(Paiement.StatutPaiement.PAYE);
                paiementDAO.updatePaiement(paiement);
                JOptionPane.showMessageDialog(this, "Paiement validé !");
                chargerPaiements();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la validation du paiement : " + e.getMessage());
        }
    }

    private void annulerPaiement() {
        try {
            int selectedRow = paiementTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un paiement à annuler.");
                return;
            }

            int idPaiement = (int) tableModel.getValueAt(selectedRow, 0);
            Paiement paiement = paiementDAO.getPaiementById(idPaiement);
            if (paiement != null) {
                paiement.setStatut(Paiement.StatutPaiement.ANNULE);
                paiementDAO.updatePaiement(paiement);
                JOptionPane.showMessageDialog(this, "Paiement annulé !");
                chargerPaiements();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'annulation du paiement : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PaiementGUI::new);
    }
}
