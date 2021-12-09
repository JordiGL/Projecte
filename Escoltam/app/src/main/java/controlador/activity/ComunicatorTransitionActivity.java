package controlador.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Date;

import controlador.gestor.GestorMain;
import controlador.gestor.GestorSharedPreferences;
import controlador.gestor.GestorUser;
import controlador.gestor.JsonUtils;
import controlador.server.get.PanellsListLoader;
import jordigomez.ioc.cat.escoltam.R;

public class ComunicatorTransitionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{
    private final static String EXTRA_MESSAGE = "jordigomez.ioc.cat.comunicador.MESSAGE";
    private static final String TOKEN_BUNDLE_KEY = "token";
    private static final String URL_BUNDLE_KEY = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunicator_transition);

        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(getSupportLoaderManager().getLoader(0)!=null){
            getSupportLoaderManager().initLoader(0,null,this);
        }

        GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(this);
        String username = gestorSharedPreferences.getEmail();
        String token = gestorSharedPreferences.getToken();
        obtenirInformacio(username, token);
    }


    /**
     * Crea el bundle i crida al Loader per a fer el request al servidor.
     * @param username L'username de l'usuari
     * @param token El token de l'usuari.
     * @author Jordi Gómez Lozano.
     */
    private void obtenirInformacio(String username, String token) {
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
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String token ="";
        String opcioUrl ="";

        if (args != null) {

            token = args.getString(TOKEN_BUNDLE_KEY);
            opcioUrl = args.getString(URL_BUNDLE_KEY);
        }

        return new PanellsListLoader(this, opcioUrl, token);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        GestorUser.createObjectsFromObtainedData(data);

        Intent intentComunicador = new Intent(this, UserActivity.class);
        intentComunicador.putExtra(EXTRA_MESSAGE, "ROLE_ADMIN");
        startActivity(intentComunicador);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

}