package controlador.gestor;

import androidx.annotation.Nullable;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {
    private static final String ROLE = "authorities";
    private static final String TOKEN_EXPIRATION = "exp";
    private static final String EMAIL = "user_name";
    private static final String ERROR_JSON_KEY = "Error";
    private static final String USER_TOKEN_JSON_KEY = "access_token";

    public JsonUtils() {}

    /**
     * Decodifica el token i obté l'email.
     * @param token L'String del token de l'usuari.
     * @return un String amb l'email.
     * @author Jordi Gómez Lozano.
     */
    @Nullable
    public static String getEmailFromToken(String token) {
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
    public static String getRoleFromToken(String token) {
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
    public static long getExpireTimeFromToken(String token) {
        JWT parsedJWT = new JWT(token);
        Claim subscriptionMetaData = parsedJWT.getClaim(TOKEN_EXPIRATION);
        return subscriptionMetaData.asLong();
    }

    /**
     * Obtinc la dada de l'error del JSON que rebo.
     * @param data dades del servidor.
     * @return l'error rebut pel servidor.
     * @throws JSONException
     * @author Jordi Gómez Lozano.
     */
    public static String getErrorInfo(String data) throws JSONException {
        JSONObject access_token = new JSONObject(data);
        return access_token.getString(ERROR_JSON_KEY);
    }

    /**
     * Obtinc la dada del token del JSON que rebo, aquest té altres dades.
     * @param data
     * @return un String amb el token
     * @throws JSONException
     * @author Jordi Gómez Lozano.
     */
    public static String obtenirToken(String data) throws JSONException {
        JSONObject access_token = new JSONObject(data);
        return access_token.getString(USER_TOKEN_JSON_KEY);
    }
}
