package model;

import java.time.LocalDate;

public class Paiement {
    private int id;
    private Reservation reservation;
    private LocalDate datePaiement;
    private String methode;
    private String etat;
}