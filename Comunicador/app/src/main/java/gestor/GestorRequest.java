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
import java.nio.charset.StandardCharsets;

/**
 * Classe que fa al servidor la petició del token.
 * @author Jordi Gómez Lozano
 */
public class GestorRequest {
    private String token;

    public GestorRequest() {}

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
        Connexio conn = new Connexio();
        String metodeDeLaPeticio = "POST";
        String requestUrl = "http://10.0.2.2:8080/oauth/token";
        String urlParameters = "username="+username+"&password="+clau+"&grant_type=password";
        byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int postDataLength = postData.length;
        conn.setConn(conn.postRequest(postDataLength, metodeDeLaPeticio, requestUrl));
        HttpURLConnection connexio = conn.getConn();

        try( DataOutputStream wr = new DataOutputStream(connexio.getOutputStream())) {

            wr.write(postData);
            InputStream data = connexio.getInputStream();

            responseCode = connexio.getResponseCode();

            token = obtenirToken(bytesToString(data));

        }catch (Exception e){
            e.printStackTrace();
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
    public String getEmailFromToken(String token) {
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
        Claim subscriptionMetaData = parsedJWT.getClaim("exp");
        return subscriptionMetaData.asLong();
    }

    //    /**
//     * Obtinc la dada del token del JSON que rebo, aquest té altres dades.
//     * @param data
//     * @return
//     * @throws JSONException
//     * @author Jordi Gómez Lozano.
//     */
//    private int obtenirExpiresIn(String data) throws JSONException {
//        JSONObject access_token = new JSONObject(data);
//        return access_token.getInt("expires_in");
//    }
}
