package controlador.gestor;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.regex.Pattern;

import controlador.activity.AdministratorTransitionActivity;
import controlador.activity.CommunicatorTransitionActivity;
import controlador.activity.UserActivity;
import model.Usuari;

/**
 * Classe gestora de l'inici de sessió.
 * @author Jordi Gómez Lozano.
 */
public class GestorLogin {
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ERROR_EMAIL_PASSWORD = "Email o clau incorrecta";
    private static final String ERROR_EMPTY_PASSWORD = "Introdueix la clau";
    private static final String ERROR_EMPTY_EMAIL = "Introdueix l'email";
    private static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final String PASSWORD_NUMBER_PATTERN = "[0-9]";
    private static final String MINIM_DE_CINC_CARACTERS = "Mínim de cinc caràcters";
    private static final String HA_DE_CONTENIR_UN_NUMERO = "Ha de contenir un número";
    private static final int MAX_LENGTH_PASSWORD = 5;
    private String email;
    private String password;
    private String error;

    public GestorLogin(){}

    public GestorLogin(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Comprova que l'email entrat entrat per l'usuari sigui correcte
     * @return true si es correcte, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    public boolean emailChecker(){
        boolean correcte = true;

        if(email.trim().length() == 0){

            error = ERROR_EMPTY_EMAIL;
            correcte = false;

        } else if (!email.matches(EMAIL_PATTERN)){

            error = "Correu inacceptable";
            correcte = false;

        }

        return correcte;
    }

    /**
     * Comprova que la clau entrada per l'usuari sigui correcte
     * @return true si es correcte, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    public boolean passwordChecker(){

        boolean correcte = true;

        if(password.trim().length() == 0){

            error = ERROR_EMPTY_PASSWORD;
            correcte = false;

        }else if(password.length() < MAX_LENGTH_PASSWORD){

            error = MINIM_DE_CINC_CARACTERS;
            correcte = false;

        }else if(!Pattern.compile(PASSWORD_NUMBER_PATTERN).matcher(password).find()) {

            error = HA_DE_CONTENIR_UN_NUMERO;
            correcte = false;
        }

        return correcte;
    }

    /**
     * Comprova l'autentificació de l'usuari.
     * @param usuaris llista d'usuaris de la base de dades
     * @return true si es correcte, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    public boolean checkAuthenticationForTest(@NonNull List<Usuari> usuaris){
        boolean correcte = false;

        for(Usuari usuari: usuaris){
            if(usuari.getEmail().equals(email) && usuari.getPassword().equals(password)){
                correcte = true;
            }
        }

        if(!correcte){

            error = ERROR_EMAIL_PASSWORD;
        }

        return correcte;
    }

    /**
     * Comprova l'autentificació de l'usuari.
     * @param usuari usuari a comprovar autentificació.
     * @return true si es correcte, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    public boolean checkAuthentication(Usuari usuari){
        boolean correcte = false;

        if(usuari != null){
            if(usuari.getPassword().equals(password)){

                correcte = true;
            }
        }

        if(!correcte){

            error = ERROR_EMAIL_PASSWORD;
        }

        return correcte;
    }

    /**
     * Direcciona un usuari segons si és usuari o client
     * @param role de l'usuari a direccionar.
     * @author Jordi Gómez Lozano.
     */
    public void dirigirUsuariSegonsRole(String role, Context context, String message) {
        Intent intent;

        if(role.equals(ROLE_ADMIN)){

            intent = new Intent(context, AdministratorTransitionActivity.class);
        }else{

            intent = new Intent(context, CommunicatorTransitionActivity.class);
        }

        intent.putExtra(message, role);
        context.startActivity(intent);
    }

}
