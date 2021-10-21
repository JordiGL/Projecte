package gestor;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Classe que estableix la connexió amb el servidor.
 * @author Jordi Gómez Lozano
 */
public class Connexio {

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
            conn.setRequestProperty( "Authorization", "Basic YW5kcm9pZGFwcDoxMjM0NQ==");
            conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty( "charset", "utf-8");
            conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            conn.setUseCaches( false );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

}
