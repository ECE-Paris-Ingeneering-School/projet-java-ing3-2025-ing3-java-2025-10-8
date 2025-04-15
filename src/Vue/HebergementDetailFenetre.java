package Vue;

import Modele.Hebergement;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HebergementDetailFenetre extends JFrame {

    private int imageIndex = 0;

    public HebergementDetailFenetre(Hebergement h) {
        setTitle("Détails de l'hébergement : " + h.getNom());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === TITRE ===
        JLabel titre = new JLabel(h.getNom(), SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        titre.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titre, BorderLayout.NORTH);

        // === IMAGE CAROUSEL ===
        JPanel imagePanel = new JPanel(new BorderLayout());
        JLabel imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(600, 300));

        List<String> images = h.getImageUrls();

        if (images != null && !images.isEmpty()) {
            updateImage(imageLabel, images, imageIndex);

            JButton btnPrev = new JButton("◀");
            JButton btnNext = new JButton("▶");

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

        add(imagePanel, BorderLayout.CENTER);

        // === INFOS ===
        JPanel infosPanel = new JPanel();
        infosPanel.setLayout(new BoxLayout(infosPanel, BoxLayout.Y_AXIS));
        infosPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        infosPanel.add(new JLabel("Adresse : " + h.getAdresse()));
        infosPanel.add(new JLabel("Prix par nuit : " + h.getPrixParNuit() + " €"));
        infosPanel.add(new JLabel("Description : " + h.getDescription()));
        infosPanel.add(new JLabel("Spécification : " + h.getSpecification()));

        add(infosPanel, BorderLayout.SOUTH);
    }

    private void updateImage(JLabel imageLabel, List<String> images, int index) {
        try {
            String path = images.get(index);
            java.net.URL imageUrl = getClass().getClassLoader().getResource(path);
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                Image img = icon.getImage().getScaledInstance(600, 300, Image.SCALE_SMOOTH);
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