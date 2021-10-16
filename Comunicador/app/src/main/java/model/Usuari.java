package model;

import androidx.annotation.Nullable;

/**
 *
 * @author Jordi Gómez Lozano
 */
public class Usuari {

    private String email;

    private boolean administrator;

    private String voice;

    private String nameSurname;

    private String password;

    private String phone;
    @Nullable
    private String token;

    public Usuari(){}

    public Usuari(String email, boolean administrator, String voice, String nameSurname, String password, String phone) {
        this.email = email;
        this.administrator = administrator;
        this.voice = voice;
        this.nameSurname = nameSurname;
        this.password = password;
        this.phone = phone;
        this.token = null;
    }

    public Usuari(String email, String voice, String nameSurname, String password, String phone) {
        this.email = email;
        this.administrator = false;
        this.voice = voice;
        this.nameSurname = nameSurname;
        this.password = password;
        this.phone = phone;
        this.token = null;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public void setNameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
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

    @Nullable
    public String getToken() {
        return token;
    }

    public void setToken(@Nullable String token) {
        this.token = token;
    }
}
