package model;

/**
 *
 * @author Jordi Gómez Lozano
 */
public class Usuari {

    private String email;

    private boolean enabled;

    private String gender;

    private String nom;

    private String password;

    private String phone;

    public Usuari(){}

    public Usuari(String email, boolean enabled, String gender, String nom, String password, String phone) {
        this.email = email;
        this.enabled = enabled;
        this.gender = gender;
        this.nom = nom;
        this.password = password;
        this.phone = phone;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
