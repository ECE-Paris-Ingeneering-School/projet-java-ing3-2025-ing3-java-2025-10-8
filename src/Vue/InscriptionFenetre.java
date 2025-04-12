package Vue;

import DAO.ClientDAO;
import Modele.Client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InscriptionFenetre extends JFrame {

    private JTextField nomField;
    private JTextField prenomField;
    private JTextField emailField;
    private JPasswordField mdpField;

    private JButton registerButton;
    private JButton retourButton;

    public InscriptionFenetre() {
        setTitle("Inscription - Booking App");
        setSize(450, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Logo en haut à gauche
        ImageIcon icon = new ImageIcon(getClass().getResource("/Vue/BookingLogo.png"));
        Image image = icon.getImage().getScaledInstance(40, 45, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(image));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoPanel.setBackground(Color.WHITE);
        logoPanel.add(imageLabel);
        add(logoPanel, BorderLayout.NORTH);

        // Panel principal
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(createStyledLabel("Nom :"), gbc);
        gbc.gridx = 1;
        nomField = new JTextField(20);
        panel.add(nomField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(createStyledLabel("Prénom :"), gbc);
        gbc.gridx = 1;
        prenomField = new JTextField(20);
        panel.add(prenomField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(createStyledLabel("Email :"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        panel.add(emailField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(createStyledLabel("Mot de passe :"), gbc);
        gbc.gridx = 1;
        mdpField = new JPasswordField(20);
        panel.add(mdpField, gbc);


        // Boutons
        row++;
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        registerButton = new JButton("S'inscrire");
        registerButton.setBackground(new Color(0, 102, 204));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 13));
        registerButton.setFocusPainted(false);

        retourButton = new JButton("Retour");
        retourButton.setBackground(Color.LIGHT_GRAY);
        retourButton.setFont(new Font("Arial", Font.PLAIN, 13));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(registerButton);
        buttonPanel.add(retourButton);
        panel.add(buttonPanel, gbc);

        add(panel, BorderLayout.CENTER);

        // Action d'inscription
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nom = nomField.getText();
                    String prenom = prenomField.getText();
                    String email = emailField.getText();
                    String mdp = new String(mdpField.getPassword());

                    Client client = new Client(0, nom, prenom, email, mdp, Client.TypeClient.Nouveau);
                    ClientDAO clientDAO = new ClientDAO();
                    boolean success = clientDAO.ajouterClient(client);

                    if (success) {
                        JOptionPane.showMessageDialog(InscriptionFenetre.this, "Inscription réussie !");
                        dispose();
                        new ConnexionFenetre().setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(InscriptionFenetre.this, "Erreur lors de l'inscription.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(InscriptionFenetre.this, "Erreur : " + ex.getMessage());
                }
            }
        });


        // Retour à la page de connexion
        retourButton.addActionListener(e -> {
            dispose();
            new ConnexionFenetre().setVisible(true);
        });
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(0, 102, 204));
        return label;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InscriptionFenetre().setVisible(true));
    }
}
