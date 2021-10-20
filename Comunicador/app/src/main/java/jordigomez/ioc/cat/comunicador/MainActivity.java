package jordigomez.ioc.cat.comunicador;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


import java.util.Date;

import gestor.GestorRequest;

/**
 * Activitat principal de benvinguda.
 * @see AppCompatActivity
 * @author Jordi Gómez Lozano.
 */
public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "jordigomez.ioc.cat.comunicador.MESSAGE";
    GestorRequest gestorRequest;
    TextView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo = findViewById(R.id.logoEscoltam);
        //Amagar barra superior del layout.
        getSupportActionBar().hide();
        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Animació
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.setAnimation(animation);


        new Handler().postDelayed(() -> {
            String role;
            String email;
            gestorRequest = new GestorRequest();
            Date expiredData = expiredDateFromSharedPreferences();
            String token = tokenFromSharedPreferences();

            if(token != null){

                role = gestorRequest.getRoleFromToken(token);
                email = gestorRequest.getEmailFromToken(token);
                dirigirUsuari(token, role, email, expiredData);

            }else{

                dirigirALogin();

            }

        }, 2000);

    }

    private void dirigirUsuari(String token, String role, String email, Date expiredData){
        Intent intent;

        if(token != null){

            if(expiredData.after(new Date())) {


                if(role.equals("ROLE_ADMIN")){

                    intent = new Intent(MainActivity.this, AdministratorActivity.class);
                }else{

                    intent = new Intent(MainActivity.this, ClientActivity.class);
                }

                intent.putExtra(EXTRA_MESSAGE, email);
                startActivity(intent);

            } else {

                dirigirALogin();
            }
        }else{

            dirigirALogin();

        }
    }

    private void dirigirALogin(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        //Assignem la transició al TextEdit que te la propietat com a logoTextTransition;
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, new Pair<>(logo, "logoTextTransition"));
        startActivity(intent, options.toBundle());
    }

    private Date expiredDateFromSharedPreferences() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        Date data = new Date(pref.getString("expired_time", null));
        return data;
    }

    private String tokenFromSharedPreferences() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        return pref.getString("token", null);
    }

    private String roleFromSharedPreferences() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        return pref.getString("role", null);
    }
}