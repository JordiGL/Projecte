package controlador.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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
public class AdministratorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,PopupMenu.OnMenuItemClickListener {
    private AutoCompleteTextView cercador;
    private List<Usuari> mUsuaris;
    private RecyclerView mRecyclerView;
    private UsuariAdapter mAdapter;
    private GestorSharedPreferences  gestorSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageButton btnSearch = findViewById(R.id.searchButton);
        cercador = findViewById(R.id.editTextBuscador);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Obtenim el correu
        Intent intent = getIntent();

        ImageView settings = findViewById(R.id.buttonMore);
        //Afegir al menu
        registerForContextMenu(settings);

        //Autocomplete del cercador.
        ArrayAdapter<CharSequence> adapterET = ArrayAdapter.createFromResource(this,  R.array.autocomplete_options, android.R.layout.simple_list_item_1);
        cercador.setThreshold(4);
        cercador.setAdapter(adapterET);

        //Objecte selector.
        Spinner spinner = findViewById(R.id.spinner_object);
        //Creem l'ArrayAdapter utilitzant l'Array i l'spinner predeterminat
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AdministratorActivity.this,
                R.array.search_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        if(getSupportLoaderManager().getLoader(0)!=null){
            getSupportLoaderManager().initLoader(0,null,this);
        }

        //Obtenir el token from EncryptedSharedPreferences
        gestorSharedPreferences = new GestorSharedPreferences(this);
        String token = gestorSharedPreferences.getToken();

        //Mostrar tots els usuaris per pantalla al iniciar la pantalla.
        String spinnerSelection = spinner.getSelectedItem().toString();
        obtenirInformacio(spinnerSelection, token);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String spinnerSelection = spinner.getSelectedItem().toString();
                obtenirInformacio(spinnerSelection, token);
            }
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
        String textIntroduit = cercador.getText().toString();

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

    /**
     * Displays a Toast with the message.
     *
     * @param message Message to display.
     */
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }

    public void openMoreMenu(View view) {

        PopupMenu popup = new PopupMenu(AdministratorActivity.this, view);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_admin_context, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.context_settings:
                Intent intentAdmin = new Intent(AdministratorActivity.this, AdminSettingsActivity.class);
                startActivity(intentAdmin);
                return true;

            case R.id.context_logout:

                gestorSharedPreferences.deleteData();
                Intent intentLogin = new Intent(AdministratorActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}