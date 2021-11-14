package controlador.server;

import android.util.Log;

import java.io.DataOutputStream;
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
    private static final String URL = "http://10.0.2.2:8080/signin";

    public static int addNewUser(Usuari usuari){
        HttpURLConnection connexio = null;
        int responseCode = 0;
        try{
            byte[] postData = usuari.toString().getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            connexio = postRequest(postDataLength, METODE_PETICIO, URL, "application/json");

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
}
