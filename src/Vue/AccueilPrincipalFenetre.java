package Vue;

import DAO.HebergementDAO;
import Modele.*;
import DAO.ClientDAO;
import DAO.ReservationDAO;
import java.sql.Connection;
import DAO.ConnexionBdd;
import DAO.AvisDAO;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

/**
 * Fenêtre principale de l'application de réservation d'hébergements.
 * Cette fenêtre permet à l'utilisateur de rechercher des hébergements en fonction de critères et de filtrer les résultats.
 * Elle affiche également des informations détaillées sur chaque hébergement et permet de procéder à une réservation.
 */
public class AccueilPrincipalFenetre extends JFrame {

    private JPanel resultPanel;
    private JTextField prixMinField, prixMaxField;
    private Client clientConnecte;
    private JCheckBoxMenuItem cbHotel, cbAppartement, cbMaison;
    private JCheckBoxMenuItem cbPetitDej, cbPiscine, cbSpa, cbJardin;

    /**
     * Constructeur de la fenêtre principale.
     *
     * @param clientConnecte Le client connecté à l'application.
     */
    public AccueilPrincipalFenetre(Client clientConnecte) {
        this.clientConnecte = clientConnecte;

        // Configuration de l'interface utilisateur
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 13));
        UIManager.put("CheckBox.font", new Font("Segoe UI", Font.PLAIN, 13));

        setTitle("Booking App");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color bleuBooking = Color.decode("#003580");
        Color orangeBooking = new Color(255, 128, 0);

        // --- BARRE SUPERIEURE ---
        JPanel topBar = new JPanel(null);
        topBar.setBackground(bleuBooking);
        topBar.setPreferredSize(new Dimension(getWidth(), 120));

        JLabel logo = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Vue/BookingLogo.png"))
                .getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        logo.setBounds(20, 20, 50, 50);
        topBar.add(logo);

        JLabel titleLabel = new JLabel("Trouvez votre hébergement");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(300, 10, 600, 30);
        topBar.add(titleLabel);

        JLabel profileIcon = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/Vue/profile.png"))
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        profileIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        profileIcon.setBounds(1120, 20, 40, 40);
        topBar.add(profileIcon);

        JButton btnFiltrePrincipal = new JButton("Filtrez votre recherche");
        btnFiltrePrincipal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnFiltrePrincipal.setBackground(Color.WHITE);
        btnFiltrePrincipal.setForeground(bleuBooking);
        btnFiltrePrincipal.setFocusPainted(false);
        btnFiltrePrincipal.setBounds(500, 55, 200, 35);
        topBar.add(btnFiltrePrincipal);

        // Menu déroulant profil
        JPopupMenu menuProfil = new JPopupMenu();

        if (clientConnecte != null) {
            JMenuItem itemResa = new JMenuItem("Mes réservations");
            JMenuItem itemDeconnexion = new JMenuItem("Se déconnecter");
            menuProfil.add(itemResa);
            menuProfil.add(itemDeconnexion);

            itemResa.addActionListener(e -> {
                Connection connection = ConnexionBdd.seConnecter();
                if (connection != null) {
                    HebergementDAO hebergementDAO = new HebergementDAO(connection);
                    ReservationDAO reservationDAO = new ReservationDAO(connection, hebergementDAO);
                    new MesReservationsFenetre(clientConnecte, reservationDAO).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données.");
                }
            });

            itemDeconnexion.addActionListener(e -> {
                dispose();
                new ConnexionFenetre().setVisible(true);
            });
        } else {
            JMenuItem itemConnexion = new JMenuItem("Se connecter");
            menuProfil.add(itemConnexion);
            itemConnexion.addActionListener(e -> {
                dispose();
                new ConnexionFenetre().setVisible(true);
            });
        }

        profileIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                menuProfil.show(profileIcon, 0, profileIcon.getHeight());
            }
        });

        // Menu déroulant filtres (invisible pour l’instant — lié à btnFiltrePrincipal)
        JPopupMenu menuFiltre = new JPopupMenu();
        cbHotel = new JCheckBoxMenuItem("Hôtel");
        cbAppartement = new JCheckBoxMenuItem("Appartement");
        cbMaison = new JCheckBoxMenuItem("Maison d'hôtes");
        cbPetitDej = new JCheckBoxMenuItem("Petit déjeuner inclus");
        cbPiscine = new JCheckBoxMenuItem("Spa et centre de bien-être");
        cbSpa = new JCheckBoxMenuItem("Spa");
        cbJardin = new JCheckBoxMenuItem("Jardin");

        prixMinField = new JTextField(5);
        prixMaxField = new JTextField(5);
        prixMinField.setBackground(Color.WHITE);
        prixMaxField.setBackground(Color.WHITE);

        JLabel minLabel = new JLabel("Min €:");
        JLabel maxLabel = new JLabel("Max €:");

        JPanel prixPanel = new JPanel();
        prixPanel.setBackground(Color.WHITE);
        prixPanel.add(minLabel);
        prixPanel.add(prixMinField);
        prixPanel.add(maxLabel);
        prixPanel.add(prixMaxField);

        JButton appliquerFiltreBtn = new JButton("Appliquer les filtres");
        appliquerFiltreBtn.setBackground(Color.decode("#003580"));
        appliquerFiltreBtn.setForeground(Color.WHITE);
        appliquerFiltreBtn.setFocusPainted(false);
        appliquerFiltreBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));

        appliquerFiltreBtn.addActionListener(e -> filtrerHebergements());

        menuFiltre.add(cbHotel);
        menuFiltre.add(cbAppartement);
        menuFiltre.add(cbMaison);
        menuFiltre.addSeparator();
        menuFiltre.add(cbPetitDej);
        menuFiltre.add(cbPiscine);
        menuFiltre.add(cbSpa);
        menuFiltre.add(cbJardin);
        menuFiltre.addSeparator();
        menuFiltre.add(prixPanel);
        menuFiltre.add(appliquerFiltreBtn);

        btnFiltrePrincipal.addActionListener(e -> menuFiltre.show(btnFiltrePrincipal, 0, btnFiltrePrincipal.getHeight()));

        add(topBar, BorderLayout.NORTH);

        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(resultPanel);
        add(scrollPane, BorderLayout.CENTER);

        filtrerHebergements();
    }

    /**
     * Applique les filtres sur les hébergements et met à jour l'affichage des résultats.
     */
    private void filtrerHebergements() {
        HebergementDAO dao = new HebergementDAO();
        List<Hebergement> tous = dao.getAllHebergements();
        List<Hebergement> filtres = new ArrayList<>();

        BigDecimal prixMin = null;
        BigDecimal prixMax = null;
        try {
            if (!prixMinField.getText().isEmpty()) prixMin = new BigDecimal(prixMinField.getText());
            if (!prixMaxField.getText().isEmpty()) prixMax = new BigDecimal(prixMaxField.getText());
        } catch (NumberFormatException ignored) {}

        for (Hebergement h : tous) {
            boolean ok = true;

            if (prixMin != null && h.getPrixParNuit().compareTo(prixMin) < 0) ok = false;
            if (prixMax != null && h.getPrixParNuit().compareTo(prixMax) > 0) ok = false;

            if (cbHotel.isSelected() || cbAppartement.isSelected() || cbMaison.isSelected()) {
                if (h instanceof Hotel && !cbHotel.isSelected()) ok = false;
                if (h instanceof Appartement && !cbAppartement.isSelected()) ok = false;
                if (h instanceof MaisonHotes && !cbMaison.isSelected()) ok = false;
            }

            if (cbPetitDej.isSelected()) {
                boolean offre = false;
                if (h instanceof Hotel) offre = ((Hotel) h).isPetitDejeuner();
                if (h instanceof Appartement) offre = ((Appartement) h).isPetitDejeuner();
                if (h instanceof MaisonHotes) offre = ((MaisonHotes) h).isPetitDejeuner();
                if (!offre) ok = false;
            }

            if (cbPiscine.isSelected() && h instanceof Hotel && !((Hotel) h).isPiscine()) ok = false;
            if (cbSpa.isSelected() && h instanceof Hotel && !((Hotel) h).isSpa()) ok = false;
            if (cbJardin.isSelected() && h instanceof MaisonHotes && !((MaisonHotes) h).isJardin()) ok = false;

            if (ok) filtres.add(h);
        }

        resultPanel.removeAll();

        if (filtres.isEmpty()) {
            JLabel rien = new JLabel("Aucun hébergement ne correspond à vos critères.", SwingConstants.CENTER);
            rien.setFont(new Font("Arial", Font.BOLD, 16));
            rien.setForeground(Color.GRAY);
            resultPanel.add(rien);
        } else {
            for (Hebergement h : filtres) {
                resultPanel.add(creerCarteHebergement(h));
                resultPanel.add(Box.createVerticalStrut(10));
            }
        }

        resultPanel.revalidate();
        resultPanel.repaint();
    }

    /**
     * Crée une carte de présentation pour un hébergement.
     *
     * @param h L'hébergement à afficher.
     * @return Le panneau représentant la carte de l'hébergement.
     */
    private JPanel creerCarteHebergement(Hebergement h) {
        JPanel carte = new JPanel();
        carte.setBackground(new Color(245, 245, 245));
        carte.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        carte.setLayout(new BorderLayout());
        carte.setPreferredSize(new Dimension(1000, 180));

        // === Image ===
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(220, 150));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            List<String> images = h.getImageUrls();
            if (images != null && !images.isEmpty()) {
                java.net.URL imageUrl = getClass().getClassLoader().getResource(images.get(0));
                if (imageUrl != null) {
                    ImageIcon icon = new ImageIcon(imageUrl);
                    Image img = icon.getImage().getScaledInstance(220, 150, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(img));
                    imageLabel.setText("");
                } else {
                    imageLabel.setText("[Image introuvable]");
                }
            } else {
                imageLabel.setText("[Aucune image]");
            }
        } catch (Exception e) {
            imageLabel.setText("[Erreur image]");
        }

        carte.add(imageLabel, BorderLayout.WEST);

        // === Infos ===
        JPanel infos = new JPanel();
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));
        infos.setBackground(Color.WHITE);
        infos.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        JPanel nomEtEtoiles = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        nomEtEtoiles.setBackground(Color.WHITE);

        JLabel nomLabel = new JLabel(h.getNom());
        nomLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        nomEtEtoiles.add(nomLabel);

        if (h instanceof Hotel) {
            int nbEtoiles = ((Hotel) h).getNombreEtoiles();
            for (int i = 0; i < nbEtoiles; i++) {
                JLabel star = new JLabel("★");
                star.setForeground(new Color(255, 191, 0));
                star.setFont(new Font("SansSerif", Font.PLAIN, 18));
                nomEtEtoiles.add(star);
            }
            for (int i = nbEtoiles; i < 5; i++) {
                JLabel starEmpty = new JLabel("★");
                starEmpty.setForeground(Color.LIGHT_GRAY);
                starEmpty.setFont(new Font("SansSerif", Font.PLAIN, 18));
                nomEtEtoiles.add(starEmpty);
            }
        }

        infos.add(nomEtEtoiles);


// === Affichage de la moyenne des avis (après le nom et les étoiles) ===
        AvisDAO avisDAO = new AvisDAO();
        List<Avis> avisList = avisDAO.getAvisParHebergement(h.getIdHebergement());

        JLabel moyenneAvisLabel;
        if (!avisList.isEmpty()) {
            double sommeNotes = 0;
            for (Avis avis : avisList) {
                sommeNotes += avis.getNote();
            }
            double moyenne = Math.round((sommeNotes / avisList.size()) * 10.0) / 10.0;
            moyenneAvisLabel = new JLabel("Avis clients : " + moyenne + " / 5");
            moyenneAvisLabel.setFont(new Font("Arial", Font.BOLD, 13));
            moyenneAvisLabel.setForeground(Color.BLACK);
        } else {
            moyenneAvisLabel = new JLabel("Avis clients : Aucun avis pour le moment");
            moyenneAvisLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            moyenneAvisLabel.setForeground(Color.GRAY);
        }

        infos.add(moyenneAvisLabel);
        infos.add(Box.createVerticalStrut(10));

        JLabel adresse = new JLabel(h.getAdresse());
        adresse.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel prix = new JLabel(h.getPrixParNuit() + " € / nuit");
        prix.setForeground(new Color(255, 128, 0));
        prix.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel desc = new JLabel("<html><p style='width:700px'>" + h.getDescription() + "</p></html>");
        desc.setFont(new Font("Arial", Font.PLAIN, 14));

        infos.add(Box.createVerticalStrut(5));
        infos.add(adresse);
        infos.add(Box.createVerticalStrut(5));
        infos.add(prix);
        infos.add(Box.createVerticalStrut(5));

        // === Boutons ===
        JButton btnDispo = new JButton("Réservez");
        btnDispo.setBackground(new Color(0, 113, 194));
        btnDispo.setForeground(Color.WHITE);
        btnDispo.setFocusPainted(false);

        btnDispo.addActionListener(e -> {
            // Récupérer la connexion via la classe ConnexionBdd
            Connection connection = ConnexionBdd.seConnecter();

            if (connection != null) {
                // Initialiser ReservationDAO avec la connexion
                HebergementDAO hebergementDAO = new HebergementDAO(connection);
                ReservationDAO reservationDAO = new ReservationDAO(connection, hebergementDAO);

                // Puis instancier DisponibiliteFenetre en passant les trois arguments nécessaires
                new DisponibiliteFenetre(h, clientConnecte, reservationDAO).setVisible(true);
            } else {
                // Gérer le cas où la connexion à la base de données échoue
                JOptionPane.showMessageDialog(null, "Erreur de connexion à la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnCarte = new JButton("Voir sur la carte");
        btnCarte.setBackground(new Color(0, 113, 194));
        btnCarte.setForeground(Color.WHITE);
        btnCarte.setFocusPainted(false);

        btnCarte.addActionListener(e -> {
            try {
                String adresseUrl = h.getAdresse().replace(" ", "+");
                String url = "https://www.google.com/maps/search/?api=1&query=" + adresseUrl;
                Desktop.getDesktop().browse(new java.net.URI(url));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ouverture de la carte.");
            }
        });

        JPanel boutonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        boutonsPanel.setBackground(Color.WHITE);
        boutonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        boutonsPanel.add(btnDispo);
        boutonsPanel.add(btnCarte);

        infos.add(Box.createVerticalStrut(10));
        infos.add(boutonsPanel);

        carte.add(infos, BorderLayout.CENTER);

        carte.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        carte.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new HebergementDetailFenetre(h, clientConnecte).setVisible(true);
            }
        });

        return carte;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AccueilPrincipalFenetre(null).setVisible(true));
    }
}