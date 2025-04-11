package Vue;

import Vue.InscriptionFenetre;
import Vue.AccueilPrincipalFenetre;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnexionFenetre extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel messageLabel;

    public ConnexionFenetre() {
        setTitle("Connexion - Booking App");
        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Logo

        ImageIcon icon = new ImageIcon(getClass().getResource("/Vue/BookingLogo.png"));
        Image image = icon.getImage().getScaledInstance(60, 70, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(image));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); // aligné à gauche sans marge
        topPanel.setBackground(Color.WHITE);
        topPanel.add(imageLabel);

        add(topPanel, BorderLayout.NORTH);


        // Panel principal
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE); // Fond blanc
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);


        // Email label stylisé
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel emailLabel = new JLabel("Email :");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setForeground(new Color(0, 102, 204));
        panel.add(emailLabel, gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        panel.add(emailField, gbc);

        // Mot de passe label stylisé
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("Mot de passe :");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(new Color(0, 102, 204));
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);

        // Message d'erreur
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.BLUE);
        panel.add(messageLabel, gbc);

        // Boutons stylisés
        gbc.gridy = 3;
        gbc.gridwidth = 1;

        loginButton = new JButton("Se connecter");
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 13));
        loginButton.setFocusPainted(false);

        registerButton = new JButton("S'inscrire");
        registerButton.setBackground(new Color(0, 102, 204));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 13));
        registerButton.setFocusPainted(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel, BorderLayout.CENTER);

        // Action de connexion
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (email.equals("admin@example.com") && password.equals("admin123")) {
                    dispose(); // ferme la fenêtre actuelle
                    new AccueilPrincipalFenetre().setVisible(true); // ouvre la page d'accueil
                }

            }
        });

        // Action d'inscription
        registerButton.addActionListener(e -> {
            dispose(); // ferme la fenêtre actuelle
            new InscriptionFenetre().setVisible(true); // ouvre la fenêtre d'inscription
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConnexionFenetre().setVisible(true));
    }
}
