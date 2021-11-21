package controlador.server.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import controlador.server.get.UsuarisListLoaderForTest;
import controlador.server.post.RequestToken;
import controlador.server.post.SignUpLoader;
import controlador.server.post.SignUpLoaderForTest;
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
    private static final String EMAIL = "JoGomLoz@gmail.com";
    public static final String BUNDLE_URL_KEY = "url";
    private TextView textResponse;
    private EditText info;
    private static final String TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2Mzc1MTE2MTQsInVzZXJfbmFtZSI6IkpvR29tTG96QGdtYWlsLmNvbSIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iXSwianRpIjoiNTYxZTNkOTQtOGU3OC00YjBhLWI3MWMtYThkOGQzNDNhOWM5IiwiY2xpZW50X2lkIjoiYW5kcm9pZGFwcCIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdfQ.iMdGf6Dg3gYwSfKU2zDi3taOrwSnBsb8HxQn6fKzFcZYo52g91QDI6mkBc0hSxGixOruSn0vrQciEAjLQPCDQ8XpMKX-xf-SjHKvz_075RXhbj0LJ56Vtkd8bHJBpfiNeLRrpFDcQfXs8Yq7lx3-ZWxnHBcOKOQSI9xouGdS5L1CvWLlDaXR58MJ-2Sqec1Nj2zW0tQNw06prUg2tdwxjFlQUG3ZDDopeQ_4JNOLna28ifuMDXpxetGHOiFhX114c088NzedMq43suz3Gzv0O1xpCe6UvObG0ll3IjfXMJGzBcDCmDwPvqMATxBqKsK0Im2RHj4N3-HJkbgZYDdoqQ";
    private static final String CLAU = "12345";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_test);

        info = findViewById(R.id.infoTest);
        textResponse = findViewById(R.id.textRequestResponse);
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
                obtenirInformacio(spinnerSelection, TOKEN, EMAIL, CLAU);
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textResponse.setText("");
                info.setText("");
            }
        });
    }

    /**
     * Crea el bundle i crida al Loader per a fer el request al servidor.
     * @param selection L'opció de l'spinner seleccionada.
     * @param token El token de l'usuari.
     * @author Jordi Gómez Lozano.
     */
    private void obtenirInformacio(String selection, String token, String email, String clau) {
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

                case "Nou usuari":

                    queryBundle = new Bundle();
                    queryBundle.putString(SERVER_OPTION, "Nou usuari");
                    queryBundle.putString(BUNDLE_EMAIL_KEY, textInfo);
                    break;

                case "Tot":

                    queryBundle = new Bundle();
                    queryBundle.putString(SERVER_OPTION, "Tot");
                    queryBundle.putString(BUNDLE_TOKEN_KEY, token);
                    queryBundle.putString(BUNDLE_URL_KEY, ""+textInfo);
                    break;

                case "Email":

                    queryBundle = new Bundle();
                    queryBundle.putString(SERVER_OPTION, "Email");
                    queryBundle.putString(BUNDLE_TOKEN_KEY, token);
                    queryBundle.putString(BUNDLE_URL_KEY, "/"+textInfo);
                    break;

                case "Veu":

                    queryBundle = new Bundle();
                    queryBundle.putString(SERVER_OPTION, "Veu");
                    queryBundle.putString(BUNDLE_TOKEN_KEY, token);
                    queryBundle.putString(BUNDLE_URL_KEY, "/voice/" + textInfo);
                    break;

                case "Rol":

                    queryBundle = new Bundle();
                    queryBundle.putString(SERVER_OPTION, "Rol");
                    queryBundle.putString(BUNDLE_TOKEN_KEY, token);
                    queryBundle.putString(BUNDLE_URL_KEY, "/roles/" + textInfo);
                    break;

                case "Canviar veu":
                    queryBundle = new Bundle();
                    queryBundle.putString(SERVER_OPTION, "Canviar veu");
                    queryBundle.putString(BUNDLE_TOKEN_KEY, token);
                    queryBundle.putString(BUNDLE_EMAIL_KEY, email);
                    queryBundle.putString(BUNDLE_PASSWORD_KEY, clau);
                    queryBundle.putString(BUNDLE_VOICE_KEY, textInfo);
                    break;

                case "Canviar clau":

                    queryBundle = new Bundle();
                    queryBundle.putString(SERVER_OPTION, "Canviar clau");
                    queryBundle.putString(BUNDLE_TOKEN_KEY, token);
                    queryBundle.putString(BUNDLE_EMAIL_KEY, email);
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

        if (args != null) {

            serverOption = args.getString(SERVER_OPTION);

            switch (serverOption) {

                case "Nou usuari":
                    email = args.getString(BUNDLE_EMAIL_KEY);
                    return new SignUpLoaderForTest(this, new Usuari(email, "MALE", "12345"));
                case "Tot":
                case "Email":
                case "Veu":
                case "Rol":
                    token = args.getString(BUNDLE_TOKEN_KEY);
                    opcioUrl = args.getString(BUNDLE_URL_KEY);
                    return new UsuarisListLoaderForTest(this, opcioUrl, token);

                case "Canviar veu":
                    token = args.getString(BUNDLE_TOKEN_KEY);
                    email = args.getString(BUNDLE_EMAIL_KEY);
                    novaClau = args.getString(BUNDLE_PASSWORD_KEY);
                    novaVeu = args.getString(BUNDLE_VOICE_KEY);
                    Log.i("Info", novaClau);
                    Log.i("Info", novaVeu);
                    Log.i("Info", email);
                    return new ChangeVoiceLoader(this, novaClau , novaVeu, email, token);

                case "Canviar clau":
                    token = args.getString(BUNDLE_TOKEN_KEY);
                    email = args.getString(BUNDLE_EMAIL_KEY);
                    novaClau = args.getString(BUNDLE_PASSWORD_KEY);

                    return new ChangePasswordLoader(this, novaClau ,email, token);

                default:
                    break;
            }

        }

        return new ChangePasswordLoader(this, novaClau ,email, token);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Integer> loader, Integer data) {
        RequestToken gestorRequest = new RequestToken(this);
        long time = System.currentTimeMillis();

        if(gestorRequest.connectionProblems(time)){

            textResponse.setText(ERROR_SERVIDOR);
        } else {
            textResponse.setText(String.valueOf(data));
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Integer> loader) {

    }
}