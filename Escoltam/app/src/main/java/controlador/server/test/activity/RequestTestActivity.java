package controlador.server.test.activity;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import controlador.activity.LoginActivity;
import controlador.server.delete.DeleteAccountLoader;
import controlador.server.delete.DeleteIconaLoader;
import controlador.server.delete.DeletePanellLoader;
import controlador.server.get.PanellsListLoader;
import controlador.server.post.NewIconLoader;
import controlador.server.post.NewPanellLoader;
import controlador.server.put.EditIconaLoader;
import controlador.server.put.EditPanellLoader;
import controlador.server.test.delete.TestDeleteAccountLoader;
import controlador.server.test.delete.TestDeleteIconaLoader;
import controlador.server.test.delete.TestDeletePanellLoader;
import controlador.server.test.get.TestPanellsListLoader;
import controlador.server.test.get.TestUsuarisListLoader;
import controlador.server.test.post.TestLoginLoader;
import controlador.server.test.post.TestNewIconLoader;
import controlador.server.test.post.TestNewPanellLoader;
import controlador.server.test.post.TestSignUpLoader;
import controlador.server.put.ChangePasswordLoader;
import controlador.server.put.ChangeVoiceLoader;
import controlador.server.test.put.TestChangePasswordLoader;
import controlador.server.test.put.TestChangeVoiceLoader;
import controlador.server.test.put.TestEditIconaLoader;
import controlador.server.test.put.TestEditPanellLoader;
import jordigomez.ioc.cat.escoltam.R;
import model.Icona;
import model.Panell;
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

    private static final String DELETE_ACCOUNT_OPTION = "delete_account";
    private static final String EMAIL_BUNDLE_KEY = "email";
    private static final String PANELL_BUNDLE_KEY = "panell";
    private static final String CREATE_NEW_PANELL_OPTION = "add";
    private static final String CREATE_ICONA_OPTION = "create_icona";
    private static final String ID_PANELL_BUNDLE_KEY = "id_panell";
    private static final String EDIT_PANELL_OPTION = "edit";
    private static final String PANELL_NAME_BUNDLE_KEY = "panell_name";
    private static final String PANELL_POSITION_BUNDLE_KEY = "panell_position";
    private static final String PANELL_FAVORITE_BUNDLE_KEY = "panell_favorite";
    private static final String PANELL_ID_BUNDLE_KEY = "panell_id";
    private static final String ICON_NAME_BUNDLE_KEY = "icon_name";
    private static final String ICON_POSITION_BUNDLE_KEY = "icon_position";
    private static final String FILE_NAME_BUNDLE_KEY = "file_name";
    private static final String EDIT_ICONA_OPTION = "edit_icona";
    private static final String ICON_ID_BUNDLE_KEY = "icon_id";
    private static final String DELETE_PANELL_OPTION = "delete_panell";
    private static final String DELETE_ICONA_OPTION = "delete_icona";
    private static final String LIST_PANELLS_OPTION = "list";


    private TextView textResponse;
    private EditText info;
    private EditText emailInput;
    //Variables de configuració del servidor.
    private static final String TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2Mzk5MzkzNjQsInVzZXJfbmFtZSI6ImpvZ29tbG96QGdtYWlsLmNvbSIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iXSwianRpIjoiZmZmNDI4NjUtMjBmNi00MzhkLWJlNWQtMmQzODQwODk2NjQ0IiwiY2xpZW50X2lkIjoiYW5kcm9pZGFwcCIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdfQ.dV4KtdKKHl9_hMo7FjME5JCeIjv0m9PIlEoyyb8APU7dplLof46ZUWJGSq2VA4S-fAYX9zXpo5ap91sVvCVpXF9nCE3dpnbPtFSY6mOrjXr5dhlIcNNkOZAP-pH0kZMN4Hj6ajgqjAOBzrdZ-MFUFT6Fr-zSvLH5BmFJJfSecRGxohThf-ihLQdehqVSxoQkDTDPyut_131mLYk2IK_9x8jXGAM3t6RKrfd91FpjXV1i-vDWlNd7kDLH7l2TkkUUhNZ3L2jzV4je3xelLEqYoqkXOoqiN85pa8SJSwlZwXn9whqjmfBQ6CN2CE0tQCDwlzPymEhDqTavLgW2IhVnFQ";
    private static final String CLAU = "12345";
    private static final String EMAIL = "jogomloz@gmail.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_test);

        info = findViewById(R.id.infoTest);
        emailInput = findViewById(R.id.emailTest);
        textResponse = findViewById(R.id.textRequestResponse);
        TextView returnToLogin = findViewById(R.id.textReturnLogin);
        Button buttonClear = findViewById(R.id.buttonClearTest);
        Button buttonRequest = findViewById(R.id.buttonRequestTest);
        Button btnAfegirPanell = findViewById(R.id.btnAfegirPanellTest);
        Button btnEditarPanell = findViewById(R.id.btnEditarPanellTest);
        Button btnEliminarPanell = findViewById(R.id.btnEliminarPanellTest);
        Button btnAfegirIcona = findViewById(R.id.btnAfegirIconaTest);
        Button btnEditarIcona = findViewById(R.id.btnEditarIconaTest);
        Button btnEliminarIcona = findViewById(R.id.btnEliminarIconaTest);
        Button btnEliminarCompte = findViewById(R.id.btnDeleteAccountTest);
        Button btnObtenirPanells = findViewById(R.id.btnObtenirPanellsTest);

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

        Icona icona = new Icona("Ok", 7);
        Icona editIcona = new Icona("Adeu", 3, null, 14);

        Panell panell = new Panell("Animals", 4, false);
        Panell editPanell = new Panell("Vegetacio", 4, false, new ArrayList<>(), 4);

        btnAfegirPanell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle queryBundle = new Bundle();
                queryBundle.putString(SERVER_OPTION, CREATE_NEW_PANELL_OPTION);
                queryBundle.putString(EMAIL_BUNDLE_KEY, EMAIL);
                queryBundle.putString(PANELL_BUNDLE_KEY, panell.toString());
                queryBundle.putString(BUNDLE_TOKEN_KEY, TOKEN);
                getSupportLoaderManager().restartLoader(0, queryBundle, RequestTestActivity.this);
            }
        });

        btnEditarPanell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle queryBundle = new Bundle();
                queryBundle.putString(SERVER_OPTION, EDIT_PANELL_OPTION);
                queryBundle.putString(PANELL_NAME_BUNDLE_KEY, editPanell.getNom());
                queryBundle.putInt(PANELL_POSITION_BUNDLE_KEY, editPanell.getPosicio());
                queryBundle.putBoolean(PANELL_FAVORITE_BUNDLE_KEY, editPanell.isFavorit());
                queryBundle.putInt(PANELL_ID_BUNDLE_KEY, editPanell.getId());
                queryBundle.putString(EMAIL_BUNDLE_KEY, EMAIL);
                queryBundle.putString(BUNDLE_TOKEN_KEY, TOKEN);
                getSupportLoaderManager().restartLoader(0, queryBundle, RequestTestActivity.this);
            }
        });

        btnEliminarPanell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle queryBundle = new Bundle();
                queryBundle.putString(SERVER_OPTION, DELETE_PANELL_OPTION);
                queryBundle.putInt(ID_PANELL_BUNDLE_KEY, editPanell.getId());
                queryBundle.putString(BUNDLE_TOKEN_KEY, TOKEN);
                queryBundle.putString(EMAIL_BUNDLE_KEY, EMAIL);
                getSupportLoaderManager().restartLoader(0, queryBundle, RequestTestActivity.this);
            }
        });

        btnAfegirIcona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle queryBundle = new Bundle();
                queryBundle.putString(SERVER_OPTION, CREATE_ICONA_OPTION);
                queryBundle.putInt(PANELL_ID_BUNDLE_KEY, editPanell.getId());
                queryBundle.putString(ICON_NAME_BUNDLE_KEY, icona.getNom());
                queryBundle.putInt(ICON_POSITION_BUNDLE_KEY, icona.getPosicio());
                queryBundle.putString(FILE_NAME_BUNDLE_KEY, "");
                queryBundle.putString(BUNDLE_TOKEN_KEY, TOKEN);
                getSupportLoaderManager().restartLoader(0, queryBundle, RequestTestActivity.this);
            }
        });

        btnEditarIcona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle queryBundle = new Bundle();
                queryBundle.putString(SERVER_OPTION, EDIT_ICONA_OPTION);
                queryBundle.putInt(ICON_ID_BUNDLE_KEY, editIcona.getId());
                queryBundle.putString(ICON_NAME_BUNDLE_KEY, editIcona.getNom());
                queryBundle.putInt(ICON_POSITION_BUNDLE_KEY, editIcona.getPosicio());
                queryBundle.putString(FILE_NAME_BUNDLE_KEY, "");
                queryBundle.putString(BUNDLE_TOKEN_KEY, TOKEN);
                getSupportLoaderManager().restartLoader(0, queryBundle, RequestTestActivity.this);
            }
        });

        btnEliminarIcona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle queryBundle = new Bundle();
                queryBundle.putString(SERVER_OPTION, DELETE_ICONA_OPTION);
                queryBundle.putInt(ICON_ID_BUNDLE_KEY, editIcona.getId());
                queryBundle.putString(BUNDLE_TOKEN_KEY, TOKEN);
                getSupportLoaderManager().restartLoader(0, queryBundle, RequestTestActivity.this);
            }
        });

        btnObtenirPanells.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle queryBundle = new Bundle();
                queryBundle.putString(SERVER_OPTION, LIST_PANELLS_OPTION);
                queryBundle.putString(BUNDLE_TOKEN_KEY, TOKEN);
                queryBundle.putString(BUNDLE_EMAIL_KEY, EMAIL);
                getSupportLoaderManager().restartLoader(0, queryBundle, RequestTestActivity.this);
            }
        });

        btnEliminarCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle queryBundle = new Bundle();
                queryBundle.putString(BUNDLE_TOKEN_KEY, TOKEN);
                queryBundle.putString(BUNDLE_EMAIL_KEY, EMAIL);
                queryBundle.putString(SERVER_OPTION, DELETE_ACCOUNT_OPTION);
                getSupportLoaderManager().restartLoader(0, queryBundle, RequestTestActivity.this);
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
        String username = "";
        String panell = "";
        String panellName = "";
        String iconName = "";
        String fileName = "";
        int iconPosition;
        int panellPosition;
        int idPanell;
        int idIcona;
        boolean panellIsFavorite;

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
                    return new TestChangeVoiceLoader(this, novaClau , novaVeu, email, token);

                case OPTION_CANVIAR_CLAU:
                    token = args.getString(BUNDLE_TOKEN_KEY);
                    email = args.getString(BUNDLE_EMAIL_KEY);
                    novaClau = args.getString(BUNDLE_PASSWORD_KEY);

                    return new TestChangePasswordLoader(this, novaClau ,email, token);


                case CREATE_NEW_PANELL_OPTION:

                    username = args.getString(EMAIL_BUNDLE_KEY);
                    panell = args.getString(PANELL_BUNDLE_KEY);
                    token = args.getString(BUNDLE_TOKEN_KEY);
                    return new TestNewPanellLoader(this, panell, username, token);

                case DELETE_PANELL_OPTION:

                    token = args.getString(BUNDLE_TOKEN_KEY);
                    idPanell = args.getInt(ID_PANELL_BUNDLE_KEY);
                    username = args.getString(EMAIL_BUNDLE_KEY);
                    return new TestDeletePanellLoader(this, idPanell, username, token);

                case EDIT_PANELL_OPTION:

                    panellName = args.getString(PANELL_NAME_BUNDLE_KEY);
                    panellPosition = args.getInt(PANELL_POSITION_BUNDLE_KEY);
                    panellIsFavorite = args.getBoolean(PANELL_FAVORITE_BUNDLE_KEY);
                    idPanell = args.getInt(PANELL_ID_BUNDLE_KEY);
                    username = args.getString(EMAIL_BUNDLE_KEY);
                    token = args.getString(BUNDLE_TOKEN_KEY);
                    return new TestEditPanellLoader(this, panellName, panellPosition,
                            panellIsFavorite, idPanell, username, token);

                case CREATE_ICONA_OPTION:
                    idPanell = args.getInt(PANELL_ID_BUNDLE_KEY);
                    iconName = args.getString(ICON_NAME_BUNDLE_KEY);
                    iconPosition = args.getInt(ICON_POSITION_BUNDLE_KEY);
                    fileName = args.getString(FILE_NAME_BUNDLE_KEY);
                    token = args.getString(BUNDLE_TOKEN_KEY);
                    return new TestNewIconLoader(this, idPanell, iconName,
                            iconPosition, fileName, token);

                case EDIT_ICONA_OPTION:
                    idIcona = args.getInt(ICON_ID_BUNDLE_KEY);
                    iconName = args.getString(ICON_NAME_BUNDLE_KEY);
                    iconPosition = args.getInt(ICON_POSITION_BUNDLE_KEY);
                    fileName = args.getString(FILE_NAME_BUNDLE_KEY);
                    token = args.getString(BUNDLE_TOKEN_KEY);
                    return new TestEditIconaLoader(this, idIcona, iconName,
                            iconPosition, fileName, token);

                case DELETE_ICONA_OPTION:
                    token = args.getString(BUNDLE_TOKEN_KEY);
                    idIcona = args.getInt(ICON_ID_BUNDLE_KEY);
                    return new TestDeleteIconaLoader(this, idIcona, token);

                case LIST_PANELLS_OPTION:

                    token = args.getString(BUNDLE_TOKEN_KEY);
                    email = args.getString(EMAIL_BUNDLE_KEY);

                    return new TestPanellsListLoader(this, email, token);

                case DELETE_ACCOUNT_OPTION:

                    token = args.getString(BUNDLE_TOKEN_KEY);
                    email = args.getString(BUNDLE_EMAIL_KEY);
                    return new TestDeleteAccountLoader(this, email, token);

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