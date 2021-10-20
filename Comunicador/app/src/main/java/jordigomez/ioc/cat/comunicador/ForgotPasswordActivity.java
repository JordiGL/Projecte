package jordigomez.ioc.cat.comunicador;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;


import gestor.GestorForgotPaswword;

/**
 * Classe que permet recuperar la clau.
 * @see AppCompatActivity
 * @author Jordi Gómez Lozano.
 */
public class ForgotPasswordActivity extends AppCompatActivity {
    Button btnRecuperar;
    EditText inputEmail;
    TextView btnTornarLogin;
    GestorForgotPaswword gestorForgotPaswword;

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
            Toast.makeText(ForgotPasswordActivity.this, "Correu enviat", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
//            enviarEmail(email); Gestiona el canvi de clau enviant un email amb firebase.
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

// GESTIONA EL CANVI DE CLAU ENVIANT UN EMAIL AMB FIREBASE
//    private void enviarEmail(String email) {
//        FirebaseAuth autentificador = FirebaseAuth.getInstance();
//
//        autentificador.sendPasswordResetEmail(email)
//                .addOnCompleteListener(task -> {
//
//                    if(task.isSuccessful()){
//
//                        Toast.makeText(ForgotPasswordActivity.this, "Correu enviat", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                        finish();
//
//                    }else{
//
//                        inputEmail.setBackgroundResource(R.drawable.bg_edittext_error);
//                        inputEmail.setError("Email incorrecte");
//                    }
//                });
//
//    }
