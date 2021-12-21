package controlador.gestor;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Classe que s'encarrega d'encriptar la clau per a ser enviada al servidor
 * @author Jordi Gómez Lozano
 */
public class GestorEncrypt {

    private static final String PUBLIC_KEY_BASE64_ENCODED = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCISLP98M/56HexX/9FDM8iuIEQozy6kn2JMcbZS5/BhJ+U4PZIChJfggYlWnd8NWn4BYr2kxxyO8Qgvc8rpRZCkN0OSLqLgZGmNvoSlDw80UXq90ZsVHDTOHuSFHw8Bv//B4evUNJBB8g9tpVxr6P5EJ6FMoR/kY2dVFQCQM4+5QIDAQAB";
    private static final String PRIVATE_KEY_BASE64_ENCODED = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIhIs/3wz/nod7Ff/0UMzyK4gRCjPLqSfYkxxtlLn8GEn5Tg9kgKEl+CBiVad3w1afgFivaTHHI7xCC9zyulFkKQ3Q5IuouBkaY2+hKUPDzRRer3RmxUcNM4e5IUfDwG//8Hh69Q0kEHyD22lXGvo/kQnoUyhH+RjZ1UVAJAzj7lAgMBAAECgYAVh26vsggY0Yl/Asw/qztZn837w93HF3cvYiaokxLErl/LVBJz5OtsHQ09f2IaxBFedfmy5CB9R0W/aly851JxrI8WAkx2W2FNllzhha01fmlNlOSumoiRF++JcbsAjDcrcIiR8eSVNuB6ymBCrx/FqhdX3+t/VUbSAFXYT9tsgQJBALsHurnovZS1qjCTl6pkNS0V5qio88SzYP7lzgq0eYGlvfupdlLX8/MrSdi4DherMTcutUcaTzgQU20uAI0EMyECQQC6il1Kdkw8Peeb0JZMHbs+cMCsbGATiAt4pfo1b/i9/BO0QnRgDqYcjt3J9Ux22dPYbDpDtMjMRNrAKFb4BJdFAkBMrdWTZOVc88IL2mcC98SJcII5wdL3YSeyOZto7icmzUH/zLFzM5CTsLq8/HDiqVArNJ4jwZia/q6Fg6e8KO2hAkB0EK1VLF/ox7e5GkK533Hmuu8XGWN6I5bHnbYd06qYQyTbbtHMBrFSaY4UH91Qwd3u9gAWqoCZoGnfT/o03V5lAkBqq8jZd2lHifey+9cf1hsHD5WQbjJKPPIb57CK08hn7vUlX5ePJ02Q8AhdZKETaW+EsqJWpNgsu5wPqsy2UynO";

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public GestorEncrypt(){
        generatePublicKey();
        generatePrivateKey();
    }

    /**
     * Mètode per a generar la clau publica
     * @author Jordi Gómez Lozano
     */
    private void generatePublicKey(){
        KeyFactory keyFactory;
        try {
            byte[] keyBytes =  Base64.decode(PUBLIC_KEY_BASE64_ENCODED, Base64.DEFAULT);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            keyFactory = KeyFactory.getInstance("RSA");
            publicKey= keyFactory.generatePublic(spec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mètode per a generar la clau privada
     * @author Jordi Gómez Lozano
     */
    private void generatePrivateKey(){
        KeyFactory keyFactory;
        try {
            byte[] keyBytes = Base64.decode(PRIVATE_KEY_BASE64_ENCODED, Base64.DEFAULT);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mètode per a encriptar
     * @author Jordi Gómez Lozano
     */
    public String RSAEncrypt(final String plain) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipherEncrypt = Cipher.getInstance("RSA");
        cipherEncrypt.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipherEncrypt.doFinal(plain.getBytes());
        return Base64.encodeToString(encryptedBytes, Base64.URL_SAFE | Base64.NO_WRAP);
    }

    /**
     * Mètode per a desencriptar
     * @author Jordi Gómez Lozano
     */
    public String RSADecrypt(final byte[] encryptedBytes) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipherDecrypt = Cipher.getInstance("RSA");
        cipherDecrypt.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipherDecrypt.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }
}
