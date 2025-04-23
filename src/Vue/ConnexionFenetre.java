package Vue;

import DAO.AdminDAO;
import DAO.ClientDAO;
import Modele.Admin;
import Modele.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ConnexionFenetre extends JFrame {

    private JTextField emailField;
    private JPasswordField mdpField;
    private JButton loginButton;
    private JButton signupButton;

    public ConnexionFenetre() {
        setTitle("Connexion - Booking");
        setSize(430, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ----- BANNIÈRE BLEUE AVEC LOGO -----
        JPanel topBanner = new JPanel(new BorderLayout());
        topBanner.setBackground(Color.decode("#003580")); // Bleu Booking

        JLabel logo = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Vue/BookingLogo.png"))
                .getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH)));
        logo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        topBanner.add(logo, BorderLayout.WEST);

        JLabel titre = new JLabel("Connexion");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titre.setForeground(Color.WHITE);
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        topBanner.add(titre, BorderLayout.CENTER);

        add(topBanner, BorderLayout.NORTH);

        // ----- CENTRE -----
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        JLabel loginLabel = new JLabel("Bienvenue sur notre application Booking");
        loginLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginLabel.setForeground(Color.decode("#003580"));
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(loginLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 25)));

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

        // Connexion
        loginButton = new RoundedButton("Connexion", Color.decode("#003580"), Color.WHITE);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(loginButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Inscription
        // Panel horizontal pour le texte + lien
        JPanel inscriptionPanel = new JPanel();
        inscriptionPanel.setBackground(Color.WHITE);
        inscriptionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

        JLabel questionLabel = new JLabel("Vous n’avez pas de compte ?");
        questionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        questionLabel.setForeground(Color.DARK_GRAY);

        JLabel signupLink = new JLabel("<html><u>S'inscrire</u></html>");
        signupLink.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        signupLink.setForeground(Color.decode("#FF8000"));
        signupLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        inscriptionPanel.add(questionLabel);
        inscriptionPanel.add(signupLink);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(inscriptionPanel);


        // Centrage global
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(centerPanel);
        add(wrapper, BorderLayout.CENTER);

        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String mdp = new String(mdpField.getPassword());

            ClientDAO clientDAO = new ClientDAO();
            AdminDAO adminDAO = new AdminDAO();

            // Essayer d'abord comme client
            Client client = clientDAO.getClientParEmail(email);

            if (client != null && client.getMdp().equals(mdp)) {
                // Vérifier s'il est aussi admin
                Admin admin = adminDAO.getAdminParId(client.getIdUtilisateur());

                JOptionPane.showMessageDialog(this, "Connexion réussie !");
                dispose();

                if (admin != null) {
                    new AccueilAdminFenetre().setVisible(true);
                } else {
                    new AccueilPrincipalFenetre(client).setVisible(true);
                }
            } else {
                // Tentative directe admin (au cas où l’admin n’est pas dans la table client)
                Admin admin = adminDAO.getAdminParEmail(email);

                if (admin != null && admin.getMdp().equals(mdp)) {
                    JOptionPane.showMessageDialog(this, "Connexion admin réussie !");
                    dispose();
                    new AccueilAdminFenetre().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Email ou mot de passe incorrect.");
                }
            }
        });

        // Redirection vers Inscription
        signupLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new InscriptionFenetre().setVisible(true);
            }
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
        protected void paintBorder(Graphics g) {
        }
    }
}
