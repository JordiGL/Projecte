package controlador.gestor;

import android.content.Context;
import android.content.Intent;

import controlador.activity.AdministratorTransitionActivity;
import controlador.activity.CommunicatorTransitionActivity;
import controlador.activity.UserActivity;

/**
 * Classe gestora de l'activitat amb la qual inicia el programa.
 * @author Jordi Gómez Lozano
 */
public class GestorMain {
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private Context context;

    public GestorMain(Context context){
        this.context = context;
    }

    /**
     * Dirigieix l'usuari a la corresponent Activitat segons el seu role.
     * @param role role de l'usuari
     * @param message missatge clau de l'intent
     * @author Jordi Gómez Lozano
     */
    public void dirigirUsuari(String role, String message){
        Intent intent;

        if (role.equals(ROLE_ADMIN)) {

            intent = new Intent(context, AdministratorTransitionActivity.class);
        } else {

            intent = new Intent(context, CommunicatorTransitionActivity.class);
        }

        intent.putExtra(message, role);
        context.startActivity(intent);

    }
}
