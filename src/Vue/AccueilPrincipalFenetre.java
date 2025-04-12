package Vue;

import DAO.HebergementDAO;
import Modele.Appartement;
import Modele.Hebergement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AccueilPrincipalFenetre extends JFrame {

    private JTable tableauAppartements;
    private JButton deconnexionButton;

    public AccueilPrincipalFenetre() {
        setTitle("Accueil - Booking App");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Titre
        JLabel welcomeLabel = new JLabel("Liste des Appartements disponibles");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setForeground(new Color(0, 102, 204));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        add(welcomeLabel, BorderLayout.NORTH);

        // Modèle de table
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"Nom", "Adresse", "Prix / nuit", "Petit Déjeuner"});
        tableauAppartements = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tableauAppartements);
        add(scrollPane, BorderLayout.CENTER);

        // Charger les données depuis le DAO
        chargerHebergements(model);

        // Bouton déconnexion
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        deconnexionButton = new JButton("Déconnexion");
        bottomPanel.add(deconnexionButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Action : retour à la page de connexion
        deconnexionButton.addActionListener(e -> {
            dispose();
            new ConnexionFenetre().setVisible(true);
        });
    }
 // charge les hebergements de la bdd
    private void chargerHebergements(DefaultTableModel model) {
        HebergementDAO dao = new HebergementDAO();
        List<Hebergement> hebergements = dao.getAllHebergements();

        for (Hebergement a : hebergements) {
            model.addRow(new Object[]{
                    a.getNom(),
                    a.getAdresse(),
                    a.getPrixParNuit() + " €",

            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AccueilPrincipalFenetre().setVisible(true));
    }
}
