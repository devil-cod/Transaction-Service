package transaction.com.demo.utils;

public class CipherUtil {

    private static AESCipher aesCipher;

    private CipherUtil() {
    }

    public static void initializeCipherUtils(String key, String algorithm) {
        CipherUtil.aesCipher = new AESCipher(key, algorithm);
    }

    public static AESCipher getAesCipher() {
        return aesCipher;
    }
}
