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
        String sql = "UPDATE hebergement SET nom = ?, adresse = ?, prix_par_nuit = ?, description = ?, specification = ?, image_url = ? WHERE id_hebergement = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, h.getNom());
            ps.setString(2, h.getAdresse());
            ps.setBigDecimal(3, h.getPrixParNuit());
            ps.setString(4, h.getDescription());
            ps.setString(5, h.getSpecification());
            ps.setString(6, h.getImageUrl());
            ps.setLong(7, h.getIdHebergement());
            ps.executeUpdate();
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
                int idHebergement = rs1.getInt("id_hebergement");
                String nom = rs1.getString("nom");
                String adresse = rs1.getString("adresse");
                BigDecimal prix = rs1.getBigDecimal("prix_par_nuit");
                String description = rs1.getString("description");
                String specification = rs1.getString("specification");
                String imageUrl = rs1.getString("image_url");

                boolean petitDejeuner = rs2.getInt("petit_dejeuner") == 1;
                boolean jardin = rs2.getInt("jardin") == 1;

                return new MaisonHotes(idHebergement, nom, adresse, prix, description, specification, imageUrl, petitDejeuner, jardin);
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
                String imageUrl = rs1.getString("image_url");

                int nombreEtoiles = rs2.getInt("nombre_etoiles");
                boolean petitDejeuner = rs2.getInt("petit_dejeuner") == 1;
                boolean piscine = rs2.getInt("piscine") == 1;
                boolean spa = rs2.getInt("spa") == 1;

                return new Hotel(idHebergement, nom, adresse, prix, description, specification, imageUrl, nombreEtoiles, petitDejeuner, piscine, spa);
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
                String imageUrl = rs1.getString("image_url");

                int nombrePieces = rs2.getInt("nombre_pieces");
                boolean petitDejeuner = rs2.getInt("petit_dejeuner") == 1;
                int etage = rs2.getInt("etage");

                return new Appartement(idHebergement, nom, adresse, prix, description, specification, imageUrl, nombrePieces, petitDejeuner, etage);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ---------------------- FILTRAGE ----------------------

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

            // Piscine (pour hôtels uniquement)
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

            // Jardin (pour maisons d’hôtes uniquement)
            if (jardin != null && h instanceof MaisonHotes) {
                if (((MaisonHotes) h).isJardin() != jardin) correspond = false;
            }

            if (correspond) filtres.add(h);
        }

        return filtres;
    }


    // Récupérer tous les hébergements (sans filtrage)
    public List<Hebergement> getAllHebergements() {
        List<Hebergement> hebergements = new ArrayList<>();
        String sql = "SELECT * FROM hebergement";

        try (Connection conn = ConnexionBdd.seConnecter();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                long id = rs.getLong("id_hebergement");
                String imageUrl = rs.getString("image_url");

                Appartement app = findAppartementById((int) id);
                if (app != null) {
                    app.setImageUrl(imageUrl);
                    hebergements.add(app);
                    continue;
                }

                Hotel hotel = findHotelById((int) id);
                if (hotel != null) {
                    hotel.setImageUrl(imageUrl);
                    hebergements.add(hotel);
                    continue;
                }

                MaisonHotes mh = findMaisonHotesById((int) id);
                if (mh != null) {
                    mh.setImageUrl(imageUrl);
                    hebergements.add(mh);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hebergements;
    }
}