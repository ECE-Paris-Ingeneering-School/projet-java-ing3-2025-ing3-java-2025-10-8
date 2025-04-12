package Vue;

import DAO.HebergementDAO;
import Modele.Appartement;
import Modele.Hebergement;

import Modele.Client;
import DAO.ClientDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AccueilPrincipalFenetre extends JFrame {

    private JTable tableauAppartements;
    private JButton deconnexionButton;

    private Client clientConnecte;

    public AccueilPrincipalFenetre(Client clientConnecte) {

        this.clientConnecte = clientConnecte;

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

        // Encart profil
        JPanel profilPanel = new JPanel();
        profilPanel.setLayout(new BoxLayout(profilPanel, BoxLayout.Y_AXIS));
        profilPanel.setBorder(BorderFactory.createTitledBorder("Mon profil"));
        profilPanel.setBackground(Color.WHITE);

        if (clientConnecte != null) {
            profilPanel.add(new JLabel("Nom : " + clientConnecte.getNom()));
            profilPanel.add(new JLabel("Prénom : " + clientConnecte.getPrenom()));
            profilPanel.add(new JLabel("Email : " + clientConnecte.getEmail()));

            JButton btnVoirReservations = new JButton("Voir mes réservations");
            JButton btnSupprimer = new JButton("Supprimer mon compte");

            profilPanel.add(Box.createVerticalStrut(10));
            profilPanel.add(btnVoirReservations);
            profilPanel.add(Box.createVerticalStrut(5));
            profilPanel.add(btnSupprimer);

            btnSupprimer.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer votre compte ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    ClientDAO dao = new ClientDAO();
                    dao.supprimerClient(clientConnecte.getIdUtilisateur());
                    JOptionPane.showMessageDialog(this, "Compte supprimé.");
                    dispose();
                    new ConnexionFenetre().setVisible(true);
                }
            });

            btnVoirReservations.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "👉Affichage des réservations de " + clientConnecte.getPrenom());
            });

        } else {
            JButton btnConnexion = new JButton("Se connecter");
            profilPanel.add(btnConnexion);
            btnConnexion.addActionListener(e -> {
                dispose();
                new ConnexionFenetre().setVisible(true);
            });
        }

        add(profilPanel, BorderLayout.WEST); // Ajout dans la fenêtre

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
        SwingUtilities.invokeLater(() -> new AccueilPrincipalFenetre(null).setVisible(true));
    }
}
