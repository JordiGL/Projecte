package gestor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.util.Date;
import jordigomez.ioc.cat.comunicador.AdministratorActivity;
import jordigomez.ioc.cat.comunicador.ClientActivity;

public class GestorMain {
    Context context;

    public GestorMain(Context context){
        this.context = context;
    }

    public void dirigirUsuari(String role, String email, String message){
        Intent intent;

        if (role.equals("ROLE_ADMIN")) {

            intent = new Intent(context, AdministratorActivity.class);
        } else {

            intent = new Intent(context, ClientActivity.class);
        }

        intent.putExtra(message, email);
        context.startActivity(intent);

    }

    public Date expiredDateFromSharedPreferences(Context context) {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences("InfoObt", 0);
        Date data = new Date(pref.getString("expired_time", null));
        return data;
    }

    public String tokenFromSharedPreferences(Context context) {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences("InfoObt", 0);
        return pref.getString("token", null);
    }
}
