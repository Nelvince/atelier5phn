package com.parking;
import com.parking.classes.Camion;
import com.parking.classes.Moto;
import com.parking.classes.Vehicule;
import com.parking.classes.Voiture;
import com.parking.database.VehiculeDAO;

// Main.java
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static VehiculeDAO vehiculeDAO;

    public static void main(String[] args) {
        try {
            vehiculeDAO = new VehiculeDAO();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                afficherMenu();
                int choix = scanner.nextInt();
                scanner.nextLine(); // Consomme la nouvelle ligne
                switch (choix) {
                    case 1:
                        ajouterVehicule(scanner);
                        break;
                    case 2:
                        supprimerVehicule(scanner);
                        break;
                    case 3:
                        modifierVehicule(scanner);
                        break;
                    case 4:
                        rechercherVehiculeParId(scanner);
                        break;
                    case 5:
                        listerVehicules();
                        break;
                    case 6:
                        afficherNombreDeVehicules();
                        break;
                    case 7:
                        System.out.println("Au revoir!");
                        return;
                    default:
                        System.out.println("Choix non valide.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void afficherMenu() {
        System.out.println("Menu:");
        System.out.println("1. Ajouter un véhicule");
        System.out.println("2. Supprimer un véhicule");
        System.out.println("3. Modifier un véhicule");
        System.out.println("4. Rechercher un véhicule par ID");
        System.out.println("5. Lister tous les véhicules");
        System.out.println("6. Afficher le nombre de véhicules");
        System.out.println("7. Quitter");
        System.out.print("Choisissez une option: ");
    }

    private static void ajouterVehicule(Scanner scanner) throws SQLException {
        System.out.print("Type de véhicule (Voiture, Camion, Moto): ");
        String type = scanner.nextLine();
        System.out.print("ID: ");
        String id = scanner.nextLine();
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        System.out.print("Marque: ");
        String marque = scanner.nextLine();
        System.out.print("Année: ");
        int annee = scanner.nextInt();
        scanner.nextLine(); // Consomme la nouvelle ligne

        Vehicule vehicule;
        if ("Voiture".equalsIgnoreCase(type)) {
            System.out.print("Nombre de portes: ");
            int nombreDePortes = scanner.nextInt();
            vehicule = new Voiture(id, nom, marque, annee, nombreDePortes);
        } else if ("Camion".equalsIgnoreCase(type)) {
            System.out.print("Capacité de charge: ");
            double capaciteDeCharge = scanner.nextDouble();
            vehicule = new Camion(id, nom, marque, annee, capaciteDeCharge);
        } else if ("Moto".equalsIgnoreCase(type)) {
            System.out.print("Sidecar (true/false): ");
            boolean sidecar = scanner.nextBoolean();
            vehicule = new Moto(id, nom, marque, annee, sidecar);
        } else {
            System.out.println("Type de véhicule non reconnu.");
            return;
        }

        vehiculeDAO.ajouterVehicule(vehicule);
        System.out.println("Véhicule ajouté avec succès.");
    }

    private static void supprimerVehicule(Scanner scanner) throws SQLException {
        System.out.print("ID du véhicule à supprimer: ");
        String id = scanner.nextLine();
        vehiculeDAO.supprimerVehicule(id);
        System.out.println("Véhicule supprimé avec succès.");
    }

    private static void modifierVehicule(Scanner scanner) throws SQLException {
        System.out.print("ID du véhicule à modifier: ");
        String id = scanner.nextLine();
        Vehicule vehicule = vehiculeDAO.getVehicule(id);
        if (vehicule == null) {
            System.out.println("Véhicule non trouvé.");
            return;
        }

        System.out.print("Nom (" + vehicule.getNom() + "): ");
        String nom = scanner.nextLine();
        if (!nom.isEmpty()) vehicule.nom = nom;

        System.out.print("Marque (" + vehicule.getMarque() + "): ");
        String marque = scanner.nextLine();
        if (!marque.isEmpty()) vehicule.marque = marque;

        System.out.print("Année (" + vehicule.getAnnee() + "): ");
        int annee = scanner.nextInt();
        scanner.nextLine(); // Consomme la nouvelle ligne
        if (annee != 0) vehicule.setAnnee(annee);

        if (vehicule instanceof Voiture) {
            System.out.print("Nombre de portes (" + ((Voiture) vehicule).getNombreDePortes() + "): ");
            int nombreDePortes = scanner.nextInt();
            if (nombreDePortes != 0) ((Voiture) vehicule).setNombreDePortes(nombreDePortes);
        } else if (vehicule instanceof Camion) {
            System.out.print("Capacité de charge (" + ((Camion) vehicule).getCapaciteDeCharge() + "): ");
            double capaciteDeCharge = scanner.nextDouble();
            if (capaciteDeCharge != 0) ((Camion) vehicule).capaciteDeCharge = capaciteDeCharge;
        } else if (vehicule instanceof Moto) {
            System.out.print("Sidecar (" + ((Moto) vehicule).isSidecar() + "): ");
            boolean sidecar = scanner.nextBoolean();
            ((Moto) vehicule).setSidecar(sidecar);
        }

        vehiculeDAO.modifierVehicule(vehicule);
        System.out.println("Véhicule modifié avec succès.");
    }

    private static void rechercherVehiculeParId(Scanner scanner) throws SQLException {
        System.out.print("ID du véhicule: ");
        String id = scanner.nextLine();
        Vehicule vehicule = vehiculeDAO.getVehicule(id);
        if (vehicule != null) {
            System.out.println("Véhicule trouvé: " + vehicule);
        } else {
            System.out.println("Véhicule non trouvé.");
        }
    }

    private static void listerVehicules() throws SQLException {
        Map<String, Vehicule> vehicules = vehiculeDAO.listerVehicules();
        for (Vehicule vehicule : vehicules.values()) {
            System.out.println(vehicule);
        }
    }

    private static void afficherNombreDeVehicules() throws SQLException {
        Map<String, Vehicule> vehicules = vehiculeDAO.listerVehicules();
        System.out.println("Nombre total de véhicules: " + vehicules.size());
    }
}
