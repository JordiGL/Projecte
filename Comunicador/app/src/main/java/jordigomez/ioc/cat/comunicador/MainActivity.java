package jordigomez.ioc.cat.comunicador;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Date;

import gestor.GestorMain;
import gestor.GestorRequest;

/**
 * Activitat principal de benvinguda.
 * @see AppCompatActivity
 * @author Jordi Gómez Lozano.
 */
public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "jordigomez.ioc.cat.comunicador.MESSAGE";
    public static final String LOGO_TO_TRANSITION_ID = "logoTextTransition";
    GestorRequest gestorRequest;
    TextView logo;
    GestorMain gestorMain;

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
            Date expiredData;
            gestorMain = new GestorMain(this);
            gestorRequest = new GestorRequest();

            String token = gestorMain.tokenFromSharedPreferences(this);

            if(token != null){
                expiredData = gestorMain.expiredDateFromSharedPreferences(this);

                if(expiredData.after(new Date())) {

                    role = gestorRequest.getRoleFromToken(token);
                    email = gestorRequest.getEmailFromToken(token);
                    gestorMain.dirigirUsuari(role, email, EXTRA_MESSAGE);

                } else {

                    dirigirALogin();
                }
            }else{

                dirigirALogin();
            }
        }, 2000);

    }

    /**
     * Dirigeix l'usuari cap al LoginActivity
     * @author Jordi Gómez Lozano
     */
    private void dirigirALogin(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        //Assignem la transició al TextEdit que te la propietat com a logoTextTransition;
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, new Pair<>(logo, LOGO_TO_TRANSITION_ID));
        startActivity(intent, options.toBundle());
    }
}