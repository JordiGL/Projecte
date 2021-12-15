package controlador.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import controlador.activity.AdminSettingsActivity;
import controlador.activity.AdministratorActivity;
import controlador.activity.LoginActivity;
import controlador.activity.UserSettingsActivity;
import controlador.gestor.GestorSharedPreferences;

import jordigomez.ioc.cat.escoltam.R;

/**
 * Classe del fragment que conté la barra de la pantalla de l'usuari.
 * @see Fragment
 * @author Jordi Gómez Lozano.
 */
public class UserToolbarFragment extends Fragment implements PopupMenu.OnMenuItemClickListener{
    private static final String ERROR = "Error";
    private final static String EXTRA_MESSAGE = "jordigomez.ioc.cat.comunicador.MESSAGE";
    private static final String ROLE_KEY = "role";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_USER = "ROLE_USER";
    private static String role;
    private View rootView;

    public UserToolbarFragment() {
    }

    public static UserToolbarFragment newInstance( String userRole){
        UserToolbarFragment userToolbarFragment = new UserToolbarFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ROLE_KEY, userRole);
        userToolbarFragment.setArguments(arguments);
        return userToolbarFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView =  inflater.inflate(R.layout.fragment_user_toolbar, container, false);
        final EditText editText = rootView.findViewById(R.id.appCompatEditText);
        final ImageButton settings = (ImageButton) rootView.findViewById(R.id.user_button_top_right);
        final ImageButton addPanel = rootView.findViewById(R.id.button_screen);

        editText.setShowSoftInputOnFocus(false);

        if (getArguments().containsKey(ROLE_KEY)) {
            role = getArguments().getString(ROLE_KEY);
        }

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMoreMenuOptions(v);
            }
        });

        return rootView;
    }

    /**
     * PopupMenu per a mostrar les diferents opcions del botó.
     * @param view del component.
     * @author Jordi Gómez Lozano.
     */
    public void openMoreMenuOptions(View view) {

        PopupMenu popup = new PopupMenu(rootView.getContext(), view);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();

        if(role.equals(ROLE_USER)){
            inflater.inflate(R.menu.menu_user_comunicador_context, popup.getMenu());
        }else{
            inflater.inflate(R.menu.menu_admin_comunicador_context, popup.getMenu());
        }

        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.context_settings_admin_comunicador:
                Intent intentAdmin = new Intent(rootView.getContext(), AdminSettingsActivity.class);
                rootView.getContext().startActivity(intentAdmin);
                return true;

            case R.id.context_settings_user_comunicador:
                Intent intentUser = new Intent(rootView.getContext(), UserSettingsActivity.class);
                rootView.getContext().startActivity(intentUser);
                return true;

            case R.id.context_administracio_admin_comunicador:

                Intent intentComunicador = new Intent(rootView.getContext(), AdministratorActivity.class);
                intentComunicador.putExtra(EXTRA_MESSAGE, ROLE_ADMIN);
                rootView.getContext().startActivity(intentComunicador);
                return true;

            case R.id.context_logout_admin_comunicador:
            case R.id.context_logout_user_comunicador:
                GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(rootView.getContext());
                gestorSharedPreferences.deleteData();
                Intent intentLogin = new Intent(rootView.getContext(), LoginActivity.class);
                rootView.getContext().startActivity(intentLogin);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
}