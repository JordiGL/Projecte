package jordigomez.ioc.cat.comunicador;

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

import gestor.GestorLogin;
import gestor.GestorRequest;
import io.github.muddz.styleabletoast.StyleableToast;

/**
 * Classe que permet iniciar sessió.
 * @see AppCompatActivity
 * @author Jordi Gómez Lozano.
 */
public class LoginActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "jordigomez.ioc.cat.comunicador.MESSAGE";
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

        btnAlta.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        btnIniciSessio.setOnClickListener(view -> {

            if(emailAndPasswordChecker()){

                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        GestorRequest gestorRequest = new GestorRequest();
                        GestorLogin gestorLogin = new GestorLogin();

                        //Faig la petició i obtinc el token i el codi de resposta
                        int responseCode = gestorRequest.requestToken(email.getText().toString(), password.getText().toString());

                        if(responseCode == 200) {

                            //Si la resposta es afirmativa(200) obtinc el rol del token i dirigeixo a l'usuari a la corresponent pantalla.
                            String token = gestorRequest.getToken();
                            String role = gestorRequest.getRoleFromToken(token);
                            long expiredTime = gestorRequest.getExpireTimeFromToken(token);

                            savingData(token, expiredTime);

                            gestorLogin.dirigirUsuariSegonsRole(role, LoginActivity.this, EXTRA_MESSAGE);

                        } else {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Si la resposta és negativa, es mostra un error
                                    password.setBackgroundResource(R.drawable.bg_edittext_error);
                                    email.setBackgroundResource(R.drawable.bg_edittext_error);
                                    StyleableToast.makeText(LoginActivity.this, getResources().getString(R.string.errorEmailOClau), Toast.LENGTH_SHORT, R.style.toastError).show();
                                }
                            });
                        }
                    }
                });
                thread.start();
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
        Date expiredDate = new Date(expiredTime *1000);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("InfoObt", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("token", token);
        editor.putString("expired_time", String.valueOf(expiredDate));
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

// RELACIONAT AMB FIREBASE QUE NO ESTEM UTILITZANT
//    /**
//     * Comprova l'usuari a Firebase i inicia sessió.
//     * @param usuari a comprovar a Firebase.
//     * @see FirebaseAuth
//     * @author Jordi Gómez Lozano
//     */
//    private void iniciarSessio(Usuari usuari) {
//        FirebaseAuth autentificacio = FirebaseAuth.getInstance();
//
//        autentificacio.signInWithEmailAndPassword(usuari.getEmail(), usuari.getPassword())
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//
//                        if(task.isSuccessful()){
//
//                            direccionarUsuari();
//
//                        }else{
//
//                            password.setBackgroundResource(R.drawable.bg_edittext_error);
//                            email.setBackgroundResource(R.drawable.bg_edittext_error);
//                            StyleableToast.makeText(LoginActivity.this, getResources().getString(R.string.errorEmailOClau), Toast.LENGTH_SHORT, R.style.toastError).show();
//                        }
//                    }
//                });
//    }

//    /**
//     * Obtè el token de l'usuari i el guarda al SharedPreferences
//     * @see SharedPreferences
//     * @see FirebaseUser
//     * @author Jordi Gómez Lozano
//     */
//    private void obtenirToken() {
//        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        mUser.getIdToken(true)
//                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
//                    public void onComplete(@NonNull Task<GetTokenResult> task) {
//                        if (task.isSuccessful()) {
//
//                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
//                            SharedPreferences.Editor editor = pref.edit();
//                            String idToken = task.getResult().getToken();
//                            editor.putString("token", idToken);
//                            editor.apply();
//
//                            Log.i("LoginToken", "Token: " + idToken);
//                        } else {
//                            Log.w("Error", "Error en guardar el token.");
//                        }
//                    }
//                });
//    }

//    /**
//     *Indico a la base de dades que està connectat per a que ningú es pugui connectar des d’un altre
//     * dispositiu i dirigeixo a l'usuari segons si és administrador o client.
//     * @author Jordi Gómez Lozano.
//     */
//    private void direccionarUsuari() {
//        Intent intent;
//
//        dao.updateEnable(usuari.getEmail(), false);
//        usuari.setEnabled(false);
//
//        if(usuari.getAdministrator() == 2){
//
//            intent = new Intent(LoginActivity.this, AdministratorActivity.class);
//        }else{
//
//            intent = new Intent(LoginActivity.this, ClientActivity.class);
//        }
//        intent.putExtra(EXTRA_MESSAGE, gestorLogin.getEmail());
//        startActivity(intent);
//    }