package controlador.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import controlador.fragment.ChangePasswordFragment;
import controlador.fragment.ChangeVoiceFragment;
import controlador.gestor.GestorSettings;
import controlador.gestor.GestorSharedPreferences;
import controlador.gestor.OnFragmentInteractionListener;
import controlador.server.get.UsuarisListLoader;
import jordigomez.ioc.cat.escoltam.R;

public class UserSettingsActivity extends AppCompatActivity implements OnFragmentInteractionListener{

    public static final String BUNDLE_TOKEN_KEY = "token";
    public static final String BUNDLE_URL_KEY = "url";
    public static final String JSON_PASSWORD_KEY = "password";
    ChangePasswordFragment changePasswordFragment;
    ChangeVoiceFragment changeVoiceFragment;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button btnChangePassword = findViewById(R.id.btnChangePasswordUser);
        Button btnChangeVoice = findViewById(R.id.btnChangeVoiceUser);

        //Fragments
        changePasswordFragment = ChangePasswordFragment.newInstance();
        changeVoiceFragment = ChangeVoiceFragment.newInstance();
        fragmentManager = getSupportFragmentManager();

        fragmentTransaction =  fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.user_settings_fragment_container, changePasswordFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        btnChangeVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(changePasswordFragment.isAdded()) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(changePasswordFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.user_settings_fragment_container, changeVoiceFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    btnChangeVoice.setBackground(getDrawable(R.drawable.principal_button_settings_top_round));
                    btnChangePassword.setBackground(getDrawable(R.drawable.button_middle_top_round));
                    btnChangeVoice.setTextColor(getColor(R.color.black));
                    btnChangePassword.setTextColor(getColor(R.color.white));
                }
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(changeVoiceFragment.isAdded()) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(changeVoiceFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.user_settings_fragment_container, changePasswordFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    btnChangeVoice.setBackground(getDrawable(R.drawable.button_middle_top_round));
                    btnChangePassword.setBackground(getDrawable(R.drawable.principal_button_settings_top_round));
                    btnChangeVoice.setTextColor(getColor(R.color.white));
                    btnChangePassword.setTextColor(getColor(R.color.black));
                }
            }
        });

    }

    @Override
    public void onButtonPressed(EditText previousPasswordEntered, EditText newPassword, EditText conformPassword) {
        if(checkPasswordChangedFields(previousPasswordEntered, newPassword, conformPassword)){
            //Guardem el nou password.
            GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(UserSettingsActivity.this);
            gestorSharedPreferences.setPassword(newPassword.getText().toString());

            Intent intentAdmin = new Intent(UserSettingsActivity.this, UserActivity.class);
            startActivity(intentAdmin);
            finish();
        }
    }

    @Override
    public void onButtonPressed(EditText passwordEntered, String choice, LinearLayout radioGroupLayout) {
        if(checkVoiceChangedFields(passwordEntered, choice, radioGroupLayout)){
            Intent intentAdmin = new Intent(UserSettingsActivity.this, UserActivity.class);
            startActivity(intentAdmin);
            finish();
        }
    }

    private boolean checkVoiceChangedFields(EditText previousPassword, String choice, LinearLayout radioGroupLayout) {
        boolean correcte = true;

        GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(UserSettingsActivity.this);
        String receivedPassword = gestorSharedPreferences.getPassword();

        GestorSettings gestorSettings = new GestorSettings(
                previousPassword.getText().toString(),
                receivedPassword,
                choice);

        if (!gestorSettings.previousPasswordChecker()) {

            previousPassword.setBackgroundResource(R.drawable.bg_edittext_error);
            previousPassword.setError(gestorSettings.getError());

            correcte = false;

        } else {

            previousPassword.setBackgroundResource(R.drawable.bg_edittext);
        }

        if (!gestorSettings.voiceChecker()) {

            radioGroupLayout.setBackgroundResource(R.drawable.bg_edittext_error);

            correcte = false;

        } else {

            radioGroupLayout.setBackgroundResource(R.drawable.bg_edittext);
        }

        return correcte;
    }

    /**
     * Obté el valor introduït per l'usuari en els diferents camps del registre i comprova que no hi hagi errors.
     * @return Un booleà: true si ha trobat error, i false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    private boolean checkPasswordChangedFields(EditText previousPassword, EditText newPassword, EditText conformPassword) {
        boolean correcte = true;

        GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(UserSettingsActivity.this);
        String receivedPassword = gestorSharedPreferences.getPassword();

        GestorSettings gestorSettings = new GestorSettings(
                previousPassword.getText().toString(),
                receivedPassword,
                newPassword.getText().toString(),
                conformPassword.getText().toString());

        if (!gestorSettings.previousPasswordChecker()) {

            previousPassword.setBackgroundResource(R.drawable.bg_edittext_error);
            previousPassword.setError(gestorSettings.getError());

            correcte = false;

        } else {

            previousPassword.setBackgroundResource(R.drawable.bg_edittext);
        }

        if (!gestorSettings.newPasswordChecker()) {

            newPassword.setBackgroundResource(R.drawable.bg_edittext_error);
            newPassword.setError(gestorSettings.getError());

            correcte = false;

        } else {

            newPassword.setBackgroundResource(R.drawable.bg_edittext);
        }

        if (!gestorSettings.conformPasswordChecker()) {

            conformPassword.setBackgroundResource(R.drawable.bg_edittext_error);
            conformPassword.setError(gestorSettings.getError());

            correcte = false;

        } else {

            conformPassword.setBackgroundResource(R.drawable.bg_edittext);
        }

        return correcte;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}