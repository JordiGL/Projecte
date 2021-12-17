package controlador.server;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import controlador.gestor.JsonUtils;
import model.Usuari;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Classe d'utilitats per a connectar amb el servidor.
 * @see Connexio
 * @author Jordi Gómez Lozano.
 */
public class NetworkUtils extends Connexio{
    private static final String SIGN_UP_URL = "http://10.0.2.2:8080/signin";
    private static final String CHANGE_PASS_URL = "http://10.0.2.2:8080/changePassword/";
    private static final String BASIC_GET_URL = "http://10.0.2.2:8080/api/usuaris";
    private static final String PANELLS_URL = "http://10.0.2.2:8080/api/usuaris/profile/";
    private static final String URL_TOKEN = "http://10.0.2.2:8080/oauth/token";
    private static final String URL_NEW_ICONA = "http://10.0.2.2:8080/app/icones/icona/panell/";
    private static final String URL_BASIC_ICONA = "http://10.0.2.2:8080/app/icones/icona/";
    private static final String AUTHORIZATION_KEY = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String METODE_PETICIO_POST = "POST";
    private static final String METODE_PETICIO_PUT = "PUT";
    private static final String APPLICATION_JSON = "application/json";
    private static final String APPLICATION_URLENCODED = "application/x-www-form-urlencoded";
    private static final String RESPONSE_CODE_BUNDLE_KEY = "responseCode";
    private static final String TOKEN_BUNDLE_KEY = "token";
    private static final String PANELLS_BUNDLE_KEY = "panells";
    private static final String SERVER_INFO_BUNDLE_KEY = "serverInfo";
    private static final String OPTION_BUNDLE_KEY = "opcio";
    private static final String LIST_PANELLS_OPTION = "list";
    private static final String ADD_PANELL_OPTION = "add";
    private static final String DELETE_PANELL_INFO_BUNDLE_KEY = "delete_panell_info";
    private static final String DELETE_PANELL_OPTION = "delete_panell";
    private static final String ID_PANELL_BUNDLE_KEY = "id_panell";
    private static final String EDIT_PANELL_OPTION = "edit";
    private static final String CREATE_ICONA_OPTION = "create_icona";
    private static final String EDIT_ICONA_OPTION = "edit_icona";
    private static final String ID_ICONA_BUNDLE_KEY = "icon_id";
    private static final String DELETE_ICONA_OPTION = "delete_icona";
    private static final String PANELL_NAME_BUNDLE_KEY = "panell_name";
    private static final String PANELL_PREDEFINIT_DELETE_URL = "http://10.0.2.2:8080/app/panellPredefinit/";
    private static final String TRANSLATOR_DATA_BUNDLE_KEY = "translated";
    private static final String TRANSLATE_TEXT_OPTION = "translate_text";

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

    public static Bundle requestTranslator(String text, String subscriptionKey, String location){

        Bundle queryBundle = null;
        OkHttpClient client = null;
        Response response = null;

        try {

            HttpUrl url = new HttpUrl.Builder()
                    .scheme("https")
                    .host("api.cognitive.microsofttranslator.com")
                    .addPathSegment("/translate")
                    .addQueryParameter("api-version", "3.0")
                    .addQueryParameter("from", "ca")
                    .addQueryParameter("to", "en")
                    .build();

            client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");

            RequestBody body = RequestBody.create(mediaType,
                    "[{\"Text\": \"" + text + "\"}]");

            Request request = new Request.Builder().url(url).post(body)
                    .addHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
                    .addHeader("Ocp-Apim-Subscription-Region", location)
                    .addHeader("Content-type", "application/json")
                    .build();

            //Enviem la sol·licitud i n'obtenim la resposta.
            response = client.newCall(request).execute();
            String textJSONString = response.body().string();

            queryBundle = new Bundle();
            queryBundle.putInt(RESPONSE_CODE_BUNDLE_KEY, response.code());

            if(response.code() == HttpURLConnection.HTTP_OK){
                queryBundle.putString(OPTION_BUNDLE_KEY, TRANSLATE_TEXT_OPTION);
                queryBundle.putString(TRANSLATOR_DATA_BUNDLE_KEY, textJSONString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.dispatcher().executorService().shutdown();
                client.connectionPool().evictAll();
            }
            if (response != null && !response.isSuccessful()) {
                response.close();
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

//PANELLS
    /**
     * Delete request per a eliminar un panell.
     * @param idPanell part de la url del request.
     * @param token token de l'usuari.
     * @return dades obtingudes del servidor.
     * @author Jordi Gómez Lozano.
     */
    public static Bundle deletePanell(int idPanell, String username, String token){

        HttpURLConnection connexio = null;
        BufferedReader reader = null;
        String infoJSONString = null;
        Bundle queryBundle = null;
        int responseCode;

        try{
            if(idPanell == 0){
                connexio = deleteRequest(token, PANELL_PREDEFINIT_DELETE_URL
                        +username+"/"+idPanell);
            } else {
                connexio = deleteRequest(token, PANELLS_URL+idPanell);
            }

            responseCode = connexio.getResponseCode();

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
                queryBundle.putString(OPTION_BUNDLE_KEY, DELETE_PANELL_OPTION);
                queryBundle.putInt(ID_PANELL_BUNDLE_KEY, idPanell);
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
            queryBundle.putString(PANELL_NAME_BUNDLE_KEY, nom);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connexio != null) {
                connexio.disconnect();
            }
        }
        return queryBundle;
    }
//ICONES
    public static Bundle addNewIcon(Context context, int idPanell, String name, int position,
                                    String fileName, String token){
        Bundle queryBundle = null;
        OkHttpClient client = null;
        RequestBody body = null;
        Response response = null;
        File file = null;

        try{
            client = new OkHttpClient().newBuilder()
                    .build();

            if(fileName.isEmpty()){

                body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("id","")
                        .addFormDataPart("nom",name)
                        .addFormDataPart("posicio",String.valueOf(position))
                        .addFormDataPart("foto",fileName,
                                RequestBody.create(MediaType.parse("application/octet-stream"),
                                        fileName))
                        .build();
            } else {
                file = new File(context.getFilesDir(),fileName);
                body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("id","")
                        .addFormDataPart("nom",name)
                        .addFormDataPart("posicio",String.valueOf(position))
                        .addFormDataPart("foto",fileName,
                                RequestBody.create(MediaType.parse("application/octet-stream"), file))
                        .build();
            }

            Request request = new Request.Builder()
                    .url(URL_NEW_ICONA +String.valueOf(idPanell))
                    .method(METODE_PETICIO_POST, body)
                    .addHeader(AUTHORIZATION_KEY, BEARER + token)
                    .build();
            response = client.newCall(request).execute();

            queryBundle = new Bundle();
            queryBundle.putInt(RESPONSE_CODE_BUNDLE_KEY, response.code());
            queryBundle.putString(OPTION_BUNDLE_KEY, CREATE_ICONA_OPTION);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.dispatcher().executorService().shutdown();
                client.connectionPool().evictAll();
            }
            if (response != null && !response.isSuccessful()) {
                response.close();
            }
        }
        return queryBundle;
    }

    public static Bundle editIcona(Context context, int idIcona, String name, int position,
                                   String fileName, String token){
        Bundle queryBundle = null;
        OkHttpClient client = null;
        RequestBody body = null;
        Response response = null;
        File file = null;

        try{
            client = new OkHttpClient().newBuilder()
                    .build();

            if(fileName.isEmpty()){

                body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("nom",name)
                        .addFormDataPart("posicio",String.valueOf(position))
                        .addFormDataPart("foto",fileName,
                                RequestBody.create(MediaType.parse("application/octet-stream"),
                                        fileName))
                        .build();
            } else {
                file = new File(context.getFilesDir(),fileName);
                body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("nom",name)
                        .addFormDataPart("posicio",String.valueOf(position))
                        .addFormDataPart("foto",file.getPath(),
                                RequestBody.create(MediaType.parse("application/octet-stream"), file))
                        .build();
            }
            Request request = new Request.Builder()
                    .url(URL_BASIC_ICONA +String.valueOf(idIcona))
                    .method(METODE_PETICIO_PUT, body)
                    .addHeader(AUTHORIZATION_KEY, BEARER + token)
                    .build();
            response = client.newCall(request).execute();

            queryBundle = new Bundle();
            queryBundle.putInt(RESPONSE_CODE_BUNDLE_KEY, response.code());
            queryBundle.putString(OPTION_BUNDLE_KEY, EDIT_ICONA_OPTION);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) {

                client.dispatcher().executorService().shutdown();
                client.connectionPool().evictAll();
            }
            if (response != null && !response.isSuccessful()) {
                response.close();
            }
        }
        return queryBundle;
    }

    /**
     * Delete request per a eliminar una icona.
     * @param idIcona part de la url del request.
     * @param token token de l'usuari.
     * @return dades obtingudes del servidor.
     * @author Jordi Gómez Lozano.
     */
    public static Bundle deleteIcona(int idIcona, String token){

        HttpURLConnection connexio = null;
        BufferedReader reader = null;
        String infoJSONString = null;
        Bundle queryBundle = null;
        int responseCode;

        try{
            connexio = deleteRequest(token, URL_BASIC_ICONA +idIcona);
            responseCode = connexio.getResponseCode();

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
                queryBundle.putString(OPTION_BUNDLE_KEY, DELETE_ICONA_OPTION);
                queryBundle.putInt(ID_ICONA_BUNDLE_KEY, idIcona);
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
