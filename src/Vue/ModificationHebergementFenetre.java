package Vue;

import DAO.HebergementDAO;
import Modele.*;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

/**
 * Fenêtre permettant de modifier les informations d'un hébergement
 * Gère les différents types d'hébergement : hôtel, appartement et maison d'hôtes
 * Affiche les champs nom, adresse, prix, etc. et les champs spécifiques à chaque hébergement
 * Permet l'enregistrement des modifications directement dans la base de données
 *
 */

public class ModificationHebergementFenetre extends JFrame {
    private JTextField nomField, adresseField, prixField, descriptionField, imageUrlField;
    private JCheckBox petitDejBox;
    private JTextField nbEtoilesField, nbPiecesField, etageField;
    private JCheckBox piscineBox, spaBox, jardinBox;
    private JPanel specificFieldsPanel;

    private Hebergement hebergement;

    /**
     * Constructeur principal de la fenêtre de modification
     * @param h L'hébergement à modifier
     */
    public ModificationHebergementFenetre(Hebergement h) {
        this.hebergement = h;

        setTitle("Modifier un hébergement");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        //Bandeau avec le logo
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(new Color(0, 45, 114));
        JLabel logo = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Vue/BookingLogo.png")).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        JLabel title = new JLabel("  Booking");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        header.add(logo);
        header.add(title);
        add(header, BorderLayout.NORTH);

        //Formulaire principal
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 60));
        formPanel.setBackground(Color.WHITE);

        //Champs communs aux hebergements
        nomField = new JTextField(h.getNom());
        adresseField = new JTextField(h.getAdresse());
        prixField = new JTextField(h.getPrixParNuit().toString());
        descriptionField = new JTextField(h.getDescription());
        imageUrlField = new JTextField(h.getImageUrl() != null ? h.getImageUrl() : "");
        petitDejBox = new JCheckBox("Petit déjeuner inclus");

        formPanel.add(createLabeledField("Nom :", nomField));
        formPanel.add(createLabeledField("Adresse :", adresseField));
        formPanel.add(createLabeledField("Prix par nuit :", prixField));
        formPanel.add(createLabeledField("Description :", descriptionField));
        formPanel.add(createLabeledField("URL image (ex: img/hotel1.jpg) :", imageUrlField));
        formPanel.add(petitDejBox);

        //Champs spécifiques aux hebergements
        specificFieldsPanel = new JPanel();
        specificFieldsPanel.setLayout(new BoxLayout(specificFieldsPanel, BoxLayout.Y_AXIS));
        specificFieldsPanel.setBackground(Color.WHITE);
        formPanel.add(specificFieldsPanel);

        if (h instanceof Hotel hotel) {
            nbEtoilesField = new JTextField(String.valueOf(hotel.getNombreEtoiles()));
            piscineBox = new JCheckBox("Piscine", hotel.isPiscine());
            spaBox = new JCheckBox("Spa", hotel.isSpa());
            petitDejBox.setSelected(hotel.isPetitDejeuner());

            specificFieldsPanel.add(createLabeledField("Nombre d'étoiles :", nbEtoilesField));
            specificFieldsPanel.add(piscineBox);
            specificFieldsPanel.add(spaBox);
        } else if (h instanceof Appartement appart) {
            nbPiecesField = new JTextField(String.valueOf(appart.getNombrePieces()));
            etageField = new JTextField(String.valueOf(appart.getEtage()));
            petitDejBox.setSelected(appart.isPetitDejeuner());

            specificFieldsPanel.add(createLabeledField("Nombre de pièces :", nbPiecesField));
            specificFieldsPanel.add(createLabeledField("Étage :", etageField));
        } else if (h instanceof MaisonHotes maison) {
            jardinBox = new JCheckBox("Jardin", maison.isJardin());
            petitDejBox.setSelected(maison.isPetitDejeuner());

            specificFieldsPanel.add(jardinBox);
        }

        //bouton pour enregistrer les modif
        JButton enregistrer = new JButton("Enregistrer les modifications");
        enregistrer.setBackground(new Color(255, 128, 0));
        enregistrer.setForeground(Color.WHITE);
        enregistrer.setAlignmentX(Component.CENTER_ALIGNMENT);
        enregistrer.addActionListener(e -> modifierHebergement());

        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(enregistrer);
        add(formPanel, BorderLayout.CENTER);
    }

    /**
     * Crée un champ de formulaire avec un label associé
     * @param label le texte du label
     * @param field le champ de saisie correspondant
     * @return le panneau contenant le label et le champ
     */
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

    /**
     * Récupère les données saisies et met à jour l'hébergement en base de données.
     * Affiche un message de succès ou d'erreur.
     */
    private void modifierHebergement() {
        try {
            hebergement.setNom(nomField.getText().trim());
            hebergement.setAdresse(adresseField.getText().trim());
            hebergement.setPrixParNuit(new BigDecimal(prixField.getText().trim()));
            hebergement.setDescription(descriptionField.getText().trim());
            hebergement.setImageUrls(
                    java.util.Arrays.stream(imageUrlField.getText().trim().split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .toList()
            );
            //MAJ selon type
            if (hebergement instanceof Hotel hotel) {
                hotel.setNombreEtoiles(Integer.parseInt(nbEtoilesField.getText().trim()));
                hotel.setPetitDejeuner(petitDejBox.isSelected());
                hotel.setPiscine(piscineBox.isSelected());
                hotel.setSpa(spaBox.isSelected());
            } else if (hebergement instanceof Appartement appart) {
                appart.setNombrePieces(Integer.parseInt(nbPiecesField.getText().trim()));
                appart.setEtage(Integer.parseInt(etageField.getText().trim()));
                appart.setPetitDejeuner(petitDejBox.isSelected());
            } else if (hebergement instanceof MaisonHotes maison) {
                maison.setJardin(jardinBox.isSelected());
                maison.setPetitDejeuner(petitDejBox.isSelected());
            }

            //MAJ BDD
            boolean success = new HebergementDAO().modifierHebergement(hebergement);
            if (success) {
                JOptionPane.showMessageDialog(this, "Modifications enregistrées !");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }
}
