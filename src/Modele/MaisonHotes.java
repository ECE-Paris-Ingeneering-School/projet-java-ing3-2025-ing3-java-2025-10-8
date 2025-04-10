package Modele;

import java.math.BigDecimal;

public class MaisonHotes extends Hebergement {
    private boolean petitDejeuner; // = petit_dejeuner dans la BDD
    private boolean jardin;        // = jardin dans la BDD

    public MaisonHotes(long idHebergement, String nom, String adresse, BigDecimal prixParNuit,
                       String description, String specification,
                       boolean petitDejeuner, boolean jardin) {
        super(idHebergement, nom, adresse, prixParNuit, description, specification);
        this.petitDejeuner = petitDejeuner;
        this.jardin = jardin;
    }

    public boolean isPetitDejeuner() {
        return petitDejeuner;
    }

    public void setPetitDejeuner(boolean petitDejeuner) {
        this.petitDejeuner = petitDejeuner;
    }

    public boolean isJardin() {
        return jardin;
    }

    public void setJardin(boolean jardin) {
        this.jardin = jardin;
    }
}
