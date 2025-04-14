package Vue;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import DAO.HebergementDAO;
import Modele.Appartement;
import Modele.Hotel;
import Modele.MaisonHotes;

public class AjoutHebergementFenetre extends JFrame {

    private JComboBox<String> typeCombo;
    private JTextField nomField, adresseField, prixField, descriptionField, imageUrlField;
    private JCheckBox petitDejBox;

    // Spécifiques
    private JTextField nbEtoilesField, nbPiecesField, etageField;
    private JCheckBox piscineBox, spaBox, jardinBox;

    private JPanel specificFieldsPanel;

    public AjoutHebergementFenetre() {
        setTitle("Ajouter un hébergement");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // En-tête avec logo
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(new Color(0, 45, 114));
        JLabel logo = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Vue/BookingLogo.png")).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        JLabel title = new JLabel("  Booking");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        header.add(logo);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Titre
        JLabel titre = new JLabel("Ajout d'un hébergement");
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setForeground(new Color(0, 45, 114));
        titre.setHorizontalAlignment(SwingConstants.CENTER);

        // Type d'hébergement
        JPanel choixPanel = new JPanel();
        choixPanel.setBackground(Color.WHITE);
        choixPanel.add(new JLabel("Type d'hébergement :"));
        String[] types = {"Hôtel", "Appartement", "Maison d'hôtes"};
        typeCombo = new JComboBox<>(types);
        choixPanel.add(typeCombo);

        // Formulaire principal
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));

        nomField = new JTextField(25);
        adresseField = new JTextField(25);
        prixField = new JTextField(25);
        descriptionField = new JTextField(25);
        imageUrlField = new JTextField(25);

        nomField.setMaximumSize(new Dimension(400, 30));
        adresseField.setMaximumSize(new Dimension(400, 30));
        prixField.setMaximumSize(new Dimension(400, 30));
        descriptionField.setMaximumSize(new Dimension(400, 30));
        imageUrlField.setMaximumSize(new Dimension(400, 30));

        petitDejBox = new JCheckBox("Petit déjeuner inclus");

        formPanel.add(titre);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(choixPanel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(createLabeledField("Nom :", nomField));
        formPanel.add(createLabeledField("Adresse :", adresseField));
        formPanel.add(createLabeledField("Prix par nuit :", prixField));
        formPanel.add(createLabeledField("Description :", descriptionField));
        formPanel.add(createLabeledField("URL de l'image :", imageUrlField));
        formPanel.add(petitDejBox);

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

    private void updateSpecificFields(String type) {
        specificFieldsPanel.removeAll();

        switch (type) {
            case "Hôtel" -> {
                nbEtoilesField = new JTextField();
                piscineBox = new JCheckBox("Piscine");
                spaBox = new JCheckBox("Spa");
                specificFieldsPanel.add(createLabeledField("Nombre d'étoiles :", nbEtoilesField));
                specificFieldsPanel.add(piscineBox);
                specificFieldsPanel.add(spaBox);
            }
            case "Appartement" -> {
                nbPiecesField = new JTextField();
                etageField = new JTextField();
                specificFieldsPanel.add(createLabeledField("Nombre de pièces :", nbPiecesField));
                specificFieldsPanel.add(createLabeledField("Étage :", etageField));
            }
            case "Maison d'hôtes" -> {
                jardinBox = new JCheckBox("Jardin");
                specificFieldsPanel.add(jardinBox);
            }
        }

        specificFieldsPanel.revalidate();
        specificFieldsPanel.repaint();
    }

    private void ajouterHebergement() {
        String nom = nomField.getText().trim();
        String adresse = adresseField.getText().trim();
        String prixStr = prixField.getText().trim();
        String description = descriptionField.getText().trim();
        String imageUrl = imageUrlField.getText().trim();
        boolean petitDej = petitDejBox.isSelected();
        String type = (String) typeCombo.getSelectedItem();

        if (nom.isEmpty() || adresse.isEmpty() || prixStr.isEmpty() || description.isEmpty() || imageUrl.isEmpty()) {
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
                    dao.ajouterHotel(new Hotel(0, nom, adresse, prix, description, "Hôtel", imageUrl, etoiles, petitDej, piscine, spa));
                }
                case "Appartement" -> {
                    int pieces = Integer.parseInt(nbPiecesField.getText().trim());
                    int etage = Integer.parseInt(etageField.getText().trim());
                    dao.ajouterAppartement(new Appartement(0, nom, adresse, prix, description, "Appartement", imageUrl, pieces, petitDej, etage));
                }
                case "Maison d'hôtes" -> {
                    boolean jardin = jardinBox.isSelected();
                    dao.ajouterMaisonHotes(new MaisonHotes(0, nom, adresse, prix, description, "Maison d'hôtes", imageUrl, petitDej, jardin));
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