package model;

/**
 * Classe Role
 * @author Jordi Gómez Lozano
 */
public class Rol {
    private int id;
    private String name;

    public Rol(int id, String name) {
        this.name = name;
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
