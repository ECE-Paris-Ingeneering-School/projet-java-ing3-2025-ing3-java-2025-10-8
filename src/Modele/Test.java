package Modele;

/**
 * Classe de test pour démontrer la création d'un utilisateur et l'affichage de son nom.
 */

public class Test {
    /**
     * Méthode principale pour exécuter le test.
     *
     * @param args Arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        Utilisateur u = new Utilisateur(1, "John", "Doe", "john@mail.com", "1234");
        System.out.println("Nom : " + u.nom);
    }
}
