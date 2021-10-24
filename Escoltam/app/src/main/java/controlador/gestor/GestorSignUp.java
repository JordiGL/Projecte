package controlador.gestor;

import java.util.Arrays;
import java.util.regex.Pattern;
import model.Usuari;

/**
 * Classe gestora del registre.
 * @author Jordi Gómez Lozano.
 */
public class GestorSignUp {

    public static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static final String PASSWORD_NUMBER_PATTERN = "[0-9]";
    public static final String MALE = "male";
    public static final String FEMALE = "female";
    public static final String ERROR_VOICE = "Selecciona la veu";
    public static final String ERROR_MATCH_PASSWORD = "La clau no coincideix";
    public static final String ERROR_CONFIRMATION_KEY = "Confirma la clau";
    public static final String ERROR_CONTAIN_NUMBER = "Ha de contenir un número";
    public static final String ERROR_MINIMUM_NUMBERS = "Mínim de vuit caràcters";
    public static final String ERROR_EMPTY_PASSWORD = "Introdueix la clau";
    public static final String ERROR_UNACCEPTABLE_EMAIL = "Correu inacceptable";
    public static final String ERROR_EMPTY_EMAIL = "Introdueix l'email";
    private String email;
    private String password;
    private String conformPassword;
    private String voice;
    private String error;

    public GestorSignUp(){}

    public GestorSignUp(Usuari usuari, String conformPassword){
        this.voice = usuari.getVoice();
        this.email = usuari.getEmail();
        this.password = usuari.getPassword();
        this.conformPassword = conformPassword;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
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

    public String getConformPassword() {
        return conformPassword;
    }

    public void setConformPassword(String conformPassword) {
        this.conformPassword = conformPassword;
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

            error = ERROR_UNACCEPTABLE_EMAIL;
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
//        else if(password.length() < 8 ){
//
//            error = ERROR_MINIMUM_NUMBERS;
//            correcte = false;
//
//        }else if(!Pattern.compile(PASSWORD_NUMBER_PATTERN).matcher(password).find()) {
//
//            error = ERROR_CONTAIN_NUMBER;
//            correcte = false;
//        }

        return correcte;
    }

    /**
     * Comprova que la clau de comformitat entrada per l'usuari sigui correcte
     * @return true si es correcte, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    public boolean conformPasswordChecker(){

        boolean correcte = true;

        if(conformPassword.trim().length() == 0){

            error = ERROR_CONFIRMATION_KEY;
            correcte = false;

        } else if (!password.equals(conformPassword)){

            error = ERROR_MATCH_PASSWORD;

            correcte = false;
        }

        return correcte;
    }

    /**
     * Comprova que la veu seleccionada per l'usuari sigui correcte
     * @return true si es correcte, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    public boolean voiceChecker(){

        boolean correcte = true;

        if(!Arrays.asList(MALE, FEMALE).contains(voice)) {

            error = ERROR_VOICE;
            correcte = false;
        }

        return correcte;
    }

}
