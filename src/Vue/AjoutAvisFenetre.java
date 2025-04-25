package Vue;

import DAO.AvisDAO;
import Modele.Avis;
import Modele.Client;
import Modele.Hebergement;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Collections;


public class AjoutAvisFenetre extends JFrame {

    public AjoutAvisFenetre(Hebergement hebergement, Client client) {
        setTitle("Laisser un avis");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        Color bleuBooking = new Color(0, 113, 194);
        Color orangeBooking = new Color(255, 128, 0);

        // ======== Header ========
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        header.setBackground(bleuBooking);

        ImageIcon icon = new ImageIcon(getClass().getResource("/Vue/BookingLogo.png"));
        Image scaledImage = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(scaledImage));

        JLabel titreApp = new JLabel("Booking - Avis");
        titreApp.setFont(new Font("Arial", Font.BOLD, 18));
        titreApp.setForeground(Color.WHITE);

        JButton btnAccueil = new JButton("← Accueil");
        btnAccueil.setFocusPainted(false);
        btnAccueil.setBackground(orangeBooking);
        btnAccueil.setForeground(Color.WHITE);
        btnAccueil.setFont(new Font("Arial", Font.PLAIN, 13));

        btnAccueil.addActionListener(e -> {
            dispose();
            new AccueilPrincipalFenetre(client).setVisible(true);
        });

        header.add(logo);
        header.add(titreApp);
        header.add(Box.createHorizontalStrut(40));
        header.add(btnAccueil);

        add(header, BorderLayout.NORTH);

        // ======== Formulaire principal ========
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        JLabel titre = new JLabel("Laisser un avis pour : " + hebergement.getNom());
        titre.setFont(new Font("Arial", Font.BOLD, 16));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(titre);
        form.add(Box.createVerticalStrut(20));

        // Note
        JLabel noteLabel = new JLabel("Note (0 à 5) :");
        noteLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        JTextField noteField = new JTextField();
        noteField.setMaximumSize(new Dimension(350, 30));
        form.add(noteLabel);
        form.add(noteField);
        form.add(Box.createVerticalStrut(15));

        // Commentaire
        JLabel commentaireLabel = new JLabel("Commentaire :");
        commentaireLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        JTextArea commentaireArea = new JTextArea(5, 20);
        commentaireArea.setLineWrap(true);
        commentaireArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(commentaireArea);
        scroll.setMaximumSize(new Dimension(450, 100));
        form.add(commentaireLabel);
        form.add(scroll);
        form.add(Box.createVerticalStrut(25));

        // Bouton envoyer
        JButton btnEnvoyer = new JButton("Envoyer mon avis");
        btnEnvoyer.setBackground(bleuBooking);
        btnEnvoyer.setForeground(Color.WHITE);
        btnEnvoyer.setFont(new Font("Arial", Font.BOLD, 14));
        btnEnvoyer.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnEnvoyer.addActionListener(e -> {
            try {
                int note = Integer.parseInt(noteField.getText().trim());
                String commentaire = commentaireArea.getText().trim();

                if (note < 0 || note > 5 || commentaire.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Note entre 0 et 5 requise + commentaire.");
                    return;
                }

                Avis avis = new Avis(0, client.getIdUtilisateur(), hebergement.getIdHebergement(), note, commentaire, java.sql.Date.valueOf(LocalDate.now()));
                new AvisDAO().ajouterAvis(avis);

                JOptionPane.showMessageDialog(this, "Merci pour votre avis !");
                dispose();
                new HebergementDetailFenetre(hebergement, client).setVisible(true);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La note doit être un nombre entier entre 0 et 5.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
            }
        });

        form.add(btnEnvoyer);

        add(form, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        Modele.Client client = new Modele.Client(
                1, "Dupont", "Jean", "jean@booking.com", "motdepasse", Client.TypeClient.NOUVEAU
        );

        // Faux hébergement de test
        Modele.Hebergement hebergement = new Modele.Appartement(
                1, "Appartement Test", "123 rue fictive, Paris",
                new java.math.BigDecimal("120.00"), "Bel appartement de test",
                "Appartement", Collections.singletonList("3"), 2, true, 1
        );

        javax.swing.SwingUtilities.invokeLater(() -> {
            new AjoutAvisFenetre(hebergement, client).setVisible(true);
        });
    }
}
