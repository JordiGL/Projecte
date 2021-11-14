package controlador.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import controlador.gestor.GestorSharedPreferences;
import controlador.server.get.UsuarisListLoader;
import jordigomez.ioc.cat.escoltam.R;
import model.Role;
import model.Usuari;

/**
 * Activitat de l'administrador.
 * @see AppCompatActivity
 * @author Jordi Gómez Lozano
 */
public class AdministratorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private EditText capsaDeText;
    private List<Usuari> mUsuaris;
    private RecyclerView mRecyclerView;
    private UsuariAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView correuAdministrador = findViewById(R.id.textCorreuAdministrador);
        Button btnLogout = findViewById(R.id.btn_LogoutAdministrador);
        ImageButton btnSearch = findViewById(R.id.searchButton);
        capsaDeText = findViewById(R.id.editTextBuscador);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Obtenim el correu
        Intent intent = getIntent();
        correuAdministrador.setText(intent.getStringExtra(LoginActivity.EXTRA_MESSAGE));

        ImageView settings = findViewById(R.id.imageMore);

        //Objecte selector.
        Spinner spinner = findViewById(R.id.spinner_object);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AdministratorActivity.this,
                R.array.search_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        if(getSupportLoaderManager().getLoader(0)!=null){
            getSupportLoaderManager().initLoader(0,null,this);
        }

        //Obtenir el token from EncryptedSharedPreferences
        GestorSharedPreferences  gestorSharedPreferences = new GestorSharedPreferences(this);
        String token = gestorSharedPreferences.getToken();

        //Mostrar tots els usuaris per pantalla al iniciar la pantalla.
        String spinnerSelection = spinner.getSelectedItem().toString();
        obtenirInformacio(spinnerSelection, token);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String spinnerSelection = spinner.getSelectedItem().toString();
                obtenirInformacio(spinnerSelection, token);
            }
        });

        //Boto per anar a la configuració d'usuari
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdministratorActivity.this, AdminSettingsActivity.class);
                startActivity(intent);
            }
        });

        //Boto per a fer el logout.
        btnLogout.setOnClickListener(view -> {
            //Borrar token
            gestorSharedPreferences.deleteData();

            Intent intent1 = new Intent(AdministratorActivity.this, LoginActivity.class);
            startActivity(intent1);
            finish();
        });
    }

    /**
     * Omplim el List d'usuaris amb les dades obtingudes del servidor.
     * @param obtainedServerData dades rebudes del GET al servidor.
     * @return List d'usuaris.
     */
    private List<Usuari> createObjectsFromObtainedData(String obtainedServerData) {
        List<Usuari> usuarisObtinguts = new ArrayList<>();

        try {

            if(obtainedServerData != null) {

                String firstChar = String.valueOf(obtainedServerData.charAt(0));

                if(firstChar.equalsIgnoreCase("[")) {

                    JSONArray jsonArray = new JSONArray(obtainedServerData);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Role role = new Role(
                                jsonObject.getJSONArray("roles").getJSONObject(0).getInt("id"),
                                jsonObject.getJSONArray("roles").getJSONObject(0).getString("name")
                        );

                        Usuari usuari = new Usuari(
                                jsonObject.getInt("id"),
                                jsonObject.getString("username"),
                                role,
                                jsonObject.getString("voice"),
                                jsonObject.getString("password"),
                                jsonObject.getBoolean("enabled")
                        );

                        usuarisObtinguts.add(usuari);
                    }

                }else{
                    
                    JSONObject jsonObject = new JSONObject(obtainedServerData);
                    Role role = new Role(
                            jsonObject.getJSONArray("roles").getJSONObject(0).getInt("id"),
                            jsonObject.getJSONArray("roles").getJSONObject(0).getString("name")
                    );

                    Usuari usuari = new Usuari(
                            jsonObject.getInt("id"),
                            jsonObject.getString("username"),
                            role,
                            jsonObject.getString("voice"),
                            jsonObject.getString("password"),
                            jsonObject.getBoolean("enabled")
                    );
                    usuarisObtinguts.add(usuari);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return usuarisObtinguts;
    }

    private void obtenirInformacio(String selection, String token) {
        Bundle queryBundle = null;
        String textIntroduit = capsaDeText.getText().toString();

        //Control del teclat per amagarlo en efectual la busqueda.
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        //Comprova la connexió i la informació introduide per l'usuari en l'EditText.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {

            switch (selection) {

                case "Tot":

                    queryBundle = new Bundle();
                    queryBundle.putString("token", token);
                    queryBundle.putString("url", "");
                    break;

                case "Email":

                    queryBundle = new Bundle();
                    queryBundle.putString("token", token);
                    queryBundle.putString("url", "/"+textIntroduit);
                    break;

                case "Veu":

                    queryBundle = new Bundle();
                    queryBundle.putString("token", token);
                    queryBundle.putString("url", "/voice/" + textIntroduit);
                    break;

                case "Rol":

                    queryBundle = new Bundle();
                    queryBundle.putString("token", token);
                    queryBundle.putString("url", "/roles/" + textIntroduit);
                    break;

                default:

                    queryBundle = new Bundle();
                    queryBundle.putString("token", "");
                    queryBundle.putString("url", "");
                    break;
            }
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {

        String token ="";
        String opcioUrl ="";

        if (args != null) {

            token = args.getString("token");
            opcioUrl = args.getString("url");
        }

        return new UsuarisListLoader(this, opcioUrl, token);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

        if(data != null) {

            mUsuaris = new ArrayList<>();
            mUsuaris = createObjectsFromObtainedData(data);

            mAdapter = new UsuariAdapter(mUsuaris, AdministratorActivity.this);
            mRecyclerView.setAdapter(mAdapter);

        } else{
            Toast.makeText(AdministratorActivity.this, "Error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {}
    
}