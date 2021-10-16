package jordigomez.ioc.cat.comunicador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPasswordActivity extends AppCompatActivity {

    Button btnRecuperar;
    EditText inputEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btnRecuperar = findViewById(R.id.btnRecuperar);
        inputEmail = findViewById(R.id.inputEmailRecuperar);

        //Amagar barra superior del layout.
        getSupportActionBar().hide();

        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validar();

            }
        });
    }

    private void validar() {

        String email = inputEmail.getText().toString();

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            inputEmail.setBackgroundResource(R.drawable.bg_edittext_error);
            inputEmail.setError("Email incorrecte");

        }else{

//            enviarEmail(email);
        }
    }

    private void enviarEmail(String email) {

//        FirebaseAuth autentificador = FirebaseAuth.getInstance();
//
//        autentificador.sendPasswordResetEmail(email)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//
//                        if(task.isSuccessful()){
//
//                            Toast.makeText(ForgotPasswordActivity.this, "Correu enviat", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                            finish();
//
//                        }else{
//
//                            inputEmail.setBackgroundResource(R.drawable.bg_edittext_error);
//                            inputEmail.setError("Email incorrecte");
//                        }
//                    }
//                });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}