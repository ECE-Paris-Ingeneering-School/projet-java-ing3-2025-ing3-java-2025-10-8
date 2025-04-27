package DAO;

import Modele.Hotel;
import Modele.Hebergement;
import Modele.Appartement;
import Modele.MaisonHotes;

import java.io.File;
import java.sql.*;
import java.math.BigDecimal;
import java.util.*;
import java.sql.Connection;
import DAO.ConnexionBdd;

/**
 * Classe HebergementDAO permettant d'effectuer des opérations CRUD sur les hébergements (hôtels, appartements, maisons d'hôtes).
 * Elle interagit avec les tables hebergement, hotel, appartement, maisonhotes, et hebergement_images.
 */
public class HebergementDAO {

    /** Connexion à la base de données. */
    private Connection connection;

    /** List d'images. */
    private List<File> fichiersImages = new ArrayList<>();


    /**
     * Constructeur avec connexion personnalisée.
     * @param connection Connexion à la base de données.
     */
    public HebergementDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Constructeur par défaut utilisant la connexion globale définie dans ConnexionBdd.
     */
    public HebergementDAO() {
        this.connection = ConnexionBdd.seConnecter();
    }

    /**
     * Met à jour les informations de base d'un hébergement (sans le type spécifique).
     * @param conn Connexion active.
     * @param h Hébergement à mettre à jour.
     * @throws SQLException si erreur SQL.
     */
    private void updateHebergementBase(Connection conn, Hebergement h) throws SQLException {
        String sql = "UPDATE hebergement SET nom = ?, adresse = ?, prix_par_nuit = ?, description = ?, specification = ? WHERE id_hebergement = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, h.getNom());
            ps.setString(2, h.getAdresse());
            ps.setBigDecimal(3, h.getPrixParNuit());
            ps.setString(4, h.getDescription());
            ps.setString(5, h.getSpecification());
            ps.setLong(6, h.getIdHebergement());
            ps.executeUpdate();
        }
    }

    /**
     * Insère les URLs d'images liées à un hébergement.
     * @param conn Connexion active.
     * @param idHebergement ID de l'hébergement.
     * @param imageUrls Liste des URL d'images.
     * @throws SQLException si erreur SQL.
     */
    private void insererImagesHebergement(Connection conn, long idHebergement, List<String> imageUrls) throws SQLException {
        if (imageUrls == null || imageUrls.isEmpty()) return;
        String sql = "INSERT INTO hebergement_images (id_hebergement, image_url) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String url : imageUrls) {
                ps.setLong(1, idHebergement);
                ps.setString(2, url);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    /**
     * Récupère la liste des URLs d'images associées à un hébergement.
     * @param conn Connexion active.
     * @param idHebergement ID de l'hébergement.
     * @return Liste d'URLs d'images.
     * @throws SQLException si erreur SQL.
     */
    private List<String> recupererImagesHebergement(Connection conn, long idHebergement) throws SQLException {
        List<String> images = new ArrayList<>();
        String sql = "SELECT image_url FROM hebergement_images WHERE id_hebergement = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idHebergement);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                images.add(rs.getString("image_url"));
            }
        }
        return images;
    }

    /**
     * Ajoute un hôtel dans la base de données.
     * @param h L'hôtel à ajouter.
     */
    public void ajouterHotel(Hotel h) {
        String sql = "INSERT INTO hebergement (nom, adresse, prix_par_nuit, description, specification, disponibilite) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlHotel = "INSERT INTO hotel (id_hebergement, nombre_etoiles, petit_dejeuner, piscine, spa) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement psHotel = conn.prepareStatement(sqlHotel)) {

            System.out.println(">>> Insertion hôtel : " + h.getNom());

            ps.setString(1, h.getNom());
            ps.setString(2, h.getAdresse());
            ps.setBigDecimal(3, h.getPrixParNuit());
            ps.setString(4, h.getDescription());
            ps.setString(5, h.getSpecification());
            ps.setBoolean(6, true);  // ou h.getDisponibilite()

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                long idGenere = rs.getLong(1);

                psHotel.setLong(1, idGenere);
                psHotel.setInt(2, h.getNombreEtoiles());
                psHotel.setBoolean(3, h.isPetitDejeuner());
                psHotel.setBoolean(4, h.isPiscine());
                psHotel.setBoolean(5, h.isSpa());
                psHotel.executeUpdate();

                insererImagesHebergement(conn, idGenere, h.getImageUrls());

                System.out.println("Hôtel ajouté avec ID : " + idGenere);
            } else {
                System.out.println("Aucune clé générée !");
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'hôtel : " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Ajoute un appartement dans la base de données.
     * @param a L'appartement à ajouter.
     */
    public void ajouterAppartement(Appartement a) {
        String sql = "INSERT INTO hebergement (nom, adresse, prix_par_nuit, description, specification, disponibilite) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlAppart = "INSERT INTO appartement (id_hebergement, nombre_pieces, petit_dejeuner, etage) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement psAppart = conn.prepareStatement(sqlAppart)) {

            // Insertion dans hebergement
            ps.setString(1, a.getNom());
            ps.setString(2, a.getAdresse());
            ps.setBigDecimal(3, a.getPrixParNuit());
            ps.setString(4, a.getDescription());
            ps.setString(5, a.getSpecification());
            ps.setBoolean(6, true);

            ps.executeUpdate();

            // Récupération de l'ID généré
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                long idGenere = rs.getLong(1);

                // Insertion dans appartement
                psAppart.setLong(1, idGenere);
                psAppart.setInt(2, a.getNombrePieces());
                psAppart.setBoolean(3, a.isPetitDejeuner());
                psAppart.setInt(4, a.getEtage());
                psAppart.executeUpdate();

                // Insertion des images dans hebergement_images
                insererImagesHebergement(conn, idGenere, a.getImageUrls());

                System.out.println("✅ Appartement inséré : " + a.getNom());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ajoute une maison d'hôtes dans la base de données.
     * @param m La maison d'hôtes à ajouter.
     */
    public void ajouterMaisonHotes(MaisonHotes m) {
        String sql = "INSERT INTO hebergement (nom, adresse, prix_par_nuit, description, specification, disponibilite) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlMaison = "INSERT INTO maisonhotes (id_hebergement, petit_dejeuner, jardin) VALUES (?, ?, ?)";

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement psMaison = conn.prepareStatement(sqlMaison)) {

            // Insertion dans hebergement
            ps.setString(1, m.getNom());
            ps.setString(2, m.getAdresse());
            ps.setBigDecimal(3, m.getPrixParNuit());
            ps.setString(4, m.getDescription());
            ps.setString(5, m.getSpecification());
            ps.setBoolean(6, true);

            ps.executeUpdate();

            // Récupérer l'ID généré
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                long idGenere = rs.getLong(1);

                // Insertion dans maisonhotes
                psMaison.setLong(1, idGenere);
                psMaison.setBoolean(2, m.isPetitDejeuner());
                psMaison.setBoolean(3, m.isJardin());
                psMaison.executeUpdate();

                // Insertion des images
                insererImagesHebergement(conn, idGenere, m.getImageUrls());

                System.out.println("✅ Maison d'hôtes insérée : " + m.getNom());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // ---------------------- READ ----------------------

    /**
     * Recherche une maison d'hôtes par son ID.
     * @param id ID de la maison d'hôtes.
     * @return Objet MaisonHotes ou null.
     */
    public MaisonHotes findMaisonHotesById(int id) {
        String sqlHebergement = "SELECT * FROM hebergement WHERE id_hebergement = ?";
        String sqlMaison = "SELECT * FROM maisonhotes WHERE id_hebergement = ?";

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps1 = conn.prepareStatement(sqlHebergement);
             PreparedStatement ps2 = conn.prepareStatement(sqlMaison)) {

            ps1.setInt(1, id);
            ps2.setInt(1, id);

            ResultSet rs1 = ps1.executeQuery();
            ResultSet rs2 = ps2.executeQuery();

            if (rs1.next() && rs2.next()) {
                int idHebergement = rs1.getInt("id_hebergement");
                String nom = rs1.getString("nom");
                String adresse = rs1.getString("adresse");
                BigDecimal prix = rs1.getBigDecimal("prix_par_nuit");
                String description = rs1.getString("description");
                String specification = rs1.getString("specification");

                List<String> images = recupererImagesHebergement(conn, idHebergement);

                boolean petitDejeuner = rs2.getInt("petit_dejeuner") == 1;
                boolean jardin = rs2.getInt("jardin") == 1;

                return new MaisonHotes(idHebergement, nom, adresse, prix, description, specification, images, petitDejeuner, jardin);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public Hotel findHotelById(int id) {
        String sqlHebergement = "SELECT * FROM hebergement WHERE id_hebergement = ?";
        String sqlHotel = "SELECT * FROM hotel WHERE id_hebergement = ?";

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps1 = conn.prepareStatement(sqlHebergement);
             PreparedStatement ps2 = conn.prepareStatement(sqlHotel)) {

            ps1.setInt(1, id);
            ps2.setInt(1, id);

            ResultSet rs1 = ps1.executeQuery();
            ResultSet rs2 = ps2.executeQuery();

            if (rs1.next() && rs2.next()) {
                int idHebergement = rs1.getInt("id_hebergement");
                String nom = rs1.getString("nom");
                String adresse = rs1.getString("adresse");
                BigDecimal prix = rs1.getBigDecimal("prix_par_nuit");
                String description = rs1.getString("description");
                String specification = rs1.getString("specification");

                List<String> images = recupererImagesHebergement(conn, idHebergement);

                int nombreEtoiles = rs2.getInt("nombre_etoiles");
                boolean petitDejeuner = rs2.getInt("petit_dejeuner") == 1;
                boolean piscine = rs2.getInt("piscine") == 1;
                boolean spa = rs2.getInt("spa") == 1;

                return new Hotel(idHebergement, nom, adresse, prix, description, specification, images, nombreEtoiles, petitDejeuner, piscine, spa);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public Appartement findAppartementById(int id) {
        String sqlHebergement = "SELECT * FROM hebergement WHERE id_hebergement = ?";
        String sqlAppart = "SELECT * FROM appartement WHERE id_hebergement = ?";

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps1 = conn.prepareStatement(sqlHebergement);
             PreparedStatement ps2 = conn.prepareStatement(sqlAppart)) {

            ps1.setInt(1, id);
            ps2.setInt(1, id);

            ResultSet rs1 = ps1.executeQuery();
            ResultSet rs2 = ps2.executeQuery();

            if (rs1.next() && rs2.next()) {
                int idHebergement = rs1.getInt("id_hebergement");
                String nom = rs1.getString("nom");
                String adresse = rs1.getString("adresse");
                BigDecimal prix = rs1.getBigDecimal("prix_par_nuit");
                String description = rs1.getString("description");
                String specification = rs1.getString("specification");

                List<String> images = recupererImagesHebergement(conn, idHebergement);

                int nombrePieces = rs2.getInt("nombre_pieces");
                boolean petitDejeuner = rs2.getInt("petit_dejeuner") == 1;
                int etage = rs2.getInt("etage");

                return new Appartement(idHebergement, nom, adresse, prix, description, specification, images, nombrePieces, petitDejeuner, etage);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    // ---------------------- FILTRAGE ----------------------

    /**
     * Récupère tous les hébergements enregistrés dans la base.
     * @return Liste des hébergements.
     */
    public List<Hebergement> getAllHebergements() {
        List<Hebergement> hebergements = new ArrayList<>();
        String sql = "SELECT id_hebergement FROM hebergement";

        try (Connection conn = ConnexionBdd.seConnecter();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id_hebergement");

                Appartement app = findAppartementById(id);
                if (app != null) {
                    hebergements.add(app);
                    continue;
                }

                Hotel hotel = findHotelById(id);
                if (hotel != null) {
                    hebergements.add(hotel);
                    continue;
                }

                MaisonHotes mh = findMaisonHotesById(id);
                if (mh != null) {
                    hebergements.add(mh);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hebergements;
    }

    /**
     * Récupère les hébergements filtrés selon différents critères.
     * @param type Type d'hébergement souhaité (Hotel, Appartement, MaisonHotes).
     * @param prixMin Prix minimum.
     * @param prixMax Prix maximum.
     * @param piscine Si la piscine est souhaitée.
     * @param petitDejeuner Si le petit-déjeuner est souhaité.
     * @param jardin Si le jardin est souhaité.
     * @return Liste d'hébergements correspondant aux filtres.
     */
    public List<Hebergement> getHebergementsAvecFiltres(String type, BigDecimal prixMin, BigDecimal prixMax,
                                                        Boolean piscine, Boolean petitDejeuner, Boolean jardin) {
        List<Hebergement> tous = getAllHebergements(); // On récupère tout
        List<Hebergement> filtres = new ArrayList<>();

        for (Hebergement h : tous) {
            boolean correspond = true;

            // Type
            if (type != null) {
                if (type.equals("Hotel") && !(h instanceof Hotel)) correspond = false;
                else if (type.equals("Appartement") && !(h instanceof Appartement)) correspond = false;
                else if (type.equals("MaisonHotes") && !(h instanceof MaisonHotes)) correspond = false;
            }

            // Prix
            if (prixMin != null && h.getPrixParNuit().compareTo(prixMin) < 0) correspond = false;
            if (prixMax != null && h.getPrixParNuit().compareTo(prixMax) > 0) correspond = false;

            // Piscine
            if (piscine != null && h instanceof Hotel) {
                if (((Hotel) h).isPiscine() != piscine) correspond = false;
            }

            // Petit déjeuner
            if (petitDejeuner != null) {
                boolean offrePd = false;
                if (h instanceof Hotel) offrePd = ((Hotel) h).isPetitDejeuner();
                else if (h instanceof Appartement) offrePd = ((Appartement) h).isPetitDejeuner();
                else if (h instanceof MaisonHotes) offrePd = ((MaisonHotes) h).isPetitDejeuner();
                if (offrePd != petitDejeuner) correspond = false;
            }

            // Jardin
            if (jardin != null && h instanceof MaisonHotes) {
                if (((MaisonHotes) h).isJardin() != jardin) correspond = false;
            }

            if (correspond) filtres.add(h);
        }

        return filtres;
    }


    /**
     * Supprime un hébergement par son ID.
     * @param idHebergement ID de l'hébergement.
     * @return true si suppression réussie, false sinon.
     */
    public boolean supprimerHebergementParId(int idHebergement) {
        String sql = "DELETE FROM hebergement WHERE id_hebergement = ?";
        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idHebergement);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Met à jour les informations principales d'un hébergement.
     * @param h Hébergement avec nouvelles valeurs.
     * @return true si mise à jour réussie, false sinon.
     */
    public boolean modifierHebergement(Hebergement h) {
        String sql = "UPDATE hebergement SET nom = ?, adresse = ?, prix_par_nuit = ?, description = ? WHERE id_hebergement = ?";
        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, h.getNom());
            ps.setString(2, h.getAdresse());
            ps.setBigDecimal(3, h.getPrixParNuit());
            ps.setString(4, h.getDescription());
            ps.setInt(5, h.getIdHebergement());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Vérifie la disponibilité d'un hébergement entre deux dates.
     * @param idHebergement ID de l'hébergement.
     * @param dateArrivee Date d'arrivée souhaitée.
     * @param dateDepart Date de départ souhaitée.
     * @return true si disponible, false sinon.
     */
    public boolean estDisponible(long idHebergement, String dateArrivee, String dateDepart) {
        boolean dispo = false;
        try (Connection conn = ConnexionBdd.seConnecter()) {
            String sql = "SELECT COUNT(*) AS nb FROM Reservation " +
                    "WHERE idHebergement = ? " +
                    "AND statut = 'payée' " +
                    "AND (dateArrivee < ? AND dateDepart > ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, idHebergement);
            stmt.setString(2, dateDepart);   // La nouvelle date de départ
            stmt.setString(3, dateArrivee);  // La nouvelle date d’arrivée
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                dispo = rs.getInt("nb") == 0; // S’il n’y a pas de chevauchement
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dispo;
    }


    /**
     * Récupère un hébergement selon son ID, en identifiant son type.
     * @param idHebergement ID de l'hébergement.
     * @return Objet Hebergement ou null.
     */
    public Hebergement getHebergementById(int idHebergement) {
        Hebergement hebergement = null;

        // On vérifie quel type d'hébergement c'est (hotel, appartement, maison d'hôtes...)
        // On utilise les méthodes findHotelById, findAppartementById, etc.
        Hotel hotel = findHotelById(idHebergement);
        if (hotel != null) {
            hebergement = hotel;
        }

        Appartement appartement = findAppartementById(idHebergement);
        if (appartement != null) {
            hebergement = appartement;
        }

        MaisonHotes maisonHotes = findMaisonHotesById(idHebergement);
        if (maisonHotes != null) {
            hebergement = maisonHotes;
        }

        return hebergement;
    }

    /**
     * Met à jour le nombre de chambres disponibles après une réservation.
     * @param idHebergement ID de l'hébergement.
     * @param chambresReservees Nombre de chambres à soustraire.
     * @return true si mise à jour effectuée, false sinon.
     */
    public boolean mettreAJourChambresDisponibles(int idHebergement, int chambresReservees) {
        String sql = "UPDATE hebergement SET chambres_disponibles = chambres_disponibles - ? WHERE id_hebergement = ?";
        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, chambresReservees);
            ps.setInt(2, idHebergement);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Met à jour l'état de disponibilité d'un hébergement.
     * @param idHebergement ID de l'hébergement.
     * @param disponible true si disponible, false sinon.
     * @return true si mise à jour effectuée, false sinon.
     */
    public boolean mettreAJourDisponibilite(int idHebergement, boolean disponible) {
        String sql = "UPDATE hebergement SET disponibilite = ? WHERE id_hebergement = ?";
        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, disponible); // true = disponible (1), false = non disponible (0)
            ps.setInt(2, idHebergement);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Récupère l'ID du dernier hébergement ajouté.
     * @return l'ID de l'hébergement
     */
    public long getDernierIdHebergementAjoute() {
        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement("SELECT MAX(id_hebergement) FROM hebergement")) {

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Ajoute plusieurs images pour un hébergement existant.
     * @param idHebergement ID de l'hébergement
     * @param cheminsImages Liste des chemins d'images
     */
    public void ajouterImagesPourHebergement(long idHebergement, List<String> cheminsImages) {
        try (Connection conn = ConnexionBdd.seConnecter()) {
            insererImagesHebergement(conn, idHebergement, cheminsImages);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}