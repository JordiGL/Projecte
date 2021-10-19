package gestor;

import android.util.Log;

public class GestorForgotPaswword {
    private String error;
    private String email;

    public GestorForgotPaswword(String email) {
        this.email = email;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
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
            Log.i("Error", "curt");
            error = "Introdueix l'email";
            correcte = false;

        } else if (!email.matches(emailPattern)){
            Log.i("Error", "patern");
            error = "Correu inacceptable";
            correcte = false;

        }
        Log.i("Error", String.valueOf(correcte));
        return correcte;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
