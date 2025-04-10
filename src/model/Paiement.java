package model;

//import java.time.LocalDate;

import java.sql.Date;

public class Paiement {

    private int idPaiement;
    private int idReservation;
    private double montant;
    private MethodePaiement methodePaiement;
    private StatutPaiement statut;
    private Date datePaiement;

    // Enum pour les méthodes de paiement
    public enum MethodePaiement {
        CARTE_BANCAIRE,
        PAYPAL,
        VIREMENT
    }

    // Enum pour le statut du paiement
    public enum StatutPaiement {
        EN_ATTENTE,
        PAYE,
        ANNULE
    }

    // Constructeurs
    public Paiement() {}

    public Paiement(int idReservation, double montant, MethodePaiement methodePaiement, StatutPaiement statut, Date datePaiement) {
        this.idReservation = idReservation;
        this.montant = montant;
        this.methodePaiement = methodePaiement;
        this.statut = statut;
        this.datePaiement = datePaiement;
    }

    public Paiement(int idPaiement, int idReservation, double montant, MethodePaiement methodePaiement, StatutPaiement statut, Date datePaiement) {
        this.idPaiement = idPaiement;
        this.idReservation = idReservation;
        this.montant = montant;
        this.methodePaiement = methodePaiement;
        this.statut = statut;
        this.datePaiement = datePaiement;
    }

    // Getters & Setters
    public int getIdPaiement() {
        return idPaiement;
    }

    public void setIdPaiement(int idPaiement) {
        this.idPaiement = idPaiement;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public MethodePaiement getMethodePaiement() {
        return methodePaiement;
    }

    public void setMethodePaiement(MethodePaiement methodePaiement) {
        this.methodePaiement = methodePaiement;
    }

    public StatutPaiement getStatut() {
        return statut;
    }

    public void setStatut(StatutPaiement statut) {
        this.statut = statut;
    }

    public Date getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
    }

    // ToString pour debug ou affichage
    @Override
    public String toString() {
        return "Paiement{" +
                "idPaiement=" + idPaiement +
                ", idReservation=" + idReservation +
                ", montant=" + montant +
                ", methodePaiement=" + methodePaiement +
                ", statut=" + statut +
                ", datePaiement=" + datePaiement +
                '}';
    }
}
