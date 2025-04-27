package DAO;

import Modele.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO (Data Access Object) pour la gestion des administrateurs dans la base de données.
 * Fournit les opérations CRUD sur les objets Admin en interaction avec les tables `utilisateur` et `admin`.
 * Pattern : DAO, utilisé via JDBC avec gestion transactionnelle.
 */
public class AdminDAO {
    /**
     * Ajoute un administrateur dans les tables `utilisateur` et `admin`.
     * @param admin L'objet Admin à ajouter
     * @return true si l'ajout a réussi, false sinon
     */

    public boolean ajouterAdmin(Admin admin) {
        String sqlUtilisateur = "INSERT INTO utilisateur (nom, prenom, email, mdp, date_inscription) VALUES (?, ?, ?, ?, ?)";
        String sqlAdmin = "INSERT INTO admin (id_utilisateur, role_specifique) VALUES (?, ?)";

        try (Connection conn = ConnexionBdd.seConnecter()) {
            conn.setAutoCommit(false);

            // Insertion dans utilisateur
            PreparedStatement psUser = conn.prepareStatement(sqlUtilisateur, Statement.RETURN_GENERATED_KEYS);
            psUser.setString(1, admin.getNom());
            psUser.setString(2, admin.getPrenom());
            psUser.setString(3, admin.getEmail());
            psUser.setString(4, admin.getMdp());
            psUser.setDate(5, new java.sql.Date(System.currentTimeMillis()));
            psUser.executeUpdate();

            // Récupération de l'ID
            ResultSet rs = psUser.getGeneratedKeys();
            if (rs.next()) {
                int idUtilisateur = rs.getInt(1);
                admin.setIdUtilisateur(idUtilisateur);

                // Insertion dans admin
                PreparedStatement psAdmin = conn.prepareStatement(sqlAdmin);
                psAdmin.setInt(1, idUtilisateur);
                psAdmin.setString(2, admin.getRole());
                psAdmin.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Récupère un administrateur à partir de son identifiant.
     * @param id Identifiant de l'utilisateur
     * @return L'objet Admin correspondant, ou null si non trouvé
     */
    public Admin getAdminParId(int id) {
        String sql = "SELECT u.*, a.role_specifique FROM utilisateur u JOIN admin a ON u.id_utilisateur = a.id_utilisateur WHERE u.id_utilisateur = ?";

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Admin(
                        rs.getInt("id_utilisateur"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("mdp"),
                        rs.getString("role_specifique")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    /**
     * Supprime un administrateur via son ID (supprime également l’entrée dans `utilisateur`).
     * @param id Identifiant de l'utilisateur
     * @return true si la suppression a réussi, false sinon
     */
    public boolean supprimerAdmin(int id) {
        String sql = "DELETE FROM utilisateur WHERE id_utilisateur = ?";

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Récupère tous les administrateurs présents en base.
     * @return Liste des objets Admin
     */
    public List<Admin> getTousLesAdmins() {
        List<Admin> admins = new ArrayList<>();
        String sql = "SELECT u.*, a.role_specifique FROM utilisateur u JOIN admin a ON u.id_utilisateur = a.id_utilisateur";

        try (Connection conn = ConnexionBdd.seConnecter();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Admin a = new Admin(
                        rs.getInt("id_utilisateur"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("mdp"),
                        rs.getString("role_specifique")
                );
                admins.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admins;
    }
    /**
     * Récupère un administrateur à partir de son email.
     * @param email Email de l'utilisateur
     * @return L'objet Admin correspondant, ou null si non trouvé
     */
    public Admin getAdminParEmail(String email) {
        String sql = "SELECT u.*, a.role_specifique FROM utilisateur u JOIN admin a ON u.id_utilisateur = a.id_utilisateur WHERE u.email = ?";

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Admin(
                        rs.getInt("id_utilisateur"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("mdp"),
                        rs.getString("role_specifique")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}