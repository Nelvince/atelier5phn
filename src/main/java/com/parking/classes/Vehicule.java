package com.parking.classes;

// Vehicule.java
public abstract class Vehicule {
    protected String id;
    public String nom;
    public String marque;
    protected int annee;

    public Vehicule(String id, String nom, String marque, int annee) {
        this.id = id;
        this.nom = nom;
        this.marque = marque;
        this.annee = annee;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMarque() {
        return this.marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public int getAnnee() {
        return this.annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    // Getters, setters et toString()
}

