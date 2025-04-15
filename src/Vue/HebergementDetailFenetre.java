package Vue;

import Modele.Hebergement;
import Modele.Hotel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HebergementDetailFenetre extends JFrame {

    private int imageIndex = 0;

    public HebergementDetailFenetre(Hebergement h) {
        setTitle("Détails de l'hébergement : " + h.getNom());
        setSize(850, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // === Couleurs ===
        Color fond = new Color(245, 245, 245);
        Color bleuBooking = new Color(0, 113, 194);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(fond);
        setContentPane(mainPanel);

        // === TITRE ===
        JPanel titrePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titrePanel.setBackground(fond);

        JLabel nomLabel = new JLabel(h.getNom());
        nomLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titrePanel.add(nomLabel);

        if (h instanceof Hotel) {
            int nbEtoiles = ((Hotel) h).getNombreEtoiles();
            for (int i = 0; i < 5; i++) {
                JLabel etoile = new JLabel("★");
                etoile.setFont(new Font("SansSerif", Font.PLAIN, 22));
                etoile.setForeground(i < nbEtoiles ? new Color(255, 191, 0) : Color.LIGHT_GRAY);
                titrePanel.add(etoile);
            }
        }

        titrePanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        mainPanel.add(titrePanel, BorderLayout.NORTH);

        // === IMAGE CAROUSEL ===
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(fond);
        imagePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(640, 320));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.WHITE);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        List<String> images = h.getImageUrls();
        if (images != null && !images.isEmpty()) {
            updateImage(imageLabel, images, imageIndex);

            JButton btnPrev = new JButton("<");
            JButton btnNext = new JButton(">");


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

        // === INFOS ===
        JPanel infosPanel = new JPanel();
        infosPanel.setLayout(new BoxLayout(infosPanel, BoxLayout.Y_AXIS));
        infosPanel.setBackground(Color.WHITE);
        infosPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        infosPanel.add(new JLabel("Adresse : " + h.getAdresse()));
        infosPanel.add(Box.createVerticalStrut(5));
        infosPanel.add(new JLabel("Prix par nuit : " + h.getPrixParNuit() + " €"));
        infosPanel.add(Box.createVerticalStrut(5));
        infosPanel.add(new JLabel("Description :"));
        infosPanel.add(Box.createVerticalStrut(5));
        JLabel descriptionLabel = new JLabel("<html><p style='width:500px'>" + h.getDescription() + "</p></html>");
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        infosPanel.add(descriptionLabel);

        infosPanel.add(new JLabel("Spécification : " + h.getSpecification()));
        infosPanel.add(Box.createVerticalStrut(15));

        // === BOUTON CARTE ===
        JButton btnCarte = new JButton("Voir sur la carte");
        btnCarte.setBackground(bleuBooking);
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

        JScrollPane scrollPane = new JScrollPane(infosPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);
    }

    private void updateImage(JLabel imageLabel, List<String> images, int index) {
        try {
            String path = images.get(index);
            java.net.URL imageUrl = getClass().getClassLoader().getResource(path);
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                Image img = icon.getImage().getScaledInstance(640, 320, Image.SCALE_SMOOTH);
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