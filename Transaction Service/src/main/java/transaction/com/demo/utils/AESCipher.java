package transaction.com.demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AESCipher {

    private static final Logger LOG = LoggerFactory.getLogger(AESCipher.class);
    private final String key;
    private final String algorithm;

    public AESCipher(String key, String algorithm) {
        this.key = key;
        this.algorithm = algorithm;
    }

    public String encryptValue(String valueToEncrypt) {
        if (valueToEncrypt == null)
            return null;
        try {
            SecretKey secretkey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretkey);

            byte[] encryptedBytes = cipher.doFinal(valueToEncrypt.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            LOG.error("Failed to encrypt the string {}", valueToEncrypt);
        }
        return null;
    }

    public String decryptKey(String encryptedKey) {
        if (encryptedKey == null)
            return null;
        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedKey);
            SecretKey Secretkey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, Secretkey);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOG.error("Failed to decrypt the string {}", encryptedKey);
        }
        return null;
    }
}
