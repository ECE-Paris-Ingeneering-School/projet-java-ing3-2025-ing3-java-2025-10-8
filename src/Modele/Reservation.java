package Modele;

import java.time.LocalDate;
import DAO.HebergementDAO;

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

    // Enum pour le statut de la réservation
    public enum Statut {
        CONFIRMEE("Confirmée"),
        ANNULEE("Annulée"),
        EN_ATTENTE("En attente");

        private String value;

        Statut(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Statut fromString(String value) {
            for (Statut statut : Statut.values()) {
                if (statut.value.equalsIgnoreCase(value)) {
                    return statut;
                }
            }
            return null;
        }
    }

    // Constructeur
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

    public Hebergement getHebergement() {
        HebergementDAO hebergementDAO = new HebergementDAO(); // Créer ou récupérer une instance de ton DAO Hebergement
        return hebergementDAO.getHebergementById(this.idHebergement);  // Recherche de l'hébergement par ID
    }


    // Getters et Setters
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

    @Override
    public String toString() {
        return "Reservation [idReservation=" + idReservation + ", idUtilisateur=" + idUtilisateur
                + ", idHebergement=" + idHebergement + ", dateArrivee=" + dateArrivee + ", dateDepart="
                + dateDepart + ", nombreAdultes=" + nombreAdultes + ", nombreEnfants=" + nombreEnfants
                + ", nombreChambres=" + nombreChambres + ", statut=" + statut + "]";
    }
}
