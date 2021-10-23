package controlador.gestor;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Classe que estableix la connexió amb el servidor.
 * @author Jordi Gómez Lozano
 */
public class Connexio {

    public static final String AUTHORIZATION_KEY = "Authorization";
    public static final String CONTENT_TYPE_KEY = "Content-Type";
    public static final String CHARSET_KEY = "charset";
    public static final String CONTENT_LENGTH_KEY = "Content-Length";
    public static final String AUTHORIZATION_VALUE = "Basic YW5kcm9pZGFwcDoxMjM0NQ==";
    public static final String CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded";
    public static final String CHARSET_VALUE = "utf-8";
    HttpURLConnection conn;

    public HttpURLConnection getConn() {
        return conn;
    }

    public void setConn(HttpURLConnection conn) {
        this.conn = conn;
    }

    /**
     * Estableix la conexio amb el servidor per a fer una petició.
     * @param postDataLength la allargada del Content-length.
     * @param requestUrl la url de connexió per a fer la petició.
     * @return la conexió amb el servidor
     * @author Jordi Gómez Lozano
     */
    public HttpURLConnection postRequest(int postDataLength, String method, String requestUrl){
        try {
            URL url = new URL( requestUrl );
            conn= (HttpURLConnection) url.openConnection();
            conn.setDoOutput( true );
            conn.setInstanceFollowRedirects( false );
            conn.setRequestMethod( method );
            conn.setRequestProperty(AUTHORIZATION_KEY, AUTHORIZATION_VALUE);
            conn.setRequestProperty(CONTENT_TYPE_KEY, CONTENT_TYPE_VALUE);
            conn.setRequestProperty(CHARSET_KEY, CHARSET_VALUE);
            conn.setRequestProperty(CONTENT_LENGTH_KEY, Integer.toString( postDataLength ));
            conn.setUseCaches( false );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
