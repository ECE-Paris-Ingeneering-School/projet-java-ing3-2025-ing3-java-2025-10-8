package Vue;

import DAO.HebergementDAO;
import Modele.Hebergement;
import Modele.Appartement;
import Modele.MaisonHotes;
import Modele.Hotel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Fenêtre d'administration qui permet de modifier les hébergements
 * Cette interface propose trois onglets :
 * - Hôtels
 * - Appartements
 * - Maisons d'hôtes
 * Chaque hébergement est affiché sous forme de carte avec ses informations comme dans l'accueil
 * et un bouton pour ouvrir une fenêtre de modification
 */
public class ModifierHebergementFenetre extends JFrame{

    /**
     * Constructeur principal
     * Initialise l'interface avec les onglets et les boutons
     */
    public ModifierHebergementFenetre() {
        setTitle("Modifier un hébergement");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //Création des onglets pour chaque type
        JTabbedPane onglets = new JTabbedPane();
        onglets.add("Hôtels", creerPanelParType("Hotel"));
        onglets.add("Appartements", creerPanelParType("Appartement"));
        onglets.add("Maisons d'hôtes", creerPanelParType("MaisonHotes"));
        onglets.setFont(new Font("Arial", Font.BOLD, 14));
        onglets.setBackground(new Color(0, 45, 114)); // couleur de fond
        onglets.setForeground(Color.WHITE);

        add(onglets, BorderLayout.CENTER);

        //bouton retour
        JButton retour = new JButton("Retour à l'accueil admin");
        retour.setBackground(new Color(0, 45, 114)); // couleur de fond
        retour.setForeground(Color.WHITE);
        retour.addActionListener(e -> {
            dispose();
            new AccueilAdminFenetre().setVisible(true);
        });

        JPanel bas = new JPanel();
        bas.add(retour);
        add(bas, BorderLayout.SOUTH);
    }

    /**
     * Crée un panneau listant les hébergements suivant le type
     *
     * @param type le type d'hébergement à afficher
     * @return Un JScrollPane contenant les cartes d'hébergements
     */
    private JScrollPane creerPanelParType(String type) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        HebergementDAO dao = new HebergementDAO();
        List<Hebergement> hebergements = dao.getAllHebergements();

        for (Hebergement h : hebergements) {
            boolean afficher = (type.equals("Hotel") && h instanceof Hotel)
                    || (type.equals("Appartement") && h instanceof Appartement)
                    || (type.equals("MaisonHotes") && h instanceof MaisonHotes);

            if (afficher) {
                panel.add(creerCarteAvecModification(h));
                panel.add(Box.createVerticalStrut(10));
            }
        }

        JScrollPane scroll = new JScrollPane(panel);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    /**
     * Crée une carte visuelle pour un hébergement
     * Affiche les informations et un bouton pour modifier
     *
     * @param h l'hébergement concerné
     * @return Un JPanel représentant la carte
     */
    private JPanel creerCarteAvecModification(Hebergement h) {
        JPanel carte = new JPanel();
        carte.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        carte.setBackground(Color.WHITE);
        carte.setLayout(new BorderLayout());
        carte.setPreferredSize(new Dimension(850, 120));

        // Image à gauche
        JLabel imageLabel = new JLabel("[Image]");
        imageLabel.setPreferredSize(new Dimension(150, 100));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        if (h.getImageUrl() != null && !h.getImageUrl().isEmpty()) {
            try {
                java.net.URL imageUrl = getClass().getClassLoader().getResource(h.getImageUrl());
                if (imageUrl != null) {
                    ImageIcon icon = new ImageIcon(imageUrl);
                    Image img = icon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(img));
                    imageLabel.setText("");
                }
            } catch (Exception e) {
                imageLabel.setText("[Image]");
            }
        }

        carte.add(imageLabel, BorderLayout.WEST);

        // Infos au centre
        JPanel infos = new JPanel();
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));
        infos.setBackground(Color.WHITE);

        JLabel nom = new JLabel(h.getNom());
        nom.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel adresse = new JLabel(h.getAdresse());
        JLabel prix = new JLabel(h.getPrixParNuit() + " € / nuit");
        prix.setForeground(new Color(255, 128, 0));
        JLabel description = new JLabel(h.getDescription());

        infos.add(nom);
        infos.add(adresse);
        infos.add(prix);
        infos.add(description);

        carte.add(infos, BorderLayout.CENTER);

        // Bouton suppression à droite
        JButton boutonModifier = new JButton("0");
        boutonModifier.setFont(new Font("Arial", Font.BOLD, 20));
        boutonModifier.setForeground(new Color(255, 128, 0));
        boutonModifier.setBackground(Color.WHITE);
        boutonModifier.setFocusPainted(false);
        boutonModifier.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        boutonModifier.addActionListener(e -> {
            new ModificationHebergementFenetre(h).setVisible(true);
        });


        carte.add(boutonModifier, BorderLayout.EAST);
        return carte;
    }

    /**
     * Méthode principale pour lancer la fenêtre indépendamment
     *
     * @param args Arguments de la ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ModifierHebergementFenetre().setVisible(true));
    }
}
