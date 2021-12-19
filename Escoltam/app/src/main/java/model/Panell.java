package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Panell
 * @see Serializable
 * @see Icona
 * @author Jordi Gómez Lozano
 */
public class Panell implements Serializable {
    private int id;
    private String nom;
    private int posicio;
    private boolean favorit;
    private List<Icona> icones;

    public Panell(){}

    public Panell(String nom, int posicio, boolean favorit) {
        this.nom = nom;
        this.posicio = posicio;
        this.favorit = favorit;
        this.icones = new ArrayList<>();
        this.id = 0;
    }

    public Panell(String nom, int posicio, boolean favorit, List<Icona> icones) {
        this.nom = nom;
        this.posicio = posicio;
        this.favorit = favorit;
        this.icones = icones;
        this.id = 0;
    }

    public Panell(String nom, int posicio, boolean favorit, List<Icona> icones, int id) {
        this.nom = nom;
        this.posicio = posicio;
        this.favorit = favorit;
        this.icones = icones;
        this.id = id;
    }

    public Panell(String nom, int posicio, List<Icona> icones, int id) {
        this.nom = nom;
        this.posicio = posicio;
        this.favorit = false;
        this.icones = icones;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        String panell =
                "{\"nom\": \""+getNom()+"\"," +
                "\"posicio\": \""+getPosicio()+"\"," +
                "\"favorit\": \""+isFavorit()+"\""+
                "}";
        return panell;
    }
}
