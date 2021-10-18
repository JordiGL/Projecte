package jordigomez.ioc.cat.comunicador;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

import dao.DAOUsuariImpl;
import interfaces.DAOUsuari;
import model.Usuari;

/**
 * Activitat principal de benvinguda.
 * @see AppCompatActivity
 * @author Jordi Gómez Lozano.
 */
public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "jordigomez.ioc.cat.comunicador.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView logo = findViewById(R.id.logoEscoltam);
        //Amagar barra superior del layout.
        getSupportActionBar().hide();
        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Animació
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.setAnimation(animation);

        new Handler().postDelayed(() -> {
            String token = tokenFromSharedPreferences();

            if( token != null) {

                direccionarUsuari(getEmailFromToken(token));
            } else {

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                //Assignem la transició al TextEdit que te la propietat com a logoTextTransition;
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, new Pair<>(logo, "logoTextTransition"));
                startActivity(intent, options.toBundle());

            }
        }, 2000);

    }

    /**
     * Decodifica el token i obte l'email.
     * @param token L'String del token de l'usuari.
     * @return un String amb l'email.
     * @author Jordi Gómez Lozano.
     */
    @Nullable
    private String getEmailFromToken(String token) {
        JWT parsedJWT = new JWT(token);
        Claim subscriptionMetaData = parsedJWT.getClaim("email");
        String decodedEmail = subscriptionMetaData.asString();
        Log.i("MainToken", "Decoded token, email: " +decodedEmail);
        return decodedEmail;
    }

    /**
     * Llegeix el document on tenim el token guardat.
     * @return String amb el token.
     * @author Jordi Gómez Lozano.
     */
    private String tokenFromSharedPreferences() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        return pref.getString("token", null);
    }

    /**
     * Direcciona un usuari segons si és usuari o client
     * @param email de l'usuari a direccionar.
     * @author Jordi Gómez Lozano.
     */
    private void direccionarUsuari(String email) {
        Intent intent;
        DAOUsuari dao = new DAOUsuariImpl(MainActivity.this);
        Usuari usuari = dao.obtenir(email);

        if(usuari.isAdministrator() == 2){

            intent = new Intent(MainActivity.this, AdministratorActivity.class);
        }else{

            intent = new Intent(MainActivity.this, ClientActivity.class);
        }
        intent.putExtra(EXTRA_MESSAGE, email);
        startActivity(intent);
    }
}