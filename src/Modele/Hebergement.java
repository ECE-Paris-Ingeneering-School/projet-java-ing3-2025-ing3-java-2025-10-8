package Modele;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Hebergement {
    private long idHebergement;
    private String nom;
    private String adresse;
    private BigDecimal prixParNuit;
    private String description;
    private String specification;
    private List<String> imageUrls = new ArrayList<>(); // Liste d’images

    // Constructeur
    public Hebergement(long idHebergement, String nom, String adresse, BigDecimal prixParNuit, String description, String specification, List<String> imageUrls) {
        this.idHebergement = idHebergement;
        this.nom = nom;
        this.adresse = adresse;
        this.prixParNuit = prixParNuit;
        this.description = description;
        this.specification = specification;
        this.imageUrls = imageUrls;
    }

    // Getters et Setters
    public long getIdHebergement() {
        return idHebergement;
    }

    public void setIdHebergement(long idHebergement) {
        this.idHebergement = idHebergement;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public BigDecimal getPrixParNuit() {
        return prixParNuit;
    }

    public void setPrixParNuit(BigDecimal prixParNuit) {
        this.prixParNuit = prixParNuit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    // Pour garder une image principale si nécessaire
    public String getImageUrl() {
        return imageUrls != null && !imageUrls.isEmpty() ? imageUrls.get(0) : null;
    }
}