package controlador.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import controlador.fragment.PanellFragment;
import controlador.fragment.UserControlFragment;
import controlador.fragment.UserToolbarFragment;
import controlador.gestor.GestorSharedPreferences;
import controlador.gestor.GestorUser;
import controlador.gestor.OnFragmentInteractionPanellListener;
import controlador.gestor.OnFragmentInteractionUserListener;
import controlador.server.delete.DeletePanellLoader;
import controlador.server.get.PanellsListLoader;
import controlador.server.post.NewPanellLoader;
import jordigomez.ioc.cat.escoltam.R;
import model.Icona;
import model.Panell;

/**
 * Activitat del client.
 * @see AppCompatActivity
 * @see FragmentActivity
 * @see OnFragmentInteractionUserListener
 * @author Jordi Gómez Lozano
 */
public class UserActivity extends FragmentActivity
        implements OnFragmentInteractionUserListener,
        OnFragmentInteractionPanellListener,
        LoaderManager.LoaderCallbacks<Bundle> {

    private final static String EXTRA_MESSAGE = "jordigomez.ioc.cat.comunicador.MESSAGE";
    private static final String URL_BUNDLE_KEY = "url";
    private static final String PANELLS_BUNDLE_KEY = "panells";
    private static final String RESPONSE_CODE_BUNDLE_KEY = "responseCode";
    private static final String SERVER_INFO_BUNDLE_KEY = "serverInfo";
    private static final String OPTION_BUNDLE_KEY = "opcio";
    private static final String EMAIL_BUNDLE_KEY = "email";
    private static final String PANELL_BUNDLE_KEY = "panell";
    private static final String TOKEN_BUNDLE_KEY = "token";
    private static final String NAME_NEW_PANELL = "Nou panell";
    public static final String ADD_OPTION = "add";
    private static final String LIST_PANELLS_OPTION = "list";
    private static final String ERROR_GET_PANELLS = "Error en obtenir la llista de panells";
    private static final String ERROR_ADD_PANELL = "Error en afegir el nou panell";
    private static final String DELETE_PANELL_INFO_BUNDLE_KEY = "delete_panell_info";
    private static final String DELETE_PANELL_OPTION = "delete";
    private static final String ID_PANELL_BUNDLE_KEY = "id_panell";
    private static final String ERROR_DELETE_PANELL = "Error en eliminar el panel del servidor";
    private static final String PANELL_SUCCESSFULLY_REMOVED = "Panell eliminat correctament";
    public static final String EDIT_TEXT_SAVED_INSTANCE = "edit_text";
    private int numPanells;
    private ViewPager viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;
    private UserToolbarFragment toolbarFragment;
//    private UserFavoritesFragment favoritesFragment;
    private UserControlFragment controlFragment;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private String role;
    private ImageButton screen;
    private EditText editTextCommunicator;
    private EditText panellTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        TextView textInfo = findViewById(R.id.textMostrarRol);

        Intent intent = getIntent();
//        textInfo.setText(intent.getStringExtra(LoginActivity.EXTRA_MESSAGE));
        role = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);

        //Fragments
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction =  fragmentManager.beginTransaction();

        toolbarFragment = UserToolbarFragment.newInstance(role);
//        favoritesFragment = UserFavoritesFragment.newInstance();
        controlFragment = UserControlFragment.newInstance();

        fragmentTransaction.add(R.id.toolbar_fragment_container, toolbarFragment);
//        fragmentTransaction.add(R.id.favorites_fragment_container, favoritesFragment);
        fragmentTransaction.add(R.id.control_fragment_container, controlFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        numPanells = GestorUser.getNumPanells();
        Log.i("Info", "numPanells: "+ numPanells);
        // Instantiate a ViewPager2 and a PagerAdapter.
        setUpViewPager();

        if(getSupportLoaderManager().getLoader(0)!=null){
            getSupportLoaderManager().initLoader(0,null,this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

//        editTextCommunicator = findViewById(R.id.appCompatEditText);
        screen = findViewById(R.id.button_screen);
        screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPanell();
            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(this);
//        editTextCommunicator.setText(gestorSharedPreferences.getEtitTextContent());
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(this);
//        gestorSharedPreferences.setEditTextContent(editTextCommunicator.getText().toString());
//    }

//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        savedInstanceState.putString(EDIT_TEXT_SAVED_INSTANCE, "hola");
//        super.onSaveInstanceState(savedInstanceState);
//    }
//
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        editTextCommunicator.setText(savedInstanceState.getString(EDIT_TEXT_SAVED_INSTANCE));
//    }

    private void setUpViewPager() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        pagerAdapter = new UserActivity.ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onToolbarButtonPressed(ImageButton button) {
//        if(button.getId() == R.id.button_screen){

//                addPanell();

//        }
    }

    public void addPanell(){
        if(GestorUser.containsPanell()){
            displayToast("Acaba d'editar el nou panell creat");

        }else{
            int position = GestorUser.getNumPanells()+1;
            pagerAdapter.addView(GestorUser.newPanell(position));
            updateViewPager(position);
        }
    }

    public void deletePanell(){
        int position = viewPager.getCurrentItem();
        pagerAdapter.removeView(position);
        updateViewPager(position - 1);
    }

    @Override
    public void onPanellButtonPressed(ImageButton optionsButton, EditText titleEditText, int idPanell) {
        if ((int) optionsButton.getTag() == R.drawable.ic_action_check) {

            titleEditText.setFocusableInTouchMode(false);
            titleEditText.setFocusable(false);

            int num_panells = GestorUser.getNumPanells() - 1;

            GestorUser.getPanells()
                    .get(num_panells)
                    .setNom(titleEditText.getText().toString()
            );

            callAddPanellLoader(GestorUser.getPanells().get(num_panells));
            callGetPanellsLoader();

        } else if ((int) optionsButton.getTag() == R.drawable.ic_action_settings) {

            if(!titleEditText.getText().toString().equals(NAME_NEW_PANELL)){
                callDeletePanellLoader(idPanell);
                callGetPanellsLoader();
            }
        }
    }

    public void updateViewPager(int position){
        viewPager.setAdapter(null);
        pagerAdapter =new UserActivity.ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(position);
    }

    @NonNull
    @Override
    public Loader<Bundle> onCreateLoader(int id, @Nullable Bundle args) {
        String username ="";
        String panell ="";
        String token ="";
        String option ="";
        String url = "";
        int idPanell;

        if (args != null) {

            option = args.getString(OPTION_BUNDLE_KEY);

            switch (option) {
                case ADD_OPTION:

                    username = args.getString(EMAIL_BUNDLE_KEY);
                    panell = args.getString(PANELL_BUNDLE_KEY);
                    token = args.getString(TOKEN_BUNDLE_KEY);
                    return new NewPanellLoader(this, panell, username, token);

                case LIST_PANELLS_OPTION:

                    token = args.getString(TOKEN_BUNDLE_KEY);
                    url = args.getString(URL_BUNDLE_KEY);

                    return new PanellsListLoader(this, url, token);

                case DELETE_PANELL_OPTION:
                    Log.i("Info", "onCreate");
                    token = args.getString(TOKEN_BUNDLE_KEY);
                    idPanell = args.getInt(ID_PANELL_BUNDLE_KEY);
                    Log.i("Info", "onCreate: "+idPanell);
                    return new DeletePanellLoader(this, idPanell, token);
            }

        }

        return new PanellsListLoader(this, url, token);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Bundle> loader, Bundle data) {
        String panells ="";
        int idPanell;
        int responseCode;

        if(data.getString(OPTION_BUNDLE_KEY) != null) {

            switch (data.getString(OPTION_BUNDLE_KEY)) {
                case LIST_PANELLS_OPTION:
                    responseCode = data.getInt(RESPONSE_CODE_BUNDLE_KEY);

                    if (responseCode == HttpURLConnection.HTTP_OK) {

                        panells = data.getString(PANELLS_BUNDLE_KEY);
                        GestorUser.createObjectsFromObtainedData(panells);

                    } else {
                        displayToast(ERROR_GET_PANELLS);
                    }

                    break;
                case ADD_OPTION:
                    responseCode = data.getInt(RESPONSE_CODE_BUNDLE_KEY);

                    if (responseCode != HttpURLConnection.HTTP_CREATED) {
                        displayToast(ERROR_ADD_PANELL);
                    }
                    break;

                case DELETE_PANELL_OPTION:
                    responseCode = data.getInt(RESPONSE_CODE_BUNDLE_KEY);

                    if (responseCode == HttpURLConnection.HTTP_OK) {

                        deletePanell();

                        displayToast(PANELL_SUCCESSFULLY_REMOVED);

                    }else if(responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR){

                        displayToast(ERROR_DELETE_PANELL);
                    }
                    break;
            }
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Bundle> loader) {

    }

    /**
     * Metode per a fer la crida al LoadManager per afegir un nou panell al servidor
     * @param panell panell a afegir.
     */
    private void callAddPanellLoader(Panell panell){
        //Comprova la connexió i la informació introduide per l'usuari en l'EditText.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {

            GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(this);
            String username = gestorSharedPreferences.getEmail();
            String token = gestorSharedPreferences.getToken();

            Bundle queryBundle = new Bundle();
            queryBundle.putString(OPTION_BUNDLE_KEY, ADD_OPTION);
            queryBundle.putString(EMAIL_BUNDLE_KEY, username);
            queryBundle.putString(PANELL_BUNDLE_KEY, panell.toString());
            queryBundle.putString(TOKEN_BUNDLE_KEY, token);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
        }
    }

    /**
     * Metode per a fer la crida al LoadManager per eliminar un panell al servidor
     * @param idPanell id del panell a eliminar.
     */
    private void callDeletePanellLoader(int idPanell){
        //Comprova la connexió i la informació introduide per l'usuari en l'EditText.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {

            GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(this);
            String token = gestorSharedPreferences.getToken();

            Bundle queryBundle = new Bundle();
            queryBundle.putString(OPTION_BUNDLE_KEY, DELETE_PANELL_OPTION);
            queryBundle.putInt(ID_PANELL_BUNDLE_KEY, idPanell);
            queryBundle.putString(TOKEN_BUNDLE_KEY, token);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
        }
    }

    private void callGetPanellsLoader(){

        //Comprova la connexió i la informació introduide per l'usuari en l'EditText.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {

            GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(this);
            String username = gestorSharedPreferences.getEmail();
            String token = gestorSharedPreferences.getToken();

            Bundle queryBundle = new Bundle();
            queryBundle.putString(OPTION_BUNDLE_KEY, LIST_PANELLS_OPTION);
            queryBundle.putString(TOKEN_BUNDLE_KEY, token);
            queryBundle.putString(URL_BUNDLE_KEY, username);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
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
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            moveTaskToBack(true);
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    /**
     * Classe adaptador del ViewPager2
     * @see FragmentStateAdapter
     * @see ViewPager2
     * @author Jordi Gómez Lozano
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Log.i("Info", "posicio: "+ position);
            return new PanellFragment(position);
        }

        @Override
        public int getCount() {
            return GestorUser.getNumPanells();
        }

        public void removeView(int position) {
            GestorUser.getPanells().remove(position);
            notifyDataSetChanged();
        }

        public void addView(Panell panell){
            GestorUser.getPanells().add(panell);
            notifyDataSetChanged();
        }

    }

}