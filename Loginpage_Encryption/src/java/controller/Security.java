package controller;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import org.apache.commons.codec.binary.Base64;

public class Security {
    private static byte[] key = {
        0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 
        0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79
    };

    public static void init(ServletConfig config) throws ServletException {
        String keyString = config.getServletContext().getInitParameter("secretKey");
        key = keyString.getBytes();
    }

    public static String encrypt(String strToEncrypt, String cipherTransformation) {
        String encryptedString = null;
        try {
            Cipher cipher = Cipher.getInstance(cipherTransformation); //Bug here
            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            encryptedString = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return encryptedString;
    }
    
    //byte[] key
    public static String decrypt(String codeDecrypt, String cipherTransformation) {
        String decryptedString = null;
        try {
            Cipher cipher = Cipher.getInstance(cipherTransformation); //"AES/ECB/PKCS5Padding"
            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            decryptedString = new String(cipher.doFinal(Base64.decodeBase64(codeDecrypt)));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return decryptedString;
    }
}