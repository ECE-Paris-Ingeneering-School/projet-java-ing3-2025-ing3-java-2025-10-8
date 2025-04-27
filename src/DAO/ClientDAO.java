package DAO;

import Modele.Client;
import Modele.Client.TypeClient;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) de la classe Client.
 * Gère toutes les opérations d’accès et de manipulation des données client dans la base.
 * Pattern : DAO, utilisé avec JDBC pour la table `client` liée à `utilisateur`.
 */

public class ClientDAO {
    /**
     * Ajoute un nouveau client dans la base (utilisateur + client).
     * @param client L'objet Client à ajouter
     * @return true si l'ajout s’est bien déroulé, false sinon
     */
    // Ajouter un nouveau client
    public boolean ajouterClient(Client client) {
        String sqlUtilisateur = "INSERT INTO utilisateur (nom, prenom, email, mdp, date_inscription) VALUES (?, ?, ?, ?, ?)";
        String sqlClient = "INSERT INTO client (id_utilisateur, type_client) VALUES (?, ?)";

        try (Connection conn = ConnexionBdd.seConnecter()) {
            conn.setAutoCommit(false);

            // Insertion dans utilisateur
            PreparedStatement psUser = conn.prepareStatement(sqlUtilisateur, Statement.RETURN_GENERATED_KEYS);
            psUser.setString(1, client.getNom());
            psUser.setString(2, client.getPrenom());
            psUser.setString(3, client.getEmail());
            psUser.setString(4, client.getMdp());
            psUser.setDate(5, Date.valueOf(LocalDate.now()));
            psUser.executeUpdate();

            // Récupérer l'ID généré
            ResultSet rs = psUser.getGeneratedKeys();
            if (rs.next()) {
                int idUtilisateur = rs.getInt(1);
                client.setIdUtilisateur(idUtilisateur);

                // Insertion dans client
                PreparedStatement psClient = conn.prepareStatement(sqlClient);
                psClient.setInt(1, idUtilisateur);
                psClient.setString(2, client.getTypeClient().name());
                psClient.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Récupère un client par son identifiant.
     * @param id ID du client
     * @return Un objet Client si trouvé, sinon null
     */
    // Récupérer un client par ID
    public Client getClientParId(int id) {
        String sql = "SELECT u.*, c.type_client FROM utilisateur u JOIN client c ON u.id_utilisateur = c.id_utilisateur WHERE u.id_utilisateur = ?";

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Client(
                        rs.getInt("id_utilisateur"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("mdp"),
                        TypeClient.valueOf(rs.getString("type_client"))
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    /**
     * Récupère un client à partir de son adresse email.
     * @param email Email du client
     * @return L'objet Client trouvé ou null
     */
    // Récupérer un client par email
    public Client getClientParEmail(String email) {
        String sql = "SELECT u.*, c.type_client FROM utilisateur u JOIN client c ON u.id_utilisateur = c.id_utilisateur WHERE u.email = ?";

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Client(
                        rs.getInt("id_utilisateur"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("mdp"),
                        TypeClient.valueOf(rs.getString("type_client"))
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    /**
     * Supprime un client de la base (supprime dans `utilisateur` et cascade dans `client` si bien paramétré en BDD).
     * @param id Identifiant du client à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    // Supprimer un client par ID
    public boolean supprimerClient(int id) {
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
     * Récupère tous les clients enregistrés dans la base de données.
     * @return Liste des objets Client
     */
    // Récupérer tous les clients
    public List<Client> getTousLesClients() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT u.*, c.type_client FROM utilisateur u JOIN client c ON u.id_utilisateur = c.id_utilisateur";

        try (Connection conn = ConnexionBdd.seConnecter();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Client c = new Client(
                        rs.getInt("id_utilisateur"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("mdp"),
                        TypeClient.valueOf(rs.getString("type_client"))
                );
                clients.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clients;
    }
    /**
     * Vérifie si un client existe avec l’email et mot de passe donnés (authentification).
     * @param email Email fourni
     * @param mdp Mot de passe fourni
     * @return L'objet Client si les identifiants sont valides, null sinon
     */
    public Client verifierConnexion(String email, String mdp) {
        String sql = "SELECT u.*, c.type_client FROM utilisateur u JOIN client c ON u.id_utilisateur = c.id_utilisateur WHERE u.email = ? AND u.mdp = ?";

        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, mdp);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Client(
                        rs.getInt("id_utilisateur"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("mdp"),
                        TypeClient.valueOf(rs.getString("type_client"))
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}

