package DAO;

import Modele.Paiement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaiementDAO {

    private Connection connection;

    public PaiementDAO(Connection connection) {
        this.connection = connection;
    }

    public void ajouterPaiement(Paiement paiement) throws SQLException {
        String query = "INSERT INTO paiement (id_reservation, montant, methode_paiement, statut, date_paiement) VALUES (?, ?, ?, ?, ?)";

        // Utilisation de Statement.RETURN_GENERATED_KEYS pour récupérer l'ID généré automatiquement
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, paiement.getIdReservation());
            stmt.setDouble(2, paiement.getMontant());
            stmt.setString(3, Paiement.getSQLMethode(paiement.getMethodePaiement()));  // 🔄 conversion
            stmt.setString(4, Paiement.getSQLStatut(paiement.getStatut()));            // 🔄 conversion
            stmt.setDate(5, paiement.getDatePaiement());

            stmt.executeUpdate();

            // Récupérer l'ID généré pour le paiement
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    paiement.setIdPaiement(rs.getInt(1));  // ⚠️ Assigner l'ID généré à l'objet Paiement
                }
            }
        }
    }

    public Paiement getPaiementById(int idPaiement) throws SQLException {
        Paiement paiement = null;
        String query = "SELECT * FROM paiement WHERE id_paiement = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPaiement);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                paiement = new Paiement(
                        rs.getInt("id_paiement"),
                        rs.getInt("id_reservation"),
                        rs.getDouble("montant"),
                        Paiement.fromSQLMethode(rs.getString("methode_paiement")),  // 🔄 conversion
                        Paiement.fromSQLStatut(rs.getString("statut")),              // 🔄 conversion
                        rs.getDate("date_paiement")
                );
            }
        }

        return paiement;
    }

    public List<Paiement> getPaiementsByReservation(int idReservation) throws SQLException {
        List<Paiement> paiements = new ArrayList<>();
        String query = "SELECT * FROM paiement WHERE id_reservation = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idReservation);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Paiement paiement = new Paiement(
                        rs.getInt("id_paiement"),
                        rs.getInt("id_reservation"),
                        rs.getDouble("montant"),
                        Paiement.fromSQLMethode(rs.getString("methode_paiement")),  // 🔄
                        Paiement.fromSQLStatut(rs.getString("statut")),              // 🔄
                        rs.getDate("date_paiement")
                );
                paiements.add(paiement);
            }
        }

        return paiements;
    }

    public void updatePaiement(Paiement paiement) throws SQLException {
        String query = "UPDATE paiement SET montant = ?, methode_paiement = ?, statut = ?, date_paiement = ? WHERE id_paiement = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, paiement.getMontant());
            stmt.setString(2, Paiement.getSQLMethode(paiement.getMethodePaiement()));  // 🔄
            stmt.setString(3, Paiement.getSQLStatut(paiement.getStatut()));            // 🔄
            stmt.setDate(4, paiement.getDatePaiement());
            stmt.setInt(5, paiement.getIdPaiement());

            stmt.executeUpdate();
        }
    }

    public void deletePaiement(int idPaiement) throws SQLException {
        String query = "DELETE FROM paiement WHERE id_paiement = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPaiement);
            stmt.executeUpdate();
        }
    }
}
