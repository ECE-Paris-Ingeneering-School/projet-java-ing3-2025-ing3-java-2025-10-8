package DAO;

import Modele.Avis;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) de la classe Avis.
 * Gère les opérations de lecture et d'écriture dans la table `avis`.
 * Permet d'ajouter un avis, de récupérer la liste des avis d’un hébergement,
 * et de calculer la note moyenne.
 * Pattern : DAO (accès à la BDD via JDBC)
 * @author Noa
 */

public class AvisDAO {
    /**
     * Ajoute un avis utilisateur pour un hébergement dans la base de données.
     * @param avis L'objet Avis à insérer
     * @return true si l'insertion a réussi, false sinon
     */
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
    /**
     * Récupère tous les avis associés à un hébergement spécifique,
     * triés du plus récent au plus ancien.
     * @param idHebergement Identifiant de l'hébergement concerné
     * @return Liste des avis associés à cet hébergement
     */
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
    /**
     * Calcule la note moyenne de tous les avis associés à un hébergement.
     * @param idHebergement Identifiant de l'hébergement concerné
     * @return Note moyenne sur 5, ou 0.0 si aucun avis
     */
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
