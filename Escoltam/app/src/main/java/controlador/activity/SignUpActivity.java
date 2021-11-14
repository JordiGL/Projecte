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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;

import controlador.server.post.SignUpLoader;

import controlador.gestor.GestorException;
import controlador.gestor.GestorSignUp;
import io.github.muddz.styleabletoast.StyleableToast;
import jordigomez.ioc.cat.escoltam.R;
import model.Usuari;

/**
 * Classe que permet registrar-se.
 * @author Jordi Gómez Lozano.
 * @see AppCompatActivity
 */
public class SignUpActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Integer> {
    private static final String USER_CREATED_SUCCESSFULLY = "Usuari creat correctament";
    private static final String ERROR_EMAIL_ALREADY_ASSIGNED = "Email ja assignat a un usuari";
    private static final String RADIO_BUTTON_COMPARED_TEXT = "Masculina";
    private static final String MALE = "MALE";
    private static final String FEMALE = "FEMALE";
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

                Log.w("Error", "Error en registrar l'usuari", ex);
            }
        });

        tornarLogin.setOnClickListener(view -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));
    }


    /**
     * Controla que el registre es porti a terme correctament, en cas contrari avisa a l'usuari.
     * Alhora crea un login al Firebase per a què aquest pugui generar el toquen corresponent.
     * @author Jordi Gómez Lozano.
     */
    public void registerUser() throws GestorException {

        if(checkFields()) {

//            Usuari usuari = createUsuari();
//            RequestAddUser requestAddUser = new RequestAddUser(this);


            //Control del teclat per amagarlo en efectual la busqueda.
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

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
                queryBundle.putString("email", emailUsuari);
                queryBundle.putString("clau", clauUsuari);
                queryBundle.putString("veu", voiceUsuari);
                getSupportLoaderManager().restartLoader(0, queryBundle, this);
            }
//            if (requestAddUser.addNewUser(usuari)) {
//
//                Toast.makeText(SignUpActivity.this, USER_CREATED_SUCCESSFULLY, Toast.LENGTH_LONG).show();
//                cleanFields();
//                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
//
//            } else {
//                StyleableToast.makeText(SignUpActivity.this, getResources().getString(R.string.errorUserSignUp), Toast.LENGTH_SHORT, R.style.toastError).show();
//            }
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
     * @return Un booleà: true si ha trobat error, i false en cas contrari.
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
    public Loader<Integer> onCreateLoader(int id, @Nullable Bundle args) {
        String emailUsuari ="";
        String passwordUsuari ="";
        String voiceUsuari ="";

        if (args != null) {
           emailUsuari = args.getString("email");
           passwordUsuari = args.getString("clau");
           voiceUsuari = args.getString("veu");
        }

        return new SignUpLoader(this, new Usuari(emailUsuari, voiceUsuari, passwordUsuari));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Integer> loader, Integer data) {

        Log.i("Info", String.valueOf(data));

        if (data == HttpURLConnection.HTTP_CREATED) {

            Toast.makeText(SignUpActivity.this, USER_CREATED_SUCCESSFULLY, Toast.LENGTH_LONG).show();
            cleanFields();
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));

        }else{
            StyleableToast.makeText(SignUpActivity.this, getResources().getString(R.string.errorUserSignUp), Toast.LENGTH_SHORT, R.style.toastError).show();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Integer> loader) {

    }
}