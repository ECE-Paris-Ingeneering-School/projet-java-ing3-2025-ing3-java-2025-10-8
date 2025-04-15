package Modele;

import java.math.BigDecimal;
import java.util.List;

public class MaisonHotes extends Hebergement {
    private boolean petitDejeuner;
    private boolean jardin;

    public MaisonHotes(long idHebergement, String nom, String adresse, BigDecimal prixParNuit, String description,
                       String specification, List<String> imageUrls, boolean petitDejeuner, boolean jardin) {
        super(idHebergement, nom, adresse, prixParNuit, description, specification, imageUrls);
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