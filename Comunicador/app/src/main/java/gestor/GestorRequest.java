package gestor;

import androidx.annotation.Nullable;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GestorRequest {

    private String token;

    public GestorRequest() {}

    public String getToken() {
        return token;
    }

    /**
     * Post request al servidor per obtenir el token.
     * @return un int per part del servidor, si retorna 200 el request s'ha efectuat correctament.
     */
    public int requestToken(){
        int responseCode = 0;
        try {
            //La urlParametres hauria d'estar formada pels paràmetres del requestToken( String username, String clau).
            String urlParameters = "username=gemmarica94@gmail.com&password=12345&grant_type=password";
            byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
            int postDataLength = postData.length;
            //Atenció a Android Studio he d'utilitzar 10.0.2.2, en comptes de localhost.
            String request = "http://10.0.2.2:8080/oauth/token";
            URL url = new URL( request );
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setDoOutput( true );
            conn.setInstanceFollowRedirects( false );
            conn.setRequestMethod( "POST" );
            conn.setRequestProperty( "Authorization", "Basic YW5kcm9pZGFwcDoxMjM0NQ==");
            conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty( "charset", "utf-8");
            conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            conn.setUseCaches( false );
            try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {

                wr.write(postData);
                InputStream data = conn.getInputStream();

                responseCode = conn.getResponseCode();

                token = obtenirToken(bytesToString(data));

            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseCode;
    }

    /**
     *
     * @param resposta
     * @return
     * @throws IOException
     * @author Jordi Gómez Lozano.
     */
    public String bytesToString(InputStream resposta) throws IOException {

        ByteArrayOutputStream into = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        for (int n; 0 < (n = resposta.read(buf));) {
            into.write(buf, 0, n);
        }
        into.close();
        return into.toString("UTF-8");
    }

    /**
     * Obtinc la dada del token del JSON que rebo, aquest té altres dades.
     * @param data
     * @return
     * @throws JSONException
     * @author Jordi Gómez Lozano.
     */
    private String obtenirToken(String data) throws JSONException {
        JSONObject access_token = new JSONObject(data);
        return access_token.getString("access_token");
    }

    /**
     * Decodifica el token i obté l'email.
     * @param token L'String del token de l'usuari.
     * @return un String amb l'email.
     * @author Jordi Gómez Lozano.
     */
    @Nullable
    private String getEmailFromToken(String token) {
        JWT parsedJWT = new JWT(token);
        Claim subscriptionMetaData = parsedJWT.getClaim("user_name");
        return subscriptionMetaData.asString();
    }

    /**
     * Decodifica el token i obté el role.
     * @param token L'String del token de l'usuari.
     * @return un String amb l'email.
     * @author Jordi Gómez Lozano.
     */
    @Nullable
    public String getRoleFromToken(String token) {
        JWT parsedJWT = new JWT(token);
        Claim subscriptionMetaData = parsedJWT.getClaim("authorities");
        String[] decodedEmail = subscriptionMetaData.asArray(String.class);
        return decodedEmail[0];
    }
}