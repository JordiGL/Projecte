package controlador.gestor;


/**
 * Classe gestora per a recuperar la clau.
 * @author Jordi Gómez Lozano.
 */
public class GestorForgotPaswword {
    private static final String ERROR_UNACCEPTABLE_EMAIL = "Correu inacceptable";
    private static final String ERROR_EMPTY_EMAIL = "Introdueix l'email";
    private static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
