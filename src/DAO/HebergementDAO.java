package DAO;

import Modele.Hotel;
import Modele.Hebergement;
import Modele.Appartement;
import Modele.MaisonHotes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HebergementDAO {

    public void ajouterHotel(Hotel h) {
        String sql = "INSERT INTO hebergement (nom, adresse, ville, code_postal, pays, description, type) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlHotel = "INSERT INTO hotel (id, nombre_etoiles, service_chambre) VALUES (LAST_INSERT_ID(), ?, ?)";

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql);
             PreparedStatement psHotel = conn.prepareStatement(sqlHotel)) {

            ps.setString(1, h.getNom());
            ps.setString(2, h.getAdresse());
            ps.setString(3, h.getVille());
            ps.setString(4, h.getCodePostal());
            ps.setString(5, h.getPays());
            ps.setString(6, h.getDescription());
            ps.setString(7, "hotel");
            ps.executeUpdate();

            psHotel.setInt(1, h.getNombreEtoiles());
            psHotel.setBoolean(2, h.isServiceChambre());
            psHotel.executeUpdate();

            System.out.println("Hôtel inséré avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ajouterAppartement(Appartement a) {
        String sql = "INSERT INTO hebergement (nom, adresse, ville, code_postal, pays, description, type) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlAppart = "INSERT INTO appartement (id, superficie, nombre_pieces, balcon) VALUES (LAST_INSERT_ID(), ?, ?, ?)";

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql);
             PreparedStatement psAppart = conn.prepareStatement(sqlAppart)) {

            ps.setString(1, a.getNom());
            ps.setString(2, a.getAdresse());
            ps.setString(3, a.getVille());
            ps.setString(4, a.getCodePostal());
            ps.setString(5, a.getPays());
            ps.setString(6, a.getDescription());
            ps.setString(7, "appartement");
            ps.executeUpdate();

            psAppart.setInt(1, a.getSuperficie());
            psAppart.setInt(2, a.getNombrePieces());
            psAppart.setBoolean(3, a.isBalcon());
            psAppart.executeUpdate();

            System.out.println("Appartement inséré avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ajouterMaisonHotes(MaisonHotes m) {
        String sql = "INSERT INTO hebergement (nom, adresse, ville, code_postal, pays, description, type) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlMaison = "INSERT INTO maison_hotes (id, nombre_chambres, petit_dejeuner_inclus) VALUES (LAST_INSERT_ID(), ?, ?)";

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql);
             PreparedStatement psMaison = conn.prepareStatement(sqlMaison)) {

            ps.setString(1, m.getNom());
            ps.setString(2, m.getAdresse());
            ps.setString(3, m.getVille());
            ps.setString(4, m.getCodePostal());
            ps.setString(5, m.getPays());
            ps.setString(6, m.getDescription());
            ps.setString(7, "maison_hotes");
            ps.executeUpdate();

            psMaison.setInt(1, m.getNombreChambres());
            psMaison.setBoolean(2, m.isPetitDejeunerInclus());
            psMaison.executeUpdate();

            System.out.println("Maison d'hôtes insérée avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
