package DAO;

import Modele.Client;
import Modele.Client.TypeClient;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

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
}

