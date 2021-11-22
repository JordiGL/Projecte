package controlador.gestor;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import controlador.activity.AdministratorActivity;
import jordigomez.ioc.cat.escoltam.R;
import model.Role;
import model.Usuari;

/**
 * Classe gestora de l'administrador.
 * @author Jordi Gómez Lozano.
 */
public class GestorAdministrator {
    private static final String MALE = "MALE";
    private static final String FEMALE = "FEMALE";
    private static final String ERROR_VOICE = "Opció de rol incorrecte";
    private static final String ERROR_ROLE = "Opció de veu  incorrecta";
    private static final String ERROR_EMPTY_EMAIL = "Introdueix l'email";
    private static final String ERROR_EMPTY_ROLE_OR_VOICE = "Introdueix una de les opcions disponibles";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLES_JSON = "roles";
    private static final String ID_JSON = "id";
    private static final String NAME_JSON = "name";
    private static final String USERNAME_JSON = "username";
    private static final String VOICE_JSON = "voice";
    private static final String PASSWORD_JSON = "password";
    private static final String ENABLED_JSON = "enabled";
    private String cercadorText;
    private String error;


    public GestorAdministrator(){}

    public void setCercadorText(String cercadorText) {
        this.cercadorText = cercadorText;
    }

    public String getError() {
        return error;
    }

    /**
     * Comprova que el camp de l'email no estigui buit
     * @return true si no esta buit, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    public boolean emailChecker(){

        boolean correcte = true;

        if(cercadorText.trim().length() == 0){

            error = ERROR_EMPTY_EMAIL;
            correcte = false;

        }

        return correcte;
    }

    /**
     * Comprova que el rol entrat per l'admonistrador sigui correcte
     * @return true si es correcte, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    public boolean roleChecker(){

        boolean correcte = true;

        if(cercadorText.trim().length() == 0){

            error = ERROR_EMPTY_ROLE_OR_VOICE;
            correcte = false;

        } else if(!Arrays.asList(ROLE_ADMIN, ROLE_USER).contains(cercadorText.toUpperCase())) {

            error = ERROR_EMPTY_ROLE_OR_VOICE;
            correcte = false;
        }

        return correcte;
    }

    /**
     * Comprova que la veu entrada per l'administrador sigui correcte
     * @return true si es correcte, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    public boolean voiceChecker(){

        boolean correcte = true;

        if(cercadorText.trim().length() == 0) {

            error = ERROR_EMPTY_ROLE_OR_VOICE;
            correcte = false;

        } else if(!Arrays.asList(MALE, FEMALE).contains(cercadorText.toUpperCase())) {
            error = ERROR_EMPTY_ROLE_OR_VOICE;
            correcte = false;
        }


        return correcte;
    }

    /**
     * Omplim el List d'usuaris amb les dades obtingudes del servidor.
     * @param obtainedServerData dades rebudes del GET al servidor.
     * @return List d'usuaris.
     * @author Jordi Gómez Lozano.
     */
    public List<Usuari> createObjectsFromObtainedData(String obtainedServerData) {
        List<Usuari> usuarisObtinguts = new ArrayList<>();

        try {

            if(obtainedServerData != null) {

                String firstChar = String.valueOf(obtainedServerData.charAt(0));

                if(firstChar.equalsIgnoreCase("[")) {

                    JSONArray jsonArray = new JSONArray(obtainedServerData);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Role role = new Role(
                                jsonObject.getJSONArray(ROLES_JSON).getJSONObject(0).getInt(ID_JSON),
                                jsonObject.getJSONArray(ROLES_JSON).getJSONObject(0).getString(NAME_JSON)
                        );

                        Usuari usuari = new Usuari(
                                jsonObject.getString(USERNAME_JSON),
                                role,
                                jsonObject.getString(VOICE_JSON),
                                jsonObject.getString(PASSWORD_JSON),
                                jsonObject.getBoolean(ENABLED_JSON)
                        );

                        usuarisObtinguts.add(usuari);
                    }

                }else{

                    JSONObject jsonObject = new JSONObject(obtainedServerData);
                    Role role = new Role(
                            jsonObject.getJSONArray(ROLES_JSON).getJSONObject(0).getInt(ID_JSON),
                            jsonObject.getJSONArray(ROLES_JSON).getJSONObject(0).getString(NAME_JSON)
                    );

                    Usuari usuari = new Usuari(
                            jsonObject.getString(USERNAME_JSON),
                            role,
                            jsonObject.getString(VOICE_JSON),
                            jsonObject.getString(PASSWORD_JSON),
                            jsonObject.getBoolean(ENABLED_JSON)
                    );
                    usuarisObtinguts.add(usuari);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return usuarisObtinguts;
    }

    /**
     * Gestiona les opcions a mostrar a l'EditText.
     * @param position Posició de l'espinner.
     * @author Jordi Gómez Lozano.
     */
    public void dropDownOptions(int position, AutoCompleteTextView cercador, Context context) {
        ArrayAdapter<CharSequence> adapterET;
        switch (position) {
            case 0:
                cercador.setText("");
                cercador.setEnabled(false);
                break;
            case 1:
                cercador.setText("");
                cercador.setEnabled(true);
                break;
            case 2:
                cercador.setEnabled(true);
                cercador.setText("");
                //Autocomplete del cercador.
                adapterET = ArrayAdapter.createFromResource(context, R.array.autocomplete_voice_options, android.R.layout.simple_list_item_1);
                cercador.setAdapter(adapterET);
                cercador.showDropDown();
                cercador.setError(null);
                break;
            case 3:
                cercador.setEnabled(true);
                cercador.setText("");
                //Autocomplete del cercador.
                adapterET = ArrayAdapter.createFromResource(context, R.array.autocomplete_role_options, android.R.layout.simple_list_item_1);
                cercador.setAdapter(adapterET);
                cercador.showDropDown();
                cercador.setError(null);
                break;
            default:
                cercador.setEnabled(true);
                cercador.setText("");
                break;
        }
    }
}
