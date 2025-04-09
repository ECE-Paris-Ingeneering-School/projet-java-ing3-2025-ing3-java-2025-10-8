package model;

import java.time.LocalDate;

public class Reservation {
    private int id;
    private Client client;
    private Hebergement hebergement;
    private LocalDate dateArrivee;
    private LocalDate dateDepart;
    private int nbAdultes;
    private int nbEnfants;
    private double prixTotal;
}