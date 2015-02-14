package dev.blogs.diffiehellman;

import java.math.BigInteger;

import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

public class TransportPublicKey implements DHPublicKey {
    private byte[] encoded;
    private BigInteger y;
    private DHParameterSpec dHParameterSpec;
     
    private TransportPublicKey(byte[] encoded, BigInteger y, DHParameterSpec dHParameterSpec) {
        this.encoded = encoded;
        this.y = y;
        this.dHParameterSpec = dHParameterSpec;
    }
     
    public static DHPublicKey getKey(byte[] encoded, BigInteger y, BigInteger p, BigInteger g, int l) {
        DHParameterSpec dHParameterSpec = new DHParameterSpec(p, g, l);
        return new TransportPublicKey(encoded, y, dHParameterSpec);
    }
 
    @Override
    public String getAlgorithm() {
        return "";
    }
 
    @Override
    public String getFormat() {
        return "";
    }
 
    @Override
    public byte[] getEncoded() {        
        return null;
    }
 
    @Override
    public DHParameterSpec getParams() {
        return this.dHParameterSpec;
    }
 
    @Override
    public BigInteger getY() {
        return this.y;
    }   
}
