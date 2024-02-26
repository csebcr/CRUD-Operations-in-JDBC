package postgres;

import java.util.Base64;

public class EncodeUtils {
    private EncodeUtils(){}

    public static String encodeString(String valueToEncode) {
        String encodedString = Base64.getEncoder().encodeToString(valueToEncode.getBytes());
        return encodedString;
    }

    public static String decodeString(String valueToDecode) {
        byte[] decodedBytes = Base64.getDecoder().decode(valueToDecode);
        String decodedString = new String(decodedBytes);
        return decodedString;
    }
}
