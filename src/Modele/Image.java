package Modele;

public class Image {
    private int idImage;
    private int idHebergement;
    private String urlImage;
    private String description;

    public Image(int idImage, int idHebergement, String urlImage, String description) {
        this.idImage = idImage;
        this.idHebergement = idHebergement;
        this.urlImage = urlImage;
        this.description = description;
    }

    // Getters
    public int getIdImage() { return idImage; }
    public int getIdHebergement() { return idHebergement; }
    public String getUrlImage() { return urlImage; }
    public String getDescription() { return description; }

    // Setters
    public void setIdImage(int idImage) { this.idImage = idImage; }
    public void setIdHebergement(int idHebergement) { this.idHebergement = idHebergement; }
    public void setUrlImage(String urlImage) { this.urlImage = urlImage; }
    public void setDescription(String description) { this.description = description; }
}