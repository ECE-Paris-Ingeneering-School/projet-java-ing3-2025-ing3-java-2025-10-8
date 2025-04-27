package DAO;

import Modele.OffreReduction;
import java.sql.*;
import java.time.LocalDate;
/**
 * DAO pour gérer les opérations liées aux offres de réduction.
 */
public class OffreReductionDAO {

    private Connection connection;
    /**
     * Constructeur de l'OffreReductionDAO.
     *
     * @param connection La connexion à la base de données.
     */
    public OffreReductionDAO(Connection connection) {
        this.connection = connection;
    }
    /**
     * Récupère l'offre de réduction active pour un utilisateur donné.
     *
     * @param idUtilisateur L'identifiant de l'utilisateur.
     * @return L'offre de réduction active, ou null s'il n'y en a pas.
     */
    public OffreReduction getOffreActivePourUtilisateur(int idUtilisateur) {
        OffreReduction offre = null;
        String sql = "SELECT * FROM offrereduction WHERE id_utilisateur = ? " +
                "AND date_debut <= CURRENT_DATE AND date_fin >= CURRENT_DATE";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idUtilisateur);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                offre = new OffreReduction(
                        rs.getInt("id_utilisateur"),
                        rs.getString("description"),
                        rs.getDouble("pourcentage_reduction"),
                        rs.getDate("date_debut"),
                        rs.getDate("date_fin")
                );
                offre.setIdOffre(rs.getInt("id_offre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return offre;
    }
    /**
     * Vérifie l'ancienneté de l'utilisateur (au moins 6 mois) et génère une offre de réduction.
     *
     * @param idUtilisateur L'identifiant de l'utilisateur.
     * @return L'offre de réduction générée, ou null si non applicable.
     */
    public OffreReduction genererOffreSiAncienUtilisateur(int idUtilisateur) {
        String sql = "SELECT date_inscription FROM utilisateur WHERE id_utilisateur = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idUtilisateur);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Date dateInscription = rs.getDate("date_inscription");
                long diffMillis = System.currentTimeMillis() - dateInscription.getTime();
                long diffJours = diffMillis / (1000 * 60 * 60 * 24);

                if (diffJours >= 180) { // 6 mois
                    OffreReduction offre = new OffreReduction(
                            idUtilisateur,
                            "Offre fidélité : -10% pour votre ancienneté",
                            10.0,
                            new Date(System.currentTimeMillis()),
                            Date.valueOf(LocalDate.now().plusDays(7))
                    );

                    // Ajouter l'offre à la bdd
                    if (ajouterOffre(offre)) {
                        return offre;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;  // Pas d'offre sinon
    }
    /**
     * Ajoute une nouvelle offre de réduction dans la base de données.
     *
     * @param offre L'offre de réduction à ajouter.
     * @return true si l'ajout a réussi, false sinon.
     */
    public boolean ajouterOffre(OffreReduction offre) {
        String sql = "INSERT INTO offrereduction (id_utilisateur, description, pourcentage_reduction, date_debut, date_fin) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, offre.getIdUtilisateur());
            ps.setString(2, offre.getDescription());
            ps.setDouble(3, offre.getPourcentageReduction());
            ps.setDate(4, offre.getDateDebut());
            ps.setDate(5, offre.getDateFin());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Ajoute une réduction suite à un paiement réalisé par un utilisateur.
     *
     * @param idPaiement L'identifiant du paiement.
     * @param reduction Le pourcentage de réduction à appliquer.
     * @param montant Le montant du paiement (non utilisé ici mais conservé pour de futures évolutions).
     * @return true si l'ajout de la réduction a réussi, false sinon.
     */
    public boolean ajouterReductionPaiement(int idPaiement, double reduction, double montant) {

        int idUtilisateur = -1;
        try {
            idUtilisateur = getIdUtilisateurFromPaiement(idPaiement);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        if (idUtilisateur == -1) {
            System.out.println("Aucun utilisateur trouvé pour ce paiement.");
            return false;
        }

        String sql = "INSERT INTO offrereduction (id_utilisateur, description, pourcentage_reduction, date_debut, date_fin) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idUtilisateur);
            ps.setString(2, "Réduction appliquée après paiement");
            ps.setDouble(3, reduction);
            ps.setDate(4, new Date(System.currentTimeMillis()));
            ps.setDate(5, Date.valueOf(LocalDate.now().plusDays(7)));  // Exemple : date de fin + 7 jours

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Récupère l'identifiant de l'utilisateur à partir de l'identifiant d'un paiement.
     *
     * @param idPaiement L'identifiant du paiement.
     * @return L'identifiant de l'utilisateur associé.
     * @throws SQLException En cas d'erreur SQL ou si aucun utilisateur n'est trouvé.
     */
    private int getIdUtilisateurFromPaiement(int idPaiement) throws SQLException {
        String sql = "SELECT r.id_utilisateur " +
                "FROM paiement p " +
                "JOIN reservation r ON p.id_reservation = r.id_reservation " +
                "WHERE p.id_paiement = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idPaiement);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int idUtilisateur = rs.getInt("id_utilisateur");
                System.out.println("ID utilisateur trouvé : " + idUtilisateur);
                return idUtilisateur;
            } else {
                throw new SQLException("Aucun utilisateur trouvé pour ce paiement.");
            }
        }
    }

}

