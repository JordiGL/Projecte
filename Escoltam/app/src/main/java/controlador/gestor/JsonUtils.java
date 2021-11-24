package controlador.gestor;

import androidx.annotation.Nullable;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

public class JsonUtils {
    private static final String ROLE = "authorities";
    private static final String TOKEN_EXPIRATION = "exp";
    private static final String EMAIL = "user_name";

    public JsonUtils() {}

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
