package Vue;

import DAO.HebergementDAO;
import DAO.ImageDAO;
import Modele.*;
import DAO.ClientDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AccueilPrincipalFenetre extends JFrame {

    private JButton deconnexionButton;
    private JPanel panelImage;
    private Client clientConnecte;

    public AccueilPrincipalFenetre(Client clientConnecte, boolean admin) {
        this.clientConnecte = clientConnecte;

        setTitle("Accueil - Booking App");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Titre
        JLabel welcomeLabel = new JLabel("Liste des Hébergements disponibles");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setForeground(new Color(0, 102, 204));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        add(welcomeLabel, BorderLayout.NORTH);

        // Onglets pour chaque type d'hébergement
        JTabbedPane onglets = new JTabbedPane();
        add(onglets, BorderLayout.CENTER);
        chargerHebergementsParType(onglets);

        // Panneau image à droite
        panelImage = new JPanel();
        panelImage.setPreferredSize(new Dimension(300, 200));
        panelImage.setBorder(BorderFactory.createTitledBorder("Image"));
        add(panelImage, BorderLayout.EAST);

        // Bouton déconnexion
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        deconnexionButton = new JButton("Déconnexion");
        bottomPanel.add(deconnexionButton);
        add(bottomPanel, BorderLayout.SOUTH);

        deconnexionButton.addActionListener(e -> {
            dispose();
            new ConnexionFenetre().setVisible(true);
        });

        // Profil utilisateur
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

            btnVoirReservations.addActionListener(e ->
                    JOptionPane.showMessageDialog(this, "👉 Affichage des réservations de " + clientConnecte.getPrenom())
            );

            btnSupprimer.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this, "Supprimer votre compte ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    new ClientDAO().supprimerClient(clientConnecte.getIdUtilisateur());
                    JOptionPane.showMessageDialog(this, "Compte supprimé.");
                    dispose();
                    new ConnexionFenetre().setVisible(true);
                }
            });
        } else {
            JButton btnConnexion = new JButton("Se connecter");
            profilPanel.add(btnConnexion);
            btnConnexion.addActionListener(e -> {
                dispose();
                new ConnexionFenetre().setVisible(true);
            });
        }
        //a implementer
        if (admin){
            JOptionPane.showMessageDialog(this, "peut modifier/supprimer");

        }

        add(profilPanel, BorderLayout.WEST);
    }

    private void chargerHebergementsParType(JTabbedPane onglets) {
        HebergementDAO dao = new HebergementDAO();
        List<Hebergement> hebergements = dao.getAllHebergements();

        DefaultTableModel modelHotels = new DefaultTableModel(new Object[]{"Nom", "Adresse", "Prix / nuit", "Petit Déjeuner", "Étoiles", "Piscine", "Spa"}, 0);
        DefaultTableModel modelApparts = new DefaultTableModel(new Object[]{"Nom", "Adresse", "Prix / nuit", "Petit Déjeuner", "Étage", "Nombre de pièces"}, 0);
        DefaultTableModel modelMaisons = new DefaultTableModel(new Object[]{"Nom", "Adresse", "Prix / nuit", "Petit Déjeuner", "Jardin"}, 0);

        for (Hebergement h : hebergements) {
            if (h instanceof Hotel hotel) {
                modelHotels.addRow(new Object[]{hotel.getNom(), hotel.getAdresse(), hotel.getPrixParNuit() + " €", hotel.isPetitDejeuner() ? "Oui" : "Non", hotel.getNombreEtoiles(), hotel.isPiscine() ? "Oui" : "Non", hotel.isSpa() ? "Oui" : "Non"});
            } else if (h instanceof Appartement appart) {
                modelApparts.addRow(new Object[]{appart.getNom(), appart.getAdresse(), appart.getPrixParNuit() + " €", appart.isPetitDejeuner() ? "Oui" : "Non", appart.getEtage(), appart.getNombrePieces()});
            } else if (h instanceof MaisonHotes maison) {
                modelMaisons.addRow(new Object[]{maison.getNom(), maison.getAdresse(), maison.getPrixParNuit() + " €", maison.isPetitDejeuner() ? "Oui" : "Non", maison.isJardin() ? "Oui" : "Non"});
            }
        }

        JTable tableHotels = new JTable(modelHotels);
        JTable tableApparts = new JTable(modelApparts);
        JTable tableMaisons = new JTable(modelMaisons);

        onglets.addTab("Hôtels", new JScrollPane(tableHotels));
        onglets.addTab("Appartements", new JScrollPane(tableApparts));
        onglets.addTab("Maisons d'hôtes", new JScrollPane(tableMaisons));

        ajouterSelectionListener(tableHotels, modelHotels);
        ajouterSelectionListener(tableApparts, modelApparts);
        ajouterSelectionListener(tableMaisons, modelMaisons);
    }

    private void ajouterSelectionListener(JTable table, DefaultTableModel model) {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    String nom = model.getValueAt(row, 0).toString();
                    //Hebergement h = new HebergementDAO().getHebergementByNom(nom);
                    //afficherImagePourHebergement(h);
                }
            }
        });
    }

   /*private void afficherImagePourHebergement(Hebergement hebergement) {
        panelImage.removeAll();
        List<Modele.Image> images = new ImageDAO().getImagesByHebergementId(hebergement.getIdHebergement());

        for (Modele.Image img : images) {
            try {
                ImageIcon icon = new ImageIcon(img.getUrlImage());
                Image imageReduite = icon.getImage().getScaledInstance(250, 150, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(imageReduite));
                panelImage.add(imageLabel);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        panelImage.revalidate();
        panelImage.repaint();
    }*/

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AccueilPrincipalFenetre(null, false).setVisible(true));
    }
}