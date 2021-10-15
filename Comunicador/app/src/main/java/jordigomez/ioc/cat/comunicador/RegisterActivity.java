package jordigomez.ioc.cat.comunicador;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.regex.Pattern;
import gestor.DbUsuaris;

/**
 * Classe que permet registrar-se.
 * @author Jordi Gómez Lozano.
 * @see AppCompatActivity
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText nom, telefon, email, clau, confirmarClau;
    private RadioGroup radioGroupVeu;
    private TextView tornarLogin;
    private CharSequence  genereIntroduit;
    private Button btnRegistrar;
    private DbUsuaris dbUsuaris;
    private LinearLayout ln_radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Amagar barra superior del layout.
        getSupportActionBar().hide();
        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        btnRegistrar = findViewById(R.id.btnRegister);
        tornarLogin = findViewById(R.id.textTornarLogin);
        nom = findViewById(R.id.inputUsername);
        telefon = findViewById(R.id.inputTelefon);
        email = findViewById(R.id.inputEmail);
        clau = findViewById(R.id.inputPassword);
        confirmarClau = findViewById(R.id.inputConformPassword);
        radioGroupVeu = findViewById(R.id.rg);
        ln_radioGroup = findViewById(R.id.linearLayoutVeu);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(validador()){

                    Toast.makeText(RegisterActivity.this, "Usuari creat correctament", Toast.LENGTH_LONG).show();
                    netejarCamps();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

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
     * Mètode que controla que el registre es porti a terme correctament, en cas contrari avisa a l'usuari.
     * @return Un booleà: true si s'ha efectuat el registre correctament, i false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    public boolean validador(){

        boolean registre = false;
        dbUsuaris = new DbUsuaris(RegisterActivity.this);

        if(!comprovarCamps()) {

            int selectedId = radioGroupVeu.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) findViewById(selectedId);
            genereIntroduit = radioButton.getText();

            long introduccio = dbUsuaris.insertarUsuaris( email.getText().toString(), false,
                    genereIntroduit.toString(), nom.getText().toString(), clau.getText().toString(), telefon.getText().toString());

            if (introduccio > 0) {

                registre = true;

            }else{
                btnRegistrar.setTextColor(getResources().getColor(R.color.red));
                Toast.makeText(RegisterActivity.this, "Error en introduir l'usuari", Toast.LENGTH_LONG).show();
            }
        }

        return registre;
    }

    /**
     * Obté el valor introduït per l'usuari en els diferents camps del registre i comprova que no hi hagi errors.
     * @return Un booleà: true si ha trobat error, i false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    private boolean comprovarCamps(){

        boolean errorEnCamp = false;
        String nomtext = nom.getText().toString().trim();
        String telefonText = telefon.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String clauText = clau.getText().toString().trim();

        if (TextUtils.isEmpty(nomtext) && nom.getId() == R.id.inputUsername) {

            nom.setBackgroundResource(R.drawable.bg_edittext_error);
            nom.setError("Introdueix el nom");

            errorEnCamp = true;

        }else{

            nom.setBackgroundResource(R.drawable.bg_edittext);
        }

        if(TextUtils.isEmpty(telefonText) && telefon.getId() == R.id.inputTelefon){

            telefon.setBackgroundResource(R.drawable.bg_edittext_error);
            telefon.setError("Introdueix el telèfon");

            errorEnCamp = true;

        }else{

            telefon.setBackgroundResource(R.drawable.bg_edittext);
        }

        if (TextUtils.isEmpty(emailText) &&  email.getId() == R.id.inputEmail){

            email.setBackgroundResource(R.drawable.bg_edittext_error);
            email.setError("Introdueix l'email");

            errorEnCamp = true;

        }else if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {

            email.setText("");
            email.setBackgroundResource(R.drawable.bg_edittext_error);
            email.setError("Correu inacceptable");

            errorEnCamp = true;

        }else if (dbUsuaris.comprovarContacte(emailText)) {

            email.setText("");
            email.setBackgroundResource(R.drawable.bg_edittext_error);
            email.setError("Email assignat a un compte");

            errorEnCamp = true;

        }else{

            email.setBackgroundResource(R.drawable.bg_edittext);
        }

        if(TextUtils.isEmpty(clauText) && clau.getId() == R.id.inputPassword){

            clau.setBackgroundResource(R.drawable.bg_edittext_error);
            clau.setError("Introdueix la clau");

            errorEnCamp = true;

        }else if(clauText.length() < 8 ){

            clau.setBackgroundResource(R.drawable.bg_edittext_error);
            clau.setError("Mínim de vuit caràcters");

            errorEnCamp = true;

        }else if(!Pattern.compile("[0-9]").matcher(clauText).find()){

            clau.setBackgroundResource(R.drawable.bg_edittext_error);
            clau.setError("Ha de contenir un número");

            errorEnCamp = true;

        }else{

            clau.setBackgroundResource(R.drawable.bg_edittext);

        }

        if(TextUtils.isEmpty(confirmarClau.getText().toString().trim()) && confirmarClau.getId() == R.id.inputConformPassword){

            confirmarClau.setBackgroundResource(R.drawable.bg_edittext_error);
            confirmarClau.setError("Confirma la clau");

            errorEnCamp = true;

        }else if (!clau.getText().toString().equals(confirmarClau.getText().toString())) {

            confirmarClau.setBackgroundResource(R.drawable.bg_edittext_error);
            confirmarClau.setError("La clau no coincideix");
            confirmarClau.setText("");

            errorEnCamp = true;

        }else{

            confirmarClau.setBackgroundResource(R.drawable.bg_edittext);
        }

        if(radioGroupVeu.getCheckedRadioButtonId() == -1){
            ln_radioGroup = findViewById(R.id.linearLayoutVeu);
            ln_radioGroup.setBackgroundResource(R.drawable.bg_edittext_error);

            errorEnCamp = true;

        }else{

            ln_radioGroup.setBackgroundResource(R.drawable.bg_edittext);
        }

        return errorEnCamp;
    }

    /**
     * Neteja tots els camps dels EditText.
     * @author Jordi Gómez Lozano.
     */
    private void netejarCamps(){
        nom.setText("");
        telefon.setText("");
        email.setText("");
        clau.setText("");
        confirmarClau.setText("");
    }
}