package Vue;

import DAO.AdminDAO;
import DAO.ClientDAO;
import Modele.Admin;
import Modele.Client;

import javax.swing.*;
import java.awt.*;

public class InscriptionFenetre extends JFrame {

    private JTextField nomField;
    private JTextField prenomField;
    private JTextField emailField;
    private JPasswordField mdpField;
    private JCheckBox adminCheckbox;
    private JButton registerButton;
    private JButton retourButton;

    public InscriptionFenetre() {
        setTitle("Inscription - Booking App");
        setSize(430, 500);
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

        // Centre vertical
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        centerPanel.add(createLabel("Nom :"));
        nomField = new JTextField(20);
        styleField(nomField);
        centerPanel.add(nomField);

        centerPanel.add(createLabel("Prénom :"));
        prenomField = new JTextField(20);
        styleField(prenomField);
        centerPanel.add(prenomField);

        centerPanel.add(createLabel("Email :"));
        emailField = new JTextField(20);
        styleField(emailField);
        centerPanel.add(emailField);

        centerPanel.add(createLabel("Mot de passe :"));
        mdpField = new JPasswordField(20);
        styleField(mdpField);
        centerPanel.add(mdpField);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Checkbox admin
        adminCheckbox = new JCheckBox("Je suis administrateur");
        adminCheckbox.setAlignmentX(Component.CENTER_ALIGNMENT);
        adminCheckbox.setBackground(Color.WHITE);
        adminCheckbox.setFont(new Font("Arial", Font.PLAIN, 13));
        centerPanel.add(adminCheckbox);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Bouton Inscription
        registerButton = new RoundedButton("S'inscrire", new Color(0, 45, 114), Color.WHITE);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(registerButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Bouton retour
        retourButton = new RoundedButton("Retour", new Color(255, 128, 0), Color.WHITE);
        retourButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(retourButton);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(centerPanel);
        add(wrapper, BorderLayout.CENTER);

        // Action inscription
        registerButton.addActionListener(e -> {
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String email = emailField.getText();
            String mdp = new String(mdpField.getPassword());

            if (adminCheckbox.isSelected()) {
                Admin admin = new Admin(0, nom, prenom, email, mdp, "Responsable");
                AdminDAO dao = new AdminDAO();
                if (dao.ajouterAdmin(admin)) {
                    JOptionPane.showMessageDialog(this, "Administrateur inscrit !");
                    dispose();
                    new ConnexionFenetre().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur d'inscription admin.");
                }
            } else {
                Client client = new Client(0, nom, prenom, email, mdp, Client.TypeClient.PARTICULIER);
                ClientDAO dao = new ClientDAO();
                if (dao.ajouterClient(client)) {
                    JOptionPane.showMessageDialog(this, "Client inscrit !");
                    dispose();
                    new ConnexionFenetre().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur d'inscription client.");
                }
            }
        });

        // Retour
        retourButton.addActionListener(e -> {
            dispose();
            new ConnexionFenetre().setVisible(true);
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

    // Bouton arrondi
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InscriptionFenetre().setVisible(true));
    }
}
