package controlador.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import android.text.InputType;
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
import controlador.activity.UserActivity;
import controlador.activity.UserSettingsActivity;
import controlador.gestor.GestorAdministrator;
import controlador.gestor.GestorSharedPreferences;
import controlador.gestor.OnFragmentInteractionListener;
import jordigomez.ioc.cat.escoltam.R;

/**
 * Classe del fragment que conté la barra de la pantalla de l'usuari.
 * @see Fragment
 * @author Jordi Gómez Lozano.
 */
public class UserToolbarFragment extends Fragment implements PopupMenu.OnMenuItemClickListener{
    private final static String EXTRA_MESSAGE = "jordigomez.ioc.cat.comunicador.MESSAGE";
    public static final String ROLE_KEY = "role";
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

        editText.setShowSoftInputOnFocus(false);
        editText.setInputType(InputType.TYPE_NULL);
        editText.setFocusable(false);

        if (getArguments().containsKey(ROLE_KEY)) {
            role = getArguments().getString(ROLE_KEY);
        }

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(role.equals("ROLE_USER")){
                    Intent intent = new Intent(rootView.getContext(), UserSettingsActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(rootView.getContext(), AdminSettingsActivity.class);
                    startActivity(intent);
                }

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

        if(role.equals("ROLE_USER")){
            inflater.inflate(R.menu.menu_user_comunicador_context, popup.getMenu());
        }else{
            inflater.inflate(R.menu.menu_admin_comunicador_context, popup.getMenu());
        }

        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.context_settings:
                Intent intentAdmin = new Intent(rootView.getContext(), AdminSettingsActivity.class);
                startActivity(intentAdmin);
                return true;

            case R.id.context_comunicador:

                Intent intentComunicador = new Intent(rootView.getContext(), UserActivity.class);
                intentComunicador.putExtra(EXTRA_MESSAGE, "ROLE_ADMIN");
                startActivity(intentComunicador);
                return true;

            case R.id.context_logout:

                GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(rootView.getContext());
                gestorSharedPreferences.deleteData();
                Intent intentLogin = new Intent(rootView.getContext(), LoginActivity.class);
                startActivity(intentLogin);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

}