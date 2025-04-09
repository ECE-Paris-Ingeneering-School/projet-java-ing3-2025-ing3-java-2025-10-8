package model;

public class Client extends Utilisateur {
    private boolean ancienClient;

    public Client() {}

    public Client(int id, String nom, String prenom, String email, String motDePasse, boolean ancienClient) {
        super(id, nom, prenom, email, motDePasse);
        this.ancienClient = ancienClient;
    }

    public boolean isAncienClient() {
        return ancienClient;
    }

    public void setAncienClient(boolean ancienClient) {
        this.ancienClient = ancienClient;
    }
}