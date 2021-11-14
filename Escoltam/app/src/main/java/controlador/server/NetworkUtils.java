package controlador.server;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import model.Usuari;

public class NetworkUtils extends Connexio{
    private static final int TIMEOUT_MILLS = 4000;
    private static final String AUTHORIZATION_KEY = "Authorization";
    private static final String CONTENT_TYPE_KEY = "Content-Type";
    private static final String CHARSET_KEY = "charset";
    private static final String CONTENT_LENGTH_KEY = "Content-Length";
    private static final String AUTHORIZATION_VALUE = "Basic YW5kcm9pZGFwcDoxMjM0NQ==";
    private static final String CONTENT_TYPE_VALUE = "application/json";
    private static final String CHARSET_VALUE = "utf-8";
    private static final String METODE_PETICIO = "POST";
    private static final String SIGN_UP_URL = "http://10.0.2.2:8080/signin";
    private static final String BASIC_GET_URL = "http://10.0.2.2:8080/api/usuaris";

    public static int addNewUser(Usuari usuari){
        HttpURLConnection connexio = null;
        int responseCode = 0;
        try{
            byte[] postData = usuari.toString().getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            connexio = postRequest(postDataLength, METODE_PETICIO, SIGN_UP_URL, "application/json");

            DataOutputStream wr = new DataOutputStream(connexio.getOutputStream());

            wr.write(postData);

            responseCode = connexio.getResponseCode();
            Log.i("Info", String.valueOf(responseCode));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connexio != null) {
                connexio.disconnect();
            }
        }

        return responseCode;
    }

    public static String getUsuarisData(String opcio, String token){
        HttpURLConnection connexio = null;
        BufferedReader reader = null;
        String usersJSONString = null;

        try{

            connexio = getRequest(token, BASIC_GET_URL+opcio);

            int responseCode = connexio.getResponseCode();
            Log.d("Info", String.valueOf(responseCode));

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
}
