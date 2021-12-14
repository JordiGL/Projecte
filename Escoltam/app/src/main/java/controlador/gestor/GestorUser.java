package controlador.gestor;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
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
    private static final String NEW_PANELL = "Nou panell";
    private static List<Panell> mPanells;



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

                    for (int i = 0; i < jsonArray.length(); i++) {
                        icones = new ArrayList<>();

                        //Obtenim l'array d'icones.
                        JSONArray iconesJsonList = jsonArray.getJSONObject(i).getJSONArray(ICONES_JSON);

                        //Ferm la cerca de les icones, les creem i les afegim a la llista.
                        for (int j = 0; j < iconesJsonList.length(); j++){

                            icones.add(new Icona(
                                    iconesJsonList.getJSONObject(j).getString(ICONA_NOM_JSON),
                                    iconesJsonList.getJSONObject(j).getInt(ICONA_POSICIO_JSON),
                                    Base64.decode(iconesJsonList.getJSONObject(j).getString("foto"), Base64.DEFAULT))
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

        Collections.sort(mPanells, new PanellSequenceComparator());
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

    public static boolean containsPanell(){

        for(Panell panell: mPanells){
            if(panell.getNom().equals(NEW_PANELL)){
                return true;
            }
        }

        return false;
    }

    public static Panell newPanell(int position){

        return new Panell(
                NEW_PANELL,
                position,
                false,
                new ArrayList<Icona>()
        );
    }

    public static void removePanell(int idPanell){
        for(Panell panell: mPanells){
            if(panell.getId() == idPanell){
                mPanells.remove(panell);
            }
        }
    }

    public static void setEditTextFocusable(boolean focusable, EditText editText) {
        editText.setFocusableInTouchMode(focusable);
        editText.setFocusable(focusable);

        if (focusable) {
            editText.requestFocusFromTouch();
        }
    }

    public static class PanellSequenceComparator implements Comparator<Panell> {

        @Override
        public int compare(Panell one, Panell two) {
            return one.getPosicio() - two.getPosicio();
        }

    }

//Icones

    public static File createFile(Context context, ContentResolver resolver, Uri uri, String name){
        FileOutputStream out = null;
        InputStream stream = null;
        File file = null;
        try {
            //Obtenim els bytes de la imatge.
            stream =   resolver.openInputStream(uri);
            byte[] imatgeEnBytes = getBytes(stream);
            //Creem la imatge.
            file = new File(context.getFilesDir(),name);

            out = new FileOutputStream(file);
            out.write(imatgeEnBytes);
            stream =   new FileInputStream(file);
            byte[] fileContent = getBytes(stream);

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

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);

        }

        Log.i("Info", String.valueOf(byteBuffer));
        return byteBuffer.toByteArray();
    }

//    public static String queryName(ContentResolver resolver, Uri uri) {
//        Cursor returnCursor = resolver.query(uri, null, null, null, null);
//        assert returnCursor != null;
//        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
//        returnCursor.moveToFirst();
//        String name = returnCursor.getString(nameIndex);
//        returnCursor.close();
//        return name;
//    }

}
