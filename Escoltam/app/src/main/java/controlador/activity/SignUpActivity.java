package controlador.activity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import controlador.gestor.GestorEncrypt;
import controlador.server.post.SignUpLoader;

import controlador.gestor.GestorException;
import controlador.gestor.GestorSignUp;
import io.github.muddz.styleabletoast.StyleableToast;
import jordigomez.ioc.cat.escoltam.R;
import model.Usuari;

/**
 * Classe que permet registrar-se.
 * @see AppCompatActivity
 * @see LoaderManager
 * @author Jordi Gómez Lozano.
 */
public class SignUpActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Bundle> {
    private static final String USER_CREATED_SUCCESSFULLY = "Usuari creat correctament";
    private static final String RADIO_BUTTON_COMPARED_TEXT = "Masculina";
    private static final String MALE = "MALE";
    private static final String FEMALE = "FEMALE";
    private static final String SERVER_DUPLICATED_EMAIL_MATCHED = "Detail: Ya existe la llave (username)";
    private static final String ERROR_EMAIL_ALREADY_ASSIGNED = "Email ja assignat a un usuari";
    private static final String SERVER_INFO_KEY = "serverInfo";
    private static final String RESPONSE_CODE_KEY = "responseCode";
    private static final String EMAIL_KEY = "email";
    private static final String CLAU_KEY = "clau";
    private static final String VEU_KEY = "veu";
    public static final String ERROR = "Error";
    public static final String SIGNUP_ERROR = "Error en registrar l'usuari";
    private EditText email, password, conformPassword;
    private RadioGroup radioGroupVeu;
    private LinearLayout ln_radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Button btnRegistrar = findViewById(R.id.btnRegister);
        TextView tornarLogin = findViewById(R.id.textTornarLogin);
        email = findViewById(R.id.inputPreviousPassword);
        password = findViewById(R.id.inputPassword);
        conformPassword = findViewById(R.id.inputConformPassword);
        radioGroupVeu = findViewById(R.id.rg);
        ln_radioGroup = findViewById(R.id.linearLayoutVeu);

        if(getSupportLoaderManager().getLoader(0)!=null){
            getSupportLoaderManager().initLoader(0,null,this);
        }

        btnRegistrar.setOnClickListener(view -> {
            try {

                registerUser();

            } catch (GestorException ex) {

                Log.w(ERROR, SIGNUP_ERROR, ex);
            }
        });

        tornarLogin.setOnClickListener(view -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));
    }


    /**
     * Controla que el registre es porti a terme correctament, en cas contrari avisa a l'usuari.
     * Alhora crea un Bundle i fa la crida del Loader per a crear el request del servidor.
     * @author Jordi Gómez Lozano.
     */
    public void registerUser() throws GestorException {
//        GestorEncrypt gestorEncrypt = new GestorEncrypt();
        if(checkFields()) {

            //Comprova la connexió i la informació introduide per l'usuari en l'EditText.
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = null;
            if (connMgr != null) {
                networkInfo = connMgr.getActiveNetworkInfo();
            }

            if (networkInfo != null && networkInfo.isConnected()) {

                String voiceUsuari = " ";
                int selectedId = radioGroupVeu.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedId);

                String emailUsuari = email.getText().toString();
                String clauUsuari = password.getText().toString();

                if(radioButton != null){

                    voiceUsuari = radioButton.getText().toString();
                    voiceUsuari = (voiceUsuari.equals(RADIO_BUTTON_COMPARED_TEXT) ? MALE : FEMALE);
                }

                Bundle queryBundle = new Bundle();
                queryBundle.putString(EMAIL_KEY, emailUsuari);
                queryBundle.putString(CLAU_KEY, clauUsuari);
                queryBundle.putString(VEU_KEY, voiceUsuari);
                getSupportLoaderManager().restartLoader(0, queryBundle, this);
            }
        }

    }

    /**
     * Crea l'usuari amb els paràmetres entrats per aquest.
     * @return usuari creat
     * @author Jordi Gómez Lozano.
     */
    @NonNull
    private Usuari createUsuari() {
        String voiceUsuari = " ";
        int selectedId = radioGroupVeu.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);

        String emailUsuari = email.getText().toString();
        String clauUsuari = password.getText().toString();

        if(radioButton != null){

            voiceUsuari = radioButton.getText().toString();
            voiceUsuari = (voiceUsuari.equals(RADIO_BUTTON_COMPARED_TEXT) ? MALE : FEMALE);
        }

        return new Usuari(
                emailUsuari,
                voiceUsuari,
                clauUsuari
        );
    }

    /**
     * Obté el valor introduït per l'usuari en els diferents camps del registre i comprova que no hi hagi errors.
     * @return Un booleà: true si es correcte, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    private boolean checkFields() {
        boolean correcte = true;
        Usuari usuari = createUsuari();

        GestorSignUp gestorSignUp = new GestorSignUp(usuari, conformPassword.getText().toString());

        if (!gestorSignUp.emailChecker()) {

            email.setBackgroundResource(R.drawable.bg_edittext_error);
            email.setError(gestorSignUp.getError());

            correcte = false;

        }

        if (!gestorSignUp.passwordChecker()) {

            password.setBackgroundResource(R.drawable.bg_edittext_error);
            password.setError(gestorSignUp.getError());

            correcte = false;

        } else {

            password.setBackgroundResource(R.drawable.bg_edittext);
        }

        if (!gestorSignUp.conformPasswordChecker()) {

            conformPassword.setBackgroundResource(R.drawable.bg_edittext_error);
            conformPassword.setError(gestorSignUp.getError());

            correcte = false;

        } else {

            conformPassword.setBackgroundResource(R.drawable.bg_edittext);
        }

        if (!gestorSignUp.voiceChecker()) {

            ln_radioGroup.setBackgroundResource(R.drawable.bg_edittext_error);

            correcte = false;

        } else {

            ln_radioGroup.setBackgroundResource(R.drawable.bg_edittext);
        }

        return correcte;
    }

    /**
     * Neteja tots els camps dels EditText.
     * @author Jordi Gómez Lozano.
     */
    private void cleanFields() {
        email.setText("");
        password.setText("");
        conformPassword.setText("");
    }

    @NonNull
    @Override
    public Loader<Bundle> onCreateLoader(int id, @Nullable Bundle args) {
        String emailUsuari ="";
        String passwordUsuari ="";
        String voiceUsuari ="";
        String encrypted = "";
        if (args != null) {
           emailUsuari = args.getString(EMAIL_KEY);
           passwordUsuari = args.getString(CLAU_KEY);
           voiceUsuari = args.getString(VEU_KEY);
        }

        GestorEncrypt gestorEncrypt = new GestorEncrypt();

        try {

            encrypted = gestorEncrypt.RSAEncrypt(passwordUsuari);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        return new SignUpLoader(this, new Usuari(emailUsuari, voiceUsuari, encrypted));

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Bundle> loader, Bundle data) {
        String serverInfo = "";
        int responseCode = 0;
        
        if (data != null) {
            serverInfo = data.getString(SERVER_INFO_KEY);
            responseCode = data.getInt(RESPONSE_CODE_KEY);
        }

        if (responseCode == HttpURLConnection.HTTP_CREATED) {

            Toast.makeText(SignUpActivity.this, USER_CREATED_SUCCESSFULLY, Toast.LENGTH_LONG).show();
            cleanFields();
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));

        }else{

            if(serverInfo.contains(SERVER_DUPLICATED_EMAIL_MATCHED)){

                email.setBackgroundResource(R.drawable.bg_edittext_error);
                email.setError(ERROR_EMAIL_ALREADY_ASSIGNED);

            } else{
                StyleableToast.makeText(SignUpActivity.this, getResources().getString(R.string.errorUserSignUp), Toast.LENGTH_SHORT, R.style.toastError).show();
            }

        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Bundle> loader) {

    }
}