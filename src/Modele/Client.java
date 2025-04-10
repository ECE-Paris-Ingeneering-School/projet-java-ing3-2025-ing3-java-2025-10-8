package Modele;

public class Client extends Utilisateur {
    private TypeClient typeClient;

    public enum TypeClient {
        Nouveau,
        Ancien
    }

    // Constructeur
    public Client(int id_utilisateur, String nom, String prenom, String email, String mdp, TypeClient typeClient) {
        super(id_utilisateur, nom, prenom, email, mdp); // Appel du constructeur de Utilisateur
        this.typeClient = typeClient;
    }

    // Getter
    public TypeClient getTypeClient() {
        return typeClient;
    }

    // Setter
    public void setTypeClient(TypeClient typeClient) {
        this.typeClient = typeClient;
    }

}
