package jordigomez.ioc.cat.comunicador;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import dao.DAOUsuariImpl;
import gestor.GestorException;
import gestor.GestorRegistre;
import dao.DAOUsuari;
import io.github.muddz.styleabletoast.StyleableToast;
import model.Usuari;

/**
 * Classe que permet registrar-se.
 * @author Jordi Gómez Lozano.
 * @see AppCompatActivity
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText nameSurname, phone, email, password, conformPassword;
    private RadioGroup radioGroupVeu;
    private LinearLayout ln_radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Amagar barra superior del layout.
        getSupportActionBar().hide();
        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Button btnRegistrar = findViewById(R.id.btnRegister);
        TextView tornarLogin = findViewById(R.id.textTornarLogin);
        email = findViewById(R.id.inputEmail);
        password = findViewById(R.id.inputPassword);
        conformPassword = findViewById(R.id.inputConformPassword);
        radioGroupVeu = findViewById(R.id.rg);
        ln_radioGroup = findViewById(R.id.linearLayoutVeu);

        btnRegistrar.setOnClickListener(view -> {
            try {

                registerUser();

            } catch (GestorException ex) {

                Log.w("Error", "Error en registrar l'usuari", ex);
            }
        });

        tornarLogin.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }


    /**
     * Controla que el registre es porti a terme correctament, en cas contrari avisa a l'usuari.
     * Alhora crea un login al Firebase per a què aquest pugui generar el toquen corresponent.
     * @author Jordi Gómez Lozano.
     */
    public void registerUser() throws GestorException {

        DAOUsuari dao = new DAOUsuariImpl(RegisterActivity.this);

        if(checkFields()) {

            Usuari usuari = createUsuari();

            if (dao.insertar(usuari)) {

                FirebaseAuth autentificacio = FirebaseAuth.getInstance();

                autentificacio.createUserWithEmailAndPassword(usuari.getEmail(), usuari.getPassword())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(RegisterActivity.this, "Usuari creat correctament", Toast.LENGTH_LONG).show();
                                    cleanFields();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                                } else {
                                    StyleableToast.makeText(RegisterActivity.this, getResources().getString(R.string.errorRegitreUsuari), Toast.LENGTH_SHORT, R.style.toastError).show();
                                    Log.w("Error", "Error en crear el login.", task.getException());
                                }
                            }
                        });

            } else {
                StyleableToast.makeText(RegisterActivity.this, getResources().getString(R.string.errorRegitreUsuari), Toast.LENGTH_SHORT, R.style.toastError).show();
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
            voiceUsuari = (voiceUsuari.equals("Masculina") ? "male" : "female");
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
        DAOUsuari dao = new DAOUsuariImpl(RegisterActivity.this);
        Usuari usuari = createUsuari();

        //Gestor del registre.
        GestorRegistre gestorRegistre = new GestorRegistre(usuari, conformPassword.getText().toString());

        if (!gestorRegistre.emailChecker()) {

            email.setBackgroundResource(R.drawable.bg_edittext_error);
            email.setError(gestorRegistre.getError());

            correcte = false;

        } else if (dao.comprovar(usuari.getEmail())) {

            correcte = false;
            email.setBackgroundResource(R.drawable.bg_edittext_error);
            email.setError("Email ja assignat a un usuari");

        } else {

            email.setBackgroundResource(R.drawable.bg_edittext);
        }

        if (!gestorRegistre.passwordChecker()) {

            password.setBackgroundResource(R.drawable.bg_edittext_error);
            password.setError(gestorRegistre.getError());

            correcte = false;

        } else {

            password.setBackgroundResource(R.drawable.bg_edittext);
        }

        if (!gestorRegistre.conformPasswordChecker()) {

            conformPassword.setBackgroundResource(R.drawable.bg_edittext_error);
            conformPassword.setError(gestorRegistre.getError());

            correcte = false;

        } else {

            conformPassword.setBackgroundResource(R.drawable.bg_edittext);
        }

        if (!gestorRegistre.voiceChecker()) {

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

}