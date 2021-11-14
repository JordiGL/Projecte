package model;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe Usuari
 * @author Jordi Gómez Lozano
 */
public class Usuari {
    private int idUsuari;
    private String email;
    private String voice;
    private String password;
    private boolean enabled;
    private Role role;

    public Usuari(int idUsuari, String email, Role role, String voice, String password, boolean enabled) {
        this.idUsuari = idUsuari;
        this.email = email;
        this.role = role;
        this.voice = voice;
        this.password = password;
        this.enabled = enabled;
    }

    public Usuari(String email, String voice, String password) {
        this.idUsuari = 0;
        this.email = email;
        this.voice = voice;
        this.password = password;
        this.enabled = true;
        this.role = new Role(1, "ROLE_USER");
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getIdUsuari() {
        return idUsuari;
    }

    public void setIdUsuari(int idUsuari) {
        this.idUsuari = idUsuari;
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
