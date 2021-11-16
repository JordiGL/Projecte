package controlador.server;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Classe que estableix la connexió amb el servidor.
 * @author Jordi Gómez Lozano
 */
public class Connexio {
    private static final int TIMEOUT_MILLS = 4000;
    private static final String AUTHORIZATION_KEY = "Authorization";
    private static final String CONTENT_TYPE_KEY = "Content-Type";
    private static final String CHARSET_KEY = "charset";
    private static final String CONTENT_LENGTH_KEY = "Content-Length";
    private static final String AUTHORIZATION_VALUE = "Basic YW5kcm9pZGFwcDoxMjM0NQ==";
    private static final String CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded";
    private static final String CHARSET_VALUE = "utf-8";
    public static final String BEARER = "Bearer ";
    private static HttpURLConnection conn;

    public HttpURLConnection getConn() {
        return conn;
    }

    public void setConn(HttpURLConnection conn) {
        this.conn = conn;
    }

    /**
     * Estableix la connexió amb el servidor per a fer una petició.
     * @param postDataLength la allargada del Content-length.
     * @param requestUrl la url de connexió per a fer la petició.
     * @return la connexió amb el servidor
     * @author Jordi Gómez Lozano
     */
    public static HttpURLConnection postRequest(int postDataLength, String method, String requestUrl, String contentType){
        try {

            URL url = new URL( requestUrl );
            conn= (HttpURLConnection) url.openConnection();
            conn.setDoOutput( true );
            conn.setInstanceFollowRedirects( false );
            conn.setRequestMethod( method );
            conn.setRequestProperty(AUTHORIZATION_KEY, AUTHORIZATION_VALUE);
            conn.setRequestProperty(CONTENT_TYPE_KEY, contentType);
            conn.setRequestProperty(CHARSET_KEY, CHARSET_VALUE);
            conn.setRequestProperty(CONTENT_LENGTH_KEY, Integer.toString( postDataLength ));
            conn.setUseCaches( false );

            conn.setConnectTimeout(TIMEOUT_MILLS);
            conn.setReadTimeout(TIMEOUT_MILLS);

        } catch (Exception e) {

            e.printStackTrace();
            if (conn != null) {
                conn.disconnect();
            }
        }

        return conn;
    }


    /**
     * Estableix la connexió amb el servidor per a fer una petició.
     * @param postDataLength la allargada del Content-length.
     * @param requestUrl la url de connexió per a fer la petició.
     * @return la connexió amb el servidor
     * @author Jordi Gómez Lozano
     */
    public static HttpURLConnection putRequest(int postDataLength, String method, String requestUrl, String contentType, String token){
        try {
            URL url = new URL( requestUrl );
            conn= (HttpURLConnection) url.openConnection();
            conn.setDoOutput( true );
            conn.setInstanceFollowRedirects( false );
            conn.setRequestMethod( method );
            conn.setRequestProperty(AUTHORIZATION_KEY, BEARER + token);
            conn.setRequestProperty(CONTENT_TYPE_KEY, contentType);
            conn.setRequestProperty(CHARSET_KEY, CHARSET_VALUE);
            conn.setRequestProperty(CONTENT_LENGTH_KEY, Integer.toString( postDataLength ));
            conn.setUseCaches( false );

            conn.setConnectTimeout(TIMEOUT_MILLS);
            conn.setReadTimeout(TIMEOUT_MILLS);

        } catch (Exception e) {

            e.printStackTrace();
            if (conn != null) {
                conn.disconnect();
            }

        }


        return conn;
    }

    /**
     * Comprovació de possibles problemes amb el servidor.
     * @param time temps actual en mil·lisegons
     * @return Torna true si s'està tardant a establir connexió, fals en cas contrari.
     */
    public boolean connectionProblems(long time){

        return (System.currentTimeMillis() - time) >= TIMEOUT_MILLS;
    }

    public static HttpURLConnection getRequest(String token, String url){

        try{
            URL requestURL = new URL(url);

            conn = (HttpURLConnection) requestURL.openConnection();
            conn.setRequestProperty("Authorization","Bearer "+ token);
            conn.setRequestMethod("GET");

            conn.setConnectTimeout(TIMEOUT_MILLS);
            conn.setReadTimeout(TIMEOUT_MILLS);

        } catch (IOException e){

            e.printStackTrace();
            if (conn != null) {
                conn.disconnect();
            }


        }

        return conn;
    }


}
