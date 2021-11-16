package controlador.gestor;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;

/**
 * Classe per encriptar dades, com ara el token.
 * @author Jordi Gómez Lozano.
 */
public class GestorSharedPreferences {
    private static final String SHARED_PREFERENCES_TOKEN_KEY = "token";
    private static final String SHARED_PREFERENCES_EXPIRED_TIME_KEY = "expired_time";
    private static final String SHARED_PREFERENCES_EMAIL_KEY = "user_name";
    private static final String SHARED_PREFERENCES_PASSWORD_KEY = "password";
    private static final String ARXIU = "InfoObt";
    private Context context;

    public GestorSharedPreferences(Context context){
        this.context = context;
    }

    /**
     * Crea l'arxiu on es guardaran les dades encriptades.
     * @return retorna l'arxiu on s'encriptaran les dades.
     */
    public SharedPreferences getEncryptedSharedPreferences(){

        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            return EncryptedSharedPreferences.create(
                    ARXIU,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );


        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void deleteData(){

        SharedPreferences sharedPreferences =  getEncryptedSharedPreferences();
        sharedPreferences.edit().remove(SHARED_PREFERENCES_TOKEN_KEY).apply();
        sharedPreferences.edit().remove(SHARED_PREFERENCES_EXPIRED_TIME_KEY).apply();

    }

    /**
     * Guarda les dades mentre duri la sessió de l'usuari.
     * @param token token de l'usuari
     * @param expiredTime data d'expiració del token
     * @author Jordi Gómez Lozano
     */
    public void saveData(String token, long expiredTime, String email, String password) {
        Date expiredDate = new Date(expiredTime *1000);

        SharedPreferences sharedPreferences =  getEncryptedSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREFERENCES_TOKEN_KEY, token);
        editor.putString(SHARED_PREFERENCES_EXPIRED_TIME_KEY, String.valueOf(expiredDate));
        editor.putString(SHARED_PREFERENCES_EMAIL_KEY, email);
        editor.putString(SHARED_PREFERENCES_PASSWORD_KEY, password);
        editor.apply();
    }

    /**
     * Obte la data d'expiració de l'arxiu.
     * @return un Date amb la data d'expiracio
     * @author Jordi Gómez Lozano
     */
    public Date getExpiredDate() {
        SharedPreferences sharedPreferences =  getEncryptedSharedPreferences();
        Date data = new Date(sharedPreferences.getString(SHARED_PREFERENCES_EXPIRED_TIME_KEY, null));
        return data;
    }

    /**
     * Obte el token de l'arxiu.
     * @return l'string corresponent del token
     * @author Jordi Gómez Lozano
     */
    public String getToken() {
        SharedPreferences sharedPreferences =  getEncryptedSharedPreferences();
        return sharedPreferences.getString(SHARED_PREFERENCES_TOKEN_KEY, null);
    }

    /**
     * Obte l'email de l'arxiu.
     * @return l'string corresponent del token
     * @author Jordi Gómez Lozano
     */
    public String getEmail() {
        SharedPreferences sharedPreferences =  getEncryptedSharedPreferences();
        return sharedPreferences.getString(SHARED_PREFERENCES_EMAIL_KEY, null);
    }

    /**
     * Obte la clau de l'arxiu.
     * @return l'string corresponent del token
     * @author Jordi Gómez Lozano
     */
    public String getPassword() {
        SharedPreferences sharedPreferences =  getEncryptedSharedPreferences();
        return sharedPreferences.getString(SHARED_PREFERENCES_PASSWORD_KEY, null);
    }

    /**
     * Obte la clau de l'arxiu.
     * @return l'string corresponent del token
     * @author Jordi Gómez Lozano
     */
    public void setPassword(String newPassword) {
        SharedPreferences sharedPreferences =  getEncryptedSharedPreferences();
        sharedPreferences.edit().putString(SHARED_PREFERENCES_PASSWORD_KEY, newPassword).apply();
    }

}
