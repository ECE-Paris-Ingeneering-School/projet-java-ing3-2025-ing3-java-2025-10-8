package Modele;

public class Hotel extends Hebergement {
    private int nombreEtoiles;
    private boolean serviceChambre;

    public Hotel(long id, String nom, String adresse, String ville, String codePostal, String pays, String description,
                 int nombreEtoiles, boolean serviceChambre) {
        super(id, nom, adresse, ville, codePostal, pays, description);
        this.nombreEtoiles = nombreEtoiles;
        this.serviceChambre = serviceChambre;
    }

    public int getNombreEtoiles() { return nombreEtoiles; }
    public void setNombreEtoiles(int nombreEtoiles) { this.nombreEtoiles = nombreEtoiles; }

    public boolean isServiceChambre() { return serviceChambre; }
    public void setServiceChambre(boolean serviceChambre) { this.serviceChambre = serviceChambre; }
}