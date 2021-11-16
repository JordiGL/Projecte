package controlador.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import controlador.fragment.UserControlFragment;
import controlador.fragment.UserFavoritesFragment;
import controlador.fragment.UserToolbarFragment;
import controlador.gestor.GestorSharedPreferences;
import jordigomez.ioc.cat.escoltam.R;

/**
 * Activitat del client.
 * @see AppCompatActivity
 * @author Jordi Gómez Lozano
 */
public class UserActivity extends AppCompatActivity {

    UserToolbarFragment toolbarFragment;
    UserFavoritesFragment favoritesFragment;
    UserControlFragment controlFragment;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView textEmailClient = findViewById(R.id.textEmailClient);
        Button btnLogout = findViewById(R.id.btn_LogoutClient);

        Intent intent = getIntent();
        textEmailClient.setText(intent.getStringExtra(LoginActivity.EXTRA_MESSAGE));

        //Fragments
        toolbarFragment = UserToolbarFragment.newInstance();
        favoritesFragment = UserFavoritesFragment.newInstance();
        controlFragment = UserControlFragment.newInstance();

        fragmentManager = getSupportFragmentManager();

        fragmentTransaction =  fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.toolbar_fragment_container, toolbarFragment);
        fragmentTransaction.add(R.id.favorites_fragment_container, favoritesFragment);
        fragmentTransaction.add(R.id.control_fragment_container, controlFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        //Listeners
        btnLogout.setOnClickListener(view -> {
            //Borrar token
            GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(this);
            gestorSharedPreferences.deleteData();

            Intent intent1 = new Intent(UserActivity.this, LoginActivity.class);
            startActivity(intent1);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}