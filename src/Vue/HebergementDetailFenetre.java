package Vue;

import Modele.Hebergement;
import javax.swing.*;
import java.awt.*;

public class HebergementDetailFenetre extends JFrame {

    public HebergementDetailFenetre(Hebergement h) {
        setTitle("Détail - " + h.getNom());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Image
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            if (h.getImageUrl() != null && !h.getImageUrl().isEmpty()) {
                java.net.URL imageUrl = getClass().getClassLoader().getResource(h.getImageUrl());
                if (imageUrl != null) {
                    ImageIcon icon = new ImageIcon(imageUrl);
                    Image img = icon.getImage().getScaledInstance(600, 300, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(img));
                } else {
                    imageLabel.setText("[Image]");
                }
            } else {
                imageLabel.setText("[Image]");
            }
        } catch (Exception e) {
            imageLabel.setText("[Image]");
        }
        add(imageLabel, BorderLayout.NORTH);

        // Détails
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        detailPanel.setBackground(Color.WHITE);

        JLabel nom = new JLabel(h.getNom());
        nom.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel prix = new JLabel(h.getPrixParNuit() + " € / nuit");
        prix.setForeground(new Color(255, 128, 0));
        prix.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel adresse = new JLabel("Adresse : " + h.getAdresse());
        adresse.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel description = new JLabel("<html><p style='width:700px'>" + h.getDescription() + "</p></html>");
        description.setFont(new Font("Arial", Font.PLAIN, 14));

        // Google Maps Link
        JButton btnMap = new JButton("🗺️ Voir sur la carte");
        btnMap.addActionListener(e -> {
            String url = "https://www.google.com/maps/search/?api=1&query=" + h.getAdresse().replace(" ", "+");
            try {
                java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
            } catch (Exception ignored) {}
        });

        detailPanel.add(nom);
        detailPanel.add(Box.createVerticalStrut(10));
        detailPanel.add(prix);
        detailPanel.add(Box.createVerticalStrut(10));
        detailPanel.add(adresse);
        detailPanel.add(Box.createVerticalStrut(10));
        detailPanel.add(description);
        detailPanel.add(Box.createVerticalStrut(20));
        detailPanel.add(btnMap);

        add(detailPanel, BorderLayout.CENTER);
    }
}