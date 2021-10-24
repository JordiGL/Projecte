package controlador.gestor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.util.Date;
import controlador.AdministratorActivity;
import controlador.ClientActivity;

/**
 * Classe gestora de l'activitat amb la qual inicia el programa.
 * @author Jordi Gómez Lozano
 */
public class GestorMain {
    public static final String SHARED_PREFERENCES_TOKEN_KEY = "token";
    public static final String SHARED_PREFERENCES_EXPIRED_KEY = "expired_time";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    Context context;

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

            intent = new Intent(context, ClientActivity.class);
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
