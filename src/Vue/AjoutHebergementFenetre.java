package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import DAO.HebergementDAO;
import Modele.Appartement;
import Modele.Hotel;
import Modele.MaisonHotes;

public class AjoutHebergementFenetre extends JFrame {

    private JComboBox<String> typeCombo;
    private JTextField nomField, adresseField, prixField, descriptionField;
    private JCheckBox petitDejBox;
    private JTextField nbEtoilesField, nbPiecesField, etageField;
    private JCheckBox piscineBox, spaBox, jardinBox;
    private JPanel specificFieldsPanel;
    private JLabel imageLabel;
    private List<String> imagePaths = new ArrayList<>();

    public AjoutHebergementFenetre() {
        setTitle("Ajouter un hébergement");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(new Color(0, 45, 114));
        JLabel logo = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Vue/BookingLogo.png")).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        JLabel title = new JLabel("  Ajouter un hébergement");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        JButton retourAccueil = new JButton("Retour à l'accueil ");
        retourAccueil.setForeground(Color.WHITE);
        retourAccueil.setBackground(new Color(255, 128, 0));
        retourAccueil.setBorderPainted(false);
        retourAccueil.setFocusPainted(false);
        retourAccueil.setFont(new Font("Arial", Font.PLAIN, 14));
        retourAccueil.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        retourAccueil.addActionListener(e -> {
            dispose();
            new AccueilAdminFenetre().setVisible(true);
        });

        header.add(logo);
        header.add(title);
        header.add(Box.createHorizontalStrut(50));
        header.add(retourAccueil);
        add(header, BorderLayout.NORTH);

        // Centre
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JPanel choixPanel = new JPanel();
        choixPanel.setBackground(Color.WHITE);
        choixPanel.add(new JLabel("Type d'hébergement :"));
        String[] types = {"Hôtel", "Appartement", "Maison d'hôtes"};
        typeCombo = new JComboBox<>(types);
        choixPanel.add(typeCombo);
        formPanel.add(choixPanel);

        nomField = createTextField(formPanel, "Nom :");
        adresseField = createTextField(formPanel, "Adresse :");
        prixField = createTextField(formPanel, "Prix par nuit :");
        descriptionField = createTextField(formPanel, "Description :");

        petitDejBox = new JCheckBox("Petit déjeuner inclus");
        formPanel.add(wrapComponent(petitDejBox));

        JLabel lienChoisirImages = new JLabel("<html><a href='#'>Choisir des images...</a></html>");
        lienChoisirImages.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lienChoisirImages.setForeground(new Color(0, 113, 194));
        lienChoisirImages.setFont(new Font("Arial", Font.BOLD, 13));
        lienChoisirImages.setAlignmentX(Component.CENTER_ALIGNMENT);

        lienChoisirImages.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setMultiSelectionEnabled(true);
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = chooser.showOpenDialog(AjoutHebergementFenetre.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File[] selected = chooser.getSelectedFiles();
                    imagePaths.clear();
                    for (File f : selected) {
                        try {
                            Path destination = Path.of("resources/images/" + f.getName());
                            Files.copy(f.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
                            imagePaths.add(destination.toString());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    imageLabel.setText(imagePaths.size() + " image(s) sélectionnée(s).");
                }
            }
        });
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(lienChoisirImages);

        imageLabel = new JLabel("Aucune image sélectionnée.");
        imageLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(imageLabel);

        specificFieldsPanel = new JPanel();
        specificFieldsPanel.setBackground(Color.WHITE);
        specificFieldsPanel.setLayout(new BoxLayout(specificFieldsPanel, BoxLayout.Y_AXIS));
        formPanel.add(specificFieldsPanel);

        JButton btnAjouter = new JButton("Ajouter");
        btnAjouter.setBackground(new Color(255, 128, 0));
        btnAjouter.setForeground(Color.WHITE);
        btnAjouter.setFont(new Font("Arial", Font.BOLD, 16));
        btnAjouter.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAjouter.addActionListener(e -> ajouterHebergement());
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(btnAjouter);

        add(formPanel, BorderLayout.CENTER);

        typeCombo.addActionListener(e -> updateSpecificFields((String) typeCombo.getSelectedItem()));
        updateSpecificFields("Hôtel");
    }

    private JTextField createTextField(JPanel container, String label) {
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField field = new JTextField(25);
        field.setMaximumSize(new Dimension(400, 30));
        container.add(jLabel);
        container.add(field);
        container.add(Box.createVerticalStrut(10));
        return field;
    }

    private JPanel wrapComponent(JComponent comp) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);
        panel.add(comp);
        return panel;
    }

    private void updateSpecificFields(String type) {
        specificFieldsPanel.removeAll();
        switch (type) {
            case "Hôtel" -> {
                nbEtoilesField = new JTextField();
                piscineBox = new JCheckBox("Piscine");
                spaBox = new JCheckBox("Spa");
                specificFieldsPanel.add(createLabeledField("Nombre d'étoiles :", nbEtoilesField));
                specificFieldsPanel.add(wrapComponent(piscineBox));
                specificFieldsPanel.add(wrapComponent(spaBox));
            }
            case "Appartement" -> {
                nbPiecesField = new JTextField();
                etageField = new JTextField();
                specificFieldsPanel.add(createLabeledField("Nombre de pièces :", nbPiecesField));
                specificFieldsPanel.add(createLabeledField("Étage :", etageField));
            }
            case "Maison d'hôtes" -> {
                jardinBox = new JCheckBox("Jardin");
                specificFieldsPanel.add(wrapComponent(jardinBox));
            }
        }
        specificFieldsPanel.revalidate();
        specificFieldsPanel.repaint();
    }

    private JPanel createLabeledField(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel jlabel = new JLabel(label);
        jlabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(jlabel, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        panel.setMaximumSize(new Dimension(450, 60));
        return panel;
    }

    private void ajouterHebergement() {
        String nom = nomField.getText().trim();
        String adresse = adresseField.getText().trim();
        String prixStr = prixField.getText().trim();
        String description = descriptionField.getText().trim();
        boolean petitDej = petitDejBox.isSelected();
        String type = (String) typeCombo.getSelectedItem();

        if (nom.isEmpty() || adresse.isEmpty() || prixStr.isEmpty() || description.isEmpty() || imagePaths.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        BigDecimal prix;
        try {
            prix = new BigDecimal(prixStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Le prix doit être un nombre valide.");
            return;
        }

        HebergementDAO dao = new HebergementDAO();
        try {
            switch (type) {
                case "Hôtel" -> {
                    int etoiles = Integer.parseInt(nbEtoilesField.getText().trim());
                    boolean piscine = piscineBox.isSelected();
                    boolean spa = spaBox.isSelected();
                    dao.ajouterHotel(new Hotel(0, nom, adresse, prix, description, "Hôtel", imagePaths, etoiles, petitDej, piscine, spa));
                }
                case "Appartement" -> {
                    int pieces = Integer.parseInt(nbPiecesField.getText().trim());
                    int etage = Integer.parseInt(etageField.getText().trim());
                    dao.ajouterAppartement(new Appartement(0, nom, adresse, prix, description, "Appartement", imagePaths, pieces, petitDej, etage));
                }
                case "Maison d'hôtes" -> {
                    boolean jardin = jardinBox.isSelected();
                    dao.ajouterMaisonHotes(new MaisonHotes(0, nom, adresse, prix, description, "Maison d'hôtes", imagePaths, petitDej, jardin));
                }
            }
            JOptionPane.showMessageDialog(this, "Hébergement ajouté avec succès !");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AjoutHebergementFenetre().setVisible(true));
    }
}