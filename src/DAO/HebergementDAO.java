package DAO;

import Modele.Hotel;
import Modele.Hebergement;
import Modele.Appartement;
import Modele.MaisonHotes;

import java.sql.*;
import java.math.BigDecimal;
import java.util.*;

public class HebergementDAO {

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


    public void ajouterHotel(Hotel h) {
        String sql = "INSERT INTO hebergement (nom, adresse, prix_par_nuit, description, specification) VALUES (?, ?, ?, ?, ?)";
        String sqlHotel = "INSERT INTO hotel (id_hebergement, nombre_etoiles, petit_dejeuner, piscine, spa) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement psHotel = conn.prepareStatement(sqlHotel)) {

            ps.setString(1, h.getNom());
            ps.setString(2, h.getAdresse());
            ps.setBigDecimal(3, h.getPrixParNuit());
            ps.setString(4, h.getDescription());
            ps.setString(5, h.getSpecification());
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
            }

            System.out.println("✅ Hôtel inséré : " + h.getNom());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void ajouterAppartement(Appartement a) {
        String sql = "INSERT INTO hebergement (nom, adresse, prix_par_nuit, description, specification) VALUES (?, ?, ?, ?, ?)";
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


    public void ajouterMaisonHotes(MaisonHotes m) {
        String sql = "INSERT INTO hebergement (nom, adresse, prix_par_nuit, description, specification) VALUES (?, ?, ?, ?, ?)";
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


    //Supprimer des hebergements
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

}