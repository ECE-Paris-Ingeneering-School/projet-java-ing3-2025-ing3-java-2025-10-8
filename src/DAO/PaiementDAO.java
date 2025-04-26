package DAO;

import Modele.Paiement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * DAO pour la gestion des paiements dans la base de données.
 */
public class PaiementDAO {

    private Connection connection;
    /**
     * Constructeur du PaiementDAO.
     *
     * @param connection Connexion à la base de données.
     */
    public PaiementDAO(Connection connection) {
        this.connection = connection;
    }
    /**
     * Ajout d'un nouveau paiement dans la bdd.
     *
     * @param paiement Le paiement à ajouter.
     * @throws SQLException Si erreur SQL.
     */
    public void ajouterPaiement(Paiement paiement) throws SQLException {
        String query = "INSERT INTO paiement (id_reservation, montant, methode_paiement, statut, date_paiement) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, paiement.getIdReservation());
            stmt.setDouble(2, paiement.getMontant());
            stmt.setString(3, Paiement.getSQLMethode(paiement.getMethodePaiement()));
            stmt.setString(4, Paiement.getSQLStatut(paiement.getStatut()));
            stmt.setDate(5, paiement.getDatePaiement());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    paiement.setIdPaiement(rs.getInt(1));
                }
            }
        }
    }
    /**
     * Récupère les paiements liés à une réservation.
     *
     * @param idReservation L'identifiant de la réservation.
     * @return Une liste de paiements associés à la réservation.
     * @throws SQLException Si erreur SQL.
     */
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
                        Paiement.fromSQLMethode(rs.getString("methode_paiement")),
                        Paiement.fromSQLStatut(rs.getString("statut")),
                        rs.getDate("date_paiement")
                );
                paiements.add(paiement);
            }
        }

        return paiements;
    }
    /**
     * Maj d'un paiement existant dans la bdd.
     *
     * @param paiement Le paiement à mettre à jour.
     * @throws SQLException Si erreur SQL.
     */
    public void updatePaiement(Paiement paiement) throws SQLException {
        String query = "UPDATE paiement SET montant = ?, methode_paiement = ?, statut = ?, date_paiement = ? WHERE id_paiement = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, paiement.getMontant());
            stmt.setString(2, Paiement.getSQLMethode(paiement.getMethodePaiement()));
            stmt.setString(3, Paiement.getSQLStatut(paiement.getStatut()));
            stmt.setDate(4, paiement.getDatePaiement());
            stmt.setInt(5, paiement.getIdPaiement());

            stmt.executeUpdate();
        }
    }
}
