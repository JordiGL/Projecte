package controlador.gestor;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private static final String ICONA_FOTO_JSON = "foto";
    private static final String ICONA_ID_JSON = "id";
    private static List<Panell> mPanells;
    private static int fileIcons = 3;
    private static String veu;

    public GestorUser(List<Panell> mPanells) {
        GestorUser.mPanells = mPanells;
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
                    JSONObject usuariJSON =  jsonArray.getJSONObject(0).getJSONObject("usuari");
                    veu = usuariJSON.getString("voice");

                    //Panell predefinit
                    getPanellPredefinedIfExists(jsonArray);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        icones = new ArrayList<>();

                        //Obtenim l'array d'icones.
                        JSONArray iconesJsonList = jsonArray.getJSONObject(i).getJSONArray(ICONES_JSON);

                        //Ferm la cerca de les icones, les creem i les afegim a la llista.
                        for (int j = 0; j < iconesJsonList.length(); j++){

                            icones.add(new Icona(
                                    iconesJsonList.getJSONObject(j).getString(ICONA_NOM_JSON),
                                    iconesJsonList.getJSONObject(j).getInt(ICONA_POSICIO_JSON),
                                    Base64.decode(iconesJsonList.getJSONObject(j).getString(ICONA_FOTO_JSON), Base64.DEFAULT),
                                    iconesJsonList.getJSONObject(j).getInt(ICONA_ID_JSON))
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
        Collections.sort(mPanells, new PanellPositionComparator());
        return mPanells;
    }

    private static void getPanellPredefinedIfExists(JSONArray jsonArray) throws JSONException {
        List<Icona> icones;
        JSONObject usuariJSON =  jsonArray.getJSONObject(0).getJSONObject("usuari");
        JSONArray predefinedJsonList = usuariJSON.getJSONArray("panellPredefinits");

        if(predefinedJsonList.length() > 0){
            icones = new ArrayList<>();

            //Obtenim l'array d'icones.
            JSONArray iconesJsonList = predefinedJsonList.getJSONObject(0).getJSONArray(ICONES_JSON);

            //Ferm la cerca de les icones, les creem i les afegim a la llista.
            for (int j = 0; j < iconesJsonList.length(); j++){

                icones.add(new Icona(
                        iconesJsonList.getJSONObject(j).getString(ICONA_NOM_JSON),
                        iconesJsonList.getJSONObject(j).getInt(ICONA_POSICIO_JSON),
                        Base64.decode(iconesJsonList.getJSONObject(j).getString(ICONA_FOTO_JSON), Base64.DEFAULT),
                        iconesJsonList.getJSONObject(j).getInt(ICONA_ID_JSON))
                );
            }

            //Afegim el panell predefinit a la llista, utilitzo un constructor amb favorit=false.
            mPanells.add(new Panell(
                    predefinedJsonList.getJSONObject(0).getString(PANELL_NOM_JSON),
                    predefinedJsonList.getJSONObject(0).getInt(PANELL_POSICIO_JSON),
                    icones,
                    predefinedJsonList.getJSONObject(0).getInt(PANELL_ID_JSON)));
        }
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

    public static Panell newPanell(int position, String panellName){

        return new Panell(
                panellName,
                position,
                false,
                new ArrayList<Icona>()
        );
    }
    public static Panell getPanellPerId(int id){

        for(Panell panell: mPanells){
            if(panell.getId() == id){
                return panell;
            }
        }

        return new Panell();
    }

//Icones

    public static File createFile(Context context, ContentResolver resolver, Uri uri, String name){
        FileOutputStream out = null;
        InputStream stream = null;
        File file = null;

        try {
            if(uri != null){
                //Obtenim els bytes de la imatge.
                stream =   resolver.openInputStream(uri);
                byte[] imatgeEnBytes = getBytes(stream);
                //Creem la imatge.
                file = new File(context.getFilesDir(),name);

                out = new FileOutputStream(file);
                out.write(imatgeEnBytes);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(out != null){
                    out.close();
                    stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    public static File createFileByte(Context context, byte[] imageBytes, String name){
        FileOutputStream out = null;
        File file = null;
        try {

            //Creem la imatge.
            file = new File(context.getFilesDir(),name);

            out = new FileOutputStream(file);
            out.write(imageBytes);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(out != null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    public static Icona findIcona(int id){

        for(Panell panell: mPanells){
            for(Icona icona: panell.getIcones()){
                if(icona.getId() == id){
                    return icona;
                }
            }
        }

        return null;
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);

        }

        return byteBuffer.toByteArray();
    }

    public static int getFileIcons(){
        return fileIcons;
    }

    public static void setFileIcons(int number){
        fileIcons = number;
    }

    public static String getVeu(){
        return veu;
    }

    public static class PanellPositionComparator implements Comparator<Panell> {

        @Override
        public int compare(Panell one, Panell two) {
            return one.getPosicio() - two.getPosicio();
        }
    }
}
