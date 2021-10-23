package controlador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import controlador.gestor.GestorCrypto;
import controlador.gestor.GestorLogin;
import controlador.gestor.GestorRequest;
import io.github.muddz.styleabletoast.StyleableToast;
import jordigomez.ioc.cat.escoltam.R;
import testservidor.ServerTestsActivity;

/**
 * Classe que permet iniciar sessió.
 * @see AppCompatActivity
 * @author Jordi Gómez Lozano.
 */
public class LoginActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "jordigomez.ioc.cat.comunicador.MESSAGE";
    public final static String ROLE_USER = "ROLE_USER";
    public final static int AFFIRMATIVE = 200;
    public static final String TEST_INPUT = "test";
    public static final String SHARED_PREFERENCES_TOKEN_KEY = "token";
    public static final String SHARED_PREFERENCES_EXPIRATION_KEY = "expired_time";
    private EditText email, password;
    private GestorLogin gestorLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Amagar barra superior del layout.
        getSupportActionBar().hide();
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
                Intent intent = new Intent(LoginActivity.this, ServerTestsActivity.class);
                startActivity(intent);
                finish();

            }else {
            //----------------------------------------------------------------------------------------------//
                if (emailAndPasswordChecker()) {

                    Thread thread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            GestorRequest gestorRequest = new GestorRequest();
                            GestorLogin gestorLogin = new GestorLogin();

                            //Faig la petició i obtinc el token i el codi de resposta
                            int responseCode = gestorRequest.requestToken(email.getText().toString(), password.getText().toString());

                            if (responseCode == AFFIRMATIVE) {

                                //Si la resposta es afirmativa(200) obtinc el rol del token i dirigeixo a l'usuari a la corresponent pantalla.
                                String token = gestorRequest.getToken();
                                long expiredTime = gestorRequest.getExpireTimeFromToken(token);
                                savingData(token, expiredTime);

                                String role = gestorRequest.getRoleFromToken(token);
                                if (role == null) {
                                    role = ROLE_USER;
                                }
                                gestorLogin.dirigirUsuariSegonsRole(role, LoginActivity.this, EXTRA_MESSAGE);

                            } else {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Si la resposta és negativa, es mostra un error
                                        password.setBackgroundResource(R.drawable.bg_edittext_error);
                                        email.setBackgroundResource(R.drawable.bg_edittext_error);
                                        StyleableToast.makeText(LoginActivity.this, getResources().getString(R.string.errorEmailPassword), Toast.LENGTH_SHORT, R.style.toastError).show();
                                    }
                                });
                            }
                        }
                    });
                    thread.start();
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
     * Guarda les dades mentre duri la sessió de l'usuari.
     * @param token token de l'usuari
     * @param expiredTime data d'expiració del token
     * @author Jordi Gómez Lozano
     */
    private void savingData(String token, long expiredTime) {
        GestorCrypto gestorCrypto = new GestorCrypto();
        Date expiredDate = new Date(expiredTime *1000);

        SharedPreferences sharedPreferences =  gestorCrypto.getEncryptedSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFERENCES_TOKEN_KEY, token);
        editor.putString(SHARED_PREFERENCES_EXPIRATION_KEY, String.valueOf(expiredDate));
        editor.apply();
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
}

