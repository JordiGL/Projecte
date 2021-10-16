package jordigomez.ioc.cat.comunicador;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import dao.DAOUsuariImpl;
import gestor.GestorException;
import gestor.GestorRegistre;
import interfaces.DAOUsuari;
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
        nameSurname = findViewById(R.id.inputUsername);
        phone = findViewById(R.id.inputTelefon);
        email = findViewById(R.id.inputEmail);
        password = findViewById(R.id.inputPassword);
        conformPassword = findViewById(R.id.inputConformPassword);
        radioGroupVeu = findViewById(R.id.rg);
        ln_radioGroup = findViewById(R.id.linearLayoutVeu);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {

                    registerUser();

                } catch (GestorException ex) {

                    Log.w("Error", "Error en registrar l'usuari", ex);
                }
            }
        });

        tornarLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }


    /**
     * Controla que el registre es porti a terme correctament, en cas contrari avisa a l'usuari.
     * @author Jordi Gómez Lozano.
     */
    public void registerUser() throws GestorException {

        DAOUsuari dao = new DAOUsuariImpl(RegisterActivity.this);

        if(checkFields()) {

            Usuari usuari = createUsuari();

            if (dao.insertar(usuari)) {

                Toast.makeText(RegisterActivity.this, "Usuari creat correctament", Toast.LENGTH_LONG).show();
                cleanFields();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

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
        RadioButton radioButton = (RadioButton) findViewById(selectedId);

        String emailUsuari = email.getText().toString();
        String nomUsuari = nameSurname.getText().toString();
        String telefonUsuari = phone.getText().toString();
        String clauUsuari = password.getText().toString();

        if(radioButton != null){

            voiceUsuari = radioButton.getText().toString();
            voiceUsuari = (voiceUsuari.equals("Masculina") ? "male" : "female");
        }

        Usuari usuari = new Usuari(
                emailUsuari,
                false,
                voiceUsuari,
                nomUsuari,
                clauUsuari,
                telefonUsuari
        );

        return usuari;
    }

    /**
     * Obté el valor introduït per l'usuari en els diferents camps del registre i comprova que no hi hagi errors.
     * @return Un booleà: true si ha trobat error, i false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    private boolean checkFields() throws GestorException {
        boolean correcte = true;
        DAOUsuari dao = new DAOUsuariImpl(RegisterActivity.this);
        Usuari usuari = createUsuari();

        //Gestor del registre.
        GestorRegistre gestorRegistre = new GestorRegistre(usuari, conformPassword.getText().toString());

        if (!gestorRegistre.nameSurnameChecker()) {

            nameSurname.setBackgroundResource(R.drawable.bg_edittext_error);
            nameSurname.setError(gestorRegistre.getError());

            correcte = false;

        } else {

            nameSurname.setBackgroundResource(R.drawable.bg_edittext);
        }

        if (!gestorRegistre.phoneChecker()) {

            phone.setBackgroundResource(R.drawable.bg_edittext_error);
            phone.setError(gestorRegistre.getError());

            correcte = false;

        } else {

            phone.setBackgroundResource(R.drawable.bg_edittext);
        }

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
        nameSurname.setText("");
        phone.setText("");
        email.setText("");
        password.setText("");
        conformPassword.setText("");
    }

}