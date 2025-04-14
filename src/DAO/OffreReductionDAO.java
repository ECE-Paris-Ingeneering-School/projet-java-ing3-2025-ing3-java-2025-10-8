package DAO;

import Modele.OffreReduction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OffreReductionDAO {
    private final Connection conn;

    public OffreReductionDAO(Connection conn) {
        this.conn = conn;
    }

    // Récupérer l'offre active pour un utilisateur
    public OffreReduction getOffreActivePourUtilisateur(int idUtilisateur) throws SQLException {
        String sql = "SELECT * FROM offrereduction WHERE id_utilisateur = ? AND CURDATE() BETWEEN date_debut AND date_fin LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtilisateur);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        }
        return null;
    }

    // Ajouter une nouvelle offre
    public boolean ajouterOffre(OffreReduction offre) throws SQLException {
        String sql = "INSERT INTO offrereduction (id_utilisateur, description, pourcentage_reduction, date_debut, date_fin) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, offre.getIdUtilisateur());
            stmt.setString(2, offre.getDescription());
            stmt.setDouble(3, offre.getPourcentageReduction());
            stmt.setDate(4, offre.getDateDebut());
            stmt.setDate(5, offre.getDateFin());
            return stmt.executeUpdate() > 0;
        }
    }

    // Supprimer une offre par ID
    public boolean supprimerOffre(int idPromotion) throws SQLException {
        String sql = "DELETE FROM offrereduction WHERE id_promotion = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPromotion);
            return stmt.executeUpdate() > 0;
        }
    }

    // Lister toutes les offres (optionnel pour interface admin)
    public List<OffreReduction> listerToutesLesOffres() throws SQLException {
        List<OffreReduction> liste = new ArrayList<>();
        String sql = "SELECT * FROM offrereduction";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(mapResultSet(rs));
            }
        }
        return liste;
    }

    // Méthode utilitaire pour mapper un ResultSet vers un objet OffreReduction
    private OffreReduction mapResultSet(ResultSet rs) throws SQLException {
        OffreReduction offre = new OffreReduction();
        offre.setIdPromotion(rs.getInt("id_promotion"));
        offre.setIdUtilisateur(rs.getInt("id_utilisateur"));
        offre.setDescription(rs.getString("description"));
        offre.setPourcentageReduction(rs.getDouble("pourcentage_reduction"));
        offre.setDateDebut(rs.getDate("date_debut"));
        offre.setDateFin(rs.getDate("date_fin"));
        return offre;
    }
}
