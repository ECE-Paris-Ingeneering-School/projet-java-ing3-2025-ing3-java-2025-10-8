package model;

public class Test {
    public static void main(String[] args) {
        Utilisateur u = new Utilisateur(1, "John", "Doe", "john@mail.com", "1234");
        System.out.println("Nom : " + u.nom);
    }
}
