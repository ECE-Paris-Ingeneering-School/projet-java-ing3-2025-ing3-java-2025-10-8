package Modele;

import java.time.LocalDate;
import DAO.HebergementDAO;

/**
 * Classe représentant une réservation.
 * Contient des informations liées à la réservation : utilisateur, dates et hébergement.
 */
public class Reservation {

    private int idReservation;
    private int idUtilisateur;
    private int idHebergement;
    private LocalDate dateArrivee;
    private LocalDate dateDepart;
    private int nombreAdultes;
    private int nombreEnfants;
    private int nombreChambres;
    private Statut statut;

    /**
     * Enumération des statuts possibles d'une réservation.
     */
    public enum Statut {
        PAYE("Confirmée"), // Confirmée et payée
        ANNULEE("Annulée"),
        EN_ATTENTE("En attente");

        private String value;

        Statut(String value) {
            this.value = value;
        }
        /**
         * Retourne la valeur du statut sous forme de chaîne.
         *
         * @return Valeur du statut.
         */
        public String getValue() {
            return value;
        }

        /**
         * Convertit une chaîne de caractères en statut.
         *
         * @param value La chaîne de caractères représentant un statut.
         * @return Le statut correspondant.
         */
        public static Statut fromString(String value) {
            for (Statut statut : Statut.values()) {
                if (statut.value.equalsIgnoreCase(value)) {
                    return statut;
                }
            }
            return null; // Si aucun statut correspond
        }
    }
    /**
     * Constructeur de la classe Reservation avec l'identifiant de l'utilisateur.
     *
     * @param idReservation L'identifiant de la réservation.
     * @param idUtilisateur L'identifiant de l'utilisateur ayant effectué la réservation.
     * @param idHebergement L'identifiant de l'hébergement réservé.
     * @param dateArrivee La date d'arrivée de la réservation.
     * @param dateDepart La date de départ de la réservation.
     * @param nombreAdultes Le nombre d'adultes pour la réservation.
     * @param nombreEnfants Le nombre d'enfants pour la réservation.
     * @param nombreChambres Le nombre de chambres réservées.
     * @param statut Le statut actuel de la réservation.
     */
    public Reservation(int idReservation, int idUtilisateur, int idHebergement,
                       LocalDate dateArrivee, LocalDate dateDepart,
                       int nombreAdultes, int nombreEnfants,
                       int nombreChambres, Statut statut) {
        this.idReservation = idReservation;
        this.idUtilisateur = idUtilisateur;
        this.idHebergement = idHebergement;
        this.dateArrivee = dateArrivee;
        this.dateDepart = dateDepart;
        this.nombreAdultes = nombreAdultes;
        this.nombreEnfants = nombreEnfants;
        this.nombreChambres = nombreChambres;
        this.statut = statut;
    }

    /**
     * Constructeur de la classe Reservation sans l'identifiant de l'utilisateur.
     * Utilisé lorsque l'utilisateur est géré séparément (pour des tests).
     *
     * @param idReservation L'identifiant de la réservation.
     * @param idHebergement L'identifiant de l'hébergement réservé.
     * @param dateArrivee La date d'arrivée de la réservation.
     * @param dateDepart La date de départ de la réservation.
     * @param nombreAdultes Le nombre d'adultes pour la réservation.
     * @param nombreEnfants Le nombre d'enfants pour la réservation.
     * @param nombreChambres Le nombre de chambres réservées.
     * @param statut Le statut actuel de la réservation.
     */
    public Reservation(int idReservation, int idHebergement,
                       LocalDate dateArrivee, LocalDate dateDepart,
                       int nombreAdultes, int nombreEnfants,
                       int nombreChambres, Statut statut) {
        this.idReservation = idReservation;
        this.idHebergement = idHebergement;
        this.dateArrivee = dateArrivee;
        this.dateDepart = dateDepart;
        this.nombreAdultes = nombreAdultes;
        this.nombreEnfants = nombreEnfants;
        this.nombreChambres = nombreChambres;
        this.statut = statut;
    }
    /**
     * Récupère l'hébergement associé à la réservation.
     *
     * @return L'hébergement lié à cette réservation.
     */
    public Hebergement getHebergement() {
        HebergementDAO hebergementDAO = new HebergementDAO();
        return hebergementDAO.getHebergementById(this.idHebergement);
    }

    // Getters et Setters pour accéder/modifier les propriétés de la réservation

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public int getIdHebergement() {
        return idHebergement;
    }

    public void setIdHebergement(int idHebergement) {
        this.idHebergement = idHebergement;
    }

    public LocalDate getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(LocalDate dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public LocalDate getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(LocalDate dateDepart) {
        this.dateDepart = dateDepart;
    }

    public int getNombreAdultes() {
        return nombreAdultes;
    }

    public void setNombreAdultes(int nombreAdultes) {
        this.nombreAdultes = nombreAdultes;
    }

    public int getNombreEnfants() {
        return nombreEnfants;
    }

    public void setNombreEnfants(int nombreEnfants) {
        this.nombreEnfants = nombreEnfants;
    }

    public int getNombreChambres() {
        return nombreChambres;
    }

    public void setNombreChambres(int nombreChambres) {
        this.nombreChambres = nombreChambres;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }
    /**
     * Méthode qui retourne une représentation sous forme texte de la réservation.
     *
     * @return La chaîne représentant l'objet Reservation.
     */
    @Override
    public String toString() {
        return "Reservation [idReservation=" + idReservation + ", idUtilisateur=" + idUtilisateur
                + ", idHebergement=" + idHebergement + ", dateArrivee=" + dateArrivee + ", dateDepart="
                + dateDepart + ", nombreAdultes=" + nombreAdultes + ", nombreEnfants=" + nombreEnfants
                + ", nombreChambres=" + nombreChambres + ", statut=" + statut + "]";
    }
}
