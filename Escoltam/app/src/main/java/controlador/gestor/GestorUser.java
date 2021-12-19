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
 * Classe gestora dels Panells i les icones
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
    private static final String ERROR_JSON_TRANSLATOR = "Error a l'hora d'extreure la paraula traduida de l'arxiu JSON rebut";
    private static List<Panell> mPanells;
    private static int fileIcons = 3;
    private static String veu;
    private static int panellFavoritePosition;

    public GestorUser() {}

    public void setPanellFavoritPosition(int position){
        panellFavoritePosition = position;
    }

    public int getPanellFavoritePosition(){
        return panellFavoritePosition;
    }

    public String getVeu(){
        return veu;
    }

    public int getFileIcons(){
        return fileIcons;
    }

    public void setFileIcons(int number){
        fileIcons = number;
    }

    public List<Panell> getPanells() {
        return mPanells;
    }

//Panells

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

                JSONObject dataJSON = new JSONObject(obtainedServerData);
                veu = dataJSON.getString("voice");


                //Panell predefinit
                getPanellPredefinedIfExists(dataJSON);

                JSONArray jsonArrayPanells = dataJSON.getJSONArray("panells");
                for (int i = 0; i < jsonArrayPanells.length(); i++) {
                    icones = new ArrayList<>();

                    //Obtenim l'array d'icones.
                    JSONArray iconesJsonList = jsonArrayPanells.getJSONObject(i).getJSONArray(ICONES_JSON);

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
                            jsonArrayPanells.getJSONObject(i).getString(PANELL_NOM_JSON),
                            jsonArrayPanells.getJSONObject(i).getInt(PANELL_POSICIO_JSON),
                            jsonArrayPanells.getJSONObject(i).getBoolean(PANELL_FAVORIT_JSON),
                            icones,
                            jsonArrayPanells.getJSONObject(i).getInt(PANELL_ID_JSON)));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collections.sort(mPanells, new PanellPositionComparator());
        return mPanells;
    }

    /**
     * Obtenim el panell predefinit amb les dades obtingudes del servidor
     * @param jsonObject dades rebudes del GET al servidor.
     * @author Jordi Gómez Lozano.
     */
    private static void getPanellPredefinedIfExists(JSONObject jsonObject) throws JSONException {
        List<Icona> icones;

        JSONArray predefinedJsonList = jsonObject.getJSONArray("panellPredefinits");

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

    /**
     * Obtenim el numero de panells
     * @return un int amb el nombre de panells.
     * @author Jordi Gomez Lozano
     */
    public int getNumPanells() {
        if(GestorUser.mPanells != null){
            return GestorUser.mPanells.size();
        }
        return 0;
    }

    /**
     * Mètode per a crear un nou panell per defecte
     * @param position posicio del panell
     * @param panellName nom del panell
     * @return el panell creat
     * @author Jordi Gomez Lozano
     */
    public Panell newPanell(int position, String panellName){

        return new Panell(
                panellName,
                position,
                false,
                new ArrayList<Icona>()
        );
    }

    /**
     * Mètode per a obtenir la posicio del panell favorit
     * @author Jordi Gomez Lozano
     */
    public void setUpPanellFavoritePosition(){
        boolean control = true;

        for(int i = 0; i<mPanells.size(); i++){
            control = false;
            if(mPanells.get(i).isFavorit()){
                panellFavoritePosition = i;
            }
        }
        if(control){
            panellFavoritePosition = -1;
        }
    }

    /**
     * Mètode per a obtenir el panell favorit
     * @return el Panell favorit
     * @author Jordi Gomez Lozano
     */
    public Panell getPanellFavorite(){

        for(Panell panell: mPanells){
            if(panell.isFavorit()){
                return panell;
            }
        }
        return null;
    }

//Icones

    /**
     * Mètode per a crear el File de la imatge de la icona per a guardar al servidor.
     * @param context Context de l'aplicació
     * @param resolver el content resolver per a obtenir els bytes
     * @param uri localitzacio de la imatge
     * @param name nom que ha de tenir la imatge
     * @return un File de la imatge
     * @author Jordi Gomez Lozano
     */
    public File createFile(Context context, ContentResolver resolver, Uri uri, String name){
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

    /**
     * Mètode per a crear un file a partir dels bytes de la imatge de la icona.
     * @param context context de l'aplicació
     * @param imageBytes bytes de la imatge
     * @param name nom que ha de tenir la imatge
     * @return un File a partir dels bytes de la imatge.
     * @author Jordi Gomez Lozano
     */
    public File createFileByte(Context context, byte[] imageBytes, String name){
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

    /**
     * Mètode per aconseguir una icona a partir del seu id
     * @param id de la icona a cercar
     * @return la icona a cercar
     * @author Jordi Gomez Lozano
     */
    public Icona findIcona(int id){

        for(Panell panell: mPanells){
            for(Icona icona: panell.getIcones()){
                if(icona.getId() == id){
                    return icona;
                }
            }
        }

        return null;
    }

//Traductor
    /**
     * Mètode per a obtenir el text traduit a partir de les dades obtingudes del servidor
     * @param json un String en format JSON amb les dades del servidor
     * @return un String amb el text traduit.
     * @throws GestorException
     */
    public static String getTranslatedText(String json) throws GestorException{

        try{
            JSONArray jsonArray = new JSONArray(json);
            JSONArray jsonArray2 = (JSONArray)jsonArray.getJSONObject(0).get("translations");
            JSONObject jsonArray3 = jsonArray2.getJSONObject(0);
            String text = jsonArray3.get("text").toString();

            return text;

        }catch(Exception e){
            throw new GestorException(ERROR_JSON_TRANSLATOR + e);
        }
    }

//Utils
    /**
     * Mètode per a obtenir els bytes d'un InputStream
     * @param inputStream del que llegirem els bytes
     * @return Array de bytes
     * @throws IOException
     * @author Jordi Gomez Lozano
     */
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

    /**
     * Inner class per a ordenar la llista de panells
     * @author Jordi Gomez Lozano
     */
    public static class PanellPositionComparator implements Comparator<Panell> {

        @Override
        public int compare(Panell one, Panell two) {
            return one.getPosicio() - two.getPosicio();
        }
    }
}
