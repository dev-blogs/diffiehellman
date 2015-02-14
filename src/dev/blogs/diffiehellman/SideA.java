package dev.blogs.diffiehellman;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

public class SideA {
    private byte[] hashSecretKey = null;
     
    // The base used with the SKIP 1024 bit modulus
    private static final BigInteger g = BigInteger.valueOf(2);
 
    // The 1024 bit Diffie-Hellman modulus values used by SKIP
    private static final byte skip1024ModulusBytes[] = { (byte) 0xF4,
        (byte) 0x88, (byte) 0xFD, (byte) 0x58, (byte) 0x4E, (byte) 0x49,
        (byte) 0xDB, (byte) 0xCD, (byte) 0x20, (byte) 0xB4, (byte) 0x9D,
        (byte) 0xE4, (byte) 0x91, (byte) 0x07, (byte) 0x36, (byte) 0x6B,
        (byte) 0x33, (byte) 0x6C, (byte) 0x38, (byte) 0x0D, (byte) 0x45,
        (byte) 0x1D, (byte) 0x0F, (byte) 0x7C, (byte) 0x88, (byte) 0xB3,
        (byte) 0x1C, (byte) 0x7C, (byte) 0x5B, (byte) 0x2D, (byte) 0x8E,
        (byte) 0xF6, (byte) 0xF3, (byte) 0xC9, (byte) 0x23, (byte) 0xC0,
        (byte) 0x43, (byte) 0xF0, (byte) 0xA5, (byte) 0x5B, (byte) 0x18,
        (byte) 0x8D, (byte) 0x8E, (byte) 0xBB, (byte) 0x55, (byte) 0x8C,
        (byte) 0xB8, (byte) 0x5D, (byte) 0x38, (byte) 0xD3, (byte) 0x34,
        (byte) 0xFD, (byte) 0x7C, (byte) 0x17, (byte) 0x57, (byte) 0x43,
        (byte) 0xA3, (byte) 0x1D, (byte) 0x18, (byte) 0x6C, (byte) 0xDE,
        (byte) 0x33, (byte) 0x21, (byte) 0x2C, (byte) 0xB5, (byte) 0x2A,
        (byte) 0xFF, (byte) 0x3C, (byte) 0xE1, (byte) 0xB1, (byte) 0x29,
        (byte) 0x40, (byte) 0x18, (byte) 0x11, (byte) 0x8D, (byte) 0x7C,
        (byte) 0x84, (byte) 0xA7, (byte) 0x0A, (byte) 0x72, (byte) 0xD6,
        (byte) 0x86, (byte) 0xC4, (byte) 0x03, (byte) 0x19, (byte) 0xC8,
        (byte) 0x07, (byte) 0x29, (byte) 0x7A, (byte) 0xCA, (byte) 0x95,
        (byte) 0x0C, (byte) 0xD9, (byte) 0x96, (byte) 0x9F, (byte) 0xAB,
        (byte) 0xD0, (byte) 0x0A, (byte) 0x50, (byte) 0x9B, (byte) 0x02,
        (byte) 0x46, (byte) 0xD3, (byte) 0x08, (byte) 0x3D, (byte) 0x66,
        (byte) 0xA4, (byte) 0x5D, (byte) 0x41, (byte) 0x9F, (byte) 0x9C,
        (byte) 0x7C, (byte) 0xBD, (byte) 0x89, (byte) 0x4B, (byte) 0x22,
        (byte) 0x19, (byte) 0x26, (byte) 0xBA, (byte) 0xAB, (byte) 0xA2,
        (byte) 0x5E, (byte) 0xC3, (byte) 0x55, (byte) 0xE9, (byte) 0x2F,
        (byte) 0x78, (byte) 0xC7 };
 
    // The SKIP 1024 bit modulus
    private static final BigInteger p = new BigInteger(1, skip1024ModulusBytes);
    
    public void showHashSecretKey() {
    	for (int i = 0; i < hashSecretKey.length; i++) {
    		System.out.print(hashSecretKey[i] + " ");
    	}
    	System.out.println();
    }
     
    public void generateSecretKey(SideB sideB, Spy spy) {
        try {
            DHParameterSpec dhParams = new DHParameterSpec(p, g);
             
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH");
            keyGen.initialize(dhParams, new SecureRandom());
             
            KeyAgreement keyAgree = KeyAgreement.getInstance("DH");
            KeyPair pair = keyGen.generateKeyPair();
             
            keyAgree.init(pair.getPrivate());
     
            PublicKey A = pair.getPublic();
             
            String pAsValueAsHexBase64 = CipherMessage.convertBigIntegerToBase64HexString(p);
            String gAsValueAsHexBase64 = CipherMessage.convertBigIntegerToBase64HexString(g);
             
            String yAsValueAsHexBase64 = CipherMessage.convertBigIntegerToBase64HexString(((DHPublicKey) A).getY());
            
            String bKeyAsHexStringBase64 = sideB.generateSecretKey(pAsValueAsHexBase64, gAsValueAsHexBase64, yAsValueAsHexBase64);
            
            // spy intercepts parameters p, g, A and B
            spy.generateSecretKey(pAsValueAsHexBase64, gAsValueAsHexBase64, yAsValueAsHexBase64);
            
            BigInteger y = CipherMessage.convertBase64HexStringToBigInteger(bKeyAsHexStringBase64);        
            PublicKey B = TransportPublicKey.getKey(null, y, p, g, 0);
             
            keyAgree.doPhase(B, true);
             
            byte[] secretKey = keyAgree.generateSecret();
             
            MessageDigest hash = MessageDigest.getInstance("SHA-256");
            hashSecretKey = hash.digest(secretKey);
            hashSecretKey = Arrays.copyOf(hashSecretKey, 16);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
    public String encipherMessage(String message) {
        return Base64.encode( CipherMessage.encodeMessage(hashSecretKey, message));
    }
     
    public String decipherMessage(String encipheredMessage) {
    	String decryptedMessage = null;
    	try {
    		decryptedMessage = CipherMessage.decodeMessage(hashSecretKey, Base64.decode(encipheredMessage));
    	} catch (javax.crypto.BadPaddingException e) {
    		decryptedMessage = "Message can not be deciphered";
    	}
        return decryptedMessage;
    }
}
