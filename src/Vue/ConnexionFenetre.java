package Vue;

import DAO.ClientDAO;
import Modele.Client;

import javax.swing.*;
import java.awt.*;

public class ConnexionFenetre extends JFrame {

    private JTextField emailField;
    private JPasswordField mdpField;
    private JButton loginButton;
    private JButton signupButton;

    public ConnexionFenetre() {
        setTitle("Connexion - Booking");
        setSize(430, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Logo et titre
        ImageIcon icon = new ImageIcon(getClass().getResource("/Vue/BookingLogo.png"));
        Image image = icon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(image));
        JLabel titreLabel = new JLabel("Reservation");
        titreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titreLabel.setForeground(new Color(0, 45, 114));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(logoLabel);
        headerPanel.add(titreLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Centre avec BoxLayout (vertical)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        // Titre "Log in"
        JLabel loginLabel = new JLabel("Log in");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 20));
        loginLabel.setForeground(new Color(0, 45, 114));
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(loginLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Email
        centerPanel.add(createLabel("Adresse email :"));
        emailField = new JTextField(20);
        styleField(emailField);
        centerPanel.add(emailField);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Mot de passe
        centerPanel.add(createLabel("Mot de passe :"));
        mdpField = new JPasswordField(20);
        styleField(mdpField);
        centerPanel.add(mdpField);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Bouton Connexion
        loginButton = new RoundedButton("Connexion", new Color(0, 45, 114), Color.WHITE);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(loginButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Bouton Inscription
        signupButton = new RoundedButton("Inscription", new Color(255, 128, 0), Color.WHITE);
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(signupButton);

        // Wrapper qui centre verticalement
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(centerPanel);
        add(wrapper, BorderLayout.CENTER);

        // Action Connexion
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String mdp = new String(mdpField.getPassword());

            ClientDAO dao = new ClientDAO();
            Client client = dao.getClientParEmail(email);

            if (client != null && client.getMdp().equals(mdp)) {
                JOptionPane.showMessageDialog(this, "Connexion réussie !");
                dispose();
                new AccueilPrincipalFenetre().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Email ou mot de passe incorrect.");
            }
        });

        // Redirection vers Inscription
        signupButton.addActionListener(e -> {
            dispose();
            new InscriptionFenetre().setVisible(true);
        });
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(Color.DARK_GRAY);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private void styleField(JTextField field) {
        field.setMaximumSize(new Dimension(280, 35));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConnexionFenetre().setVisible(true));
    }

    // ----- CLASSE INTERNE POUR BOUTONS ARRONDIS -----
    static class RoundedButton extends JButton {
        public RoundedButton(String text, Color bg, Color fg) {
            super(text);
            setContentAreaFilled(false);
            setOpaque(true);
            setBackground(bg);
            setForeground(fg);
            setFont(new Font("Arial", Font.BOLD, 15));
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            setFocusPainted(false);
            setPreferredSize(new Dimension(200, 40));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) { }
    }
}
