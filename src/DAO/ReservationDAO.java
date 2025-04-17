package DAO;

import Modele.Reservation;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class ReservationDAO {

    private Connection connection;
    private HebergementDAO hebergementDAO;

    // Constructeur avec HebergementDAO
    public ReservationDAO(Connection connection, HebergementDAO hebergementDAO) {
        this.connection = connection;
        this.hebergementDAO = hebergementDAO;
    }

    // Ajouter une nouvelle réservation
    public boolean ajouterReservation(Reservation reservation) {
        String sql = "INSERT INTO reservation (id_utilisateur, id_hebergement, date_arrivee, date_depart, nombre_adultes, nombre_enfants, nombre_chambres, statut) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, reservation.getIdUtilisateur());
            ps.setInt(2, reservation.getIdHebergement());
            ps.setDate(3, Date.valueOf(reservation.getDateArrivee()));
            ps.setDate(4, Date.valueOf(reservation.getDateDepart()));
            ps.setInt(5, reservation.getNombreAdultes());
            ps.setInt(6, reservation.getNombreEnfants());
            ps.setInt(7, reservation.getNombreChambres());
            ps.setString(8, reservation.getStatut().getValue());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean estDisponible(long idHebergement, LocalDate dateArrivee, LocalDate dateDepart) {
        boolean dispo = false;
        try (Connection conn = ConnexionBdd.seConnecter()) {
            String sql = "SELECT COUNT(*) AS nb FROM Reservation " +
                    "WHERE id_hebergement = ? " +
                    "AND statut = 'Confirmée' " +
                    "AND (date_arrivee < ? AND date_depart > ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, idHebergement);
            stmt.setDate(2, Date.valueOf(dateDepart));
            stmt.setDate(3, Date.valueOf(dateArrivee));  // La nouvelle date d’arrivée
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                dispo = rs.getInt("nb") == 0; // S’il n’y a pas de chevauchement
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dispo;
    }

    // Récupérer une réservation par son ID
    public Reservation getReservationById(int idReservation) {
        String sql = "SELECT * FROM reservation WHERE id_reservation = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idReservation);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
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
        return null;
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

                reservations.add(new Reservation(idReservation, idUtilisateur, idHebergement, dateArrivee, dateDepart, nombreAdultes, nombreEnfants, nombreChambres, statut));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    // Modifier une réservation
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

    // Récupérer les réservations d'un client
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

                reservations.add(new Reservation(
                        idReservation,
                        idUtilisateur,
                        idHebergement,
                        dateArrivee,
                        dateDepart,
                        nombreAdultes,
                        nombreEnfants,
                        nombreChambres,
                        statut
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    // Confirmer une réservation + mettre à jour les chambres disponibles
    public boolean confirmerReservation(int idReservation) {
        Reservation reservation = getReservationById(idReservation);
        if (reservation == null) return false;

        // Mettre à jour le statut
        String sql = "UPDATE reservation SET statut = ? WHERE id_reservation = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, Reservation.Statut.PAYE.getValue());
            ps.setInt(2, idReservation);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                // Mise à jour du nombre de chambres disponibles dans l'hébergement
                return hebergementDAO.mettreAJourChambresDisponibles(reservation.getIdHebergement(), reservation.getNombreChambres());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Mise à jour manuelle du statut
    public boolean mettreAJourStatutReservation(int idReservation, Reservation.Statut nouveauStatut) {
        String sql = "UPDATE reservation SET statut = ? WHERE id_reservation = ?";
        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            System.out.println("Statut envoyé à MySQL : " + nouveauStatut.getValue());

            ps.setString(1, nouveauStatut.getValue()); // Attention : .name() renvoie "PAYE", "EN_ATTENTE", ...
            ps.setInt(2, idReservation);

            int rows = ps.executeUpdate();
            System.out.println(">> Mise à jour statut - lignes affectées : " + rows);

            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
