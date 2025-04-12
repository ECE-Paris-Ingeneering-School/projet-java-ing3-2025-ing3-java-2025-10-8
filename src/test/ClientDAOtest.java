package test;

import Modele.Client;
import Modele.Client.TypeClient;
import DAO.ClientDAO;
import java.util.Scanner;

public class ClientDAOtest {
    public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            ClientDAO clientDAO = new ClientDAO();

            System.out.println("=== AJOUT D'UN CLIENT ===");

            System.out.print("Nom : ");
            String nom = scanner.nextLine();

            System.out.print("Prénom : ");
            String prenom = scanner.nextLine();

            System.out.print("Email : ");
            String email = scanner.nextLine();

            System.out.print("Mot de passe : ");
            String mdp = scanner.nextLine();

            System.out.print("Type de client (Particulier / Entreprise): ");
            String type = scanner.nextLine().trim();

            // Vérification du type
            TypeClient typeClient;
            if (type.equalsIgnoreCase("Nouveau")) {
                typeClient = TypeClient.PARTICULIER;
            } else if (type.equalsIgnoreCase("Ancien")) {
                typeClient = TypeClient.ENTREPRISE;
            } else {
                System.out.println("Type de client invalide");
                return;
            }

            // Création et ajout
            Client nouveauClient = new Client(0, nom, prenom, email, mdp, typeClient);
            boolean resultat = clientDAO.ajouterClient(nouveauClient);

            if (resultat) {
                System.out.println("Client ajouté avec succès !");
            } else {
                System.out.println("Échec de l'ajout du client.");
            }

            // (Optionnel) Rechercher et afficher le client
            Client clientTrouve = clientDAO.getClientParEmail(email);
            if (clientTrouve != null) {
                System.out.println("\nInfos client depuis la base :");
                System.out.println("Nom : " + clientTrouve.getNom());
                System.out.println("Prénom : " + clientTrouve.getPrenom());
                System.out.println("Email : " + clientTrouve.getEmail());
                System.out.println("Type : " + clientTrouve.getTypeClient());
            }

            scanner.close();
        }
}
