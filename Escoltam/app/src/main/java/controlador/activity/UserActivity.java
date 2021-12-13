package controlador.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;

import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
import controlador.server.put.EditPanellLoader;
import jordigomez.ioc.cat.escoltam.R;
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
        LoaderManager.LoaderCallbacks<Bundle>,
        PopupMenu.OnMenuItemClickListener{

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
    private static final String ADD_OPTION = "add";
    private static final String LIST_PANELLS_OPTION = "list";
    private static final String ERROR_GET_PANELLS = "Error en obtenir la llista de panells";
    private static final String ERROR_ADD_PANELL = "Error en afegir el nou panell";
    private static final String DELETE_PANELL_INFO_BUNDLE_KEY = "delete_panell_info";
    private static final String DELETE_PANELL_OPTION = "delete";
    private static final String ID_PANELL_BUNDLE_KEY = "id_panell";
    private static final String ERROR_DELETE_PANELL = "Error en eliminar el panel del servidor";
    private static final String PANELL_SUCCESSFULLY_REMOVED = "Panell eliminat correctament";
    private static final String EDIT_TEXT_SAVED_INSTANCE = "edit_text";
    private static final String DIALOG_TITLE = "Atenció";
    private static final String DIALOG_MESSAGE_DELETE = "Segur que vols eliminar el panell?";
    private static final String DIALOG_MESSAGE_EDIT = "Segur que vols editar el panell?";
    private static final String USER_INFO_EDIT = "Canvia el títol del panell";
    private static final String EDIT_PANELL_OPTION = "edit";
    private static final String PANELL_NOM_BUNDLE_KEY = "panell_name";
    private static final String PANELL_POSITION_BUNDLE_KEY = "panell_position";
    private static final String PANELL_FAVORITE_BUNDLE_KEY = "panell_favorite";
    private static final String PANELL_ID_BUNDLE_KEY = "panell_id";
    private static final String PANELL_SUCCESSFULLY_EDITED = "El panell s'ha editat correctament";
    private static final String ERROR_EDIT_PANELL = "El panell no es pot editar";
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
    private ImageButton optionsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        panellTitle = findViewById(R.id.titolPanell);
        optionsButton = findViewById(R.id.optionsPanell);
        optionsButton.setTag(R.drawable.ic_action_settings);

        registerForContextMenu(optionsButton);
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


        // Instanciar viewpager i adaptador d'aquest
        setUpViewPager();

        panellTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if (hasFocus) {

                    optionsButton.setImageResource(R.drawable.ic_action_check);
                    optionsButton.setTag(R.drawable.ic_action_check);
                } else {

                    optionsButton.setImageResource(R.drawable.ic_action_settings);
                    optionsButton.setTag(R.drawable.ic_action_settings);
                }
            }
        });


        if(getSupportLoaderManager().getLoader(0)!=null){
            getSupportLoaderManager().initLoader(0,null,this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(GestorUser.getNumPanells() > 0){
            panellTitle.setText(GestorUser.getPanells().get(0).getNom());
        }

        GestorUser.setEditTextFocusable(false, panellTitle);

        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((int)optionsButton.getTag() == R.drawable.ic_action_settings){
                    if(pagerAdapter.getCount() > 0){
                        openMoreMenuOptions(v);
                    }
                }else{
                    editDialog();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    GestorUser.setEditTextFocusable(false, panellTitle);
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
        pagerAdapter = new UserActivity.ScreenSlidePagerAdapter(getSupportFragmentManager(), GestorUser.getPanells());
        viewPager.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onToolbarButtonPressed(ImageButton button) {
//        if(button.getId() == R.id.button_screen){

//                addPanell();

//        }
    }

    @Override
    public void onPanellButtonPressed(ImageButton optionsButton, EditText titleEditText, int idPanell) {
        if ((int) optionsButton.getTag() == R.drawable.ic_action_check) {
        } else if ((int) optionsButton.getTag() == R.drawable.ic_action_settings) {
        }
    }

//    public void updateViewPager(){
//        viewPager.setAdapter(null);
//        pagerAdapter.notifyDataSetChanged();
//        pagerAdapter =new UserActivity.ScreenSlidePagerAdapter(getSupportFragmentManager(), GestorUser.getPanells());
//        pagerAdapter.notifyDataSetChanged();
//        viewPager.setAdapter(pagerAdapter);
//        pagerAdapter.notifyDataSetChanged();
//    }

    @NonNull
    @Override
    public Loader<Bundle> onCreateLoader(int id, @Nullable Bundle args) {
        String username ="";
        String panell ="";
        String token ="";
        String option ="";
        String url = "";
        String panellName = "";
        int panellPosition;
        int idPanell;
        boolean panellIsFavorite;


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

                    token = args.getString(TOKEN_BUNDLE_KEY);
                    idPanell = args.getInt(ID_PANELL_BUNDLE_KEY);
                    return new DeletePanellLoader(this, idPanell, token);

                case EDIT_PANELL_OPTION:

                    panellName = args.getString(PANELL_NOM_BUNDLE_KEY);
                    panellPosition = args.getInt(PANELL_POSITION_BUNDLE_KEY);
                    panellIsFavorite = args.getBoolean(PANELL_FAVORITE_BUNDLE_KEY);
                    idPanell = args.getInt(PANELL_ID_BUNDLE_KEY);
                    username = args.getString(EMAIL_BUNDLE_KEY);
                    token = args.getString(TOKEN_BUNDLE_KEY);
                    return new EditPanellLoader(this, panellName, panellPosition,
                            panellIsFavorite, idPanell, username, token);
            }

        }

        return new PanellsListLoader(this, url, token);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Bundle> loader, Bundle data) {
        String panells ="";
        int responseCode;

        if(data.getString(OPTION_BUNDLE_KEY) != null) {
            Log.i("Info", data.getString(OPTION_BUNDLE_KEY));
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
                    } else {
                        callGetPanellsLoader();
                    }
                    break;

                case DELETE_PANELL_OPTION:
                    responseCode = data.getInt(RESPONSE_CODE_BUNDLE_KEY);

                    if (responseCode == HttpURLConnection.HTTP_OK) {

                        int idPanell = data.getInt(ID_PANELL_BUNDLE_KEY);
                        deletePanell(idPanell);
                        displayToast(PANELL_SUCCESSFULLY_REMOVED);
                        callGetPanellsLoader();
                    }else if(responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR){

                        displayToast(ERROR_DELETE_PANELL);
                    }
                    break;

                case EDIT_PANELL_OPTION:

                    responseCode = data.getInt(RESPONSE_CODE_BUNDLE_KEY);
                    Log.i("Info", "OF responseCode: "+responseCode);
                    if (responseCode == HttpURLConnection.HTTP_CREATED) {
                        callGetPanellsLoader();
                        displayToast(PANELL_SUCCESSFULLY_EDITED);

                    }else if(responseCode == HttpURLConnection.HTTP_NOT_FOUND){
                        displayToast(ERROR_EDIT_PANELL);
                    }
                    break;
            }
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Bundle> loader) {}

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
     * @author Jordi Gómez Lozano
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
    /**
     * Metode per a fer la crida al LoadManager per obtenir els panells del servidor
     * @author Jordi Gómez Lozano
     */
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
     * Metode per a fer la crida al LoadManager per obtenir els panells del servidor
     * @author Jordi Gómez Lozano
     */
    private void callEditPanellLoader(Panell panell){

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
            queryBundle.putString(OPTION_BUNDLE_KEY, EDIT_PANELL_OPTION);
            queryBundle.putString(PANELL_NOM_BUNDLE_KEY, panell.getNom());
            queryBundle.putInt(PANELL_POSITION_BUNDLE_KEY, panell.getPosicio());
            queryBundle.putBoolean(PANELL_FAVORITE_BUNDLE_KEY, panell.isFavorit());
            queryBundle.putInt(PANELL_ID_BUNDLE_KEY, panell.getId());
            queryBundle.putString(EMAIL_BUNDLE_KEY, username);
            queryBundle.putString(TOKEN_BUNDLE_KEY, token);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
        }

    }

    public void addPanell(){

        int position = pagerAdapter.getCount()+1;
        Panell panell = GestorUser.newPanell(position);
        callAddPanellLoader(panell);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                pagerAdapter.addView();
                viewPager.setCurrentItem(pagerAdapter.getCount());
                panellTitle.setText(NAME_NEW_PANELL);
            }
        }, 500);
    }

    public void deletePanell(int idPanell){
        pagerAdapter.removeView(idPanell);
    }

    public void deleteDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(DIALOG_TITLE);
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
                            panellTitle.getText().clear();
                        }else{
                            panellTitle.setText(pagerAdapter.getCurrentPanell(viewPager.getCurrentItem()).getNom());
                        }
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

    public void editDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(DIALOG_TITLE);
        alert.setMessage(DIALOG_MESSAGE_EDIT);
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                Panell panell = pagerAdapter.getCurrentPanell(viewPager.getCurrentItem());
                panell.setNom(panellTitle.getText().toString());
                callEditPanellLoader(panell);

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pagerAdapter.addView();
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
     * PopupMenu per a mostrar les diferents opcions del botó.
     * @param view del component.
     * @author Jordi Gómez Lozano.
     */
    public void openMoreMenuOptions(View view) {

        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();

        inflater.inflate(R.menu.menu_panell_context, popup.getMenu());

        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.context_panell_edit:

                GestorUser.setEditTextFocusable(true, panellTitle);
                return true;

            case R.id.context_panell_delete:

                deleteDialog();

                return true;

            default:
                return super.onContextItemSelected(item);
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
     * Classe adaptador del ViewPager
     * @see FragmentStateAdapter
     * @see ViewPager2
     * @author Jordi Gómez Lozano
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
            return new PanellFragment(panellList.get(position));
        }

        @Override
        public int getCount() {
            return panellList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void removeView(int idPanell) {
            Log.i("Info", "Adapter:"+idPanell);
            Panell panellToDelete = new Panell();

            for(Panell panell: panellList){
                if(panell.getId() == idPanell){
                   panellToDelete = panell;
                }
            }
            panellList.remove(panellToDelete);
            notifyDataSetChanged();
        }

        public void addView(){
            panellList = GestorUser.getPanells();
            notifyDataSetChanged();
        }

        public Panell getCurrentPanell(int position){
            return panellList.get(position);
        }

    }

}