package controlador.gestor;

import android.content.Context;
import android.content.Intent;

import controlador.activity.AdministratorActivity;
import controlador.activity.UserActivity;

/**
 * Classe gestora de l'activitat amb la qual inicia el programa.
 * @author Jordi Gómez Lozano
 */
public class GestorMain {
    private static final String SHARED_PREFERENCES_TOKEN_KEY = "token";
    private static final String SHARED_PREFERENCES_EXPIRED_KEY = "expired_time";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private Context context;

    public GestorMain(Context context){
        this.context = context;
    }

    /**
     * Dirigieix l'usuari a la corresponent Activitat segons el seu role.
     * @param role role de l'usuari
     * @param email email de l'usuari
     * @param message missatge clau de l'intent
     * @author Jordi Gómez Lozano
     */
    public void dirigirUsuari(String role, String email, String message){
        Intent intent;

        if (role.equals(ROLE_ADMIN)) {

            intent = new Intent(context, AdministratorActivity.class);
        } else {

            intent = new Intent(context, UserActivity.class);
        }

        intent.putExtra(message, email);
        context.startActivity(intent);

    }

//    /**
//     * Obte la data d'expiració de l'arxiu.
//     * @param context
//     * @return un Date amb la data d'expiracio
//     * @author Jordi Gómez Lozano
//     */
//    public Date expiredDateFromSharedPreferences(Context context) {
//        GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(context);
//        SharedPreferences sharedPreferences =  gestorSharedPreferences.getEncryptedSharedPreferences();
//        Date data = new Date(sharedPreferences.getString(SHARED_PREFERENCES_EXPIRED_KEY, null));
//        return data;
//    }
//
//    /**
//     * Obte el token de l'arxiu.
//     * @param context
//     * @return l'string corresponent del token
//     * @author Jordi Gómez Lozano
//     */
//    public String tokenFromSharedPreferences(Context context) {
//        GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences();
//        SharedPreferences sharedPreferences =  gestorSharedPreferences.getEncryptedSharedPreferences(context);
//        return sharedPreferences.getString(SHARED_PREFERENCES_TOKEN_KEY, null);
//    }
}
