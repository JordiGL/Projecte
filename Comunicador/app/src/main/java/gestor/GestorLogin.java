package gestor;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import java.util.List;

import jordigomez.ioc.cat.comunicador.AdministratorActivity;
import jordigomez.ioc.cat.comunicador.ClientActivity;
import model.Usuari;

/**
 * Classe gestora de l'inici de sessió.
 * @author Jordi Gómez Lozano.
 */
public class GestorLogin {
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ERROR_EMAIL_PASSWORD = "Email o clau incorrecta";
    public static final String ERROR_EMPTY_PASSWORD = "Introdueix la clau";
    public static final String ERROR_EMPTY_EMAIL = "Introdueix l'email";
    public static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String email;
    private String password;
    private String error;
    private int isAdministrator;

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


    public int isAdministrator() {
        return isAdministrator;
    }

    public void setAdministrator(int administrator) {
        isAdministrator = administrator;
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

        }
// Ho he tret per a fer proves amb el servidor.
//        else if(password.length() < 8 ){
//
//            error = "Mínim de vuit caràcters";
//            correcte = false;
//
//        }else if(!Pattern.compile("[0-9]").matcher(password).find()) {
//
//            error = "Ha de contenir un número";
//            correcte = false;
//        }

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
        isAdministrator = 1;

        for(Usuari usuari: usuaris){
            if(usuari.getEmail().equals(email) && usuari.getPassword().equals(password)){
                isAdministrator = usuari.getAdministrator();
                correcte = true;
            }
        }

        if(!correcte){

            error = "ERROR_EMAIL_PASSWORD";
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

            intent = new Intent(context, AdministratorActivity.class);
        }else{

            intent = new Intent(context, ClientActivity.class);
        }

        intent.putExtra(message, role);
        context.startActivity(intent);

    }

}
