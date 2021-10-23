package controlador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import controlador.gestor.GestorCrypto;
import jordigomez.ioc.cat.escoltam.R;

/**
 * Activitat del client.
 * @see AppCompatActivity
 * @author Jordi Gómez Lozano
 */
public class ClientActivity extends AppCompatActivity {

    public static final String SHARED_PREFERENCES_TOKEN_KEY = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        //Amagar barra superior del layout.
        getSupportActionBar().hide();
        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView textEmailClient = findViewById(R.id.textEmailClient);
        Button btnLogout = findViewById(R.id.btn_LogoutClient);

        Intent intent = getIntent();
        textEmailClient.setText(intent.getStringExtra(LoginActivity.EXTRA_MESSAGE));


        btnLogout.setOnClickListener(view -> {
            //Borrar token
            GestorCrypto gestorCrypto = new GestorCrypto();

            SharedPreferences sharedPreferences =  gestorCrypto.getEncryptedSharedPreferences(this);
            sharedPreferences.edit().remove(SHARED_PREFERENCES_TOKEN_KEY).apply();

            Intent intent1 = new Intent(ClientActivity.this, LoginActivity.class);
            startActivity(intent1);
            finish();
        });

    }
}