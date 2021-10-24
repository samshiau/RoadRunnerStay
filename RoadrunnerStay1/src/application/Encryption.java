package application;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
 
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Part of the application.model package. Contains methods to encrypt and decrypt a string passed to the parameters.
 * The code for encryption was obtained from this source and is modified for the project: 
 * https://howtodoinjava.com/java/java-security/java-aes-encryption-example/.
 * 
 */
public class Encryption {
 
    private static SecretKeySpec secretKey;
    private static byte[] key;
 
    /**
     * Sets the key.
     * 
     * @param myKey the key for encrypting and decrypting.
     */
    private static void setKey(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); 
            secretKey = new SecretKeySpec(key, "AES");
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
 
    /**
     * Encrypts the string and returns it as a cipher string. The maximum characters for the cipher is 44.
     * 
     * @param strToEncrypt  the message to encrypt.
     * @param secret        the secret key for encryption.
     * @return              the encrypted message.
     */
    public static String encrypt(String strToEncrypt, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } 
        catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
 
    /**
     * Decrypts the encrypted string.
     * 
     * @param strToDecrypt  the encrypted string
     * @param secret        the key used to encrypt the message.
     * @return              the decrypted message.
     */
    public static String decrypt(String strToDecrypt, String secret)  {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    /*
    public static void main(String[] args) {
        final String secretKey = "ssshhhhhhhhhhh!!!!";
        Scanner input = new Scanner(System.in);
        
        System.out.print("Enter a message: ");
        String originalString = input.nextLine();
        String encryptedString = encrypt(originalString, secretKey) ;
        String decryptedString = decrypt(encryptedString, secretKey) ;
        
        System.out.println("Original: " + originalString);
        System.out.println("Encrypted: " + encryptedString);
        System.out.println("Decrypted: " + decryptedString);
        System.out.println("Encrypted length: " + encryptedString.length());
        System.out.println("Decrypted length: " + decryptedString.length());
        input.close();
    }
    */
}