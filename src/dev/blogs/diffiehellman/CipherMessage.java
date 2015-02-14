package dev.blogs.diffiehellman;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CipherMessage {
    public static byte[] encodeMessage(byte[] key, String message) {
        byte[] encodedMessageAsBytes = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] iv = digest.digest(key);
            iv = Arrays.copyOf(iv, 16);
 
            javax.crypto.spec.SecretKeySpec keyspec = new javax.crypto.spec.SecretKeySpec(key, "AES");
            javax.crypto.spec.IvParameterSpec ivspec = new javax.crypto.spec.IvParameterSpec(iv);
 
            javax.crypto.Cipher encryptCipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
 
            encodedMessageAsBytes = encryptCipher.doFinal(message.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return encodedMessageAsBytes;
    }
     
    public static String decodeMessage(byte[] key, byte[] encodedMessage) throws javax.crypto.BadPaddingException {
        String value = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] iv = digest.digest(key);
            iv = Arrays.copyOf(iv, 16);
 
            javax.crypto.spec.SecretKeySpec keyspec = new javax.crypto.spec.SecretKeySpec(key, "AES");
            javax.crypto.spec.IvParameterSpec ivspec = new javax.crypto.spec.IvParameterSpec(iv);
 
            javax.crypto.Cipher c = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(javax.crypto.Cipher.DECRYPT_MODE, keyspec, ivspec);
 
            byte[] decValue = c.doFinal(encodedMessage);
            value = new String(decValue);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return value;
    }
     
    private static String convertBigIntegerToString(BigInteger value) {
        return value.toString();
    }
     
    private static byte[] convertStringToBytes(String value) {
        return value.getBytes();
    }
     
    private static String convertStringToHexString(String value) {
        return new BigInteger(value).toString(16);
    }
     
    public static String convertBigIntegerToBase64HexString(BigInteger value) {
        String valueAsString = convertBigIntegerToString(value);
        String valueAsHexString = convertStringToHexString(valueAsString);
        return Base64.encode(convertStringToBytes(valueAsHexString));
    }
     
    private static String convertByteArrayToString(byte[] byteArray) {
        return new String(byteArray);
    }
     
    private static BigInteger convertStringToBigInteger(String value, int base) {
        return new BigInteger(value, base);
    }
     
    public static BigInteger convertBase64HexStringToBigInteger(String base64HexString) {
        byte[] decodedStringAsByteArrayOfHex = Base64.decode(base64HexString);
        String valueAsHex = convertByteArrayToString(decodedStringAsByteArrayOfHex);
        return convertStringToBigInteger(valueAsHex, 16);
    }
}
