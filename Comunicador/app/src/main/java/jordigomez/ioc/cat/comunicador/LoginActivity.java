package jordigomez.ioc.cat.comunicador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import dao.DAOUsuariImpl;
import dao.Database;
import gestor.GestorLogin;
import gestor.GestorRequest;
import interfaces.DAOUsuari;
import io.github.muddz.styleabletoast.StyleableToast;
import model.Usuari;

/**
 * Classe que permet iniciar sessió.
 * @see AppCompatActivity
 * @author Jordi Gómez Lozano.
 */
public class LoginActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "jordigomez.ioc.cat.comunicador.MESSAGE";
    private EditText email, password;
    private GestorLogin gestorLogin;
    private DAOUsuari dao;
    private Usuari usuari;

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
                            Log.i("Info", "role: " + role);
                            long expiredTime = gestorRequest.getExpireTimeFromToken(token);
                            Date expiredDate = new Date(expiredTime*1000);
                            Log.i("Info", "Expired time: "+String.valueOf(expiredDate));

                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("token", token);
                            editor.putString("expired_time", String.valueOf(expiredDate));
                            editor.putString("role", role);
                            editor.apply();

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