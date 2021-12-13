package controlador.server;

import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import controlador.gestor.JsonUtils;
import model.Usuari;

/**
 * Classe d'utilitats per a connectar amb el servidor.
 * @see Connexio
 * @author Jordi Gómez Lozano.
 */
public class NetworkUtils extends Connexio{
    private static final String CHARSET_NAME = "UTF-8";
    private static final String METODE_PETICIO_POST = "POST";
    private static final String METODE_PETICIO_PUT = "PUT";
    private static final String SIGN_UP_URL = "http://10.0.2.2:8080/signin";
    private static final String CHANGE_PASS_URL = "http://10.0.2.2:8080/changePassword/";
    private static final String BASIC_GET_URL = "http://10.0.2.2:8080/api/usuaris";
    private static final String PANELLS_URL = "http://10.0.2.2:8080/app/panells/";
    private static final String APPLICATION_JSON = "application/json";
    private static final String APPLICATION_URLENCODED = "application/x-www-form-urlencoded";
    private static final String URL_TOKEN = "http://10.0.2.2:8080/oauth/token";
    private static final String RESPONSE_CODE_BUNDLE_KEY = "responseCode";
    private static final String TOKEN_BUNDLE_KEY = "token";
    private static final String PANELLS_BUNDLE_KEY = "panells";
    private static final String SERVER_INFO_BUNDLE_KEY = "serverInfo";
    private static final String OPTION_BUNDLE_KEY = "opcio";
    private static final String LIST_PANELLS_OPTION = "list";
    private static final String ADD_PANELL_OPTION = "add";
    private static final String DELETE_PANELL_INFO_BUNDLE_KEY = "delete_panell_info";
    private static final String DELETE_PANELL_OPTION = "delete";
    private static final String ID_PANELL_BUNDLE_KEY = "id_panell";
    public static final String EDIT_PANELL_OPTION = "edit";

    /**
     * Post request al servidor per obtenir el token.
     * @return un int per part del servidor, si retorna 200 el request s'ha efectuat correctament.
     * @author Jordi Gómez Lozano.
     */
    public static Bundle requestToken(String username, String clau){
        int responseCode = 0;
        Bundle queryBundle = null;

        String urlParameters = "username="+username+"&password="+clau+"&grant_type=password";
        byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int postDataLength = postData.length;
        HttpURLConnection connexio = postRequest(postDataLength, METODE_PETICIO_POST, URL_TOKEN, APPLICATION_URLENCODED);

        try( DataOutputStream wr = new DataOutputStream(connexio.getOutputStream())) {

            wr.write(postData);
            responseCode = connexio.getResponseCode();

            queryBundle = new Bundle();
            queryBundle.putInt(RESPONSE_CODE_BUNDLE_KEY, responseCode);

            if(responseCode == HttpURLConnection.HTTP_OK){
                InputStream data = connexio.getInputStream();
                queryBundle.putString(TOKEN_BUNDLE_KEY, JsonUtils.obtenirToken(bytesToString(data)));
            }

        }catch (Exception e){
            e.printStackTrace();
        } finally{
            if (connexio != null) {
                connexio.disconnect();
            }
        }

        return queryBundle;
    }

    /**
     * Post request al servidor per afegir un nou usuari.
     * @param usuari usuari a afegir al servidor.
     * @return un Bundle amb el codi de resposta i possible informació d'error.
     * @author Jordi Gómez Lozano.
     */
    public static Bundle addNewUser(Usuari usuari){
        Bundle queryBundle = null;
        String serverInfo = "";
        HttpURLConnection connexio = null;
        int responseCode = 0;
        try{
            byte[] postData = usuari.toString().getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            connexio = postRequest(postDataLength, METODE_PETICIO_POST, SIGN_UP_URL, APPLICATION_JSON);

            DataOutputStream wr = new DataOutputStream(connexio.getOutputStream());

            wr.write(postData);

            InputStream error = connexio.getErrorStream();

            if(error != null){
                serverInfo = JsonUtils.getErrorInfo(bytesToString(error));
            }

            responseCode = connexio.getResponseCode();

            queryBundle = new Bundle();
            queryBundle.putInt(RESPONSE_CODE_BUNDLE_KEY, responseCode);
            queryBundle.putString(SERVER_INFO_BUNDLE_KEY, serverInfo);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connexio != null) {
                connexio.disconnect();
            }
        }

        return queryBundle;
    }

    /**
     * Put request al servidor per a fer el canvi de clau.
     * @param password nova clau.
     * @param email email de l'usuari.
     * @param token token de l'usuari.
     * @return un int amb el codi de resposta.
     * @author Jordi Gómez Lozano.
     */
    public static int sendPassword(String password, String email, String token){
        HttpURLConnection connexio = null;
        int responseCode = 0;

        try{
            String body = "{\"password\": \""+password+"\"}";
            byte[] postData = body.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            connexio = putRequest(postDataLength, METODE_PETICIO_PUT, CHANGE_PASS_URL+email, APPLICATION_JSON, token);

            DataOutputStream wr = new DataOutputStream(connexio.getOutputStream());

            wr.write(postData);

            responseCode = connexio.getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connexio != null) {
                connexio.disconnect();
            }
        }

        return responseCode;
    }

    /**
     * Put request per a enviar la nova veu al servidor.
     * @param password clau de l'usuari.
     * @param voice nova veu de l'usuari.
     * @param email email de l'usuari.
     * @param token token de l'usuari.
     * @return un int amb el codi de resposta.
     * @author Jordi Gómez Lozano.
     */
    public static int sendVoice(String password, String voice, String email, String token){
        HttpURLConnection connexio = null;
        int responseCode = 0;

        try{
            String body = "{\"password\": \""+password+"\",\"voice\": \""+voice+"\"}";
            byte[] postData = body.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            connexio = putRequest(postDataLength, METODE_PETICIO_PUT, BASIC_GET_URL+"/profile/"+email, APPLICATION_JSON, token);

            DataOutputStream wr = new DataOutputStream(connexio.getOutputStream());

            wr.write(postData);

            responseCode = connexio.getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connexio != null) {
                connexio.disconnect();
            }
        }

        return responseCode;
    }

    /**
     * Get request per a obtenir dades dels usuaris o usuari.
     * @param opcio part de la url del request.
     * @param token token de l'usuari.
     * @return dades obtingudes del servidor.
     * @author Jordi Gómez Lozano.
     */
    public static String getUsuarisData(String opcio, String token){
        HttpURLConnection connexio = null;
        BufferedReader reader = null;
        String usersJSONString = null;

        try{

            connexio = getRequest(token, BASIC_GET_URL+opcio);

            InputStream inputStream = connexio.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            // StringBuilder on hi guardarem la resposta.
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }

            if (builder.length() == 0) {
                return null;
            }

            usersJSONString = builder.toString();

        } catch (IOException e){
            e.printStackTrace();
        } finally{
            if (connexio != null) {
                connexio.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return usersJSONString;
    }

//DELETE PANELL
    /**
     * Delete request per a eliminar un panell.
     * @param opcio part de la url del request.
     * @param token token de l'usuari.
     * @return dades obtingudes del servidor.
     * @author Jordi Gómez Lozano.
     */
    public static Bundle deletePanell(int opcio, String token){
        Log.i("Info",""+opcio);
        HttpURLConnection connexio = null;
        BufferedReader reader = null;
        String infoJSONString = null;
        Bundle queryBundle = null;
        int responseCode;

        try{

            connexio = deleteRequest(token, PANELLS_URL+opcio);
            responseCode = connexio.getResponseCode();
            Log.i("Info", "123: "+String.valueOf(responseCode));
            queryBundle = new Bundle();
            queryBundle.putInt(RESPONSE_CODE_BUNDLE_KEY, responseCode);

            if(responseCode == HttpURLConnection.HTTP_OK) {

                InputStream inputStream = connexio.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                // StringBuilder on hi guardarem la resposta.
                StringBuilder builder = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                    builder.append("\n");
                }

                if (builder.length() == 0) {
                    return null;
                }

                infoJSONString = builder.toString();
                queryBundle.putString(DELETE_PANELL_INFO_BUNDLE_KEY, infoJSONString);
                queryBundle.putString(OPTION_BUNDLE_KEY, "delete");
                queryBundle.putInt(ID_PANELL_BUNDLE_KEY, opcio);
            }

        } catch (IOException e){
            e.printStackTrace();
        } finally{
            if (connexio != null) {
                connexio.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return queryBundle;
    }

    /**
     * Get request per a obtenir els panells dels usuaris o usuari.
     * @param opcio part de la url del request.
     * @param token token de l'usuari.
     * @return dades obtingudes del servidor.
     * @author Jordi Gómez Lozano.
     */
    public static Bundle getPanellsData(String opcio, String token){
        HttpURLConnection connexio = null;
        BufferedReader reader = null;
        String panellsJSONString = null;
        Bundle queryBundle = null;
        int responseCode;

        try{

            connexio = getRequest(token, PANELLS_URL +opcio);
            responseCode = connexio.getResponseCode();

            queryBundle = new Bundle();
            queryBundle.putInt(RESPONSE_CODE_BUNDLE_KEY, responseCode);

            if(responseCode == HttpURLConnection.HTTP_OK){

                InputStream inputStream = connexio.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                // StringBuilder on hi guardarem la resposta.
                StringBuilder builder = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                    builder.append("\n");
                }

                if (builder.length() == 0) {
                    return null;
                }

                panellsJSONString = builder.toString();

                queryBundle.putString(PANELLS_BUNDLE_KEY, panellsJSONString);
                queryBundle.putString(OPTION_BUNDLE_KEY, LIST_PANELLS_OPTION);
            }

        } catch (IOException e){
            e.printStackTrace();
        } finally{
            if (connexio != null) {
                connexio.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return queryBundle;
    }


    /**
     * Post request al servidor per afegir un nou panell.
     * @param panell panell a afegir al servidor.
     * @return un Bundle amb el codi de resposta i possible informació d'error.
     * @author Jordi Gómez Lozano.
     */
    public static Bundle addNewPanell(String panell, String opcio, String token){
        Bundle queryBundle = null;
        String serverInfo = "";
        HttpURLConnection connexio = null;
        int responseCode = 0;
        try{
            byte[] postData = panell.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            connexio = postRequestPanell(postDataLength, METODE_PETICIO_POST, PANELLS_URL+opcio, APPLICATION_JSON, token);

            DataOutputStream wr = new DataOutputStream(connexio.getOutputStream());

            wr.write(postData);

            InputStream error = connexio.getErrorStream();

            if(error != null){
                serverInfo = error.toString();
            }

            responseCode = connexio.getResponseCode();

            queryBundle = new Bundle();
            queryBundle.putInt(RESPONSE_CODE_BUNDLE_KEY, responseCode);
            queryBundle.putString(SERVER_INFO_BUNDLE_KEY, serverInfo);
            queryBundle.putString(OPTION_BUNDLE_KEY, ADD_PANELL_OPTION);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connexio != null) {
                connexio.disconnect();
            }
        }

        return queryBundle;
    }

    /**
     * Put request al servidor per editar un panell.
     * @param nom del panell.
     * @param posicio del panell.
     * @param favorit si es favorit o no.
     * @param idPanell ide del panell.
     * @param username email de l'usuari.
     * @param token token de l'usuari.
     * @return un int amb el codi de resposta.
     * @author Jordi Gómez Lozano.
     */
    public static Bundle editPanell(String nom, int posicio, boolean favorit, int idPanell,
                                 String username, String token){
        Bundle queryBundle = null;
        HttpURLConnection connexio = null;
        int responseCode = 0;

        try{
            String body = "{\"nom\": \""+nom+"\",\"posicio\": "+posicio+",\"favorit\": "+favorit+"}";
            byte[] postData = body.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            connexio = putRequest(
                    postDataLength,
                    METODE_PETICIO_PUT,
                    PANELLS_URL+username+"/"+idPanell, APPLICATION_JSON, token);

            DataOutputStream wr = new DataOutputStream(connexio.getOutputStream());

            wr.write(postData);

            responseCode = connexio.getResponseCode();

            queryBundle = new Bundle();
            queryBundle.putInt(RESPONSE_CODE_BUNDLE_KEY, responseCode);
            queryBundle.putString(OPTION_BUNDLE_KEY, EDIT_PANELL_OPTION);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connexio != null) {
                connexio.disconnect();
            }
        }

        return queryBundle;
    }

    /**
     * Obté l'estring a partir dels bytes.
     * @param resposta la resposta del servidor.
     * @return l'estring a partir dels bytes.
     * @throws IOException
     * @author Jordi Gómez Lozano.
     */
    private static String bytesToString(InputStream resposta) throws IOException {
        ByteArrayOutputStream into = null;
        try {
            into = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            for (int n; 0 < (n = resposta.read(buf));) {
                into.write(buf, 0, n);
            }

        } catch (IOException e){

            e.printStackTrace();

        } finally{
            if (into != null) {
                try {
                    into.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return into.toString(CHARSET_NAME);
    }

    //Métodes creats només per a fer tests

    /**
     * Post request al servidor per obtenir el token.
     * @return un int per part del servidor, si retorna 200 el request s'ha efectuat correctament.
     * @author Jordi Gómez Lozano.
     */
    public static int testRequestToken(String username, String clau){
        int responseCode = 0;

        String urlParameters = "username="+username+"&password="+clau+"&grant_type=password";
        byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int postDataLength = postData.length;
        HttpURLConnection connexio = postRequest(postDataLength, METODE_PETICIO_POST, URL_TOKEN, APPLICATION_URLENCODED);

        try( DataOutputStream wr = new DataOutputStream(connexio.getOutputStream())) {

            wr.write(postData);
            responseCode = connexio.getResponseCode();

        }catch (Exception e){
            e.printStackTrace();
        } finally{
            if (connexio != null) {
                connexio.disconnect();
            }
        }

        return responseCode;
    }

    /**
     * Post request al servidor per afegir un nou usuari.
     * @param usuari usuari a afegir al servidor.
     * @return un Bundle amb el codi de resposta i possible informació d'error.
     * @author Jordi Gómez Lozano.
     */
    public static int testAddNewUser(Usuari usuari){
        HttpURLConnection connexio = null;
        int responseCode = 0;
        try{
            byte[] postData = usuari.toString().getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            connexio = postRequest(postDataLength, METODE_PETICIO_POST, SIGN_UP_URL, APPLICATION_JSON);

            DataOutputStream wr = new DataOutputStream(connexio.getOutputStream());

            wr.write(postData);

            responseCode = connexio.getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connexio != null) {
                connexio.disconnect();
            }
        }

        return responseCode;
    }

    /**
     * Get request per a obtenir el codi de resposte en fer la consulta
     * per a obtenir dades dels usuaris o usuari.
     * @param opcio part de la url del request.
     * @param token token de l'usuari.
     * @return codi de resposta del servidor.
     * @author Jordi Gómez Lozano.
     */
    public static int testGetUsuarisData(String opcio, String token){
        HttpURLConnection connexio = null;
        BufferedReader reader = null;
        int responseCode = 0;

        try{

            connexio = getRequest(token, BASIC_GET_URL+opcio);

            responseCode = connexio.getResponseCode();

        } catch (IOException e){
            e.printStackTrace();
        } finally{
            if (connexio != null) {
                connexio.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return responseCode;
    }
}
