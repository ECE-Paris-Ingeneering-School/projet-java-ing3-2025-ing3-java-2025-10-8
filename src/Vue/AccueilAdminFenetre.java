package Vue;

import javax.swing.*;
import java.awt.*;

public class AccueilAdminFenetre extends JFrame {

    public AccueilAdminFenetre() {
        setTitle("Espace Administrateur - Booking");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel titleLabel = new JLabel("Bienvenue dans l'espace administrateur");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 45, 114));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton ajouterHebergementButton = new JButton("Ajouter un hébergement");
        ajouterHebergementButton.setFont(new Font("Arial", Font.BOLD, 14));
        ajouterHebergementButton.setBackground(new Color(0, 102, 204));
        ajouterHebergementButton.setForeground(Color.WHITE);
        ajouterHebergementButton.setFocusPainted(false);
        ajouterHebergementButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        ajouterHebergementButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Action pour ajouter un hébergement (à implémenter)
        ajouterHebergementButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Redirection vers le formulaire d'ajout...");
             new AjoutHebergementFenetre().setVisible(true);
        });

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(40));
        panel.add(ajouterHebergementButton);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AccueilAdminFenetre().setVisible(true));
    }
}