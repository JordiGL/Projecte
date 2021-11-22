package controlador.server;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

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
    private static final String APPLICATION_JSON = "application/json";

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
                serverInfo = getInfo(bytesToString(error));
            }

            responseCode = connexio.getResponseCode();

            queryBundle = new Bundle();
            queryBundle.putInt("responseCode", responseCode);
            queryBundle.putString("serverInfo", serverInfo);


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
     * Post request al servidor per afegir un nou usuari.
     * @param usuari usuari a afegir al servidor.
     * @return un Bundle amb el codi de resposta i possible informació d'error.
     * @author Jordi Gómez Lozano.
     */
    public static int addNewUserForTest(Usuari usuari){
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

    /**
     * Get request per a obtenir el codi de resposte en fer la consulta
     * per a obtenir dades dels usuaris o usuari.
     * @param opcio part de la url del request.
     * @param token token de l'usuari.
     * @return codi de resposta del servidor.
     * @author Jordi Gómez Lozano.
     */
    public static int getUsuarisDataResponse(String opcio, String token){
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

    /**
     * Obtinc la dada de l'error del JSON que rebo.
     * @param data dades del servidor.
     * @return l'error rebut pel servidor.
     * @throws JSONException
     * @author Jordi Gómez Lozano.
     */
    private static String getInfo(String data) throws JSONException {
        JSONObject access_token = new JSONObject(data);
        return access_token.getString("Error");
    }
}
