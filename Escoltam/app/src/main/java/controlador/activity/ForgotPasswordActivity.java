package controlador.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import controlador.gestor.GestorForgotPaswword;
import jordigomez.ioc.cat.escoltam.R;

/**
 * Classe que permet recuperar la clau.
 * @see AppCompatActivity
 * @author Jordi Gómez Lozano.
 */
public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String EMAIL_SENT = "Correu enviat";
    private Button btnRecuperar;
    private EditText inputEmail;
    private TextView btnTornarLogin;
    private GestorForgotPaswword gestorForgotPaswword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        btnRecuperar = findViewById(R.id.btnRecuperar);
        inputEmail = findViewById(R.id.inputEmailRecuperar);
        btnTornarLogin = findViewById(R.id.textTornarLoginForgotPassword);

        //Amagar barra superior del layout.
        getSupportActionBar().hide();

        btnRecuperar.setOnClickListener(view -> validar());

        btnTornarLogin.setOnClickListener(view -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    /**
     * valida l'email entrat per l'usuari i el dirigeix.
     * @author Jordi Gómez Lozano
     */
    private void validar() {
        String email = inputEmail.getText().toString();
        gestorForgotPaswword = new GestorForgotPaswword(email);

        if(gestorForgotPaswword.emailChecker()){
            //De moment retorna al login.
            Toast.makeText(ForgotPasswordActivity.this, EMAIL_SENT, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }else{

            inputEmail.setBackgroundResource(R.drawable.bg_edittext_error);
            inputEmail.setError(gestorForgotPaswword.getError());
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
