package controlador.gestor;

import android.util.Log;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Classe gestora de la pantalla de configuració.
 * @author Jordi Gómez Lozano
 */
public class GestorSettings {
    private static final String ERROR_VOICE = "Selecciona la veu";
    private static final String ERROR_MATCH_PASSWORD = "La clau no coincideix";
    private static final String ERROR_CONFIRMATION_KEY = "Confirma la clau";
    private static final String ERROR_EMPTY_PASSWORD = "Introdueix la clau";
    private static final String MINIM_DE_CINC_CARACTERS = "Mínim de cinc caràcters";
    private static final String HA_DE_CONTENIR_UN_NUMERO = "Ha de contenir un número";
    private static final String MALE = "MALE";
    private static final String FEMALE = "FEMALE";
    private static final String CLAU_INCORRECTA = "Clau incorrecta";

    private String receivedPassword;
    private String previousPassword;
    private String newPassword;
    private String conformPassword;
    private String voice;
    private String error;

    public GestorSettings() {
    }

    public GestorSettings(String previousPassword, String receivedPassword, String voice) {
        this.previousPassword = previousPassword;
        this.receivedPassword = receivedPassword;
        this.voice = voice;
    }

    public GestorSettings(String previousPassword, String receivedPassword, String newPassword, String conformPassword) {
        this.previousPassword = previousPassword;
        this.receivedPassword = receivedPassword;
        this.newPassword = newPassword;
        this.conformPassword = conformPassword;
    }

    public void setReceivedPassword(String receivedPassword) {
        this.receivedPassword = receivedPassword;
    }

    public void setPreviousPassword(String previousPassword) {
        this.previousPassword = previousPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setConformPassword(String conformPassword) {
        this.conformPassword = conformPassword;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getError() {
        return error;
    }

    /**
     * Comprova que la clau entrada per l'usuari sigui correcte
     * @return true si es correcte, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    public boolean previousPasswordChecker(){

        boolean correcte = true;

        if(previousPassword.trim().length() == 0){

            error = ERROR_EMPTY_PASSWORD;
            correcte = false;

        }

        if(!previousPassword.equals(receivedPassword)){
            error = CLAU_INCORRECTA;
            correcte = false;
        }

        return correcte;
    }

    /**
     * Comprova que la clau entrada per l'usuari sigui correcte
     * @return true si es correcte, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    public boolean newPasswordChecker(){

        boolean correcte = true;

        if(newPassword.trim().length() == 0){

            error = ERROR_EMPTY_PASSWORD;
            correcte = false;

        }else if(newPassword.length() < 5  ){

            error = MINIM_DE_CINC_CARACTERS;
            correcte = false;

        }else if(!Pattern.compile("[0-9]").matcher(newPassword).find()) {

            error = HA_DE_CONTENIR_UN_NUMERO;
            correcte = false;
        }

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

        } else if (!newPassword.equals(conformPassword)){

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
