package controlador.server;

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
    private HttpURLConnection conn;

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

            conn.setConnectTimeout(TIMEOUT_MILLS);
            conn.setReadTimeout(TIMEOUT_MILLS);

        } catch (Exception e) {
            e.printStackTrace();
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
}
