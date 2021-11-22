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
public class UserActivity extends AppCompatActivity{

    public static final String ROLE_USER = "ROLE_USER";
    private UserToolbarFragment toolbarFragment;
    private UserFavoritesFragment favoritesFragment;
    private UserControlFragment controlFragment;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView textInfo = findViewById(R.id.textMostrarRol);

        Intent intent = getIntent();
        textInfo.setText(intent.getStringExtra(LoginActivity.EXTRA_MESSAGE));
        role = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);

        //Fragments
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction =  fragmentManager.beginTransaction();

        toolbarFragment = UserToolbarFragment.newInstance(role);
        favoritesFragment = UserFavoritesFragment.newInstance();
        controlFragment = UserControlFragment.newInstance();

        fragmentTransaction.add(R.id.toolbar_fragment_container, toolbarFragment);
        fragmentTransaction.add(R.id.favorites_fragment_container, favoritesFragment);
        fragmentTransaction.add(R.id.control_fragment_container, controlFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}