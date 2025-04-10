package Modele;

public class Appartement extends Hebergement {
    private int superficie;
    private int nombrePieces;
    private boolean balcon;

    public Appartement(long id, String nom, String adresse, String ville, String codePostal, String pays, String description,
                       int superficie, int nombrePieces, boolean balcon) {
        super(id, nom, adresse, ville, codePostal, pays, description);
        this.superficie = superficie;
        this.nombrePieces = nombrePieces;
        this.balcon = balcon;
    }

    public int getSuperficie() { return superficie; }
    public void setSuperficie(int superficie) { this.superficie = superficie; }

    public int getNombrePieces() { return nombrePieces; }
    public void setNombrePieces(int nombrePieces) { this.nombrePieces = nombrePieces; }

    public boolean isBalcon() { return balcon; }
    public void setBalcon(boolean balcon) { this.balcon = balcon; }
}
