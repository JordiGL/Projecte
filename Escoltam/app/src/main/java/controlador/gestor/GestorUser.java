package controlador.gestor;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Icona;
import model.Panell;
import model.Role;
import model.Usuari;

//CREAR CLASEE STATIC?


public class GestorUser {
    public static final String ICONES_JSON = "icones";
    public static final String ICONA_NOM_JSON = "nom";
    public static final String ICONA_POSICIO_JSON = "posicio";
    public static final String PANELL_NOM_JSON = "nom";
    public static final String PANELL_POSICIO_JSON = "posicio";
    public static final String PANELL_FAVORIT_JSON = "favorit";
    public static List<Panell> mPanells;


    public GestorUser(List<Panell> mPanells) {
        this.mPanells = mPanells;
    }

    /**
     * Omplim el List de panells amb les dades obtingudes del servidor.
     * @param obtainedServerData dades rebudes del GET al servidor.
     * @return List d'usuaris.
     * @author Jordi Gómez Lozano.
     */
    public static List<Panell> createObjectsFromObtainedData(String obtainedServerData) {
        mPanells = new ArrayList<>();
        List<Icona> icones;
        Log.i("Info", "JSON: "+obtainedServerData);

        try {

            if(obtainedServerData != null) {

                String firstChar = String.valueOf(obtainedServerData.charAt(0));

                if(firstChar.equalsIgnoreCase("[")) {

                    JSONArray jsonArray = new JSONArray(obtainedServerData);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        icones = new ArrayList<>();

                        //Obtenim l'array d'icones.
                        JSONArray iconesJsonList = jsonArray.getJSONObject(i).getJSONArray(ICONES_JSON);

                        //Ferm la cerca de les icones, les creem i les afegim a la llista.
                        for (int j = 0; j < iconesJsonList.length(); j++){

                            icones.add(new Icona(
                                    iconesJsonList.getJSONObject(j).getString(ICONA_NOM_JSON),
                                    iconesJsonList.getJSONObject(j).getInt(ICONA_POSICIO_JSON))
                            );
                        }

                        //Afegim el panell a la llista
                        mPanells.add(new Panell(
                                jsonArray.getJSONObject(i).getString(PANELL_NOM_JSON),
                                jsonArray.getJSONObject(i).getInt(PANELL_POSICIO_JSON),
                                jsonArray.getJSONObject(i).getBoolean(PANELL_FAVORIT_JSON),
                                icones));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mPanells;
    }
}
