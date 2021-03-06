package controlador.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.List;

import controlador.fragment.PanellFragment;
import controlador.fragment.UserControlFragment;
import controlador.fragment.UserToolbarFragment;
import controlador.gestor.GestorException;
import controlador.gestor.GestorSharedPreferences;
import controlador.gestor.GestorText;
import controlador.gestor.GestorUser;
import controlador.gestor.OnFragmentInteractionPanellListener;
import controlador.gestor.OnFragmentInteractionUserControlListener;
import controlador.gestor.OnFragmentInteractionUserToolbarListener;
import controlador.server.delete.DeleteIconaLoader;
import controlador.server.delete.DeletePanellLoader;
import controlador.server.get.PanellsListLoader;
import controlador.server.post.NewIconLoader;
import controlador.server.post.NewPanellLoader;
import controlador.server.post.TranslatorLoader;
import controlador.server.put.EditIconaLoader;
import controlador.server.put.EditPanellLoader;
import jordigomez.ioc.cat.escoltam.R;
import model.Icona;
import model.Panell;
import model.Reproductor;
import model.Traductor;

/**
 * FragmentActivity del comunicador.
 * @see FragmentActivity
 * @see OnFragmentInteractionPanellListener
 * @see LoaderManager
 * @see PopupMenu
 * @author Jordi G??mez Lozano
 */
public class UserActivity extends FragmentActivity
        implements OnFragmentInteractionPanellListener,
        OnFragmentInteractionUserControlListener,
        OnFragmentInteractionUserToolbarListener,
        LoaderManager.LoaderCallbacks<Bundle>,
        PopupMenu.OnMenuItemClickListener{
    private final static String EXTRA_MESSAGE = "jordigomez.ioc.cat.comunicador.MESSAGE";
    private static final int PICK_IMAGE = 1;
    private static final String URL_BUNDLE_KEY = "url";
    private static final String EDIT_TEXT_SAVED_INSTANCE = "text_content";
    private static final String PANELLS_BUNDLE_KEY = "panells";
    private static final String RESPONSE_CODE_BUNDLE_KEY = "responseCode";
    private static final String OPTION_BUNDLE_KEY = "opcio";
    private static final String EMAIL_BUNDLE_KEY = "email";
    private static final String PANELL_BUNDLE_KEY = "panell";
    private static final String TOKEN_BUNDLE_KEY = "token";
    private static final String CREATE_NEW_PANELL_OPTION = "add";
    private static final String CREATE_ICONA_OPTION = "create_icona";
    private static final String LIST_PANELLS_OPTION = "list";
    private static final String ERROR_GET_PANELLS = "Error en obtenir la llista de panells";
    private static final String ERROR_ADD_PANELL = "Error en afegir el nou panell";
    private static final String ID_PANELL_BUNDLE_KEY = "id_panell";
    private static final String ERROR_DELETE_PANELL = "Error en eliminar el panel del servidor";
    private static final String PANELL_SUCCESSFULLY_REMOVED = "Panell eliminat correctament";
    private static final String DIALOG_DELETE_PANELL_TITLE = "Atenci??";
    private static final String DIALOG_MESSAGE_DELETE = "Segur que vols eliminar el panell?";
    private static final String EDIT_PANELL_OPTION = "edit";
    private static final String PANELL_NAME_BUNDLE_KEY = "panell_name";
    private static final String PANELL_POSITION_BUNDLE_KEY = "panell_position";
    private static final String PANELL_FAVORITE_BUNDLE_KEY = "panell_favorite";
    private static final String PANELL_ID_BUNDLE_KEY = "panell_id";
    private static final String PANELL_SUCCESSFULLY_EDITED = "El panell s'ha editat correctament";
    private static final String ERROR_EDIT_PANELL = "El panell no es pot editar";
    private static final String ICON_NAME_BUNDLE_KEY = "icon_name";
    private static final String ICON_POSITION_BUNDLE_KEY = "icon_position";
    private static final String FILE_NAME_BUNDLE_KEY = "file_name";
    private static final String EDIT_ICONA_OPTION = "edit_icona";
    private static final String ICON_ID_BUNDLE_KEY = "icon_id";
    private static final String DIALOG_CREATE_ICONA_TITLE = "Crear icona";
    private static final String DIALOG_CREATE_ICONA_INFO = "Selecciona la imatge de la icona";
    private static final String DIALOG_EDIT_ICONA_TITLE = "Editar icona";
    private static final String ERROR_ADD_ICONA = "La icona no s'ha afegit al servidor";
    private static final String ERROR_EDIT_ICONA = "La icona no s'ha editat";
    private static final String DELETE_PANELL_OPTION = "delete_panell";
    private static final String DELETE_ICONA_OPTION = "delete_icona";
    private static final String ERROR_DELETE_ICONA = "Error en eliminar la icona en el servidor";
    private static final String DIALOG_CREATE_PANELL_TITLE = "Crear panell";
    private static final String DIALOG_EDIT_PANELL_TITLE = "Editar panell";
    private static final String ERROR_NO_ICON_TEXT = "No has introduit el text de la icona";
    private static final String INTENT_TYPE = "image/*";
    private static final String DIALOG_MODIFY_ROW_ICONS_TITLE = "Modificar icones per fila";
    private static final String SUB_BUNDLE_KEY = "subscription";
    private static final String LOCATION_BUNDLE_KEY = "location";
    private static final String TEXT_BUNDLE_KEY = "text";
    private static final String TRANSLATE_TEXT_OPTION = "translate_text";
    private static final String TRANSLATED_TEXT_BUNDLE_KEY = "translated";
    private static final String DIALOG_FAVORITE_TITLE = "Vols aquest panell com a favorit?";
    private ViewPager viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;
    private UserToolbarFragment toolbarFragment;
    private UserControlFragment controlFragment;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private String role;
    private TextView panellTitle;
    private ImageButton optionsButton;
    private ImageButton favoriteButton;
    private Uri uriIconImage = null;
    private Reproductor reproductor;

    private Traductor translator;
    private String translatedText;
    private boolean translatorEnabled;
    private int panellFavoritPosition;
    private GestorUser gestorUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        panellTitle = findViewById(R.id.titolPanell);

        favoriteButton = findViewById(R.id.favoritePanell);
        optionsButton = findViewById(R.id.optionsPanell);
        registerForContextMenu(optionsButton);

        gestorUser = new GestorUser();

        Intent intent = getIntent();
        role = intent.getStringExtra(EXTRA_MESSAGE);

        //Fragments
        setUpFragmentManager();

        // Instanciar viewpager i adaptador d'aquest
        setUpViewPager();

        if(gestorUser.getNumPanells() > 0){
            panellTitle.setText(gestorUser.getPanells().get(0).getNom());
        }

        //Inicialitzem el reproductor per a controlar aquest.
        setUpReproductor();

        translator = new Traductor();
        translatorEnabled = false;

        if(getSupportLoaderManager().getLoader(0)!=null){
            getSupportLoaderManager().initLoader(0,null,this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpPanellListeners();

        gestorUser.setUpPanellFavoritePosition();
        panellFavoritPosition = gestorUser.getPanellFavoritePosition();
    }

//SetUps
    /**
     * M??tode d'administraci?? dels fragments
     * @author Jordi Gomez Lozano
     */
    public void setUpFragmentManager(){

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction =  fragmentManager.beginTransaction();

        toolbarFragment = UserToolbarFragment.newInstance(role);
        controlFragment = UserControlFragment.newInstance();

        fragmentTransaction.add(R.id.toolbar_fragment_container, toolbarFragment);
        fragmentTransaction.add(R.id.control_fragment_container, controlFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * M??tode inicialitzador i configuraci?? del reproductor de so
     * @author Jordi Gomez Lozano
     */
    public void setUpReproductor(){

        String veu = gestorUser.getVeu();

        try {
            reproductor = new Reproductor(veu, this);
        } catch (GestorException e) {
            e.printStackTrace();
        }
    }

    /**
     * M??tode per a inicialitzar el ViewPager
     * @author Jordi Gomez Lozano
     */
    private void setUpViewPager() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        pagerAdapter = new UserActivity.ScreenSlidePagerAdapter(getSupportFragmentManager(), gestorUser.getPanells());
        viewPager.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();
    }

    /**
     * M??tode dels listeners dels viws d'aquest FragmentActivity
     * @author Jordi Gomez Lozano
     */
    public void setUpPanellListeners(){

        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pagerAdapter.getCount() > 0){
                    openPanellMenuOptions(v);
                }
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(panellFavoritPosition != -1) {
                    viewPager.setCurrentItem(panellFavoritPosition);
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                panellTitle.setText(pagerAdapter.getCurrentPanell(position).getNom());
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

//Estat i control de l'aplicaci??
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            try {
                //Obtenim la imatge seleccionada per l'usuari.
                uriIconImage = data.getData();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GestorText.refreshEditText();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(EDIT_TEXT_SAVED_INSTANCE, GestorText.getText());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String[] textWords = savedInstanceState.getString(EDIT_TEXT_SAVED_INSTANCE).split(" ");

        for(String word: textWords){
            GestorText.getList().add(word + " ");
        }

        GestorText.refreshEditText();
    }

//Loaders
    @NonNull
    @Override
    public Loader<Bundle> onCreateLoader(int id, @Nullable Bundle args) {
        String username = "";
        String panell = "";
        String token = "";
        String option = "";
        String url = "";
        String panellName = "";
        String iconName = "";
        String fileName = "";
        String sub = "";
        String text = "";
        String location = "";
        int iconPosition;
        int panellPosition;
        int idPanell;
        int idIcona;
        boolean panellIsFavorite;


        if (args != null) {

            option = args.getString(OPTION_BUNDLE_KEY);

            switch (option) {
                case CREATE_NEW_PANELL_OPTION:

                    username = args.getString(EMAIL_BUNDLE_KEY);
                    panell = args.getString(PANELL_BUNDLE_KEY);
                    token = args.getString(TOKEN_BUNDLE_KEY);
                    return new NewPanellLoader(this, panell, username, token);

                case LIST_PANELLS_OPTION:

                    token = args.getString(TOKEN_BUNDLE_KEY);
                    url = args.getString(URL_BUNDLE_KEY);

                    return new PanellsListLoader(this, url, token);

                case DELETE_PANELL_OPTION:

                    token = args.getString(TOKEN_BUNDLE_KEY);
                    idPanell = args.getInt(ID_PANELL_BUNDLE_KEY);
                    username = args.getString(EMAIL_BUNDLE_KEY);
                    return new DeletePanellLoader(this, idPanell, username, token);

                case EDIT_PANELL_OPTION:

                    panellName = args.getString(PANELL_NAME_BUNDLE_KEY);
                    panellPosition = args.getInt(PANELL_POSITION_BUNDLE_KEY);
                    panellIsFavorite = args.getBoolean(PANELL_FAVORITE_BUNDLE_KEY);
                    idPanell = args.getInt(PANELL_ID_BUNDLE_KEY);
                    username = args.getString(EMAIL_BUNDLE_KEY);
                    token = args.getString(TOKEN_BUNDLE_KEY);
                    return new EditPanellLoader(this, panellName, panellPosition,
                            panellIsFavorite, idPanell, username, token);

                case CREATE_ICONA_OPTION:
                    idPanell = args.getInt(PANELL_ID_BUNDLE_KEY);
                    iconName = args.getString(ICON_NAME_BUNDLE_KEY);
                    iconPosition = args.getInt(ICON_POSITION_BUNDLE_KEY);
                    fileName = args.getString(FILE_NAME_BUNDLE_KEY);
                    token = args.getString(TOKEN_BUNDLE_KEY);
                    return new NewIconLoader(this, idPanell, iconName,
                            iconPosition, fileName, token);

                case EDIT_ICONA_OPTION:
                    idIcona = args.getInt(ICON_ID_BUNDLE_KEY);
                    iconName = args.getString(ICON_NAME_BUNDLE_KEY);
                    iconPosition = args.getInt(ICON_POSITION_BUNDLE_KEY);
                    fileName = args.getString(FILE_NAME_BUNDLE_KEY);
                    token = args.getString(TOKEN_BUNDLE_KEY);
                    return new EditIconaLoader(this, idIcona, iconName,
                            iconPosition, fileName, token);

                case DELETE_ICONA_OPTION:
                    token = args.getString(TOKEN_BUNDLE_KEY);
                    idIcona = args.getInt(ICON_ID_BUNDLE_KEY);
                    return new DeleteIconaLoader(this, idIcona, token);

                case TRANSLATE_TEXT_OPTION:
                    sub = args.getString(SUB_BUNDLE_KEY);
                    text = args.getString(TEXT_BUNDLE_KEY);
                    location = args.getString(LOCATION_BUNDLE_KEY);
                    return new TranslatorLoader(this, text, sub, location);

            }

        }

        return new PanellsListLoader(this, url, token);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Bundle> loader, Bundle data) {
        String panells ="";
        String panellName = "";
        int idIcona;
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
                case CREATE_NEW_PANELL_OPTION:

                    responseCode = data.getInt(RESPONSE_CODE_BUNDLE_KEY);

                    if (responseCode != HttpURLConnection.HTTP_CREATED) {
                        displayToast(ERROR_ADD_PANELL);
                    } else {
                        callGetPanellsLoader();
                    }
                    break;

                case DELETE_PANELL_OPTION:
                    responseCode = data.getInt(RESPONSE_CODE_BUNDLE_KEY);

                    if (responseCode == HttpURLConnection.HTTP_OK) {

                        int idPanell = data.getInt(ID_PANELL_BUNDLE_KEY);
                        pagerAdapter.removePanelView(idPanell);
                        displayToast(PANELL_SUCCESSFULLY_REMOVED);

                        if(idPanell == 0){
                            panellFavoritPosition--;
                        }

                        callGetPanellsLoader();

                    }else if(responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR){

                        displayToast(ERROR_DELETE_PANELL);
                    }
                    break;

                case EDIT_PANELL_OPTION:

                    responseCode = data.getInt(RESPONSE_CODE_BUNDLE_KEY);

                    if (responseCode == HttpURLConnection.HTTP_CREATED) {

                        panellName = data.getString(PANELL_NAME_BUNDLE_KEY);
                        panellTitle.setText(panellName);
                        displayToast(PANELL_SUCCESSFULLY_EDITED);

                    }else if(responseCode == HttpURLConnection.HTTP_NOT_FOUND){
                        displayToast(ERROR_EDIT_PANELL);
                    }
                    break;

                case CREATE_ICONA_OPTION:

                    responseCode = data.getInt(RESPONSE_CODE_BUNDLE_KEY);

                    if (responseCode == HttpURLConnection.HTTP_CREATED) {
                        callGetPanellsLoader();
                        uriIconImage = null;
                    }else{
                        displayToast(ERROR_ADD_ICONA);
                    }
                    break;

                case EDIT_ICONA_OPTION:

                    responseCode = data.getInt(RESPONSE_CODE_BUNDLE_KEY);

                    if (responseCode == HttpURLConnection.HTTP_CREATED) {

                        callGetPanellsLoader();

                    }else{
                        displayToast(ERROR_EDIT_ICONA);
                    }
                    break;

                case DELETE_ICONA_OPTION:

                    responseCode = data.getInt(RESPONSE_CODE_BUNDLE_KEY);

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        idIcona = data.getInt(ICON_ID_BUNDLE_KEY);
                        Icona icona = gestorUser.findIcona(idIcona);
                        File file = new File(getFilesDir(),icona.getNom());

                        if(file.delete()){
                            displayToast("Icona borrada correctament");
                        }

                        callGetPanellsLoader();
                    }else if(responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR){

                        displayToast(ERROR_DELETE_ICONA);
                    }
                    break;

                case TRANSLATE_TEXT_OPTION:


                    try {
                        translatedText = GestorUser.getTranslatedText(data.getString(TRANSLATED_TEXT_BUNDLE_KEY));

                    } catch (GestorException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Bundle> loader) {}

//Loader Callers
    /**
     * M??tode per a fer la crida al LoadManager per afegir un nou panell al servidor
     * @param panell panell a afegir.
     * @author Jordi Gomez Lozano
     */
    private void callAddPanellLoader(Panell panell){
        //Comprova la connexi?? i la informaci?? introduide per l'usuari en l'EditText.
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
            queryBundle.putString(OPTION_BUNDLE_KEY, CREATE_NEW_PANELL_OPTION);
            queryBundle.putString(EMAIL_BUNDLE_KEY, username);
            queryBundle.putString(PANELL_BUNDLE_KEY, panell.toString());
            queryBundle.putString(TOKEN_BUNDLE_KEY, token);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
        }
    }

    /**
     * M??tode per a fer la crida al LoadManager per eliminar un panell al servidor
     * @param idPanell id del panell a eliminar.
     * @author Jordi G??mez Lozano
     */
    private void callDeletePanellLoader(int idPanell){
        //Comprova la connexi?? i la informaci?? introduide per l'usuari en l'EditText.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {

            GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(this);
            String token = gestorSharedPreferences.getToken();
            String username = gestorSharedPreferences.getEmail();

            Bundle queryBundle = new Bundle();
            queryBundle.putString(OPTION_BUNDLE_KEY, DELETE_PANELL_OPTION);
            queryBundle.putInt(ID_PANELL_BUNDLE_KEY, idPanell);
            queryBundle.putString(TOKEN_BUNDLE_KEY, token);
            queryBundle.putString(EMAIL_BUNDLE_KEY, username);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
        }
    }

    /**
     * M??tode per a fer la crida al LoadManager per eliminar una icona al servidor
     * @param idIcona id de la icona a eliminar.
     * @author Jordi G??mez Lozano
     */
    private void callDeleteIconaLoader(int idIcona){
        //Comprova la connexi?? i la informaci?? introduide per l'usuari en l'EditText.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {

            GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(this);
            String token = gestorSharedPreferences.getToken();

            Bundle queryBundle = new Bundle();
            queryBundle.putString(OPTION_BUNDLE_KEY, DELETE_ICONA_OPTION);
            queryBundle.putInt(ICON_ID_BUNDLE_KEY, idIcona);
            queryBundle.putString(TOKEN_BUNDLE_KEY, token);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
        }
    }

    /**
     * M??tode per a fer la crida al LoadManager per obtenir els panells del servidor
     * @author Jordi G??mez Lozano
     */
    private void callGetPanellsLoader(){

        //Comprova la connexi?? i la informaci?? introduide per l'usuari en l'EditText.
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
     * M??tode per a fer la crida al LoadManager per obtenir la traducci?? del text
     * @author Jordi G??mez Lozano
     */
    private void callGetTranslatedTextLoader(){

        //Comprova la connexi?? i la informaci?? introduide per l'usuari en l'EditText.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {
            String textToTranslate = GestorText.getText();

            Bundle queryBundle = new Bundle();
            queryBundle.putString(OPTION_BUNDLE_KEY, TRANSLATE_TEXT_OPTION);
            queryBundle.putString(TEXT_BUNDLE_KEY, textToTranslate);
            queryBundle.putString(SUB_BUNDLE_KEY, translator.getSubscriptionKey());
            queryBundle.putString(LOCATION_BUNDLE_KEY, translator.getLocation());
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
        }

    }

    /**
     * M??tode per a fer la crida al LoadManager per editar un panell
     * @param panell panell a editar.
     * @author Jordi G??mez Lozano
     */
    private void callEditPanellLoader(Panell panell){

        //Comprova la connexi?? i la informaci?? introduide per l'usuari en l'EditText.
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
            queryBundle.putString(OPTION_BUNDLE_KEY, EDIT_PANELL_OPTION);
            queryBundle.putString(PANELL_NAME_BUNDLE_KEY, panell.getNom());
            queryBundle.putInt(PANELL_POSITION_BUNDLE_KEY, panell.getPosicio());
            queryBundle.putBoolean(PANELL_FAVORITE_BUNDLE_KEY, panell.isFavorit());
            queryBundle.putInt(PANELL_ID_BUNDLE_KEY, panell.getId());
            queryBundle.putString(EMAIL_BUNDLE_KEY, username);
            queryBundle.putString(TOKEN_BUNDLE_KEY, token);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
        }

    }

    /**
     * M??tode per a fer la crida al LoadManager per a afegir una nova icona al servidor
     * @param idPanell id del panell de la icona.
     * @param icon icona a guardar.
     * @param fileName nom que ha de tenir l'arxiu.
     * @author Jordi G??mez Lozano
     */
    private void callNewIconLoader(int idPanell, Icona icon, String fileName){

        //Comprova la connexi?? i la informaci?? introduide per l'usuari en l'EditText.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {

            GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(this);
            String token = gestorSharedPreferences.getToken();

            Bundle queryBundle = new Bundle();
            queryBundle.putString(OPTION_BUNDLE_KEY, CREATE_ICONA_OPTION);
            queryBundle.putInt(PANELL_ID_BUNDLE_KEY, idPanell);
            queryBundle.putString(ICON_NAME_BUNDLE_KEY, icon.getNom());
            queryBundle.putInt(ICON_POSITION_BUNDLE_KEY, icon.getPosicio());
            queryBundle.putString(FILE_NAME_BUNDLE_KEY, fileName);
            queryBundle.putString(TOKEN_BUNDLE_KEY, token);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
        }
    }

    /**
     * M??tode per a fer la crida al LoadManager per editar una icona
     * @param icon icona a editar.
     * @param fileName nou nom de la icona.
     * @author Jordi G??mez Lozano
     */
    private void callEditIconLoader(Icona icon, String fileName){

        //Comprova la connexi?? i la informaci?? introduide per l'usuari en l'EditText.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {

            GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(this);
            String token = gestorSharedPreferences.getToken();

            Bundle queryBundle = new Bundle();
            queryBundle.putString(OPTION_BUNDLE_KEY, EDIT_ICONA_OPTION);
            queryBundle.putInt(ICON_ID_BUNDLE_KEY, icon.getId());
            queryBundle.putString(ICON_NAME_BUNDLE_KEY, icon.getNom());
            queryBundle.putInt(ICON_POSITION_BUNDLE_KEY, icon.getPosicio());
            queryBundle.putString(FILE_NAME_BUNDLE_KEY, fileName);
            queryBundle.putString(TOKEN_BUNDLE_KEY, token);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
        }
    }

    /**
     * M??tode per a gestionar el nou panell
     * @param panellName nom del panell a guardar.
     * @author Jordi G??mez Lozano
     */
    public void addNewPanell(String panellName){

        int position = pagerAdapter.getCount()+1;
        Panell panell = gestorUser.newPanell(position, panellName);
        callAddPanellLoader(panell);

        final Handler handler = new Handler(Looper.getMainLooper());
        refreshView(handler,500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(pagerAdapter.getCount() > 0){
                    panellTitle.setText(pagerAdapter.getCurrentPanell(viewPager.getCurrentItem()).getNom());
                    viewPager.setCurrentItem(pagerAdapter.getCount());
                }
            }
        }, 500);
    }

    /**
     * M??tode per a gestionar una nova icona
     * @param file imatge de la icona a guardar.
     * @param iconName nom de la icona a guardar.
     * @author Jordi G??mez Lozano
     */
    private void addNewIcon(File file, String iconName){
        Icona icona;
        int posicio = viewPager.getCurrentItem();

        Panell panell = pagerAdapter.getCurrentPanell(posicio);

        if(file == null){
            icona = new Icona(iconName, panell.getIcones().size()+1);
            callNewIconLoader(panell.getId(), icona, "");
        }else{
            icona = new Icona(file.getName(), panell.getIcones().size()+1);
            callNewIconLoader(panell.getId(), icona, file.getName());
        }

        pagerAdapter.getCurrentPanell(viewPager.getCurrentItem()).getIcones().add(icona);
        final Handler handler = new Handler(Looper.getMainLooper());
        refreshView(handler,750);

    }

    /**
     * M??tode per a gestionar l'edici?? d'una icona.
     * @param icona panell a editar.
     * @param file panell a editar.
     * @author Jordi G??mez Lozano
     */
    private void editIcon(Icona icona, File file){
        callEditIconLoader(icona, file.getName());
        pagerAdapter.notifyDataSetChanged();
        final Handler handler = new Handler(Looper.getMainLooper());
        refreshView(handler,750);
    }

//Dialogs
    /**
     * M??tode per a fer i mostrar el dialog per a afegir un nou panell
     * @author Jordi G??mez Lozano
     */
    private void addNewPanellDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(DIALOG_CREATE_PANELL_TITLE);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_new_panell,
                (ViewGroup) findViewById(android.R.id.content), false);


        final EditText inputText = (EditText) viewInflated.findViewById(R.id.textPanellInput);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                addNewPanell(inputText.getText().toString());

            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /**
     * M??tode per a fer i mostrar el dialog per a afegir una nova icona
     * @author Jordi G??mez Lozano
     */
    private void addNewIconDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(DIALOG_CREATE_ICONA_TITLE);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_new_icon,
                (ViewGroup) findViewById(android.R.id.content), false);

        final EditText inputText = (EditText) viewInflated.findViewById(R.id.textIconInput);
        final Button imageButton = (Button) viewInflated.findViewById(R.id.btnSelectImage);
        builder.setView(viewInflated);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent implicit per a seleccionar la imatge.
                Intent intent = new Intent();
                intent.setType(INTENT_TYPE);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(
                        intent,
                        DIALOG_CREATE_ICONA_INFO),
                        PICK_IMAGE);
            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String iconName = inputText.getText().toString();

                if(!iconName.matches("")){
                    dialog.dismiss();
                    File iconFile = gestorUser.createFile(
                            UserActivity.this,
                            getContentResolver(),
                            uriIconImage,
                            iconName);
                    addNewIcon(iconFile, iconName);
                } else {
                    displayToast(ERROR_NO_ICON_TEXT);
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /**
     * M??tode per a fer i mostrar el dialog per a editar una icona
     * @param idIcona l'id de la icona a editar.
     * @author Jordi G??mez Lozano
     */
    private void editIconDialog(int idIcona){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(DIALOG_EDIT_ICONA_TITLE);
        Icona icona = gestorUser.findIcona(idIcona);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_new_icon,
                (ViewGroup) findViewById(android.R.id.content), false);

        final EditText inputText = (EditText) viewInflated.findViewById(R.id.textIconInput);
        final Button imageButton = (Button) viewInflated.findViewById(R.id.btnSelectImage);

        if (icona != null) {
            inputText.setText(icona.getNom());
        }

        builder.setView(viewInflated);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent implicit per a seleccionar la imatge.
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(
                        intent,
                        DIALOG_CREATE_ICONA_INFO),
                        PICK_IMAGE);
            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                File iconFile = null;
                String newIconName = inputText.getText().toString();

                if(icona != null) {

                    if (!newIconName.matches("")) {

                        icona.setNom(newIconName);
                    }

                    if (uriIconImage != null) {

                        iconFile = gestorUser.createFile(
                                UserActivity.this,
                                getContentResolver(),
                                uriIconImage,
                                icona.getNom());
                    } else {

                        iconFile = gestorUser.createFileByte(
                                UserActivity.this,
                                icona.getImatge(),
                                icona.getNom());
                    }

                    if (iconFile != null) {
                        editIcon(icona, iconFile);
                    } else {
                        displayToast(ERROR_EDIT_ICONA);
                    }
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /**
     * M??tode per a fer i mostrar el dialog per a eliminar un panell
     * @author Jordi G??mez Lozano
     */
    private void deletePanellDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(DIALOG_DELETE_PANELL_TITLE);
        alert.setMessage(DIALOG_MESSAGE_DELETE);
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                int idPanell = pagerAdapter.getCurrentPanell(viewPager.getCurrentItem()).getId();

                callDeletePanellLoader(idPanell);

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if(pagerAdapter.getCount() == 1){
                            panellTitle.setText(pagerAdapter.getCurrentPanell(0).getNom());
                        } else if(pagerAdapter.getCount() == 0){
                            panellTitle.setText("");
                        }else{
                            panellTitle.setText(pagerAdapter.getCurrentPanell(viewPager.getCurrentItem()).getNom());
                        }
                        gestorUser.setUpPanellFavoritePosition();
                    }
                }, 250);


            }
        });
        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    /**
     * M??tode per a fer i mostrar el dialog per a assignar un panell com a favorit
     * @author Jordi G??mez Lozano
     */
    private void panellFavoriteDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(DIALOG_FAVORITE_TITLE);

        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                panellFavoritPosition = viewPager.getCurrentItem();
                Panell previousFavoritePanell = gestorUser.getPanellFavorite();
                Panell newFavoritePanell = pagerAdapter.getCurrentPanell(viewPager.getCurrentItem());
                final Handler handler = new Handler(Looper.getMainLooper());

                if(previousFavoritePanell != null){
                    if (previousFavoritePanell.getId() != newFavoritePanell.getId()) {
                        previousFavoritePanell.setFavorit(false);
                        callEditPanellLoader(previousFavoritePanell);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                newFavoritePanell.setFavorit(true);
                                callEditPanellLoader(newFavoritePanell);
                                pagerAdapter.getCurrentPanell(viewPager.getCurrentItem()).setFavorit(true);
                            }
                        }, 100);
                    }
                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            newFavoritePanell.setFavorit(true);
                            callEditPanellLoader(newFavoritePanell);
                            pagerAdapter.getCurrentPanell(viewPager.getCurrentItem()).setFavorit(true);
                        }
                    }, 100);
                }

                refreshListData(handler, 150);
                refreshView(handler, 250);

            }
        });
        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    /**
     * M??tode per a fer i mostrar el dialog per a eliminar una icona
     * @param idIcona l'id de la icona a eliminar.
     * @author Jordi G??mez Lozano
     */
    private void deleteIconDialog(int idIcona){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(DIALOG_DELETE_PANELL_TITLE);
        alert.setMessage(DIALOG_MESSAGE_DELETE);

        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                callDeleteIconaLoader(idIcona);
                final Handler handler = new Handler(Looper.getMainLooper());
                refreshView(handler,500);
            }
        });
        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    /**
     * M??tode per a fer i mostrar el dialog per a editar un panell
     * @author Jordi G??mez Lozano
     */
    private void editPanellDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(DIALOG_EDIT_PANELL_TITLE);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_new_panell,
                (ViewGroup) findViewById(android.R.id.content), false);

        final EditText inputText = (EditText) viewInflated.findViewById(R.id.textPanellInput);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Panell panell = pagerAdapter.getCurrentPanell(viewPager.getCurrentItem());
                String newPanellName = inputText.getText().toString();

                if (!newPanellName.matches("")) {
                    panell.setNom(inputText.getText().toString());
                }

                callEditPanellLoader(panell);
                final Handler handler = new Handler(Looper.getMainLooper());
                refreshListData(handler,1000);
                refreshView(handler,1000);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /**
     * M??tode per a fer i mostrar el dialog per a canviar el tamany de les icones a partir
     * del nombre de icones per fila
     * @author Jordi G??mez Lozano
     */
    private void rowIconsNumberDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(DIALOG_MODIFY_ROW_ICONS_TITLE);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_icons_number,
                (ViewGroup) findViewById(android.R.id.content), false);
        final RadioGroup radioNumberGroup = viewInflated.findViewById(R.id.radio_number);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                int selectedId = radioNumberGroup.getCheckedRadioButtonId();

                switch(selectedId){
                    case R.id.radio_two:
                        gestorUser.setFileIcons(2);
                        break;
                    case R.id.radio_three:
                        gestorUser.setFileIcons(3);
                        break;
                    case R.id.radio_four:
                        gestorUser.setFileIcons(4);
                        break;
                    case R.id.radio_five:
                        gestorUser.setFileIcons(5);
                        break;
                    default:
                        break;
                }
                pagerAdapter.refreshAdapterView();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

//Menus
    /**
     * PopupMenu per a mostrar les diferents opcions del bot?? d'options del panell.
     * @param view del component.
     * @author Jordi G??mez Lozano.
     */
    public void openPanellMenuOptions(View view) {

        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();

        int position = viewPager.getCurrentItem();

        if(pagerAdapter.getCurrentPanell(position).getId() == 0){

            inflater.inflate(R.menu.menu_panell_predefined_context, popup.getMenu());
        } else {

            inflater.inflate(R.menu.menu_panell_context, popup.getMenu());
        }

        popup.show();
    }

    /**
     * PopupMenu per a mostrar les diferents opcions del bot?? del traductor.
     * @param view del component.
     * @author Jordi G??mez Lozano.
     */
    public void openTraductorMenuOptions(View view) {

        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();

        inflater.inflate(R.menu.menu_translator_context, popup.getMenu());


        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.context_panell_edit:

                editPanellDialog();
                return true;

            case R.id.context_panell_favorite:

                panellFavoriteDialog();
                return true;

            case R.id.context_panell_delete:

                deletePanellDialog();
                return true;

            case R.id.context_panell_icon_size:

                rowIconsNumberDialog();
                return true;

            case R.id.context_translator_enabled:

                try {
                    translatorEnabled = true;
                    reproductor.changeSynthesizer("en-GB", gestorUser.getVeu());
                } catch (GestorException e) {
                    e.printStackTrace();
                }
                return true;

            case R.id.context_translator_disabled:

                try {
                    translatorEnabled = false;
                    reproductor.changeSynthesizer("ca-ES", gestorUser.getVeu());
                } catch (GestorException e) {
                    e.printStackTrace();
                }

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onIconMenuItemPressed(@NonNull MenuItem menuItem, int idIcona) {
        switch (menuItem.getItemId()) {

            case R.id.context_icona_edit:

                editIconDialog(idIcona);
                break;

            case R.id.context_icona_delete:

                deleteIconDialog(idIcona);
                break;

            default:
                break;
        }
    }

//Fragment listeners
    @Override
    public void onControlButtonPressed(ImageButton imageButton) {

        switch (imageButton.getId()) {
            case R.id.button_play:

                String text = GestorText.getText();
                if (!text.isEmpty()) {

                    speechControl(text);
                }
                break;

            case R.id.button_pause:

                if (reproductor.getMediaPlayer().isPlaying()) {
                    reproductor.getMediaPlayer().pause();
                }
                break;

            case R.id.button_stop:

                try {
                    reproductor.stop();
                } catch (GestorException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onToolbarButtonPressed(ImageButton imageButton) {

        switch (imageButton.getId()) {

            case R.id.button_screen:

                addNewPanellDialog();
                break;

            case R.id.button_icon:

                addNewIconDialog();
                break;

            case R.id.button_delete_back:

                if(!GestorText.getList().isEmpty()){
                    GestorText.getList().removeLast();
                    GestorText.refreshEditText();
                }
                break;

            case R.id.button_delete_all:

                GestorText.getList().clear();
                GestorText.refreshEditText();
                break;

            case R.id.button_translator:

                openTraductorMenuOptions(imageButton);

                break;
        }
    }

//Control del reproductor
    /**
     * M??tode per a gestionar i controlar l'idioma del so a reproduir.
     * @param text text a reproduir per veu.
     * @author Jordi G??mez Lozano
     */
    public void speechControl( String text){

        if (translatorEnabled) {

            callGetTranslatedTextLoader();
            final Handler handler = new Handler(Looper.getMainLooper());

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    reproductor.playInEnglish(translatedText);
                }
            }, 1000);

        } else {

            final Handler handler = new Handler(Looper.getMainLooper());

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {

                    reproductor.playInCatalan(text);
                }
            }, 500);
        }
    }

//Utils

    /**
     * M??tode per a refrescar el ViewPager
     * @param handler per a controlar el temps en que es refrescara el view.
     * @param delayMillis temps d'espera per a efectuar l'acci??.
     * @author Jordi G??mez Lozano
     */
    public void refreshView(Handler handler, int delayMillis){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pagerAdapter.refreshAdapterView();

            }
        }, delayMillis);
    }

    /**
     * M??tode per a fer la crida al servidor i refrescar la llista de panells.
     * @param handler per a controlar el temps en que es refrescara el view.
     * @param delayMillis temps d'espera per a efectuar l'acci??.
     * @author Jordi G??mez Lozano
     */
    public void refreshListData(Handler handler, int delayMillis){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callGetPanellsLoader();
            }
        }, delayMillis);
    }

    /**
     * M??tode que mostra informaci?? per pantalla.
     * @param message missatge que es mostrara per pantalla.
     * @author Jordi G??mez Lozano.
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
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    /**
     * Inner class de l' adaptador del ViewPager
     * @see FragmentStateAdapter
     * @see ViewPager
     * @author Jordi G??mez Lozano
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        List<Panell> panellList;

        public ScreenSlidePagerAdapter(@NonNull FragmentManager fm, List<Panell> panellList) {
            super(fm);
            this.panellList = panellList;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return PanellFragment.newInstance(panellList.get(position));
        }

        @Override
        public int getCount() {
            return panellList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        /**
         * M??tode que elimina un panell del view segons el seu id.
         * @param idPanell del panell a eliminar
         * @author Jordi G??mez Lozano
         */
        public void removePanelView(int idPanell) {

            Panell panellToDelete = new Panell();

            for(Panell panell: panellList){
                if(panell.getId() == idPanell){
                   panellToDelete = panell;
                }
            }
            panellList.remove(panellToDelete);
            notifyDataSetChanged();
        }

        /**
         * M??tode que refresca el ViewPager
         * @author Jordi G??mez Lozano
         */
        public void refreshAdapterView(){
            panellList = gestorUser.getPanells();
            notifyDataSetChanged();
        }

        /**
         * M??tode que obte el panell segons la posici??
         * @param position del panell a la llista
         * @return el panell cercat
         * @author Jordi G??mez Lozano
         */
        public Panell getCurrentPanell(int position){
            return panellList.get(position);
        }

    }
}