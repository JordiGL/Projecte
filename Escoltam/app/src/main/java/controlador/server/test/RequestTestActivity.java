package controlador.server.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationBarView;

import controlador.activity.LoginActivity;
import controlador.activity.SignUpActivity;
import controlador.server.test.get.TestUsuarisListLoader;
import controlador.server.test.post.TestLoginLoader;
import controlador.server.test.post.TestSignUpLoader;
import controlador.server.put.ChangePasswordLoader;
import controlador.server.put.ChangeVoiceLoader;
import jordigomez.ioc.cat.escoltam.R;
import model.Usuari;

public class RequestTestActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Integer> {

    private static final String ERROR_SERVIDOR = "Problemes amb el servidor";
    private static final String BUNDLE_TOKEN_KEY = "token";
    private static final String BUNDLE_EMAIL_KEY = "email";
    private static final String BUNDLE_PASSWORD_KEY = "password";
    private static final String BUNDLE_VOICE_KEY = "voice";
    private static final String SERVER_OPTION = "server_option";
    private static final String BUNDLE_URL_KEY = "url";
    private static final int SERVER_PROBLEMS = 0;
    private static final String OPTION_LOGIN = "Login";
    private static final String OPTION_NOU_USUARI = "Nou usuari";
    private static final String OPTION_CERCA_PER_TOT = "Cerca per tot";
    private static final String OPTION_CERCA_PER_EMAIL = "Cerca per email";
    private static final String OPTION_CERCA_PER_VEU = "Cerca per veu";
    private static final String OPTION_CERCA_PER_ROL = "Cerca per rol";
    private static final String OPTION_CANVIAR_VEU = "Canviar veu";
    private static final String OPTION_CANVIAR_CLAU = "Canviar clau";
    private static final String HINT_CLAU = "Clau";
    private static final String HINT_EMAIL = "Email";
    private static final String HINT_NOVA_CLAU = "Nova clau";
    private static final String HINT_NOVA_VEU = "Nova veu";
    private static final String HINT_VEU = "Veu";
    private static final String HINT_ROL = "Rol";

    private TextView textResponse;
    private EditText info;
    private EditText emailInput;
    //Variables de configuració del servidor.
    private static final String TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2Mzc3ODI3NTYsInVzZXJfbmFtZSI6IkpvR29tTG96QGdtYWlsLmNvbSIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iXSwianRpIjoiNDk5YzQ1NjAtNDBmZS00ODA2LWIzYjgtYjc0NjdlZjQ4MGFkIiwiY2xpZW50X2lkIjoiYW5kcm9pZGFwcCIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdfQ.C6vipMF3WdMPDsXkk--dUbU2Yy9p10bA70jMFBGekGf6Zvh9RtYIxYG7PkF7nk6FTsBhuYwuZ6U1odR54vyhGzvJTY-O8dIoJkjWJgHCF5ku0RLtFTK9XgYpeAbOT7rBDMXuNOmIYrsgRA71pqebNyUZlAfj_Hpnbm4Z7y9Kwp_6nK_tex7sNKWW7jQHVzBnDn_DyPx5qSGZ5LsA3IITSEnj1dHt7zdISvKVK7TQWHi78BDYbWyfswy8JBZR9wpKkT48cy-U8j1iCFI_gpdmWIuE72epYXI9PVDArWrc2BohrlpgOLfAE1y1whHgJoTYnccM_5gR-IOP6yNKtO3aow";
    private static final String CLAU = "12345";
    private static final String EMAIL = "JoGomLoz@gmail.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_test);

        info = findViewById(R.id.infoTest);
        emailInput = findViewById(R.id.emailTest);
        textResponse = findViewById(R.id.textRequestResponse);
        TextView returnToLogin = findViewById(R.id.textReturnLogin);
        Button buttonRequest = findViewById(R.id.buttonRequestTest);
        Button buttonClear = findViewById(R.id.buttonClearTest);

        //Objecte selector.
        Spinner spinner = findViewById(R.id.spinnerRequest);
        //Creem l'ArrayAdapter utilitzant l'Array i l'spinner predeterminat
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(RequestTestActivity.this,
                R.array.request_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        if(getSupportLoaderManager().getLoader(0)!=null){
            getSupportLoaderManager().initLoader(0,null,this);
        }

        buttonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String spinnerSelection = spinner.getSelectedItem().toString();
                obtenirInformacio(spinnerSelection);
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textResponse.setText("");
                info.setText("");
                emailInput.setText("");
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch(position){
                    case 0:
                        info.setHint(HINT_CLAU);
                        emailInput.setHint(HINT_EMAIL);
                        emailInput.setEnabled(true);
                        info.setEnabled(true);
                        break;
                    case 1:
                        info.setHint("");
                        info.setEnabled(false);
                        emailInput.setEnabled(true);
                        break;
                    case 2:
                        info.setHint(HINT_NOVA_CLAU);
                        emailInput.setHint(EMAIL);
                        emailInput.setEnabled(false);
                        info.setEnabled(true);
                        break;
                    case 3:
                        info.setHint(HINT_NOVA_VEU);
                        emailInput.setHint(EMAIL);
                        emailInput.setEnabled(false);
                        info.setEnabled(true);
                        break;
                    case 4:
                        info.setHint("");
                        emailInput.setHint("");
                        emailInput.setEnabled(false);
                        info.setEnabled(false);
                        break;
                    case 5:
                        emailInput.setHint(HINT_EMAIL);
                        info.setHint("");
                        emailInput.setEnabled(true);
                        info.setEnabled(false);
                        break;
                    case 6:
                        emailInput.setHint("");
                        info.setHint(HINT_VEU);
                        emailInput.setEnabled(false);
                        info.setEnabled(true);
                        break;
                    case 7:
                        emailInput.setHint("");
                        info.setHint(HINT_ROL);
                        emailInput.setEnabled(false);
                        info.setEnabled(true);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        returnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RequestTestActivity.this, LoginActivity .class));
                finish();
            }
        });
    }

    /**
     * Crea el bundle i crida al Loader per a fer el request al servidor.
     * @param selection L'opció de l'spinner seleccionada.
     * @author Jordi Gómez Lozano.
     */
    private void obtenirInformacio(String selection) {
        Bundle queryBundle = null;
        String textInfo = info.getText().toString();


        //Comprova la connexió i la informació introduide per l'usuari en l'EditText.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {

            switch (selection) {
                case OPTION_LOGIN:

                    queryBundle = new Bundle();
                    queryBundle.putString(SERVER_OPTION, OPTION_LOGIN);
                    queryBundle.putString(BUNDLE_PASSWORD_KEY, textInfo);
                    queryBundle.putString(BUNDLE_EMAIL_KEY, emailInput.getText().toString());
                    break;

                case OPTION_NOU_USUARI:

                    queryBundle = new Bundle();
                    queryBundle.putString(SERVER_OPTION, OPTION_NOU_USUARI);
                    queryBundle.putString(BUNDLE_EMAIL_KEY, emailInput.getText().toString());
                    break;

                case OPTION_CERCA_PER_TOT:

                    queryBundle = new Bundle();
                    queryBundle.putString(SERVER_OPTION, OPTION_CERCA_PER_TOT);
                    queryBundle.putString(BUNDLE_TOKEN_KEY, TOKEN);
                    queryBundle.putString(BUNDLE_URL_KEY, "");
                    break;

                case OPTION_CERCA_PER_EMAIL:

                    queryBundle = new Bundle();
                    queryBundle.putString(SERVER_OPTION, OPTION_CERCA_PER_EMAIL);
                    queryBundle.putString(BUNDLE_TOKEN_KEY, TOKEN);
                    queryBundle.putString(BUNDLE_URL_KEY, "/"+emailInput.getText().toString());
                    break;

                case OPTION_CERCA_PER_VEU:

                    queryBundle = new Bundle();
                    queryBundle.putString(SERVER_OPTION, OPTION_CERCA_PER_VEU);
                    queryBundle.putString(BUNDLE_TOKEN_KEY, TOKEN);
                    queryBundle.putString(BUNDLE_URL_KEY, "/voice/" + textInfo);
                    break;

                case OPTION_CERCA_PER_ROL:

                    queryBundle = new Bundle();
                    queryBundle.putString(SERVER_OPTION, OPTION_CERCA_PER_ROL);
                    queryBundle.putString(BUNDLE_TOKEN_KEY, TOKEN);
                    queryBundle.putString(BUNDLE_URL_KEY, "/roles/" + textInfo);
                    break;

                case OPTION_CANVIAR_VEU:

                    queryBundle = new Bundle();
                    queryBundle.putString(SERVER_OPTION, OPTION_CANVIAR_VEU);
                    queryBundle.putString(BUNDLE_TOKEN_KEY, TOKEN);
                    queryBundle.putString(BUNDLE_EMAIL_KEY, EMAIL);
                    queryBundle.putString(BUNDLE_PASSWORD_KEY, CLAU);
                    queryBundle.putString(BUNDLE_VOICE_KEY, textInfo);
                    break;

                case OPTION_CANVIAR_CLAU:

                    queryBundle = new Bundle();
                    queryBundle.putString(SERVER_OPTION, OPTION_CANVIAR_CLAU);
                    queryBundle.putString(BUNDLE_TOKEN_KEY, TOKEN);
                    queryBundle.putString(BUNDLE_EMAIL_KEY, EMAIL);
                    queryBundle.putString(BUNDLE_PASSWORD_KEY, textInfo);
                    break;
                default:
                    return;
            }
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
        String serverOption = "";
        String opcioUrl = "";
        String clau = "";

        if (args != null) {

            serverOption = args.getString(SERVER_OPTION);

            switch (serverOption) {
                case OPTION_LOGIN:
                    email = args.getString(BUNDLE_EMAIL_KEY);
                    clau = args.getString(BUNDLE_PASSWORD_KEY);
                    return new TestLoginLoader(this, email, clau);
                case OPTION_NOU_USUARI:
                    email = args.getString(BUNDLE_EMAIL_KEY);
                    return new TestSignUpLoader(this, new Usuari(email, "MALE", "12345"));
                case OPTION_CERCA_PER_TOT:
                case OPTION_CERCA_PER_EMAIL:
                case OPTION_CERCA_PER_VEU:
                case OPTION_CERCA_PER_ROL:

                    token = args.getString(BUNDLE_TOKEN_KEY);
                    opcioUrl = args.getString(BUNDLE_URL_KEY);
                    return new TestUsuarisListLoader(this, opcioUrl, token);

                case OPTION_CANVIAR_VEU:
                    token = args.getString(BUNDLE_TOKEN_KEY);
                    email = args.getString(BUNDLE_EMAIL_KEY);
                    novaClau = args.getString(BUNDLE_PASSWORD_KEY);
                    novaVeu = args.getString(BUNDLE_VOICE_KEY);
                    return new ChangeVoiceLoader(this, novaClau , novaVeu, email, token);

                case OPTION_CANVIAR_CLAU:
                    token = args.getString(BUNDLE_TOKEN_KEY);
                    email = args.getString(BUNDLE_EMAIL_KEY);
                    novaClau = args.getString(BUNDLE_PASSWORD_KEY);

                    return new ChangePasswordLoader(this, novaClau ,email, token);

                default:
                    break;
            }

        }

        return null;

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Integer> loader, Integer data) {

        if(data == SERVER_PROBLEMS){

            textResponse.setText(ERROR_SERVIDOR);

        } else {

            textResponse.setText(String.valueOf(data));
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Integer> loader) {

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RequestTestActivity.this, LoginActivity .class));
        finish();
    }

}