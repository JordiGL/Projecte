package model;

/**
 * Classe Usuari
 * @author Jordi Gómez Lozano
 */
public class Usuari {
    private static final String DEFAULT_ROL = "ROLE_USER";
    private static final int ROL_USER_ID = 1;
    private String email;
    private String voice;
    private String password;
    private boolean enabled;
    private Rol rol;

    public Usuari(String email, Rol rol, String voice, String password, boolean enabled) {
        this.email = email;
        this.rol = rol;
        this.voice = voice;
        this.password = password;
        this.enabled = enabled;
    }

    public Usuari(String email, String voice, String password) {
        this.email = email;
        this.voice = voice;
        this.password = password;
        this.enabled = true;
        this.rol = new Rol(ROL_USER_ID, DEFAULT_ROL);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Rol getRole() {
        return rol;
    }

    public void setRole(Rol rol) {
        this.rol = rol;
    }

    @Override
    public String toString(){

        String usuari =
                "{\"username\":\""+getEmail()+"\"," +
                "\"password\":\""+getPassword()+"\"," +
                "\"enabled\":"+isEnabled()+"," +
                "\"voice\":\""+getVoice()+"\"," +
                "\"roles\":[{\"id\":" +getRole().getId()+"," +
                "\"name\":\""+getRole().getName()+ "\""+
                "}]" +
                "}";

        return usuari;
    }
}
