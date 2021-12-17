package model;

import java.io.Serializable;

public class Icona implements Serializable {
    private int id;
    private String nom;
    private int posicio;
    private byte[] imatge;


    public Icona(String nom, int posicio) {
        this.nom = nom;
        this.posicio = posicio;
        this.imatge = null;
    }

    public Icona(String nom, int posicio, byte[] imatge, int id) {
        this.nom = nom;
        this.posicio = posicio;
        this.imatge = imatge;
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPosicio() {
        return posicio;
    }

    public void setPosicio(int posicio) {
        this.posicio = posicio;
    }

    public byte[] getImatge() {
        return imatge;
    }

    public void setImatge(byte[] imatge) {
        this.imatge = imatge;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
