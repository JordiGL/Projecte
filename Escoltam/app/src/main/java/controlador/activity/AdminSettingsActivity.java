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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.net.HttpURLConnection;

import controlador.fragment.ChangePasswordFragment;
import controlador.fragment.ChangeVoiceFragment;
import controlador.gestor.GestorSettings;
import controlador.gestor.GestorSharedPreferences;
import controlador.gestor.OnFragmentInteractionListener;
import controlador.server.put.ChangePasswordLoader;
import controlador.server.put.ChangeVoiceLoader;
import io.github.muddz.styleabletoast.StyleableToast;
import jordigomez.ioc.cat.escoltam.R;

/**
 * Classe de la configuració de l'usuari per a canviar la clau o la veu.
 * @see OnFragmentInteractionListener
 * @see LoaderManager
 * @author Jordi Gómez Lozano.
 */
public class AdminSettingsActivity extends AppCompatActivity implements OnFragmentInteractionListener, LoaderManager.LoaderCallbacks<Integer> {

    private static final String BUNDLE_TOKEN_KEY = "token";
    private static final String BUNDLE_EMAIL_KEY = "email";
    private static final String BUNDLE_PASSWORD_KEY = "password";
    private static final String BUNDLE_VOICE_KEY = "voice";
    private ChangePasswordFragment changePasswordFragment;
    private ChangeVoiceFragment changeVoiceFragment;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private String passwordChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);

        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button btnChangePassword = findViewById(R.id.btnChangePasswordAdmin);
        Button btnChangeVoice = findViewById(R.id.btnChangeVoiceAdmin);

        //Fragments
        changePasswordFragment = ChangePasswordFragment.newInstance();
        changeVoiceFragment = ChangeVoiceFragment.newInstance();
        fragmentManager = getSupportFragmentManager();

        fragmentTransaction =  fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.admin_settings_fragment_container, changePasswordFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        if(getSupportLoaderManager().getLoader(0)!=null){
            getSupportLoaderManager().initLoader(0,null,this);
        }


        btnChangeVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(changePasswordFragment.isAdded()) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(changePasswordFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.admin_settings_fragment_container, changeVoiceFragment);
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
                    fragmentTransaction.add(R.id.admin_settings_fragment_container, changePasswordFragment);
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
            passwordChanged = newPassword.getText().toString();
            sendPasswordToServer(newPassword.getText().toString());
        }
    }

    @Override
    public void onButtonPressed(EditText passwordEntered, String choice, LinearLayout radioGroupLayout) {
        if(checkVoiceChangedFields(passwordEntered, choice, radioGroupLayout)){
            sendVoiceToServer(choice);
        }
    }

    /**
     * Obté el valor introduït per l'usuari en els diferents camps del fragment del canvi
     * de veu i comprova que no hi hagi errors.
     * @param passwordEntered EditText del camp de la clau.
     * @param choice String amb la veu seleccionada.
     * @param radioGroupLayout LinearLayout del grup de radiobuttons.
     * @return Un booleà: true si es correcte, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    private boolean checkVoiceChangedFields(EditText passwordEntered, String choice, LinearLayout radioGroupLayout) {
        boolean correcte = true;

        GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(AdminSettingsActivity.this);
        String actualPassword = gestorSharedPreferences.getPassword();

        GestorSettings gestorSettings = new GestorSettings(
                passwordEntered.getText().toString(),
                actualPassword,
                choice);

        if (!gestorSettings.previousPasswordChecker()) {

            passwordEntered.setBackgroundResource(R.drawable.bg_edittext_error);
            passwordEntered.setError(gestorSettings.getError());

            correcte = false;

        } else {

            passwordEntered.setBackgroundResource(R.drawable.bg_edittext);
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
     * Obté el valor introduït per l'usuari en els diferents camps del fragment del canvi
     * de clau i comprova que no hi hagi errors.
     * @return Un booleà: true si es correcte, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    private boolean checkPasswordChangedFields(EditText previousPasswordEntered, EditText newPassword, EditText conformPassword) {
        boolean correcte = true;


        GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(AdminSettingsActivity.this);
        String receivedPassword = gestorSharedPreferences.getPassword();

        GestorSettings gestorSettings = new GestorSettings(
                previousPasswordEntered.getText().toString(),
                receivedPassword,
                newPassword.getText().toString(),
                conformPassword.getText().toString());

        if (!gestorSettings.previousPasswordChecker()) {

            previousPasswordEntered.setBackgroundResource(R.drawable.bg_edittext_error);
            previousPasswordEntered.setError(gestorSettings.getError());

            correcte = false;

        } else {

            previousPasswordEntered.setBackgroundResource(R.drawable.bg_edittext);
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

    /**
     * Crea el bundle i crida al Loader per a fer el request al servidor.
     * @param novaClau la nova clau introduida per l'usuari.
     * @author Jordi Gómez Lozano.
     */
    private void sendPasswordToServer(String novaClau) {

        GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(AdminSettingsActivity.this);
        String email = gestorSharedPreferences.getEmail();
        String token = gestorSharedPreferences.getToken();

        //Comprova la connexió i la informació introduide per l'usuari en l'EditText.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {
            Bundle queryBundle = new Bundle();
            queryBundle.putString(BUNDLE_TOKEN_KEY, token);
            queryBundle.putString(BUNDLE_EMAIL_KEY, email);
            queryBundle.putString(BUNDLE_PASSWORD_KEY, novaClau);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
        }
    }

    /**
     * Crea el bundle i crida al Loader per a fer el request al servidor.
     * @param novaVeu la nova veu seleccionada per l'usuari.
     * @author Jordi Gómez Lozano.
     */
    private void sendVoiceToServer(String novaVeu) {

        GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(AdminSettingsActivity.this);
        String email = gestorSharedPreferences.getEmail();
        String token = gestorSharedPreferences.getToken();
        String passwordActual = gestorSharedPreferences.getPassword();

        //Comprova la connexió i la informació introduide per l'usuari en l'EditText.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {
            Bundle queryBundle = new Bundle();
            queryBundle.putString(BUNDLE_TOKEN_KEY, token);
            queryBundle.putString(BUNDLE_EMAIL_KEY, email);
            queryBundle.putString(BUNDLE_PASSWORD_KEY, passwordActual);
            queryBundle.putString(BUNDLE_VOICE_KEY, novaVeu);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
        }
    }

    @NonNull
    @Override
    public Loader<Integer> onCreateLoader(int id, @Nullable Bundle args) {
        String token ="";
        String email ="";
        String novaClau ="";
        String novaVeu ="";

        if (args != null) {

            if(args.size() == 4){

                token = args.getString(BUNDLE_TOKEN_KEY);
                email = args.getString(BUNDLE_EMAIL_KEY);
                novaClau = args.getString(BUNDLE_PASSWORD_KEY);
                novaVeu = args.getString(BUNDLE_VOICE_KEY);

                return new ChangeVoiceLoader(this, novaClau , novaVeu, email, token);

            }else{
                token = args.getString(BUNDLE_TOKEN_KEY);
                email = args.getString(BUNDLE_EMAIL_KEY);
                novaClau = args.getString(BUNDLE_PASSWORD_KEY);

                return new ChangePasswordLoader(this, novaClau ,email, token);
            }

        }

        return new ChangePasswordLoader(this, novaClau ,email, token);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Integer> loader, Integer data) {

        if (data == HttpURLConnection.HTTP_CREATED) {

            if(passwordChanged != null){
                GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(AdminSettingsActivity.this);
                gestorSharedPreferences.setPassword(passwordChanged);
            }

            Intent intentAdmin = new Intent(AdminSettingsActivity.this, AdministratorActivity.class);
            startActivity(intentAdmin);
            finish();
        }else{
            StyleableToast.makeText(AdminSettingsActivity.this, "Error, no s'ha pogut canviar la clau", Toast.LENGTH_SHORT, R.style.toastError).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Integer> loader) {

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}