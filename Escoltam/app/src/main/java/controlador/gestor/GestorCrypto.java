package controlador.gestor;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Classe per encriptar dades, com ara el token.
 * @author Jordi Gómez Lozano.
 */
public class GestorCrypto {

    private static final String ARXIU = "InfoObt";

    /**
     * Crea l'arxiu on es guardaran les dades encriptades.
     * @param context
     * @return retorna l'arxiu on s'encriptaran les dades.
     */
    public SharedPreferences getEncryptedSharedPreferences(Context context){

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

}
