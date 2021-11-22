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
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import controlador.gestor.GestorAdministrator;
import controlador.gestor.GestorSharedPreferences;
import controlador.gestor.GestorSignUp;
import controlador.server.get.UsuarisListLoader;
import jordigomez.ioc.cat.escoltam.R;
import model.Role;
import model.Usuari;

/**
 * Activitat de l'administrador.
 * @see AppCompatActivity
 * @see LoaderManager
 * @see PopupMenu
 * @author Jordi Gómez Lozano
 */
public class AdministratorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,PopupMenu.OnMenuItemClickListener {
    private final static String EXTRA_MESSAGE = "jordigomez.ioc.cat.comunicador.MESSAGE";
    private static final String CERCA_SENSE_RESULTAT = "No s'ha obtingut cap resultat";
    private AutoCompleteTextView cercador;
    private List<Usuari> mUsuaris;
    private RecyclerView mRecyclerView;
    private UsuariAdapter mAdapter;
    private GestorSharedPreferences  gestorSharedPreferences;
    private String adminEmail;
    private GestorAdministrator gestorAdministrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageButton btnSearch = findViewById(R.id.searchButton);
        cercador = findViewById(R.id.editTextBuscador);

        //RecyclerView i adapter.
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new UsuariAdapter(mUsuaris, AdministratorActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        gestorAdministrator = new GestorAdministrator();
        //Obtenim el correu
        Intent intent = getIntent();
        adminEmail = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);

        ImageView settings = findViewById(R.id.buttonMore);
        //Afegir al menu
        registerForContextMenu(settings);

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

        cercador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cercador.setError(null);
                int position = spinner.getSelectedItemPosition();
                gestorAdministrator.dropDownOptions(position, cercador, AdministratorActivity.this);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gestorAdministrator.dropDownOptions(position, cercador, AdministratorActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String spinnerSelection = spinner.getSelectedItem().toString();
                if(checkFields(spinnerSelection)){
                    Log.i("Info", "error");
                    obtenirInformacio(spinnerSelection, token);
                }
            }
        });

    }

    /**
     * Obté el valor introduït per l'usuari en els diferents camps del registre i comprova que no hi hagi errors.
     * @return Un booleà: true si es correcte, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    private boolean checkFields(String opcio) {
        boolean correcte = true;

        gestorAdministrator.setCercadorText(cercador.getText().toString());

        switch(opcio){
            case "Email":

                if(!gestorAdministrator.emailChecker()){
                    cercador.setError(gestorAdministrator.getError());
                    return false;
                }
                break;
            case "Rol":

                if(!gestorAdministrator.roleChecker()){
                    cercador.setError(gestorAdministrator.getError());
                    return false;
                }
                break;
            case "Veu":

                if(!gestorAdministrator.voiceChecker()){
                    cercador.setError(gestorAdministrator.getError());
                    return false;
                }
                break;
            default:
                break;
        }

        return correcte;
    }

    /**
     * Crea el bundle i crida al Loader per a fer el request al servidor.
     * @param selection L'opció de l'spinner seleccionada.
     * @param token El token de l'usuari.
     * @author Jordi Gómez Lozano.
     */
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
                    queryBundle.putString("url", "/voice/" + textIntroduit.toUpperCase());
                    break;

                case "Rol":

                    queryBundle = new Bundle();
                    queryBundle.putString("token", token);
                    queryBundle.putString("url", "/roles/" + textIntroduit.toUpperCase());
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
            mUsuaris = gestorAdministrator.createObjectsFromObtainedData(data);

            mAdapter = new UsuariAdapter(mUsuaris, AdministratorActivity.this);
            mRecyclerView.setAdapter(mAdapter);

        } else{
            displayToast(CERCA_SENSE_RESULTAT);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {}

    /**
     * Mostra informació per pantalla.
     * @param message missatge que es mostrara per pantalla.
     * @author Jordi Gómez Lozano.
     */
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_LONG).show();
    }

    /**
     * PopupMenu per a mostrar les diferents opcions del botó.
     * @param view del component.
     * @author Jordi Gómez Lozano.
     */
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

            case R.id.context_comunicador:

                Intent intentComunicador = new Intent(AdministratorActivity.this, UserActivity.class);
                intentComunicador.putExtra(EXTRA_MESSAGE, "ROLE_ADMIN");
                startActivity(intentComunicador);
                finish();
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