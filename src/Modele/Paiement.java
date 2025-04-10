package Modele;

import java.sql.Date;

public class Paiement {

    private int idPaiement;
    private int idReservation;
    private double montant;
    private MethodePaiement methodePaiement;
    private StatutPaiement statut;
    private Date datePaiement;

    public enum MethodePaiement {
        CARTE_BANCAIRE,
        PAYPAL,
        VIREMENT
    }

    public enum StatutPaiement {
        EN_ATTENTE,
        PAYE,
        ANNULE
    }

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

    // 🆕 Méthodes utilitaires pour la BDD
    public static String getSQLMethode(MethodePaiement methode) {
        switch (methode) {
            case CARTE_BANCAIRE: return "Carte bancaire";
            case PAYPAL: return "PayPal";
            case VIREMENT: return "Virement";
            default: return null;
        }
    }

    public static String getSQLStatut(StatutPaiement statut) {
        switch (statut) {
            case EN_ATTENTE: return "En attente";
            case PAYE: return "Payé";
            case ANNULE: return "Annulé";
            default: return null;
        }
    }

    public static MethodePaiement fromSQLMethode(String value) {
        switch (value) {
            case "Carte bancaire": return MethodePaiement.CARTE_BANCAIRE;
            case "PayPal": return MethodePaiement.PAYPAL;
            case "Virement": return MethodePaiement.VIREMENT;
            default: throw new IllegalArgumentException("Méthode inconnue : " + value);
        }
    }

    public static StatutPaiement fromSQLStatut(String value) {
        switch (value) {
            case "En attente": return StatutPaiement.EN_ATTENTE;
            case "Payé": return StatutPaiement.PAYE;
            case "Annulé": return StatutPaiement.ANNULE;
            default: throw new IllegalArgumentException("Statut inconnu : " + value);
        }
    }
}
