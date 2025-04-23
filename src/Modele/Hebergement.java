package Modele;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un hébergement dans le système de réservation.
 * Un hébergement possède des informations comme le nom, l'adresse, le prix par nuit, la description, la spécification,
 * les images associées, et sa disponibilité.
 */
public class Hebergement {

    /**
     * L'identifiant de l'hébergement.
     */
    private int idHebergement;

    /**
     * Le nom de l'hébergement.
     */
    private String nom;

    /**
     * L'adresse de l'hébergement.
     */
    private String adresse;

    /**
     * Le prix par nuit pour l'hébergement.
     */
    private BigDecimal prixParNuit;

    /**
     * La description de l'hébergement.
     */
    private String description;

    /**
     * Des spécifications supplémentaires pour l'hébergement.
     */
    private String specification;

    /**
     * La liste des URLs des images de l'hébergement.
     */
    private List<String> imageUrls = new ArrayList<>();

    /**
     * Le nombre de chambres disponibles pour cet hébergement.
     */
    private int disponibilite;

    /**
     * Constructeur de l'hébergement.
     *
     * @param idHebergement L'identifiant de l'hébergement.
     * @param nom Le nom de l'hébergement.
     * @param adresse L'adresse de l'hébergement.
     * @param prixParNuit Le prix par nuit pour cet hébergement.
     * @param description Une description détaillée de l'hébergement.
     * @param specification Des spécifications supplémentaires pour l'hébergement.
     * @param imageUrls Une liste d'URLs des images de l'hébergement.
     */
    public Hebergement(int idHebergement, String nom, String adresse, BigDecimal prixParNuit, String description, String specification, List<String> imageUrls) {
        this.idHebergement = idHebergement;
        this.nom = nom;
        this.adresse = adresse;
        this.prixParNuit = prixParNuit;
        this.description = description;
        this.specification = specification;
        this.imageUrls = imageUrls;
    }

    // Getters et Setters

    /**
     * Retourne l'identifiant de l'hébergement.
     *
     * @return L'identifiant de l'hébergement.
     */
    public int getIdHebergement() {
        return idHebergement;
    }

    /**
     * Définit l'identifiant de l'hébergement.
     *
     * @param idHebergement L'identifiant de l'hébergement.
     */
    public void setIdHebergement(int idHebergement) {
        this.idHebergement = idHebergement;
    }

    /**
     * Retourne le nom de l'hébergement.
     *
     * @return Le nom de l'hébergement.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom de l'hébergement.
     *
     * @param nom Le nom de l'hébergement.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne l'adresse de l'hébergement.
     *
     * @return L'adresse de l'hébergement.
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * Définit l'adresse de l'hébergement.
     *
     * @param adresse L'adresse de l'hébergement.
     */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    /**
     * Retourne le prix par nuit de l'hébergement.
     *
     * @return Le prix par nuit.
     */
    public BigDecimal getPrixParNuit() {
        return prixParNuit;
    }

    /**
     * Définit le prix par nuit de l'hébergement.
     *
     * @param prixParNuit Le prix par nuit.
     */
    public void setPrixParNuit(BigDecimal prixParNuit) {
        this.prixParNuit = prixParNuit;
    }

    /**
     * Retourne la disponibilité de l'hébergement.
     *
     * @return La disponibilité de l'hébergement.
     */
    public int getDisponibilite() {
        return disponibilite;
    }

    /**
     * Définit la disponibilité de l'hébergement.
     *
     * @param disponibilite La disponibilité de l'hébergement.
     */
    public void setDisponibilite(int disponibilite) {
        this.disponibilite = disponibilite;
    }

    /**
     * Retourne la description de l'hébergement en fonction de son nom.
     * Cette méthode retourne des descriptions spécifiques pour certains hébergements.
     *
     * @return La description de l'hébergement.
     */
    public String getDescription() {
        switch (nom) {
            case "Studio Chic":
                return "Ce studio lumineux au style haussmannien offre un cadre élégant au cœur de Paris. Son design raffiné, ses moulures dorées et son mobilier chic séduisent immédiatement. Parfait pour les couples ou les voyageurs d'affaires, il dispose d'une cuisine toute équipée et d'une salle de bain moderne. Situé à proximité des musées et des cafés parisiens, c'est un point de départ idéal pour découvrir la ville. Profitez d'une ambiance chaleureuse et d'un confort optimal dans ce cocon citadin.";
            case "Appartement Vue Seine":
                return "Cet appartement contemporain propose une vue imprenable sur la Seine depuis le salon et la chambre. Sa décoration épurée et ses matériaux haut de gamme offrent une atmosphère sereine. Il dispose d’une chambre spacieuse, d’une salle de bain design et d’un espace de vie confortable. À quelques pas des quais et des monuments parisiens, il vous place au cœur de la capitale. Idéal pour un séjour romantique ou un voyage culturel en toute élégance.";
            case "Loft Industriel":
                return "Ce loft spacieux au style industriel marie parfaitement modernité et authenticité. Parquet en bois massif, murs en brique et mobilier vintage composent une atmosphère unique. Il comprend une grande pièce à vivre avec coin salon, cuisine ouverte et coin nuit, parfait pour les couples ou les solos. Situé dans le quartier animé du Marais, vous êtes à deux pas des galeries d'art et cafés. Ce lieu atypique séduira les amateurs d’architecture et d’ambiance urbaine.";
            case "Appartement Design":
                return "Cet appartement design se distingue par ses lignes épurées, ses finitions modernes et sa luminosité exceptionnelle. Il est doté d'une cuisine ouverte sur un séjour élégant, d'une chambre douillette et d'une salle de bain contemporaine. Sa décoration aux tons clairs crée une ambiance calme et reposante. Situé dans un quartier chic de Paris, vous profitez d’un accès immédiat aux boutiques de luxe et restaurants branchés. Idéal pour un séjour urbain stylé et confortable.";
            case "Appartement Cosy Paris":
                return "Ce charmant appartement offre un espace accueillant avec balcon donnant sur une rue calme. L’intérieur mêle touches parisiennes classiques et confort moderne. Une cuisine équipée, un salon convivial et deux chambres agréables le rendent parfait pour les familles. Situé entre les Grands Boulevards et Montmartre, vous serez proche des plus beaux sites. Un lieu cosy idéal pour explorer Paris avec sérénité.";
            case "Hotel Le Majestic":
                return "L’Hôtel Le Majestic vous accueille dans un cadre élégant et raffiné avec ses prestations haut de gamme. Chaque chambre offre un confort optimal avec literie luxueuse, salle de bain spacieuse et décoration soignée. Vous pourrez profiter d’un spa relaxant et d’un petit-déjeuner copieux inclus dans votre séjour. Situé près des Champs-Élysées, il offre un accès direct aux boutiques de luxe. Un choix parfait pour un séjour 4 étoiles à Paris.";
            case "Hotel Bleu Ciel":
                return "L’Hôtel Bleu Ciel mêle élégance moderne et accueil chaleureux dans le quartier vivant de République. Ses chambres bien agencées sont idéales pour les séjours courts ou longs. Profitez d’un petit-déjeuner varié chaque matin dans la salle à manger lumineuse. Le restaurant gastronomique propose une cuisine française raffinée. Parfait pour les couples, familles ou voyageurs d’affaires.";
            case "Grand Hotel Central":
                return "Le Grand Hôtel Central est situé en plein cœur de Paris, à deux pas des grands magasins et de l’Opéra. Il propose des chambres spacieuses avec climatisation, Wi-Fi et TV écran plat. Un espace de remise en forme est à votre disposition pour vous détendre après vos visites. Le personnel attentif est à votre service 24h/24 pour rendre votre séjour agréable. L’idéal pour découvrir la capitale dans un grand confort.";
            case "Maison Bord de Mer":
                return "La Maison Bord de Mer à Nice est parfaite pour ceux qui rêvent de vacances les pieds dans l’eau. Avec ses chambres vue mer, sa terrasse ensoleillée et son accès direct à la plage, elle offre un séjour inoubliable. Chaque chambre est décorée avec goût dans un style marin. Les hôtes proposent un petit-déjeuner gourmand servi face à la mer. Une retraite idéale pour un moment de détente au bord de la Méditerranée.";
            case "Maison des Arts":
                return "Située à Lyon, la Maison des Arts est une maison d’hôtes pleine de charme et de créativité. Chaque chambre est inspirée par un courant artistique, vous plongeant dans une ambiance unique. Le salon commun, avec sa bibliothèque et ses œuvres d’art, invite au calme. Vous êtes à proximité des musées et théâtres de la ville. Un lieu parfait pour les amateurs d’art et de culture.";
            case "Maison Campagne":
                return "La Maison Campagne est nichée au cœur de la nature près de Toulouse. Son grand jardin, son mobilier rustique et ses poutres apparentes lui donnent une atmosphère chaleureuse. Idéale pour les familles ou les couples en quête de tranquillité, elle dispose de chambres confortables et d’un salon spacieux. La campagne environnante est propice aux randonnées et balades à vélo. Un lieu apaisant pour se ressourcer loin de l’agitation.";
            default:
                return "Ce charmant hébergement offre un cadre agréable et fonctionnel, parfait pour tous les types de voyageurs. Vous y trouverez tout le confort nécessaire avec un mobilier moderne, une literie de qualité et une atmosphère accueillante. Idéalement situé, il permet de rejoindre facilement les points d’intérêt touristiques ou les zones d’affaires. Chaque détail a été pensé pour que vous vous sentiez comme chez vous. Que ce soit pour une escapade de quelques jours ou un séjour prolongé, il saura répondre à toutes vos attentes.";
        }

    }

    /**
     * Définit la description de l'hébergement.
     *
     * @param description La description à définir.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retourne les spécifications de l'hébergement.
     *
     * @return Les spécifications de l'hébergement.
     */
    public String getSpecification() {
        return specification;
    }

    /**
     * Définit les spécifications de l'hébergement.
     *
     * @param specification Les spécifications à définir.
     */
    public void setSpecification(String specification) {
        this.specification = specification;
    }

    /**
     * Retourne la liste des URLs des images de l'hébergement.
     *
     * @return La liste des URLs des images.
     */
    public List<String> getImageUrls() {
        return imageUrls;
    }

    /**
     * Définit les URLs des images de l'hébergement.
     *
     * @param imageUrls La liste des URLs des images.
     */
    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    /**
     * Retourne l'URL de la première image de l'hébergement.
     * Utilisé pour afficher une image principale si nécessaire.
     *
     * @return L'URL de la première image.
     */
    public String getImageUrl() {
        return imageUrls != null && !imageUrls.isEmpty() ? imageUrls.get(0) : null;
    }
}