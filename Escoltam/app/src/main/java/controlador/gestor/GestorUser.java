package controlador.gestor;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import model.Icona;
import model.Panell;


/**
 * Classe gestora dels Panells
 * @author Jordi Gómez Lozano
 */
public class GestorUser {
    private static final String ICONES_JSON = "icones";
    private static final String ICONA_NOM_JSON = "nom";
    private static final String ICONA_POSICIO_JSON = "posicio";
    private static final String PANELL_ID_JSON = "id";
    private static final String PANELL_NOM_JSON = "nom";
    private static final String PANELL_POSICIO_JSON = "posicio";
    private static final String PANELL_FAVORIT_JSON = "favorit";
    private static final String NEW_PANELL = "Nou panell";
    private static List<Panell> mPanells;
    private static int numPanells;


    public GestorUser(List<Panell> mPanells) {
        GestorUser.mPanells = mPanells;
        GestorUser.numPanells = mPanells.size();
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
                                icones,
                                jsonArray.getJSONObject(i).getInt(PANELL_ID_JSON)));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mPanells;
    }

    public static List<Panell> getPanells() {
        return mPanells;
    }

    public static void setPanells(List<Panell> mPanells) {
        GestorUser.mPanells = mPanells;
    }

    public static int getNumPanells() {
        if(GestorUser.mPanells != null){
            return GestorUser.mPanells.size();
        }
        return 0;
    }

    public static void setNumPanells(int num_panells) {
        GestorUser.numPanells = num_panells;
    }

    public static boolean containsPanell(){

        for(Panell panell: mPanells){
            if(panell.getNom().equals(NEW_PANELL)){
                return true;
            }
        }

        return false;
    }
}
