package jordigomez.ioc.cat.comunicador;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


import gestor.GestorRequest;

/**
 * Activitat principal de benvinguda.
 * @see AppCompatActivity
 * @author Jordi Gómez Lozano.
 */
public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "jordigomez.ioc.cat.comunicador.MESSAGE";
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
            Log.i("Info" , "ha entrat");
            String token = null;
            String role = null;

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    GestorRequest gestorRequest = new GestorRequest();

//                    En el requestToken() hi tinc per defecte l'email i la clau d'un usuari, hauria
//                    de passar-los com a paràmetre requestToken(username, clau). El mètode el tinc aquí
//                    per a proves, hauria d'estar en el LoginActivity.
                    int responseCode = gestorRequest.requestToken();

                    if(responseCode == 200) {

                        String token = gestorRequest.getToken();
                        String role = gestorRequest.getRoleFromToken(token);
                        dirigirUsuariSegonsRole(role);

                    } else {

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        //Assignem la transició al TextEdit que te la propietat com a logoTextTransition;
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, new Pair<>(logo, "logoTextTransition"));
                        startActivity(intent, options.toBundle());

                    }
                }
            });
            thread.start();
        }, 2000);

    }

    /**
     * Direcciona un usuari segons si és usuari o client
     * @param role de l'usuari a direccionar.
     * @author Jordi Gómez Lozano.
     */
    private void dirigirUsuariSegonsRole(String role) {
        Intent intent;

        if(role.equals("ROLE_ADMIN")){

            intent = new Intent(MainActivity.this, AdministratorActivity.class);
        }else{

            intent = new Intent(MainActivity.this, ClientActivity.class);
        }

        intent.putExtra(EXTRA_MESSAGE, role);
        startActivity(intent);

    }
}