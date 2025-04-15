package Vue;

import DAO.HebergementDAO;
import Modele.*;
import DAO.ClientDAO;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.*;

public class AccueilPrincipalFenetre extends JFrame {

    private JPanel filtrePanel, resultPanel;
    private JCheckBox cbHotel, cbAppartement, cbMaison;
    private JCheckBox cbPetitDej, cbPiscine, cbSpa, cbJardin;
    private JTextField prixMinField, prixMaxField;
    private JButton deconnexionButton;
    private Client clientConnecte;

    public AccueilPrincipalFenetre(Client clientConnecte) {
        this.clientConnecte = clientConnecte;

        // Style global
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 13));
        UIManager.put("CheckBox.font", new Font("Segoe UI", Font.PLAIN, 13));

        setTitle("Booking App");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color bleuBooking = new Color(0, 113, 194);
        Color orangeBooking = new Color(255, 128, 0);

        // Filtres
        filtrePanel = new JPanel();
        filtrePanel.setLayout(new BoxLayout(filtrePanel, BoxLayout.Y_AXIS));
        filtrePanel.setBackground(new Color(250, 250, 250));
        filtrePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        filtrePanel.setPreferredSize(new Dimension(250, getHeight()));

        cbHotel = new JCheckBox("Hôtel");
        cbAppartement = new JCheckBox("Appartement");
        cbMaison = new JCheckBox("Maison d'hôtes");
        cbPetitDej = new JCheckBox("Petit déjeuner inclus");
        cbPiscine = new JCheckBox("Spa et centre de bien-être");
        cbSpa = new JCheckBox("Spa");
        cbJardin = new JCheckBox("Jardin");

        JPanel prixPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        prixPanel.setBackground(filtrePanel.getBackground());
        prixPanel.add(new JLabel("Min (€) :"));
        prixMinField = new JTextField(5);
        prixPanel.add(prixMinField);
        prixPanel.add(new JLabel("Max (€) :"));
        prixMaxField = new JTextField(5);
        prixPanel.add(prixMaxField);

        JButton btnFiltrer = new JButton("Appliquer les filtres");
        btnFiltrer.setBackground(orangeBooking);
        btnFiltrer.setForeground(Color.WHITE);

        filtrePanel.add(cbHotel);
        filtrePanel.add(cbAppartement);
        filtrePanel.add(cbMaison);
        filtrePanel.add(Box.createVerticalStrut(10));
        filtrePanel.add(cbPetitDej);
        filtrePanel.add(cbPiscine);
        filtrePanel.add(cbSpa);
        filtrePanel.add(cbJardin);
        filtrePanel.add(Box.createVerticalStrut(10));
        filtrePanel.add(prixPanel);
        filtrePanel.add(btnFiltrer);

        add(filtrePanel, BorderLayout.WEST);

        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(resultPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Bas
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

        add(profilPanel, BorderLayout.EAST);

        btnFiltrer.addActionListener(e -> filtrerHebergements());
        filtrerHebergements();
    }

    private void filtrerHebergements() {
        HebergementDAO dao = new HebergementDAO();
        List<Hebergement> tous = dao.getAllHebergements();
        List<Hebergement> filtres = new ArrayList<>();

        BigDecimal prixMin = null;
        BigDecimal prixMax = null;
        try {
            if (!prixMinField.getText().isEmpty()) prixMin = new BigDecimal(prixMinField.getText());
            if (!prixMaxField.getText().isEmpty()) prixMax = new BigDecimal(prixMaxField.getText());
        } catch (NumberFormatException ignored) {}

        for (Hebergement h : tous) {
            boolean ok = true;

            if (prixMin != null && h.getPrixParNuit().compareTo(prixMin) < 0) ok = false;
            if (prixMax != null && h.getPrixParNuit().compareTo(prixMax) > 0) ok = false;

            if (cbHotel.isSelected() || cbAppartement.isSelected() || cbMaison.isSelected()) {
                if (h instanceof Hotel && !cbHotel.isSelected()) ok = false;
                if (h instanceof Appartement && !cbAppartement.isSelected()) ok = false;
                if (h instanceof MaisonHotes && !cbMaison.isSelected()) ok = false;
            }

            if (cbPetitDej.isSelected()) {
                boolean offre = false;
                if (h instanceof Hotel) offre = ((Hotel) h).isPetitDejeuner();
                if (h instanceof Appartement) offre = ((Appartement) h).isPetitDejeuner();
                if (h instanceof MaisonHotes) offre = ((MaisonHotes) h).isPetitDejeuner();
                if (!offre) ok = false;
            }

            if (cbPiscine.isSelected() && h instanceof Hotel && !((Hotel) h).isPiscine()) ok = false;
            if (cbSpa.isSelected() && h instanceof Hotel && !((Hotel) h).isSpa()) ok = false;
            if (cbJardin.isSelected() && h instanceof MaisonHotes && !((MaisonHotes) h).isJardin()) ok = false;

            if (ok) filtres.add(h);
        }

        resultPanel.removeAll();

        if (filtres.isEmpty()) {
            JLabel rien = new JLabel("Aucun hébergement ne correspond à vos critères.", SwingConstants.CENTER);
            rien.setFont(new Font("Arial", Font.BOLD, 16));
            rien.setForeground(Color.GRAY);
            resultPanel.add(rien);
        } else {
            for (Hebergement h : filtres) {
                resultPanel.add(creerCarteHebergement(h));
                resultPanel.add(Box.createVerticalStrut(10));
            }
        }

        resultPanel.revalidate();
        resultPanel.repaint();
    }

    private JPanel creerCarteHebergement(Hebergement h) {
        JPanel carte = new JPanel();
        carte.setBackground(new Color(245, 245, 245));
        carte.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        carte.setLayout(new BorderLayout());
        carte.setPreferredSize(new Dimension(1000, 180));

        // === Image ===
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(220, 150));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            List<String> images = h.getImageUrls();
            if (images != null && !images.isEmpty()) {
                java.net.URL imageUrl = getClass().getClassLoader().getResource(images.get(0));
                if (imageUrl != null) {
                    ImageIcon icon = new ImageIcon(imageUrl);
                    Image img = icon.getImage().getScaledInstance(220, 150, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(img));
                    imageLabel.setText("");
                } else {
                    imageLabel.setText("[Image introuvable]");
                }
            } else {
                imageLabel.setText("[Aucune image]");
            }
        } catch (Exception e) {
            imageLabel.setText("[Erreur image]");
        }

        carte.add(imageLabel, BorderLayout.WEST);

        // === Infos ===
        JPanel infos = new JPanel();
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));
        infos.setBackground(Color.WHITE);
        infos.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        JPanel nomEtEtoiles = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        nomEtEtoiles.setBackground(Color.WHITE);

        JLabel nomLabel = new JLabel(h.getNom());
        nomLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        nomEtEtoiles.add(nomLabel);

        if (h instanceof Hotel) {
            int nbEtoiles = ((Hotel) h).getNombreEtoiles();
            for (int i = 0; i < nbEtoiles; i++) {
                JLabel star = new JLabel("★");
                star.setForeground(new Color(255, 191, 0));
                star.setFont(new Font("SansSerif", Font.PLAIN, 18));
                nomEtEtoiles.add(star);
            }
            for (int i = nbEtoiles; i < 5; i++) {
                JLabel starEmpty = new JLabel("★");
                starEmpty.setForeground(Color.LIGHT_GRAY);
                starEmpty.setFont(new Font("SansSerif", Font.PLAIN, 18));
                nomEtEtoiles.add(starEmpty);
            }
        }

        infos.add(nomEtEtoiles);

        JLabel adresse = new JLabel(h.getAdresse());
        adresse.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel prix = new JLabel(h.getPrixParNuit() + " € / nuit");
        prix.setForeground(new Color(255, 128, 0));
        prix.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel desc = new JLabel("<html><p style='width:700px'>" + h.getDescription() + "</p></html>");
        desc.setFont(new Font("Arial", Font.PLAIN, 14));

        infos.add(Box.createVerticalStrut(5));
        infos.add(adresse);
        infos.add(Box.createVerticalStrut(5));
        infos.add(prix);
        infos.add(Box.createVerticalStrut(5));

        // === Boutons ===
        JButton btnDispo = new JButton("Voir les disponibilités");
        btnDispo.setBackground(new Color(0, 113, 194));
        btnDispo.setForeground(Color.WHITE);
        btnDispo.setFocusPainted(false);

        btnDispo.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Affichage des disponibilités pour : " + h.getNom());
        });

        JButton btnCarte = new JButton("Voir sur la carte");
        btnCarte.setBackground(new Color(0, 113, 194));
        btnCarte.setForeground(Color.WHITE);
        btnCarte.setFocusPainted(false);

        btnCarte.addActionListener(e -> {
            try {
                String adresseUrl = h.getAdresse().replace(" ", "+");
                String url = "https://www.google.com/maps/search/?api=1&query=" + adresseUrl;
                Desktop.getDesktop().browse(new java.net.URI(url));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ouverture de la carte.");
            }
        });

        JPanel boutonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        boutonsPanel.setBackground(Color.WHITE);
        boutonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        boutonsPanel.add(btnDispo);
        boutonsPanel.add(btnCarte);

        infos.add(Box.createVerticalStrut(10));
        infos.add(boutonsPanel);

        carte.add(infos, BorderLayout.CENTER);

        carte.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        carte.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new HebergementDetailFenetre(h).setVisible(true);
            }
        });

        return carte;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AccueilPrincipalFenetre(null).setVisible(true));
    }
}