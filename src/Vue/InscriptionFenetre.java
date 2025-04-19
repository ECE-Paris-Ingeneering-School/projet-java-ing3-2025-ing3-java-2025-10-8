package Vue;

import DAO.AdminDAO;
import DAO.ClientDAO;
import Modele.Admin;
import Modele.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InscriptionFenetre extends JFrame {

    private JTextField nomField;
    private JTextField prenomField;
    private JTextField emailField;
    private JPasswordField mdpField;
    private JCheckBox adminCheckbox;
    private JButton registerButton;
    private JButton retourButton;

    public InscriptionFenetre() {
        setTitle("Inscription - Booking");
        setSize(450, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ----- BANNIÈRE BLEUE -----
        JPanel topBanner = new JPanel(new BorderLayout());
        topBanner.setBackground(Color.decode("#003580"));

        JLabel logo = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Vue/BookingLogo.png"))
                .getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH)));
        logo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        topBanner.add(logo, BorderLayout.WEST);

        JLabel titre = new JLabel("Inscription");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titre.setForeground(Color.WHITE);
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        topBanner.add(titre, BorderLayout.CENTER);

        add(topBanner, BorderLayout.NORTH);

        // ----- CONTENU CENTRAL -----
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Champs
        centerPanel.add(createLabel("Nom :"));
        JTextField nomField = new JTextField(20);
        styleField(nomField);
        centerPanel.add(nomField);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(createLabel("Prénom :"));
        JTextField prenomField = new JTextField(20);
        styleField(prenomField);
        centerPanel.add(prenomField);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(createLabel("Adresse email :"));
        JTextField emailField = new JTextField(20);
        styleField(emailField);
        centerPanel.add(emailField);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(createLabel("Mot de passe :"));
        JPasswordField mdpField = new JPasswordField(20);
        styleField(mdpField);
        centerPanel.add(mdpField);

        // Option admin


        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Bouton s'inscrire
        RoundedButton inscrireButton = new RoundedButton("S'inscrire", Color.decode("#003580"), Color.WHITE);
        inscrireButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(inscrireButton);

        // Lien vers connexion
        JPanel retourPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        retourPanel.setBackground(Color.WHITE);
        JLabel dejaCompte = new JLabel("Vous avez déjà un compte ?");
        dejaCompte.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JLabel lienConnexion = new JLabel("<html><u>Se connecter</u></html>");
        lienConnexion.setForeground(Color.decode("#FF8000"));
        lienConnexion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lienConnexion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        retourPanel.add(dejaCompte);
        retourPanel.add(lienConnexion);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(retourPanel);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(centerPanel);
        add(wrapper, BorderLayout.CENTER);

        // Action inscription
        inscrireButton.addActionListener(e -> {
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String email = emailField.getText();
            String mdp = new String(mdpField.getPassword());


                Client client = new Client(0, nom, prenom, email, mdp, Client.TypeClient.PARTICULIER);
                ClientDAO dao = new ClientDAO();
                if (dao.ajouterClient(client)) {
                    JOptionPane.showMessageDialog(this, "Client inscrit !");
                    dispose();
                    new ConnexionFenetre().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur d'inscription client.");
                }

        });

        // Retour
        lienConnexion.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new ConnexionFenetre().setVisible(true);
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
