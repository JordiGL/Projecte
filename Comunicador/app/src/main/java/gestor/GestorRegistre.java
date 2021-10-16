package gestor;

import android.util.Log;

import java.util.Arrays;
import java.util.regex.Pattern;

import model.Usuari;

/**
 * Classe gestora del registre.
 * @author Jordi Gómez Lozano.
 */
public class GestorRegistre {

    private String nameSurname;
    private String phone;
    private String email;
    private String password;
    private String conformPassword;
    private String voice;
    private String error;

    public GestorRegistre(){}

    public GestorRegistre(Usuari usuari, String conformPassword){
        this.nameSurname = usuari.getNameSurname();
        this.phone = usuari.getPhone();
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

    public String getNameSurname() {
        return nameSurname;
    }

    public void setNameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
     * Comprova que el nom entrat entrat per l'usuari sigui correcte
     * @return true si es correcte, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    public boolean nameSurnameChecker(){

        boolean correcte = true;

        if(nameSurname.trim().length() == 0){

            error = "Introdueix el nom";
            correcte = false;
        }

        return correcte;
    }

    /**
     * Comprova que el telèfon entrat entrat per l'usuari sigui correcte
     * @return true si es correcte, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    public boolean phoneChecker(){

        boolean correcte = true;
        String telefonPattern = "^\\+?\\(?[0-9]{1,3}\\)? ?-?[0-9]{1,3} ?-?[0-9]{3,5} ?-?[0-9]{4}( ?-?[0-9]{3})?";

        if(phone.trim().length() == 0){

            error = "Introdueix el telèfon";
            correcte = false;

        } else if (!phone.matches(telefonPattern)){

            error = "Telèfon inacceptable";
            correcte = false;

        } else {
            correcte = true;
        }

        return correcte;
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
     * Comprova que la clau de comformitat entrada per l'usuari sigui correcte
     * @return true si es correcte, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    public boolean conformPasswordChecker(){

        boolean correcte = true;

        if(conformPassword.trim().length() == 0){

            error = "Confirma la clau";
            correcte = false;

        } else if (!password.equals(conformPassword)){

            error = "La clau no coincideix";

            correcte = false;
        }

        return correcte;
    }

    public boolean voiceChecker(){

        boolean correcte = true;

        if(!Arrays.asList("male", "female").contains(voice)) {

            error = "Selecciona la veu";
            correcte = false;
        }

        return correcte;
    }

}
