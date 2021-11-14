package controlador.server.post;

import android.content.Context;

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

import java.nio.charset.StandardCharsets;

import controlador.server.Connexio;
import controlador.server.interfaces.RequestTokenImpl;


/**
 * Classe que fa al servidor la petició del token.
 * @author Jordi Gómez Lozano
 */
public class RequestToken extends Connexio implements RequestTokenImpl {
    private static final String ROLE = "authorities";
    private static final String TOKEN_EXPIRATION = "exp";
    private static final String EMAIL = "user_name";
    private static final String USER_TOKEN = "access_token";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String METODE_PETICIO = "POST";
    private static final String URL = "http://10.0.2.2:8080/oauth/token";
    private String token;
    private Context context;

    public RequestToken(Context context) {
        this.context = context;
    }

    public String getToken() {
        return token;
    }

    /**
     * Post request al servidor per obtenir el token.
     * @return un int per part del servidor, si retorna 200 el request s'ha efectuat correctament.
     * @author Jordi Gómez Lozano.
     */
    public int requestToken(String username, String clau){
        int responseCode = 0;

        String urlParameters = "username="+username+"&password="+clau+"&grant_type=password";
        byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int postDataLength = postData.length;
        HttpURLConnection connexio = postRequest(postDataLength, METODE_PETICIO, URL, "application/x-www-form-urlencoded");

        try( DataOutputStream wr = new DataOutputStream(connexio.getOutputStream())) {

            wr.write(postData);
            InputStream data = connexio.getInputStream();

            responseCode = connexio.getResponseCode();

            token = obtenirToken(bytesToString(data));

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
     * Obté l'estring a partir dels bytes.
     * @param resposta la resposta del servidor.
     * @return l'estring a partir dels bytes.
     * @throws IOException
     * @author Jordi Gómez Lozano.
     */
    private String bytesToString(InputStream resposta) throws IOException {
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
     * Obtinc la dada del token del JSON que rebo, aquest té altres dades.
     * @param data
     * @return
     * @throws JSONException
     * @author Jordi Gómez Lozano.
     */
    private String obtenirToken(String data) throws JSONException {
        JSONObject access_token = new JSONObject(data);
        return access_token.getString(USER_TOKEN);
    }

    /**
     * Decodifica el token i obté l'email.
     * @param token L'String del token de l'usuari.
     * @return un String amb l'email.
     * @author Jordi Gómez Lozano.
     */
    @Nullable
    public String getEmailFromToken(String token) {
        JWT parsedJWT = new JWT(token);
        Claim subscriptionMetaData = parsedJWT.getClaim(EMAIL);
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
        Claim subscriptionMetaData = parsedJWT.getClaim(ROLE);
        String[] decodedRole = subscriptionMetaData.asArray(String.class);
        return decodedRole[0];
    }

    /**
     * Decodifica el token i obté el la data d'expiració.
     * @param token L'String del token de l'usuari.
     * @return un String amb l'email.
     * @author Jordi Gómez Lozano.
     */
    @Nullable
    public long getExpireTimeFromToken(String token) {
        JWT parsedJWT = new JWT(token);
        Claim subscriptionMetaData = parsedJWT.getClaim(TOKEN_EXPIRATION);
        return subscriptionMetaData.asLong();
    }
}
