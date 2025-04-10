package Modele;

public class MaisonHotes extends Hebergement {
    private int nombreChambres;
    private boolean petitDejeunerInclus;

    public MaisonHotes(long id, String nom, String adresse, String ville, String codePostal, String pays, String description,
                       int nombreChambres, boolean petitDejeunerInclus) {
        super(id, nom, adresse, ville, codePostal, pays, description);
        this.nombreChambres = nombreChambres;
        this.petitDejeunerInclus = petitDejeunerInclus;
    }

    public int getNombreChambres() { return nombreChambres; }
    public void setNombreChambres(int nombreChambres) { this.nombreChambres = nombreChambres; }

    public boolean isPetitDejeunerInclus() { return petitDejeunerInclus; }
    public void setPetitDejeunerInclus(boolean petitDejeunerInclus) { this.petitDejeunerInclus = petitDejeunerInclus; }
}

