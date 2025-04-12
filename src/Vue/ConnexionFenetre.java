package Vue;

import DAO.ClientDAO;
import Modele.Client;

import javax.swing.*;
import java.awt.*;

public class ConnexionFenetre extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel messageLabel;

    public ConnexionFenetre() {
        setTitle("Connexion - Booking App");
        setSize(400, 300);
        setLocationRelativeTo(null); // centre la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel principal
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel emailLabel = new JLabel("Email :");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setForeground(new Color(0, 102, 204));
        panel.add(emailLabel, gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        panel.add(emailField, gbc);

        // Mot de passe
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
        messageLabel.setForeground(Color.RED);
        panel.add(messageLabel, gbc);

        // Boutons
        gbc.gridy = 3;
        gbc.gridwidth = 1;

        loginButton = new JButton("Se connecter");
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 13));
        loginButton.setFocusPainted(false);

        registerButton = new JButton("S'inscrire");
        registerButton.setBackground(Color.LIGHT_GRAY);
        registerButton.setFont(new Font("Arial", Font.PLAIN, 13));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel, BorderLayout.CENTER);

        // Action de connexion
        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            ClientDAO dao = new ClientDAO();
            Client client = dao.verifierConnexion(email, password);

            if (client != null) {
                JOptionPane.showMessageDialog(this, "Connexion réussie !");
                dispose();
                new AccueilPrincipalFenetre().setVisible(true); // à remplacer par la bonne page
            } else {
                messageLabel.setText("Email ou mot de passe incorrect.");
            }
        });

        // Action d'inscription
        registerButton.addActionListener(e -> {
            dispose();
            new InscriptionFenetre().setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConnexionFenetre().setVisible(true));
    }
}
