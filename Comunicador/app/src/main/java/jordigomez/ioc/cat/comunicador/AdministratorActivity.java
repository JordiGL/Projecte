package jordigomez.ioc.cat.comunicador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import gestor.GestorCrypto;

/**
 * Activitat de l'administrador.
 * @see AppCompatActivity
 * @author Jordi Gómez Lozano
 */
public class AdministratorActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);

        //Amagar barra superior del layout.
        getSupportActionBar().hide();
        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView correuAdministrador = findViewById(R.id.textCorreuAdministrador);
        Button btnLogout = findViewById(R.id.btn_tancarAdministrador);

        Intent intent = getIntent();
        correuAdministrador.setText(intent.getStringExtra(LoginActivity.EXTRA_MESSAGE));

        btnLogout.setOnClickListener(view -> {
            //Borrar token
            GestorCrypto gestorCrypto = new GestorCrypto();

            SharedPreferences sharedPreferences =  gestorCrypto.getEncryptedSharedPreferences(this);
            sharedPreferences.edit().remove("token").apply();

            Intent intent1 = new Intent(AdministratorActivity.this, LoginActivity.class);
            startActivity(intent1);
            finish();
        });
    }
}