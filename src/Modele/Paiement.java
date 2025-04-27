package Modele;

import java.sql.Date;
/**
 * Cette classe représente un paiement associé à une réservation.
 * Contient des informations sur le montant, la méthode et le statut du paiement.
 */
public class Paiement {

    private int idPaiement;
    private int idReservation;
    private double montant;
    private MethodePaiement methodePaiement;
    private StatutPaiement statut;
    private Date datePaiement;
    /**
     * Enumération des méthodes de paiement disponibles.
     */
    public enum MethodePaiement {
        CARTE_BANCAIRE,
        PAYPAL,
        VIREMENT
    }
    /**
     * Enumération des différents statuts d'un paiement.
     */
    public enum StatutPaiement {
        EN_ATTENTE,
        PAYE,
        ANNULE
    }
    /**
     * Constructeur pour créer un nouveau paiement.
     *
     * @param idReservation L'ID de la réservation associée.
     * @param montant Le montant du paiement.
     * @param methodePaiement La méthode de paiement utilisée.
     * @param statut Le statut du paiement.
     * @param datePaiement La date du paiement.
     */
    public Paiement(int idReservation, double montant, MethodePaiement methodePaiement, StatutPaiement statut, Date datePaiement) {
        this.idReservation = idReservation;
        this.montant = montant;
        this.methodePaiement = methodePaiement;
        this.statut = statut;
        this.datePaiement = datePaiement;
    }
    /**
     * Constructeur pour créer un paiement existant avec un ID.
     *
     * @param idPaiement L'ID du paiement.
     * @param idReservation L'ID de la réservation associée.
     * @param montant Le montant du paiement.
     * @param methodePaiement La méthode de paiement utilisée.
     * @param statut Le statut du paiement.
     * @param datePaiement La date du paiement.
     */
    public Paiement(int idPaiement, int idReservation, double montant, MethodePaiement methodePaiement, StatutPaiement statut, Date datePaiement) {
        this.idPaiement = idPaiement;
        this.idReservation = idReservation;
        this.montant = montant;
        this.methodePaiement = methodePaiement;
        this.statut = statut;
        this.datePaiement = datePaiement;
    }

    /** @return l'ID du paiement. */
    public int getIdPaiement() {
        return idPaiement;
    }

    /** @param idPaiement l'ID du paiement à définir. */
    public void setIdPaiement(int idPaiement) {
        this.idPaiement = idPaiement;
    }

    /** @return l'ID de la réservation associée. */
    public int getIdReservation() {
        return idReservation;
    }

    /** @return le montant du paiement. */
    public double getMontant() {
        return montant;
    }

    /** @param montant le montant du paiement à définir. */
    public void setMontant(double montant) {
        this.montant = montant;
    }

    /** @return la méthode de paiement utilisée. */
    public MethodePaiement getMethodePaiement() {
        return methodePaiement;
    }

    /** @return le statut du paiement. */
    public StatutPaiement getStatut() {
        return statut;
    }

    /** @param statut le statut du paiement à définir. */
    public void setStatut(StatutPaiement statut) {
        this.statut = statut;
    }

    /** @return la date du paiement. */
    public Date getDatePaiement() {
        return datePaiement;
    }
    /**
     * Retourne une représentation texte de l'objet Paiement.
     *
     * @return une chaîne représentant le paiement.
     */
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
    /**
     * Convertit une méthode de paiement en chaîne qui est compatible avec la bdd.
     *
     * @param methode La méthode de paiement.
     * @return La chaîne correspondante pour la bdd.
     */
    public static String getSQLMethode(MethodePaiement methode) {
        switch (methode) {
            case CARTE_BANCAIRE: return "Carte bancaire";
            case PAYPAL: return "PayPal";
            case VIREMENT: return "Virement";
            default: return null;
        }
    }
    /**
     * Convertit un statut de paiement en chaîne compatible avec la bdd.
     *
     * @param statut Le statut de paiement.
     * @return La chaîne correspondante pour la bdd.
     */
    public static String getSQLStatut(StatutPaiement statut) {
        switch (statut) {
            case EN_ATTENTE: return "En attente";
            case PAYE: return "Payé";
            case ANNULE: return "Annulé";
            default: return null;
        }
    }
    /**
     * Convertit une chaîne de la BDD en méthode de paiement.
     *
     * @param value La valeur de la méthode de paiement en base.
     * @return La méthode de paiement correspondante.
     * @throws IllegalArgumentException si la méthode est inconnue.
     */
    public static MethodePaiement fromSQLMethode(String value) {
        switch (value) {
            case "Carte bancaire": return MethodePaiement.CARTE_BANCAIRE;
            case "PayPal": return MethodePaiement.PAYPAL;
            case "Virement": return MethodePaiement.VIREMENT;
            default: throw new IllegalArgumentException("Méthode inconnue : " + value);
        }
    }
    /**
     * Convertit une chaîne de la BDD en statut de paiement.
     *
     * @param value La valeur du statut en base.
     * @return Le statut de paiement correspondant.
     * @throws IllegalArgumentException si le statut est inconnu.
     */
    public static StatutPaiement fromSQLStatut(String value) {
        switch (value) {
            case "En attente": return StatutPaiement.EN_ATTENTE;
            case "Payé": return StatutPaiement.PAYE;
            case "Annulé": return StatutPaiement.ANNULE;
            default: throw new IllegalArgumentException("Statut inconnu : " + value);
        }
    }
}
