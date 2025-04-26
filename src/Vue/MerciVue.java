package Vue;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
/**
 * Vue affichée après un paiement effectué
 * Affiche un message de remerciement, une animation, et génère un reçu qui peut se télécharger.
 */
public class MerciVue extends JFrame {

    private File recuFile; // Fichier du reçu généré
    /**
     * Constructeur de la vue de remerciement
     *
     * @param prenomClient   Prénom du client
     * @param idReservation  ID de la réservation
     * @param montant        Montant payé
     * @param methodePaiement Méthode de paiement utilisée
     */
    public MerciVue(String prenomClient, int idReservation, double montant, String methodePaiement) {
        setTitle("Merci pour votre paiement");
        setSize(400, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Création du panneau principal
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Titre de remerciement
        JLabel merciLabel = new JLabel("Merci " + prenomClient + " !");
        merciLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        merciLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Message de confirmation
        JLabel messageLabel = new JLabel("Votre paiement a bien été reçu.");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Icône de validation en mettant un "check"
        JLabel checkLabel = new JLabel("✅"); // Emoji trouvé et copié sur wprock.fr
        checkLabel.setFont(new Font("SansSerif", Font.BOLD, 60));
        checkLabel.setForeground(new Color(46, 204, 113));
        checkLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkLabel.setVisible(false); // L'emoji sera affiché avec une animation

        // Animation pour faire apparaître progressivement le "check"
        // Source utilisée : article Fade Effect for JLabel using Timer sur Java Demos
        Timer timer = new Timer(50, null);
        final float[] opacity = {0f};
        timer.addActionListener(e -> {
            opacity[0] += 0.1f;
            checkLabel.setForeground(new Color(46, 204, 113, Math.min(255, (int) (opacity[0] * 255))));
            if (opacity[0] >= 1f) timer.stop();
        });

        // Message de sécurité affiché
        JLabel securiteLabel = new JLabel("\uD83D\uDD12 Votre paiement est sécurisé.");
        // Emoji trouvé et copié sur emojis.wiki
        securiteLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        securiteLabel.setForeground(Color.GRAY);
        securiteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        securiteLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Bouton pour ouvrir le reçu
        JButton ouvrirRecuBtn = new JButton("Ouvrir mon reçu");
        ouvrirRecuBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        ouvrirRecuBtn.setVisible(false); // il sera visible après la création du fichier

        ouvrirRecuBtn.addActionListener(e -> {
            // Ouvre le fichier du reçu avec l'application par défaut
            if (recuFile != null && recuFile.exists()) {
                try {
                    Desktop.getDesktop().open(recuFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Impossible d'ouvrir le reçu.");
                }
            }
        });

        // Ajout des composants au panneau principal
        contentPanel.add(merciLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(messageLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(checkLabel);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(securiteLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(ouvrirRecuBtn);

        add(contentPanel, BorderLayout.CENTER);

        // Afficher la fenêtre et démarrer l'animation
        setVisible(true);
        checkLabel.setVisible(true);
        timer.start();

        // Génération automatique du reçu après affichage
        genererRecu(prenomClient, idReservation, montant, methodePaiement, ouvrirRecuBtn);
    }
    /**
     * Génère un fichier texte contenant les détails du reçu de paiement
     *
     * @param prenomClient   Prénom du client
     * @param idReservation  Numéro de la réservation
     * @param montant        Montant payé
     * @param methodePaiement Mode de paiement utilisé
     * @param boutonAfficher Bouton qui s'affiche une fois le reçu prêt
     */
    private void genererRecu(String prenomClient, int idReservation, double montant, String methodePaiement, JButton boutonAfficher) {
        try {
            // Création du dossier 'recus' s'il n'existe pas
            File dossier = new File("recus");
            if (!dossier.exists()) dossier.mkdir();

            // Création du fichier de reçu
            recuFile = new File(dossier, "recu_" + idReservation + ".txt");
            try (FileWriter fw = new FileWriter(recuFile)) {
                fw.write("----- Reçu de Paiement -----\n");
                fw.write("Date        : " + LocalDate.now() + "\n");
                fw.write("Client      : " + prenomClient + "\n");
                fw.write("Réservation : #" + idReservation + "\n");
                fw.write("Montant     : " + String.format("%.2f", montant) + " €\n");
                fw.write("Méthode     : " + methodePaiement + "\n");
                fw.write("-----------------------------\n");
                fw.write("Merci pour votre confiance !");
            }

            // Afficher le bouton
            boutonAfficher.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la génération du reçu.");
        }
    }
}
