package controlador.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import controlador.fragment.AdminToolbarFragment;
import controlador.gestor.GestorSharedPreferences;
import jordigomez.ioc.cat.escoltam.R;

/**
 * Activitat de l'administrador.
 * @see AppCompatActivity
 * @author Jordi Gómez Lozano
 */
public class AdministratorActivity extends AppCompatActivity {

    AdminToolbarFragment adminToolbarFragment;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        //Amagar barra superior del layout.
        getSupportActionBar().hide();
        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView correuAdministrador = findViewById(R.id.textCorreuAdministrador);
        Button btnLogout = findViewById(R.id.btn_LogoutAdministrador);

        Intent intent = getIntent();
        correuAdministrador.setText(intent.getStringExtra(LoginActivity.EXTRA_MESSAGE));

        adminToolbarFragment = AdminToolbarFragment.newInstance();

        fragmentManager = getSupportFragmentManager();

        fragmentTransaction =  fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.fragment_admin_toolbar_container, adminToolbarFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        btnLogout.setOnClickListener(view -> {
            //Borrar token
            GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(this);
            gestorSharedPreferences.deleteData();

            Intent intent1 = new Intent(AdministratorActivity.this, LoginActivity.class);
            startActivity(intent1);
            finish();
        });
    }
}