package test;

import Modele.Admin;
import DAO.AdminDAO;
import java.util.Scanner;

public class AdminDAOtest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AdminDAO adminDAO = new AdminDAO();

        System.out.println("=== AJOUT D'UN ADMIN ===");

        System.out.print("Nom : ");
        String nom = scanner.nextLine();

        System.out.print("Prénom : ");
        String prenom = scanner.nextLine();

        System.out.print("Email : ");
        String email = scanner.nextLine();

        System.out.print("Mot de passe : ");
        String mdp = scanner.nextLine();

        System.out.print("Role : ");
        String role = scanner.nextLine().trim();

        // Création et ajout
        Admin nouveauAdmin = new Admin(0, nom, prenom, email, mdp, role);
        boolean resultat = adminDAO.ajouterAdmin(nouveauAdmin);

        if (resultat) {
            System.out.println("Admin ajouté avec succès !");
        } else {
            System.out.println("Échec de l'ajout de l'admin.");
        }

        // ✅ Récupérer l'ID après insertion
        int id = nouveauAdmin.getIdUtilisateur();

        // (Optionnel) Rechercher et afficher l'admin
        Admin adminTrouve = adminDAO.getAdminParId(id);
        if (adminTrouve != null) {
            System.out.println("\nInfos Admin depuis la base :");
            System.out.println("Nom : " + adminTrouve.getNom());
            System.out.println("Prénom : " + adminTrouve.getPrenom());
            System.out.println("Email : " + adminTrouve.getEmail());
            System.out.println("Role : " + adminTrouve.getRole());
        }

        scanner.close();
    }
}
