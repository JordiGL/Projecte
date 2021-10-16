package gestor;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.regex.Pattern;

import model.Usuari;

/**
 * Classe gestora de l'inici de sessió.
 * @author Jordi Gómez Lozano.
 */
public class GestorLogin {
    private String email;
    private String password;
    private String error;
    private boolean isAdministrator;

    public GestorLogin(){}

    public GestorLogin(String email, String password){
        this.email = email;
        this.password = password;
    }

    public GestorLogin(String email, String password, boolean isAdministrator){
        this.email = email;
        this.password = password;
        this.isAdministrator = isAdministrator;
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


    public boolean isAdministrator() {
        return isAdministrator;
    }

    public void setAdministrator(boolean administrator) {
        isAdministrator = administrator;
    }

    /**
     * Comprova que l'email entrat entrat per l'usuari sigui correcte
     * @return true si es correcte, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    public boolean emailChecker(){
        boolean correcte = true;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(email.trim().length() == 0){

            error = "Introdueix l'email";
            correcte = false;

        } else if (!email.matches(emailPattern)){

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

            error = "Introdueix la clau";
            correcte = false;

        } else if(password.length() < 8 ){

            error = "Mínim de vuit caràcters";
            correcte = false;

        }else if(!Pattern.compile("[0-9]").matcher(password).find()) {

            error = "Ha de contenir un número";
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
    public boolean checkAuthentication(@NonNull List<Usuari> usuaris){
        boolean correcte = false;
        isAdministrator = false;

        for(Usuari usuari: usuaris){
            if(usuari.getEmail().equals(email) && usuari.getPassword().equals(password)){
                isAdministrator = usuari.isAdministrator();
                correcte = true;
            }
        }

        if(!correcte){

            error = "Usuari o clau incorrectes";
        }

        return correcte;
    }
}
