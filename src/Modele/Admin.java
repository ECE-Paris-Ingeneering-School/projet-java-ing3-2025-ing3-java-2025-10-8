package Modele;

public class Admin extends Utilisateur{
    private String role;
    public Admin (int id_utilisateur, String nom, String prenom, String email, String mdp, String roleSpecifique){
        super(id_utilisateur, nom, prenom, email, mdp); // hérité de la classe Utilisateur
        this.role = roleSpecifique;
    }

    //Getter
    public String getRole(){
        return role;
    }

    //Setter
    public void setRole(String role){
        this.role = role;
    }

}
