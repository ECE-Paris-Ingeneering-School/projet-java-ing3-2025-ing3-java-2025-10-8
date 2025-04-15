package Vue;

import Modele.Hebergement;
import Modele.Hotel;
import Modele.Appartement;
import Modele.MaisonHotes;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HebergementDetailFenetre extends JFrame {

    private int imageIndex = 0;

    public HebergementDetailFenetre(Hebergement h) {
        setTitle("Détails de l'hébergement : " + h.getNom());
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Color fond = new Color(245, 245, 245);
        Color bleuBooking = new Color(0, 113, 194);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(fond);
        setContentPane(mainPanel);

        // === TITRE ===
        JPanel titrePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titrePanel.setBackground(fond);

        JLabel nomLabel = new JLabel(h.getNom());
        nomLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titrePanel.add(nomLabel);

        if (h instanceof Hotel hotel) {
            int nbEtoiles = hotel.getNombreEtoiles();
            for (int i = 0; i < 5; i++) {
                JLabel etoile = new JLabel("★");
                etoile.setFont(new Font("SansSerif", Font.PLAIN, 22));
                etoile.setForeground(i < nbEtoiles ? new Color(255, 191, 0) : Color.LIGHT_GRAY);
                titrePanel.add(etoile);
            }
        }

        titrePanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        mainPanel.add(titrePanel, BorderLayout.NORTH);

        // === IMAGE PANEL ===
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setPreferredSize(new Dimension(1000, 450));
        imagePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JLabel imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.WHITE);

        List<String> images = h.getImageUrls();
        if (images != null && !images.isEmpty()) {
            updateImage(imageLabel, images, imageIndex);

            JButton btnPrev = new JButton("<");
            JButton btnNext = new JButton(">");

            btnPrev.setPreferredSize(new Dimension(50, 450));
            btnNext.setPreferredSize(new Dimension(50, 450));
            btnPrev.setFocusPainted(false);
            btnNext.setFocusPainted(false);

            btnPrev.addActionListener(e -> {
                imageIndex = (imageIndex - 1 + images.size()) % images.size();
                updateImage(imageLabel, images, imageIndex);
            });

            btnNext.addActionListener(e -> {
                imageIndex = (imageIndex + 1) % images.size();
                updateImage(imageLabel, images, imageIndex);
            });

            imagePanel.add(btnPrev, BorderLayout.WEST);
            imagePanel.add(imageLabel, BorderLayout.CENTER);
            imagePanel.add(btnNext, BorderLayout.EAST);
        } else {
            imageLabel.setText("Aucune image disponible");
            imagePanel.add(imageLabel, BorderLayout.CENTER);
        }

        mainPanel.add(imagePanel, BorderLayout.CENTER);

        JPanel infosPanel = new JPanel();
        infosPanel.setLayout(new BoxLayout(infosPanel, BoxLayout.Y_AXIS));
        infosPanel.setBackground(Color.WHITE);
        infosPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

// === INFOS DE BASE ===
        infosPanel.add(createInfoLigne("Adresse :", h.getAdresse()));
        infosPanel.add(Box.createVerticalStrut(10));
        infosPanel.add(createInfoLigne("Prix par nuit :", h.getPrixParNuit() + " €"));
        infosPanel.add(Box.createVerticalStrut(15));

// === DESCRIPTION ===
        JLabel descTitre = new JLabel("Description :");
        descTitre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        infosPanel.add(descTitre);
        infosPanel.add(Box.createVerticalStrut(5));

        JLabel descriptionLabel = new JLabel("<html><p style='width:800px'>" + h.getDescription() + "</p></html>");
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        infosPanel.add(descriptionLabel);
        infosPanel.add(Box.createVerticalStrut(15));

// === SPÉCIFICATION ===
        infosPanel.add(createInfoLigne("Spécification :", h.getSpecification()));
        infosPanel.add(Box.createVerticalStrut(20));

// === POINTS FORTS ===
        JLabel pointsTitre = new JLabel("Points forts de l'établissement :");
        pointsTitre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        infosPanel.add(pointsTitre);
        infosPanel.add(Box.createVerticalStrut(10));

        JPanel pointsFortsPanel = new JPanel();
        pointsFortsPanel.setLayout(new BoxLayout(pointsFortsPanel, BoxLayout.Y_AXIS));
        pointsFortsPanel.setBackground(Color.WHITE);

        if (h instanceof Hotel hotel) {
            if (hotel.isPetitDejeuner()) {
                pointsFortsPanel.add(createPointFort("☕", "Petit-déjeuner disponible", "Bon petit-déjeuner"));
            }
            if (hotel.isPiscine()) {
                pointsFortsPanel.add(createPointFort("🏊", "Piscine", "Accès piscine inclus"));
            }
            if (hotel.isSpa()) {
                pointsFortsPanel.add(createPointFort("🧖", "Bien-être", "Spa ou massages disponibles"));
            }
        }

        if (h instanceof Appartement appartement) {
            if (appartement.isPetitDejeuner()) {
                pointsFortsPanel.add(createPointFort("☕", "Petit-déjeuner disponible", "Petit-déjeuner proposé"));
            }
            pointsFortsPanel.add(createPointFort("🏢", "Étage", "Situé au " + appartement.getEtage() + (appartement.getEtage() == 0 ? "ᵉʳ" : "ᵉ") + " étage"));
        }

        if (h instanceof MaisonHotes maison) {
            if (maison.isPetitDejeuner()) {
                pointsFortsPanel.add(createPointFort("☕", "Petit-déjeuner disponible", "Bon petit-déjeuner maison"));
            }
            if (maison.isJardin()) {
                pointsFortsPanel.add(createPointFort("🌳", "Jardin", "Jardin privé accessible"));
            }
        }

        infosPanel.add(pointsFortsPanel);
        infosPanel.add(Box.createVerticalStrut(25));

// === BOUTON CARTE ===
        JButton btnCarte = new JButton("Voir sur la carte");
        btnCarte.setBackground(new Color(0, 113, 194));
        btnCarte.setForeground(Color.WHITE);
        btnCarte.setFocusPainted(false);
        btnCarte.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCarte.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnCarte.addActionListener(e -> {
            try {
                String adresse = h.getAdresse().replace(" ", "+");
                String url = "https://www.google.com/maps/search/?api=1&query=" + adresse;
                Desktop.getDesktop().browse(new java.net.URI(url));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ouverture de la carte.");
            }
        });

        infosPanel.add(btnCarte);

        // === SCROLLPANE WRAP ===
        JScrollPane scrollPane = new JScrollPane(infosPanel);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(1000, 350));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getViewport().setBackground(Color.WHITE);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);
    }

    private JPanel createInfoLigne(String label, String value) {
        JPanel line = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        line.setBackground(Color.WHITE);
        JLabel l1 = new JLabel(label);
        l1.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JLabel l2 = new JLabel(value);
        l2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        line.add(l1);
        line.add(l2);
        return line;
    }

    private JPanel createPointFort(String emoji, String titre, String details) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel iconLabel = new JLabel(emoji);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        textPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        JLabel titreLabel = new JLabel(titre);
        titreLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JLabel detailsLabel = new JLabel(details);
        detailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        textPanel.add(titreLabel);
        textPanel.add(detailsLabel);

        panel.add(iconLabel);
        panel.add(textPanel);

        return panel;
    }


    private void updateImage(JLabel imageLabel, List<String> images, int index) {
        try {
            String path = images.get(index);
            java.net.URL imageUrl = getClass().getClassLoader().getResource(path);
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                Image img = icon.getImage().getScaledInstance(900, 450, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(img));
                imageLabel.setText("");
            } else {
                imageLabel.setIcon(null);
                imageLabel.setText("[Image introuvable]");
            }
        } catch (Exception e) {
            imageLabel.setIcon(null);
            imageLabel.setText("[Erreur chargement]");
        }
    }
}
