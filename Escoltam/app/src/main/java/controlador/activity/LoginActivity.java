package controlador.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import controlador.gestor.JsonUtils;
import controlador.server.post.LoginLoader;
import controlador.server.test.RequestTestActivity;
import io.github.muddz.styleabletoast.StyleableToast;

import java.net.HttpURLConnection;

import controlador.gestor.GestorSharedPreferences;
import controlador.gestor.GestorLogin;
import controlador.server.post.RequestToken;

import jordigomez.ioc.cat.escoltam.R;

/**
 * Classe que permet iniciar sessió.
 * @see AppCompatActivity
 * @author Jordi Gómez Lozano.
 */
public class LoginActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Bundle>{
    public final static String EXTRA_MESSAGE = "jordigomez.ioc.cat.comunicador.MESSAGE";
    private final static String ROLE_USER = "ROLE_USER";
    private static final String TEST_INPUT = "test";
    private static final String EMAIL_BUNDLE_KEY = "email";
    private static final String CLAU_BUNDLE_KEY = "clau";
    private static final String RESPONSE_CODE_BUNDLE_KEY = "responseCode";
    private static final String TOKEN_BUNDLE_KEY = "token";

    private EditText email, password;
    private GestorLogin gestorLogin;
    private RequestToken gestorRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView btnAlta = findViewById(R.id.textAlta);
        Button btnIniciSessio = findViewById(R.id.btnLogin);
        TextView btnClauPerduda = findViewById(R.id.clauPerduda);
        email = findViewById(R.id.inputEmailLogin);
        password = findViewById(R.id.inputPasswordLogin);

        btnAlta.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));

        btnIniciSessio.setOnClickListener(view -> {

            //Per a fer tests al servidor.-----------------------------------------------------------------//
            if(email.getText().toString().equals(TEST_INPUT) && password.getText().toString().equals(TEST_INPUT)){
                Intent intent = new Intent(LoginActivity.this, RequestTestActivity.class);
                startActivity(intent);
                finish();

            }else {
                //----------------------------------------------------------------------------------------------//
                if (emailAndPasswordChecker()) {
                    loginUser();
                }
            }
        });

        btnClauPerduda.setOnClickListener(view -> {

            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
            finish();
        });

    }

    /**
     * Controla que el registre es porti a terme correctament, en cas contrari avisa a l'usuari.
     * Alhora crea un Bundle i fa la crida del Loader per a crear el request del servidor.
     * @author Jordi Gómez Lozano.
     */
    public void loginUser() {

        //Comprova la connexió i la informació introduide per l'usuari en l'EditText.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {

            String emailUsuari = email.getText().toString();
            String clauUsuari = password.getText().toString();

            Bundle queryBundle = new Bundle();
            queryBundle.putString(EMAIL_BUNDLE_KEY, emailUsuari);
            queryBundle.putString(CLAU_BUNDLE_KEY, clauUsuari);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
        }
    }

    /**
     * Comprova que l'email i el password introduits per l'usuari
     * @return true si es correcte, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    private boolean emailAndPasswordChecker(){

        boolean correcte = true;

        gestorLogin = new GestorLogin(email.getText().toString(), password.getText().toString());


        if (!gestorLogin.emailChecker()) {

            email.setBackgroundResource(R.drawable.bg_edittext_error);
            email.setError(gestorLogin.getError());

            correcte = false;

        } else {

            email.setBackgroundResource(R.drawable.bg_edittext);
        }

        if (!gestorLogin.passwordChecker()) {

            password.setBackgroundResource(R.drawable.bg_edittext_error);
            password.setError(gestorLogin.getError());

            correcte = false;

        } else {

            password.setBackgroundResource(R.drawable.bg_edittext);
        }

        return correcte;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @NonNull
    @Override
    public Loader<Bundle> onCreateLoader(int id, @Nullable Bundle args) {
        String emailUsuari ="";
        String passwordUsuari ="";

        if (args != null) {
            emailUsuari = args.getString(EMAIL_BUNDLE_KEY);
            passwordUsuari = args.getString(CLAU_BUNDLE_KEY);
        }

        return new LoginLoader(this, emailUsuari, passwordUsuari);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Bundle> loader, Bundle data) {

        GestorLogin gestorLogin = new GestorLogin();
        GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(LoginActivity.this);
        JsonUtils jsonUtils = new JsonUtils();

        if(data != null) {

            int responseCode = data.getInt(RESPONSE_CODE_BUNDLE_KEY);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                //Si la resposta es afirmativa(200) obtinc el rol del token i dirigeixo a l'usuari a la corresponent pantalla.
                String token = data.getString(TOKEN_BUNDLE_KEY);
                long expiredTime = jsonUtils.getExpireTimeFromToken(token);
                String email = jsonUtils.getEmailFromToken(token);
                gestorSharedPreferences.saveData(token, expiredTime, email, password.getText().toString());

                String role = jsonUtils.getRoleFromToken(token);

                if (role == null) {
                    role = ROLE_USER;
                }

                gestorLogin.dirigirUsuariSegonsRole(role, LoginActivity.this, EXTRA_MESSAGE);

            } else {
                //Error de clau o email.
                password.setBackgroundResource(R.drawable.bg_edittext_error);
                email.setBackgroundResource(R.drawable.bg_edittext_error);
                StyleableToast.makeText(LoginActivity.this, getResources().getString(R.string.errorEmailPassword), Toast.LENGTH_SHORT, R.style.toastError).show();
            }

        } else {
            //Problemes del servidor.
            StyleableToast.makeText(LoginActivity.this, getResources().getString(R.string.errorServidor), Toast.LENGTH_SHORT, R.style.toastError).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Bundle> loader) {

    }
}

