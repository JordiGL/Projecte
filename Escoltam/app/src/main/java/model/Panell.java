package model;

import java.util.List;

public class Panell {
    private String nom;
    private int posicio;
    private boolean favorit;
    private List<Icona> icones;

    public Panell(String nom, int posicio, boolean favorit, List<Icona> icones) {
        this.nom = nom;
        this.posicio = posicio;
        this.favorit = favorit;
        this.icones = icones;
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

    public boolean isFavorit() {
        return favorit;
    }

    public void setFavorit(boolean favorit) {
        this.favorit = favorit;
    }

    public List<Icona> getIcones() {
        return icones;
    }

    public void setIcones(List<Icona> icones) {
        this.icones = icones;
    }

}
