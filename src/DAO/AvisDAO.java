package DAO;

import Modele.Avis;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvisDAO {

    public boolean ajouterAvis(Avis avis) {
        String sql = "INSERT INTO avis (id_utilisateur, id_hebergement, note, commentaire, date_avis) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, avis.getIdUtilisateur());
            ps.setInt(2, avis.getIdHebergement());
            ps.setInt(3, avis.getNote());
            ps.setString(4, avis.getCommentaire());
            ps.setDate(5, avis.getDateAvis());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Avis> getAvisParHebergement(int idHebergement) {
        List<Avis> avisList = new ArrayList<>();
        String sql = "SELECT * FROM avis WHERE id_hebergement = ? ORDER BY date_avis DESC";

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idHebergement);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Avis avis = new Avis(
                        rs.getInt("id_avis"),
                        rs.getInt("id_utilisateur"),
                        rs.getInt("id_hebergement"),
                        rs.getInt("note"),
                        rs.getString("commentaire"),
                        rs.getDate("date_avis")
                );
                avisList.add(avis);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return avisList;
    }

    public double getNoteMoyenneParHebergement(int idHebergement) {
        String sql = "SELECT AVG(note) AS moyenne FROM avis WHERE id_hebergement = ?";
        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idHebergement);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("moyenne");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
