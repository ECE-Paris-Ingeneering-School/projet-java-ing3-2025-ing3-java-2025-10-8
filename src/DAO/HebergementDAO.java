package DAO;

import Modele.Hotel;
import Modele.Hebergement;
import Modele.Appartement;
import Modele.MaisonHotes;

import java.sql.*;
import java.math.BigDecimal;
import java.util.*;

public class HebergementDAO {

    // Méthode utilitaire pour update la table hebergement
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

    // ---------------------- INSERT ----------------------

    public void ajouterHotel(Hotel h) {
        String sql = "INSERT INTO hebergement (nom, adresse, prix_par_nuit, description, specification) VALUES (?, ?, ?, ?, ?)";
        String sqlHotel = "INSERT INTO hotel (id_hebergement, nombre_etoiles, petit_dejeuner, piscine, spa) VALUES (LAST_INSERT_ID(), ?, ?, ?, ?)";

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql);
             PreparedStatement psHotel = conn.prepareStatement(sqlHotel)) {

            ps.setString(1, h.getNom());
            ps.setString(2, h.getAdresse());
            ps.setBigDecimal(3, h.getPrixParNuit());
            ps.setString(4, h.getDescription());
            ps.setString(5, h.getSpecification());
            ps.executeUpdate();

            psHotel.setInt(1, h.getNombreEtoiles());
            psHotel.setBoolean(2, h.isPetitDejeuner());
            psHotel.setBoolean(3, h.isPiscine());
            psHotel.setBoolean(4, h.isSpa());
            psHotel.executeUpdate();

            System.out.println("Hôtel inséré avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ajouterAppartement(Appartement a) {
        String sql = "INSERT INTO hebergement (nom, adresse, prix_par_nuit, description, specification) VALUES (?, ?, ?, ?, ?)";
        String sqlAppart = "INSERT INTO appartement (id_hebergement, nombre_pieces, petit_dejeuner, etage) VALUES (LAST_INSERT_ID(), ?, ?, ?)";

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql);
             PreparedStatement psAppart = conn.prepareStatement(sqlAppart)) {

            ps.setString(1, a.getNom());
            ps.setString(2, a.getAdresse());
            ps.setBigDecimal(3, a.getPrixParNuit());
            ps.setString(4, a.getDescription());
            ps.setString(5, a.getSpecification());
            ps.executeUpdate();

            psAppart.setInt(1, a.getNombrePieces());
            psAppart.setBoolean(2, a.isPetitDejeuner());
            psAppart.setInt(3, a.getEtage());
            psAppart.executeUpdate();

            System.out.println("Appartement inséré avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ajouterMaisonHotes(MaisonHotes m) {
        String sql = "INSERT INTO hebergement (nom, adresse, prix_par_nuit, description, specification) VALUES (?, ?, ?, ?, ?)";
        String sqlMaison = "INSERT INTO maisonhotes (id_hebergement, petit_dejeuner, jardin) VALUES (LAST_INSERT_ID(), ?, ?)";

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql);
             PreparedStatement psMaison = conn.prepareStatement(sqlMaison)) {

            ps.setString(1, m.getNom());
            ps.setString(2, m.getAdresse());
            ps.setBigDecimal(3, m.getPrixParNuit());
            ps.setString(4, m.getDescription());
            ps.setString(5, m.getSpecification());
            ps.executeUpdate();

            psMaison.setBoolean(1, m.isPetitDejeuner());
            psMaison.setBoolean(2, m.isJardin());
            psMaison.executeUpdate();

            System.out.println("Maison d'hôtes insérée avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------------- UPDATE ----------------------

    public void updateHotel(Hotel h) {
        String sqlHotel = "UPDATE hotel SET nombre_etoiles = ?, petit_dejeuner = ?, piscine = ?, spa = ? WHERE id_hebergement = ?";

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement psHotel = conn.prepareStatement(sqlHotel)) {

            updateHebergementBase(conn, h);

            psHotel.setInt(1, h.getNombreEtoiles());
            psHotel.setBoolean(2, h.isPetitDejeuner());
            psHotel.setBoolean(3, h.isPiscine());
            psHotel.setBoolean(4, h.isSpa());
            psHotel.setLong(5, h.getIdHebergement());
            psHotel.executeUpdate();

            System.out.println("Hôtel mis à jour avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAppartement(Appartement a) {
        String sqlAppart = "UPDATE appartement SET nombre_pieces = ?, petit_dejeuner = ?, etage = ? WHERE id_hebergement = ?";

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement psAppart = conn.prepareStatement(sqlAppart)) {

            updateHebergementBase(conn, a);

            psAppart.setInt(1, a.getNombrePieces());
            psAppart.setBoolean(2, a.isPetitDejeuner());
            psAppart.setInt(3, a.getEtage());
            psAppart.setLong(4, a.getIdHebergement());
            psAppart.executeUpdate();

            System.out.println("Appartement mis à jour avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMaisonHotes(MaisonHotes m) {
        String sqlMaison = "UPDATE maisonhotes SET petit_dejeuner = ?, jardin = ? WHERE id_hebergement = ?";

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement psMaison = conn.prepareStatement(sqlMaison)) {

            updateHebergementBase(conn, m);

            psMaison.setBoolean(1, m.isPetitDejeuner());
            psMaison.setBoolean(2, m.isJardin());
            psMaison.setLong(3, m.getIdHebergement());
            psMaison.executeUpdate();

            System.out.println("Maison d'hôtes mise à jour avec succès !");
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
                return new MaisonHotes(
                        rs1.getInt("id_hebergement"),
                        rs1.getString("nom"),
                        rs1.getString("adresse"),
                        rs1.getBigDecimal("prix_par_nuit"),
                        rs1.getString("description"),
                        rs1.getString("specification"),
                        rs2.getBoolean("petit_dejeuner"),
                        rs2.getBoolean("jardin")
                );
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
                return new Hotel(
                        rs1.getInt("id_hebergement"),
                        rs1.getString("nom"),
                        rs1.getString("adresse"),
                        rs1.getBigDecimal("prix_par_nuit"),
                        rs1.getString("description"),
                        rs1.getString("specification"),
                        rs2.getInt("nombre_etoiles"),
                        rs2.getBoolean("petit_dejeuner"),
                        rs2.getBoolean("piscine"),
                        rs2.getBoolean("spa")
                );
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
                return new Appartement(
                        rs1.getInt("id_hebergement"),
                        rs1.getString("nom"),
                        rs1.getString("adresse"),
                        rs1.getBigDecimal("prix_par_nuit"),
                        rs1.getString("description"),
                        rs1.getString("specification"),
                        rs2.getInt("nombre_pieces"),
                        rs2.getBoolean("petit_dejeuner"),
                        rs2.getInt("etage")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    //get les Hebergements
    public List<Hebergement> getAllHebergements() {
        List<Hebergement> hebergements;
        hebergements = new ArrayList<>();
        String sql = "SELECT * FROM hebergement";

        try (Connection conn = ConnexionBdd.seConnecter();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                long id = rs.getLong("id_hebergement");
                String spec = rs.getString("specification");

                // Vérifie si c'est un appartement
                Appartement app = findAppartementById((int) id);
                if (app != null) {
                    hebergements.add(app);
                    continue;
                }

                // Vérifie si c'est un hotel
                Hotel hotel = findHotelById((int) id);
                if (hotel != null) {
                    hebergements.add(hotel);
                    continue;
                }

                // Vérifie si c'est une maison d'hôte
                MaisonHotes mh = findMaisonHotesById((int) id);
                if (mh != null) {
                    hebergements.add(mh);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hebergements;
    }

}