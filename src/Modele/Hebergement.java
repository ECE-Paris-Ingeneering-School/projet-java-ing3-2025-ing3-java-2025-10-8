package Modele;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Hebergement {
    private int idHebergement;
    private String nom;
    private String adresse;
    private BigDecimal prixParNuit;
    private String description;
    private String specification;
    private List<String> imageUrls = new ArrayList<>(); // Liste d’images
    private int disponibilite; // ajout de la variable pour les disponibilites

    // Constructeur
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
    public int getIdHebergement() {
        return idHebergement;
    }

    public void setIdHebergement(int idHebergement) {
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

    public int getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(int disponibilite) {
        this.disponibilite = disponibilite;
    }


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