package gestor;

import android.util.Log;

import androidx.annotation.Nullable;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GestorRequest {

    private String Token;

    public GestorRequest() {}

    public String getToken() {
        return Token;
    }


    private String obtenirDades(){
        String dades = "";

        try {
            String urlParameters = "username=gemmarica94@gmail.com&password=12345&grant_type=password";
            byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
            int postDataLength = postData.length;
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
                dades = bytesToString(data);
                Log.i("Info", String.valueOf(conn.getResponseCode()));
                Log.i("Info" , bytesToString(data));
            }
            conn.disconnect();
        } catch (Exception e) {
            e.getMessage();
        }

        return dades;
    }

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
     * Decodifica el token i obte l'email.
     * @param token L'String del token de l'usuari.
     * @return un String amb l'email.
     * @author Jordi Gómez Lozano.
     */
    @Nullable
    private String getEmailFromToken(String token) {
        JWT parsedJWT = new JWT(token);
        Claim subscriptionMetaData = parsedJWT.getClaim("email");
        String decodedEmail = subscriptionMetaData.asString();
        Log.i("MainToken", "Decoded token, email: " +decodedEmail);
        return decodedEmail;
    }
}
