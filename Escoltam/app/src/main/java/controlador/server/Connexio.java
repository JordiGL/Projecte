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
            Log.i("Info", "1");
        } catch (Exception e) {
            e.printStackTrace();

        } finally{
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

    public HttpURLConnection getRequest(String token, String url){

        token ="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MzY2NjUxMDMsInVzZXJfbmFtZ" +
                "SI6IkpvR29tTG96QGdtYWlsLmNvbSIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iXSwianRpIjoi" +
                "MzExYTc2ZjMtYzY3MC00NTk4LTlkOGItOTM0ODVlYzNhMWEwIiwiY2xpZW50X2lkIjoiYW5kcm9pZGFw" +
                "cCIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdfQ.xnTd8fOl2NrzSQxXb6eaCMmI7hcDhDzfZP_12LEKBR" +
                "iP5XTV75ZfC1-xXbVcwB3X5h6Q8OrpoAEHju4dV3XtNvKRgS3dDD822Nnpwuv9t32-Pu37to7JV8Avb" +
                "ukoU7Br-oF1pwU00kNZo618GmrfJan-N2Ah61rAEV8DYgdRI3tmstO0V6iZUJEgbC7tiin0sakStldfZ5" +
                "s8bIxZZAUjZli0SuUeYKzdv9DUVtpjHpOfU9jKsDnFBK07jA-KZVYTtYKZJwlnDckxffk2ytCUKWKQn" +
                "q2dqhHd8DjSkf69Ck371MhHl_4I0qIwzTmb6vY6l1IQmMdUzZ7RRexMBiSidg";

        Log.d("Info", "1");

        try{
            URL requestURL = new URL(url);

            conn = (HttpURLConnection) requestURL.openConnection();
            conn.setRequestProperty("Authorization","Bearer "+ token);
            conn.setRequestMethod("GET");

            conn.setConnectTimeout(TIMEOUT_MILLS);
            conn.setReadTimeout(TIMEOUT_MILLS);

        } catch (IOException e){

            e.printStackTrace();

        }

        return conn;
    }


}
