package Vue;

import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre d'accueil pour l'administrateur
 * Permet d'ajouter, modifier ou supprimer des hébergements
 * Offre également la posibilité de se déconnecter
 */

public class AccueilAdminFenetre extends JFrame {

    /**
     * Constructeur principal de l'accueil administrateur
     * Initialise la fenêtre et met en place les differents boutons
     */
    public AccueilAdminFenetre() {
        setTitle("Espace Administrateur - Booking");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Panneau principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel titleLabel = new JLabel("Bienvenue dans l'espace administrateur");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 45, 114));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Bas du panneau où le bouton de déconnexion est
        JButton deconnexionButton = new JButton("Ajouter un hébergement");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        deconnexionButton = new JButton("Déconnexion");
        bottomPanel.add(deconnexionButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // action pour se déconnecter
        deconnexionButton.addActionListener(e -> {
            dispose();
            new ConnexionFenetre().setVisible(true);
        });

        // bouton pour ajouter
        JButton ajouterHebergementButton = new JButton("Ajouter un hébergement");
        ajouterHebergementButton.setFont(new Font("Arial", Font.BOLD, 14));
        ajouterHebergementButton.setBackground(new Color(0, 102, 204));
        ajouterHebergementButton.setForeground(Color.WHITE);
        ajouterHebergementButton.setFocusPainted(false);
        ajouterHebergementButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        ajouterHebergementButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // bouton pour supprimer
        JButton supprimerHebergementButton = new JButton("Supprimer un hébergement");
        supprimerHebergementButton.setFont(new Font("Arial", Font.BOLD, 14));
        supprimerHebergementButton.setBackground(new Color(0, 102, 204));
        supprimerHebergementButton.setForeground(Color.WHITE);
        supprimerHebergementButton.setFocusPainted(false);
        supprimerHebergementButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        supprimerHebergementButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // bouton pour modifier
        JButton modifierHebergementButton = new JButton("Modifier un hébergement");
        modifierHebergementButton.setFont(new Font("Arial", Font.BOLD, 14));
        modifierHebergementButton.setBackground(new Color(0, 102, 204));
        modifierHebergementButton.setForeground(Color.WHITE);
        modifierHebergementButton.setFocusPainted(false);
        modifierHebergementButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        modifierHebergementButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Action pour ajouter un hébergement
        ajouterHebergementButton.addActionListener(e -> {
             new AjoutHebergementFenetre().setVisible(true);
        });

        // Action pour modifier un hébergement
        modifierHebergementButton.addActionListener(e -> {
            new ModifierHebergementFenetre().setVisible(true);
        });

        // Action pour supprimer un hébergement
        supprimerHebergementButton.addActionListener(e -> {
            new SupprimerHebergementFenetre().setVisible(true);
        });

        //esthétique du panneau
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(60));
        panel.add(ajouterHebergementButton);
        panel.add(Box.createVerticalStrut(20));
        panel.add(modifierHebergementButton);
        panel.add(Box.createVerticalStrut(20));
        panel.add(supprimerHebergementButton);
        add(panel);
    }
    /**
     * Lancement de la fenêtre d'administration
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AccueilAdminFenetre().setVisible(true));
    }
}