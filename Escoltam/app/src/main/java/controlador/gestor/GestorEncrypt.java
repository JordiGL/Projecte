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

    private static final String PUBLIC_KEY_BASE64_ENCODED = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0pqDLzG01d4KQCOxa3nK\r\n"
            + "EGuwho/JPwHydUE8dliCQiJFc6qsumf8N9Ui2iznWwhkGYTAICRnD89Bmnxj/8z8\r\n"
            + "Xjas/M3ar5ZsoyAJWVomuqYYsRaBrm8NGQ/XMYJ5e2ydGXCvZVP217hwMFP3ecv1\r\n"
            + "Q1naco0MbUdb2Nm9+OBe+q78hdwkTVZC+yPXofc1uc+kewoyccLlhWpy8xOwiN1A\r\n"
            + "MI5H4vGmWxTygB5+BNA5a0oyWNDCJRHZzuwOywWm45IYZT/FX5HHZ1lOZrzHreZZ\r\n"
            + "WJ8qsPBbxpDuNkdL8YPWMefSKN3fpIS1twY0s/iqg022goRC3FyAgsJ6NdV0ixDS\r\n"
            + "MQIDAQAB\r\n";
    private static final String PRIVATE_KEY_BASE64_ENCODED = "MIIEogIBAAKCAQEA0pqDLzG01d4KQCOxa3nKEGuwho/JPwHydUE8dliCQiJFc6qs\r\n"
            + "umf8N9Ui2iznWwhkGYTAICRnD89Bmnxj/8z8Xjas/M3ar5ZsoyAJWVomuqYYsRaB\r\n"
            + "rm8NGQ/XMYJ5e2ydGXCvZVP217hwMFP3ecv1Q1naco0MbUdb2Nm9+OBe+q78hdwk\r\n"
            + "TVZC+yPXofc1uc+kewoyccLlhWpy8xOwiN1AMI5H4vGmWxTygB5+BNA5a0oyWNDC\r\n"
            + "JRHZzuwOywWm45IYZT/FX5HHZ1lOZrzHreZZWJ8qsPBbxpDuNkdL8YPWMefSKN3f\r\n"
            + "pIS1twY0s/iqg022goRC3FyAgsJ6NdV0ixDSMQIDAQABAoIBACd+dk9pYOIia4Wv\r\n"
            + "q9vd0PjXWRlC6Zh5QwCeJTqESpkgWCnivdWPHotR2uFTPmeh4TOv41+js/gRA9p2\r\n"
            + "rUIRLNp2ImaeK/51lRXaMiahc1XZRBQhhlwx0yD6CX7tWt68dZGJwjqcTlQR8Gf/\r\n"
            + "tw8OIoZo2UBWFxh/I0g9PRTo7qbPxrxN4bu7MmtK2gm4N8pW5h3nkKcJ1+LOr68C\r\n"
            + "0SToiHRyyBXuzTYDiIowyxKAKS9glMXb4REGIJVnYYcktqsXVHmHAfunB+iTSkDH\r\n"
            + "W3tenKHWRe4cnyAobGipcwZ0dzHAPsmbiA99D2KnkkSeJU66Oz96QWtVftNquW7M\r\n"
            + "u5Q9kOECgYEA8meZodsS/lYalKJvdXvez62boDTlAQX1LEYWmb0HAwcmmBnIX423\r\n"
            + "fSXSZYtmrsnRCKDcMSXlnhwBGUBqDc3rqkdV2n3PINCw/lS87LzNSO8U8WrRJqWf\r\n"
            + "oQTQl7S9r2Eji9aUaXOSedQqBf+BCvzm7K57bPTx7Lu5wn13q72n8TUCgYEA3mpR\r\n"
            + "YaBcYsvWM10ifGh2DNF6xBeSIrllJLqvLoxP8NIEu7ov1cMPQFNP8ZQh2fX2463c\r\n"
            + "LRPaVomiM5qO+p1LdDkWNlBt9yARh0SqfRvMHkaRhODrJggUvgb5OyJUor/H+KD5\r\n"
            + "0J0Saze+NbMqzcp7vmtlLUX3F7yrjGdgRq2kGI0CgYADw+w1AWwoGzpKeMN2dbge\r\n"
            + "TboPIQM1otwOFgCf3rnPKDtHPY9JPLBO91lvTl0H9gaTNbx13vrUG+dAdymukTZJ\r\n"
            + "7NW+kRm4jsVRDgvpOX2O1nfi0B/zk7sJC0QaNyqDIOUmdOzVPoBV+B+2/CMLAqar\r\n"
            + "ooI0TRG89aWLCdvkBQwoyQKBgElegxgUYUWJ/lAVUhH+F6sk0z2qlN91jwLtvmo5\r\n"
            + "iQllDoCq66q1XOuYUA/eEMio1dfHe5E3MlhHsMUbID1V9BUT29rw70WNQ5r+sMry\r\n"
            + "h4QZPVm7JHb4RXdmL4MtUQQbBalj2OTTW8cYrVtjjiQyvl1Tah3kLZ2b9G8tjtnh\r\n"
            + "3/blAoGAP7Q6NI49ND+XqbtBn5cQBslew7S6L52AVCijEfGC9raZI6Q3zAIfdjST\r\n"
            + "4nazDZYQIOsmn0Y8BSKpiYi94ZuquIMiukGIQDiYaPEujfxRX9cLre8tBbE/fKkp\r\n"
            + "2ykfmlVcBKxyZPNmtXpeX+HgmEN/mEJLuI1PnLxwuz+3m4Be2i8=";

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
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
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
