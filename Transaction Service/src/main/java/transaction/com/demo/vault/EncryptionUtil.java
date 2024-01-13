package transaction.com.demo.vault;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Slf4j
public class EncryptionUtil {
    private static final String INIT_VECTOR = "oX8PmA5970UywqXy";
    private static final String AES_TRANSFORM = "AES/CBC/PKCS5PADDING";
    private static final String ENCRYPTION_DECRYPTION_ALGORITHM = "AES";

    protected static String encrypt(String key, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ENCRYPTION_DECRYPTION_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_TRANSFORM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            log.error("Error in encrypting key : {} ", value, ex);
        }
        return null;
    }

    protected static String decrypt(String key, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ENCRYPTION_DECRYPTION_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_TRANSFORM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
            return new String(original);
        } catch (Exception ex) {
            log.error("Error in decrypting key : {} ", encrypted, ex);
        }
        return null;
    }
}

