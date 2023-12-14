package Backend;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionHelper {

    // Default encryption key
    public static String key = "1234567891011121";

    // Method to generate a random encryption key
    public static String generateKey(int keySize) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[keySize];
        random.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    // Method to encrypt data using AES
    public static String encrypt(String data) throws Exception {
        // Create a secret key from the encryption key
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

        // Generate a random Initialization Vector (IV)
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        // Initialize the AES cipher for encryption
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        // Encrypt the data and combine it with the IV
        byte[] encrypted = cipher.doFinal(data.getBytes("UTF-8"));
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

        // Return the Base64-encoded result as ciphertext
        return Base64.getEncoder().encodeToString(combined);
    }

    // Method to decrypt data using AES
    public static String decrypt(String encryptedData) throws Exception {
        try {
            // Decode the Base64-encoded input
            byte[] combined = Base64.getDecoder().decode(encryptedData);

            // Extract the IV
            byte[] iv = new byte[16];
            System.arraycopy(combined, 0, iv, 0, iv.length);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            // Calculate the size of the encrypted data
            int encryptedSize = combined.length - iv.length;
            byte[] encryptedBytes = new byte[encryptedSize];
            System.arraycopy(combined, iv.length, encryptedBytes, 0, encryptedSize);

            // Create a secret key from the encryption key
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            // Initialize the AES cipher for decryption
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            // Decrypt the data and return it as plaintext
            byte[] decrypted = cipher.doFinal(encryptedBytes);
            return new String(decrypted, "UTF-8");
        } catch (Exception e) {
            // Handle decryption errors and print stack trace
            e.printStackTrace();
            return null;
        }
    }
}
