package controlador.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import jordigomez.ioc.cat.escoltam.R;

public class AdministratorTransitionActivity extends AppCompatActivity {
    private final static String EXTRA_MESSAGE = "jordigomez.ioc.cat.comunicador.MESSAGE";
    private String adminRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_transition);

        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageView btnAdmin = findViewById(R.id.adminImage);
        ImageView btnCommunicator = findViewById(R.id.communicatorImage);

        //Obtenim el correu
        Intent intent = getIntent();
        adminRole = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);

        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentComunicador = new Intent(AdministratorTransitionActivity.this, AdministratorActivity.class);
                intentComunicador.putExtra(EXTRA_MESSAGE, adminRole);
                startActivity(intentComunicador);
                finish();
            }
        });

        btnCommunicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentComunicador = new Intent(AdministratorTransitionActivity.this, UserActivity.class);
                intentComunicador.putExtra(EXTRA_MESSAGE, adminRole);
                startActivity(intentComunicador);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}