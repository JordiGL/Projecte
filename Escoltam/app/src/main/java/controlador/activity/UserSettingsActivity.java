package controlador.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.net.HttpURLConnection;

import controlador.fragment.ChangePasswordFragment;
import controlador.fragment.ChangeVoiceFragment;
import controlador.gestor.GestorSettings;
import controlador.gestor.GestorSharedPreferences;
import controlador.gestor.OnFragmentInteractionSettingsListener;
import controlador.server.delete.DeleteAccountLoader;
import controlador.server.put.ChangePasswordLoader;
import controlador.server.put.ChangeVoiceLoader;
import io.github.muddz.styleabletoast.StyleableToast;
import jordigomez.ioc.cat.escoltam.R;

public class UserSettingsActivity extends AppCompatActivity implements OnFragmentInteractionSettingsListener, LoaderManager.LoaderCallbacks<Bundle>{
    private final static String EXTRA_MESSAGE = "jordigomez.ioc.cat.comunicador.MESSAGE";
    private static final String ACCOUNT_DELETED_TEXT = "El compte s'ha eliminat";
    private static final String BUNDLE_TOKEN_KEY = "token";
    private static final String BUNDLE_EMAIL_KEY = "email";
    private static final String BUNDLE_PASSWORD_KEY = "password";
    private static final String BUNDLE_VOICE_KEY = "change_voice";
    private static final String MODIFICACIO_OK = "Modificació efectuada correctament";
    private static final String MODIFICACIO_ERROR = "Error, no s'ha pogut dur a terme la modificació";
    private static final String INTENT_VALUE_ROLE_USER = "ROLE_USER";
    private static final String DELETE_ACCOUNT_OPTION = "delete_account";
    private static final String CHANGE_VOICE_OPTION = "change_voice";
    private static final String CHANGE_PASSWORD_OPTION = "change_password";
    private static final String RESPONSE_CODE_BUNDLE_KEY = "responseCode";
    private static final String OPTION_BUNDLE_KEY = "opcio";
    private static final String DIALOG_TITLE_DELETE = "Atenció";
    private static final String DIALOG_MESSAGE_DELETE = "Segur que vols eliminar el compte?";
    private ChangePasswordFragment changePasswordFragment;
    private ChangeVoiceFragment changeVoiceFragment;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private String passwordChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button btnChangePassword = findViewById(R.id.btnChangePasswordUser);
        Button btnChangeVoice = findViewById(R.id.btnChangeVoiceUser);
        Button btnDeleteAccount = findViewById(R.id.btnDeleteAccount);

        //Fragments
        changePasswordFragment = ChangePasswordFragment.newInstance();
        changeVoiceFragment = ChangeVoiceFragment.newInstance();
        fragmentManager = getSupportFragmentManager();

        fragmentTransaction =  fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.user_settings_fragment_container, changePasswordFragment);
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

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccountDialog();
            }
        });

    }

    /**
     * Mètode per a fer i mostrar el dialog per a eliminar el compte
     * @author Jordi Gómez Lozano
     */
    private void deleteAccountDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(DIALOG_TITLE_DELETE);
        alert.setMessage(DIALOG_MESSAGE_DELETE);

        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteAccountCaller();
            }
        });
        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    @Override
    public void onButtonPressed(EditText previousPasswordEntered, EditText newPassword, EditText conformPassword) {
        if(checkPasswordChangedFields(previousPasswordEntered, newPassword, conformPassword)){

            passwordChanged = newPassword.getText().toString();
            sendPasswordToServer(newPassword.getText().toString());

            hideKeyboard();
        }
    }

    @Override
    public void onButtonPressed(EditText passwordEntered, String choice, LinearLayout radioGroupLayout) {
        if(checkVoiceChangedFields(passwordEntered, choice, radioGroupLayout)){

            sendVoiceToServer(choice);
            hideKeyboard();
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

        GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(UserSettingsActivity.this);
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
     * Obté el valor introduït per l'usuari en els diferents camps del registre i comprova que no hi hagi errors.
     * @return Un booleà: true si ha trobat error, i false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    private boolean checkPasswordChangedFields(EditText previousPasswordEntered, EditText newPassword, EditText conformPassword) {
        boolean correcte = true;

        GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(UserSettingsActivity.this);
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

        GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(UserSettingsActivity.this);
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
            queryBundle.putString(OPTION_BUNDLE_KEY, CHANGE_PASSWORD_OPTION);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
        }
    }

    private void deleteAccountCaller(){
        GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(UserSettingsActivity.this);
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
            queryBundle.putString(OPTION_BUNDLE_KEY, DELETE_ACCOUNT_OPTION);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
        }
    }

    /**
     * Crea el bundle i crida al Loader per a fer el request al servidor.
     * @param novaVeu la nova veu seleccionada per l'usuari.
     * @author Jordi Gómez Lozano.
     */
    private void sendVoiceToServer(String novaVeu) {

        GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(UserSettingsActivity.this);
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
            queryBundle.putString(OPTION_BUNDLE_KEY, BUNDLE_VOICE_KEY);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
        }
    }

    @NonNull
    @Override
    public Loader<Bundle> onCreateLoader(int id, @Nullable Bundle args) {
        String token ="";
        String email ="";
        String novaClau ="";
        String novaVeu ="";
        String option = "";

        if (args != null) {

            option = args.getString(OPTION_BUNDLE_KEY);

            switch (option) {
                case CHANGE_PASSWORD_OPTION:

                    token = args.getString(BUNDLE_TOKEN_KEY);
                    email = args.getString(BUNDLE_EMAIL_KEY);
                    novaClau = args.getString(BUNDLE_PASSWORD_KEY);

                    return new ChangePasswordLoader(this, novaClau, email, token);

                case CHANGE_VOICE_OPTION:

                    token = args.getString(BUNDLE_TOKEN_KEY);
                    email = args.getString(BUNDLE_EMAIL_KEY);
                    novaClau = args.getString(BUNDLE_PASSWORD_KEY);
                    novaVeu = args.getString(BUNDLE_VOICE_KEY);

                    return new ChangeVoiceLoader(this, novaClau, novaVeu, email, token);

                case DELETE_ACCOUNT_OPTION:

                    token = args.getString(BUNDLE_TOKEN_KEY);
                    email = args.getString(BUNDLE_EMAIL_KEY);
                    return new DeleteAccountLoader(this, email, token);
            }
        }

        return new ChangePasswordLoader(this, novaClau ,email, token);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Bundle> loader, Bundle data) {
        int responseCode = data.getInt(RESPONSE_CODE_BUNDLE_KEY);

        if (responseCode == HttpURLConnection.HTTP_OK) {

            Toast.makeText(UserSettingsActivity.this, ACCOUNT_DELETED_TEXT, Toast.LENGTH_SHORT).show();
            GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(UserSettingsActivity.this);
            gestorSharedPreferences.deleteData();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();

        }if (responseCode == HttpURLConnection.HTTP_CREATED) {

            if(passwordChanged != null){
                GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(UserSettingsActivity.this);
                gestorSharedPreferences.setPassword(passwordChanged);
            }
            Toast.makeText(UserSettingsActivity.this, MODIFICACIO_OK, Toast.LENGTH_SHORT).show();
        }else{
            StyleableToast.makeText(UserSettingsActivity.this,MODIFICACIO_ERROR , Toast.LENGTH_SHORT, R.style.toastError).show();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Bundle> loader) {}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, UserActivity.class);
                intent.putExtra(EXTRA_MESSAGE, INTENT_VALUE_ROLE_USER);
                startActivity(intent);
                return true;
        }
        return false;
    }

    /**
     * Mètode per amagar el teclat
     * @author Jordi Gómez Lozano
     */
    private void hideKeyboard(){
        //Amagar el teclat un cop l'usuari pitja el botó
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}