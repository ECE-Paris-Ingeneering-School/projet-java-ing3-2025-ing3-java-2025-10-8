package Vue;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class MerciVue extends JFrame {

    private File recuFile;

    public MerciVue(String prenomClient, int idReservation, double montant, String methodePaiement) {
        setTitle("Merci pour votre paiement");
        setSize(400, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel merciLabel = new JLabel("Merci " + prenomClient + " !");
        merciLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        merciLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel messageLabel = new JLabel("Votre paiement a bien été reçu.");
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel checkLabel = new JLabel("✓");
        checkLabel.setFont(new Font("SansSerif", Font.BOLD, 60));
        checkLabel.setForeground(new Color(46, 204, 113));
        checkLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkLabel.setVisible(false);

        Timer timer = new Timer(50, null);
        final float[] opacity = {0f};
        timer.addActionListener(e -> {
            opacity[0] += 0.1f;
            checkLabel.setForeground(new Color(46, 204, 113, Math.min(255, (int) (opacity[0] * 255))));
            if (opacity[0] >= 1f) timer.stop();
        });

        JLabel securiteLabel = new JLabel("🔒 Votre paiement est sécurisé.");
        securiteLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        securiteLabel.setForeground(Color.GRAY);
        securiteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        securiteLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // 🧾 Bouton pour ouvrir le reçu
        JButton ouvrirRecuBtn = new JButton("Ouvrir mon reçu");
        ouvrirRecuBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        ouvrirRecuBtn.setVisible(false); // affiché après génération du reçu

        ouvrirRecuBtn.addActionListener(e -> {
            if (recuFile != null && recuFile.exists()) {
                try {
                    Desktop.getDesktop().open(recuFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Impossible d'ouvrir le reçu.");
                }
            }
        });

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

        setVisible(true);
        checkLabel.setVisible(true);
        timer.start();

        // Générer le reçu
        genererRecu(prenomClient, idReservation, montant, methodePaiement, ouvrirRecuBtn);
    }

    private void genererRecu(String prenomClient, int idReservation, double montant, String methodePaiement, JButton boutonAfficher) {
        try {
            File dossier = new File("recus");
            if (!dossier.exists()) dossier.mkdir();

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

            boutonAfficher.setVisible(true); // afficher le bouton une fois le reçu généré

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la génération du reçu.");
        }
    }
}
