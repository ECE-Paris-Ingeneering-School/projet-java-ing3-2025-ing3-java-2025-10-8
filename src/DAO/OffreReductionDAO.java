package DAO;

import Modele.OffreReduction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class OffreReductionDAO {

    private Connection connection;

    public OffreReductionDAO(Connection connection) {
        this.connection = connection;
    }

    // Récupérer l'offre active pour un utilisateur donné
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

    // Vérifier l'ancienneté de l'utilisateur et générer une offre si applicable
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

                    // Ajouter l'offre à la BDD
                    if (ajouterOffre(offre)) {
                        return offre;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;  // Aucun droit à l'offre
    }

    // Ajouter une nouvelle offre de réduction
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

    // Ajouter une réduction suite à un paiement
    public boolean ajouterReductionPaiement(int idPaiement, double reduction, double montant) {
        // Récupérer l'ID utilisateur associé au paiement
        int idUtilisateur = -1;
        try {
            idUtilisateur = getIdUtilisateurFromPaiement(idPaiement);  // Récupération de l'utilisateur
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Si une erreur survient dans la récupération de l'utilisateur, retourner false
        }

        // Vérifier si un utilisateur a été trouvé
        if (idUtilisateur == -1) {
            System.out.println("Aucun utilisateur trouvé pour ce paiement.");
            return false;  // Aucun utilisateur trouvé, retour sans appliquer la réduction
        }

        // Préparer la requête d'insertion
        String sql = "INSERT INTO offrereduction (id_utilisateur, description, pourcentage_reduction, date_debut, date_fin) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idUtilisateur);
            ps.setString(2, "Réduction appliquée après paiement");
            ps.setDouble(3, reduction);
            ps.setDate(4, new Date(System.currentTimeMillis()));
            ps.setDate(5, Date.valueOf(LocalDate.now().plusDays(7)));  // Exemple : date de fin + 7 jours

            // Exécution de la requête d'insertion
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // En cas d'erreur d'insertion, retourner false
        }
    }


    // 🔽 ICI : Nouvelle méthode pour récupérer l'ID utilisateur depuis un paiement
    // Récupère l'id_utilisateur à partir d'un id_paiement, via la réservation
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
                System.out.println("ID utilisateur trouvé : " + idUtilisateur);  // Ajout de la ligne pour le debug
                return idUtilisateur;
            } else {
                throw new SQLException("Aucun utilisateur trouvé pour ce paiement.");
            }
        }
    }

}

