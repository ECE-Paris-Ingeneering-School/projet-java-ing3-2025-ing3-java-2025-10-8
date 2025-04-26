package DAO;

import Modele.Reservation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/**
 * DAO pour la gestion des réservations.
 * Permet d'ajouter, modifier, supprimer et récupérer des réservations en base de données.
 */

public class ReservationDAO {

    private Connection connection;
    private HebergementDAO hebergementDAO;
    /**
     * Constructeur de ReservationDAO.
     *
     * @param connection Connexion à la base de données
     * @param hebergementDAO DAO pour la gestion des hébergements
     */
    public ReservationDAO(Connection connection, HebergementDAO hebergementDAO) {
        this.connection = connection;
        this.hebergementDAO = hebergementDAO;
    }
    /**
     * Ajoute une nouvelle réservation à la base de données.
     *
     * @param reservation La réservation à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
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
    /**
     * Vérifie si un hébergement est disponible pour une période donnée.
     *
     * @param idHebergement Identifiant de l'hébergement
     * @param dateArrivee Date d'arrivée souhaitée
     * @param dateDepart Date de départ souhaitée
     * @return true si l'hébergement est disponible, false sinon
     */
    public boolean estDisponible(int idHebergement, LocalDate dateArrivee, LocalDate dateDepart) {
        boolean dispo = false;
        try (Connection conn = ConnexionBdd.seConnecter()) {
            String sql = "SELECT COUNT(*) AS nb FROM Reservation " +
                    "WHERE id_hebergement = ? " +
                    "AND statut = 'Confirmée' " +
                    "AND (date_arrivee < ? AND date_depart > ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, idHebergement);
            stmt.setDate(2, Date.valueOf(dateDepart));
            stmt.setDate(3, Date.valueOf(dateArrivee));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                dispo = rs.getInt("nb") == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dispo;
    }
    /**
     * Récupère une réservation par son identifiant.
     *
     * @param idReservation Identifiant de la réservation
     * @return La réservation correspondante, ou null si non trouvée
     */
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
    /**
     * Modifie une réservation existante.
     *
     * @param reservation La réservation avec les nouvelles informations
     * @return true si la modification a réussi, false sinon
     */
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
    /**
     * Supprime une réservation de la base de données.
     *
     * @param idReservation Identifiant de la réservation à supprimer
     * @return true si la suppression a réussi, false sinon
     */
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
    /**
     * Récupère toutes les réservations d'un client donné.
     *
     * @param idClient Identifiant du client
     * @return Liste des réservations du client
     */
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

    /**
     * Confirme une réservation et met à jour le nombre de chambres disponibles dans l'hébergement.
     *
     * @param idReservation Identifiant de la réservation à confirmer
     * @return true si la confirmation a réussi, false sinon
     */
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
    /**
     * Met à jour le statut d'une réservation.
     *
     * @param idReservation Identifiant de la réservation
     * @param nouveauStatut Nouveau statut à appliquer
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean mettreAJourStatutReservation(int idReservation, Reservation.Statut nouveauStatut) {
        String sql = "UPDATE reservation SET statut = ? WHERE id_reservation = ?";
        try (Connection conn = ConnexionBdd.seConnecter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            System.out.println("Statut envoyé à MySQL : " + nouveauStatut.getValue());

            ps.setString(1, nouveauStatut.getValue());
            ps.setInt(2, idReservation);

            int rows = ps.executeUpdate();
            System.out.println(">> Mise à jour statut - lignes affectées : " + rows);

            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Vérifie si un utilisateur a déjà réservé un hébergement donné.
     *
     * @param idUtilisateur Identifiant de l'utilisateur
     * @param idHebergement Identifiant de l'hébergement
     * @return true si l'utilisateur a déjà réservé cet hébergement, false sinon
     */
    public static boolean utilisateurAReserve(int idUtilisateur, int idHebergement) {
        boolean aReserve = false;
        try {
            Connection conn = ConnexionBdd.seConnecter();
            String sql = "SELECT COUNT(*) FROM reservation WHERE id_utilisateur = ? AND id_hebergement = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idUtilisateur);
            pstmt.setInt(2, idHebergement);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                aReserve = rs.getInt(1) > 0;
            }
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aReserve;
    }



}
