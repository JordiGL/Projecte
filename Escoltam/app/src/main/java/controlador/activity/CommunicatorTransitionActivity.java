package controlador.activity;

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

import android.view.WindowManager;
import android.widget.Toast;

import java.net.HttpURLConnection;

import controlador.gestor.GestorSharedPreferences;
import controlador.gestor.GestorUser;
import controlador.server.get.PanellsListLoader;
import jordigomez.ioc.cat.escoltam.R;

/**
 * Classe de transició cap al comunicador per a carregar els panells
 * @author Jordi Gómez Lozano
 */
public class CommunicatorTransitionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Bundle>{
    private static final String PANELLS_BUNDLE_KEY = "panells";
    private static final String RESPONSE_CODE_BUNDLE_KEY = "responseCode";
    private final static String EXTRA_MESSAGE = "jordigomez.ioc.cat.comunicador.MESSAGE";
    private static final String TOKEN_BUNDLE_KEY = "token";
    private static final String URL_BUNDLE_KEY = "url";
    private static final String ERROR_GET_PANELLS = "Error en obtenir la llista de panells";
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicator_transition);

        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        role = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);

        if(getSupportLoaderManager().getLoader(0)!=null){
            getSupportLoaderManager().initLoader(0,null,this);
        }


        GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(this);
        String username = gestorSharedPreferences.getEmail();
        String token = gestorSharedPreferences.getToken();
        getPanellsData(username, token);
    }


    /**
     * Crea el bundle i crida al Loader per a fer el request al servidor.
     * @param username L'username de l'usuari
     * @param token El token de l'usuari.
     * @author Jordi Gómez Lozano.
     */
    private void getPanellsData(String username, String token) {
        Bundle queryBundle = null;


        //Comprova la connexió i la informació introduide per l'usuari en l'EditText.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {

            queryBundle = new Bundle();
            queryBundle.putString(TOKEN_BUNDLE_KEY, token);
            queryBundle.putString(URL_BUNDLE_KEY, username);

            getSupportLoaderManager().restartLoader(0, queryBundle, this);
        }
    }

    @NonNull
    @Override
    public Loader<Bundle> onCreateLoader(int id, @Nullable Bundle args) {
        String token ="";
        String opcioUrl ="";

        if (args != null) {

            token = args.getString(TOKEN_BUNDLE_KEY);
            opcioUrl = args.getString(URL_BUNDLE_KEY);
        }

        return new PanellsListLoader(this, opcioUrl, token);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Bundle> loader, Bundle data) {
        String panells ="";
        int responseCode;

        if (data != null) {
            responseCode = data.getInt(RESPONSE_CODE_BUNDLE_KEY);

            if(responseCode == HttpURLConnection.HTTP_OK){

                panells = data.getString(PANELLS_BUNDLE_KEY);
                GestorUser.createObjectsFromObtainedData(panells);
                Intent intentComunicador = new Intent(this, UserActivity.class);
//Eliminar
                intentComunicador.putExtra(EXTRA_MESSAGE, role);
                startActivity(intentComunicador);
            } else {
                displayToast(ERROR_GET_PANELLS);
            }
        }
    }

    /**
     * Mostra informació per pantalla.
     * @param message missatge que es mostrara per pantalla.
     * @author Jordi Gómez Lozano.
     */
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Bundle> loader) {}
}