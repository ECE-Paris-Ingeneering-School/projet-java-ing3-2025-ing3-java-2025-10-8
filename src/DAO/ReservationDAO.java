package DAO;

import Modele.Reservation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class ReservationDAO {

    private Connection connection;

    // Constructeur pour établir la connexion à la base de données
    public ReservationDAO(Connection connection) {
        this.connection = connection;
    }

    // Ajouter une nouvelle réservation
    public boolean ajouterReservation(Reservation reservation) {
        String sql = "INSERT INTO reservation (id_utilisateur, id_hebergement, date_arrivee, date_depart, nombre_adultes, nombre_enfants, nombre_chambres, statut) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, reservation.getIdUtilisateur());
            ps.setInt(2, reservation.getIdHebergement());
            ps.setDate(3, Date.valueOf(reservation.getDateArrivee())); // Convertir LocalDate en Date
            ps.setDate(4, Date.valueOf(reservation.getDateDepart()));
            ps.setInt(5, reservation.getNombreAdultes());
            ps.setInt(6, reservation.getNombreEnfants());
            ps.setInt(7, reservation.getNombreChambres());
            ps.setString(8, reservation.getStatut().getValue()); // Statut en chaîne de caractères
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Vérifier la disponibilité de l'hébergement
    public boolean estDisponible(int idHebergement, LocalDate dateArrivee, LocalDate dateDepart) {
        String sql = "SELECT * FROM reservation WHERE id_hebergement = ? AND statut = 'Confirmée' " +
                "AND ((date_arrivee BETWEEN ? AND ?) OR (date_depart BETWEEN ? AND ?))";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idHebergement);
            ps.setDate(2, Date.valueOf(dateArrivee)); // Convertir LocalDate en Date
            ps.setDate(3, Date.valueOf(dateDepart));
            ps.setDate(4, Date.valueOf(dateArrivee));
            ps.setDate(5, Date.valueOf(dateDepart));
            ResultSet rs = ps.executeQuery();
            return !rs.next(); // Si le résultat est vide, l'hébergement est disponible
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // En cas d'erreur, considérer que la réservation n'est pas possible
        }
    }

    // Récupérer une réservation par son ID
    public Reservation getReservationById(int idReservation) {
        String sql = "SELECT * FROM reservation WHERE id_reservation = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idReservation);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Créer un objet Reservation à partir des résultats
                int idUtilisateur = rs.getInt("id_utilisateur");
                int idHebergement = rs.getInt("id_hebergement");
                LocalDate dateArrivee = rs.getDate("date_arrivee").toLocalDate();
                LocalDate dateDepart = rs.getDate("date_depart").toLocalDate();
                int nombreAdultes = rs.getInt("nombre_adultes");
                int nombreEnfants = rs.getInt("nombre_enfants");
                int nombreChambres = rs.getInt("nombre_chambres");
                Reservation.Statut statut = Reservation.Statut.fromString(rs.getString("statut"));

                return new Reservation(idReservation, idUtilisateur, idHebergement, dateArrivee, dateDepart, nombreAdultes, nombreEnfants, nombreChambres, statut);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retourne null si aucune réservation trouvée
    }

    // Récupérer toutes les réservations
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int idReservation = rs.getInt("id_reservation");
                int idUtilisateur = rs.getInt("id_utilisateur");
                int idHebergement = rs.getInt("id_hebergement");
                LocalDate dateArrivee = rs.getDate("date_arrivee").toLocalDate();
                LocalDate dateDepart = rs.getDate("date_depart").toLocalDate();
                int nombreAdultes = rs.getInt("nombre_adultes");
                int nombreEnfants = rs.getInt("nombre_enfants");
                int nombreChambres = rs.getInt("nombre_chambres");
                Reservation.Statut statut = Reservation.Statut.fromString(rs.getString("statut"));

                Reservation reservation = new Reservation(idReservation, idUtilisateur, idHebergement, dateArrivee, dateDepart, nombreAdultes, nombreEnfants, nombreChambres, statut);
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    // Modifier une réservation (changer son statut par exemple)
    public boolean modifierReservation(Reservation reservation) {
        String sql = "UPDATE reservation SET id_utilisateur = ?, id_hebergement = ?, date_arrivee = ?, date_depart = ?, nombre_adultes = ?, nombre_enfants = ?, nombre_chambres = ?, statut = ? WHERE id_reservation = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, reservation.getIdUtilisateur());
            ps.setInt(2, reservation.getIdHebergement());
            ps.setDate(3, Date.valueOf(reservation.getDateArrivee()));
            ps.setDate(4, Date.valueOf(reservation.getDateDepart()));
            ps.setInt(5, reservation.getNombreAdultes());
            ps.setInt(6, reservation.getNombreEnfants());
            ps.setInt(7, reservation.getNombreChambres());
            ps.setString(8, reservation.getStatut().getValue());
            ps.setInt(9, reservation.getIdReservation());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Supprimer une réservation
    public boolean supprimerReservation(int idReservation) {
        String sql = "DELETE FROM reservation WHERE id_reservation = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idReservation);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Récupérer les réservations d'un client spécifique
    public List<Reservation> getReservationsByClient(int idClient) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation WHERE id_utilisateur = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idClient);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int idReservation = rs.getInt("id_reservation");
                int idUtilisateur = rs.getInt("id_utilisateur");
                int idHebergement = rs.getInt("id_hebergement");
                LocalDate dateArrivee = rs.getDate("date_arrivee").toLocalDate();
                LocalDate dateDepart = rs.getDate("date_depart").toLocalDate();
                int nombreAdultes = rs.getInt("nombre_adultes");
                int nombreEnfants = rs.getInt("nombre_enfants");
                int nombreChambres = rs.getInt("nombre_chambres");
                Reservation.Statut statut = Reservation.Statut.fromString(rs.getString("statut"));

                Reservation reservation = new Reservation(
                        idReservation,
                        idUtilisateur,
                        idHebergement,
                        dateArrivee,
                        dateDepart,
                        nombreAdultes,
                        nombreEnfants,
                        nombreChambres,
                        statut
                );

                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

}
