package model;

import androidx.annotation.Nullable;

/**
 *
 * @author Jordi Gómez Lozano
 */
public class Usuari {
    private String email;
    private int administrator;
    private String voice;
    private String password;
    private boolean enabled;

    public Usuari(){}

    public Usuari(String email, int administrator, String voice, String password, boolean enabled) {
        this.email = email;
        this.administrator = administrator;
        this.voice = voice;
        this.password = password;
        this.enabled = enabled;
    }

    public Usuari(String email, String voice, String password) {
        this.email = email;
        this.administrator = 1;
        this.voice = voice;
        this.password = password;
        this.enabled = true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int isAdministrator() {
        return administrator;
    }

    public void setAdministrator(int administrator) {
        this.administrator = administrator;
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
}
