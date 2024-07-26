
package com.parking.database;
// VehiculeDAO.java
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import com.parking.classes.Camion;
import com.parking.classes.Moto;
import com.parking.classes.Vehicule;
import com.parking.classes.Voiture;

public class VehiculeDAO {
    private Connection connection;

    public VehiculeDAO() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/parc_vehicules", "root", "");
    }

    public void ajouterVehicule(Vehicule vehicule) throws SQLException {
        String query = "INSERT INTO vehicule (id, nom, marque, annee, type, nombre_de_portes, capacite_de_charge, sidecar) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, vehicule.getId());
            stmt.setString(2, vehicule.nom);
            stmt.setString(3, vehicule.marque);
            stmt.setInt(4, vehicule.getAnnee());
            if (vehicule instanceof Voiture) {
                Voiture voiture = (Voiture) vehicule;
                stmt.setString(5, "Voiture");
                stmt.setInt(6, voiture.getNombreDePortes());
                stmt.setNull(7, Types.DOUBLE);
                stmt.setNull(8, Types.BOOLEAN);
            } else if (vehicule instanceof Camion) {
                Camion camion = (Camion) vehicule;
                stmt.setString(5, "Camion");
                stmt.setNull(6, Types.INTEGER);
                stmt.setDouble(7, camion.getCapaciteDeCharge());
                stmt.setNull(8, Types.BOOLEAN);
            } else if (vehicule instanceof Moto) {
                Moto moto = (Moto) vehicule;
                stmt.setString(5, "Moto");
                stmt.setNull(6, Types.INTEGER);
                stmt.setNull(7, Types.DOUBLE);
                stmt.setBoolean(8, moto.isSidecar());
            }
            stmt.executeUpdate();
        }
    }

    public void supprimerVehicule(String id) throws SQLException {
        String query = "DELETE FROM vehicule WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    public void modifierVehicule(Vehicule vehicule) throws SQLException {
        String query = "UPDATE vehicule SET nom = ?, marque = ?, annee = ?, type = ?, nombre_de_portes = ?, capacite_de_charge = ?, sidecar = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, vehicule.nom);
            stmt.setString(2, vehicule.marque);
            stmt.setInt(3, vehicule.getAnnee());
            stmt.setString(8, vehicule.getId());
            if (vehicule instanceof Voiture) {
                Voiture voiture = (Voiture) vehicule;
                stmt.setString(4, "Voiture");
                stmt.setInt(5, voiture.getNombreDePortes());
                stmt.setNull(6, Types.DOUBLE);
                stmt.setNull(7, Types.BOOLEAN);
            } else if (vehicule instanceof Camion) {
                Camion camion = (Camion) vehicule;
                stmt.setString(4, "Camion");
                stmt.setNull(5, Types.INTEGER);
                stmt.setDouble(6, camion.getCapaciteDeCharge());
                stmt.setNull(7, Types.BOOLEAN);
            } else if (vehicule instanceof Moto) {
                Moto moto = (Moto) vehicule;
                stmt.setString(4, "Moto");
                stmt.setNull(5, Types.INTEGER);
                stmt.setNull(6, Types.DOUBLE);
                stmt.setBoolean(7, moto.isSidecar());
            }
            stmt.executeUpdate();
        }
    }

    public Vehicule getVehicule(String id) throws SQLException {
        String query = "SELECT * FROM vehicule WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String type = rs.getString("type");
                    if ("Voiture".equals(type)) {
                        return new Voiture(id, rs.getString("nom"), rs.getString("marque"), rs.getInt("annee"), rs.getInt("nombre_de_portes"));
                    } else if ("Camion".equals(type)) {
                        return new Camion(id, rs.getString("nom"), rs.getString("marque"), rs.getInt("annee"), rs.getDouble("capacite_de_charge"));
                    } else if ("Moto".equals(type)) {
                        return new Moto(id, rs.getString("nom"), rs.getString("marque"), rs.getInt("annee"), rs.getBoolean("sidecar"));
                    }
                }
            }
        }
        return null;
    }

    public Map<String, Vehicule> listerVehicules() throws SQLException {
        Map<String, Vehicule> vehicules = new HashMap<>();
        String query = "SELECT * FROM vehicule";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String id = rs.getString("id");
                String type = rs.getString("type");
                Vehicule vehicule = null;
                if ("Voiture".equals(type)) {
                    vehicule = new Voiture(id, rs.getString("nom"), rs.getString("marque"), rs.getInt("annee"), rs.getInt("nombre_de_portes"));
                } else if ("Camion".equals(type)) {
                    vehicule = new Camion(id, rs.getString("nom"), rs.getString("marque"), rs.getInt("annee"), rs.getDouble("capacite_de_charge"));
                } else if ("Moto".equals(type)) {
                    vehicule = new Moto(id, rs.getString("nom"), rs.getString("marque"), rs.getInt("annee"), rs.getBoolean("sidecar"));
                }
                if (vehicule != null) {
                    vehicules.put(id, vehicule);
                }
            }
        }
        return vehicules;
    }
}
