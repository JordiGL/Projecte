package model;

public class Icona {
    private String nom;
    private int posicio;
//    private byte[] imatge;


    public Icona(String nom, int posicio) {
        this.nom = nom;
        this.posicio = posicio;
    }

//    public Icona(String nom, int posicio, byte[] imatge) {
//        this.nom = nom;
//        this.posicio = posicio;
//        this.imatge = imatge;
//    }

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

//    public byte[] getImatge() {
//        return imatge;
//    }
//
//    public void setImatge(byte[] imatge) {
//        this.imatge = imatge;
//    }
}
