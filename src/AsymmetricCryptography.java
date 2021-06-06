
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;


public class AsymmetricCryptography 
{
    private Cipher cipher;

    public AsymmetricCryptography() throws NoSuchAlgorithmException, NoSuchPaddingException 
    {
        this.cipher = Cipher.getInstance("RSA");
    }

    // https://docs.oracle.com/javase/8/docs/api/java/security/spec/PKCS8EncodedKeySpec.html
    public PrivateKey getPrivate(String filename) throws Exception 
    {
        byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    // https://docs.oracle.com/javase/8/docs/api/java/security/spec/X509EncodedKeySpec.html
    public PublicKey getPublic(String filename) throws Exception 
    {
        byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    
    public String encryptText(String msg, PrivateKey key) 
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            UnsupportedEncodingException, IllegalBlockSizeException, 
            BadPaddingException, InvalidKeyException {
        this.cipher.init(Cipher.ENCRYPT_MODE, key);
        return new String (cipher.doFinal(msg.getBytes("UTF-8")));
    }

    public String decryptText(String msg, PublicKey key)
            throws InvalidKeyException, UnsupportedEncodingException, 
            IllegalBlockSizeException, BadPaddingException,Exception {
        this.cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] bytes = Base64.decodeBase64 (msg.getBytes());
        int inputLen = bytes.length;
        int offLen = 0;
        int i = 0;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while(inputLen - offLen > 0){
            byte[] cache;
            if(inputLen - offLen > 128){
                cache = this.cipher.doFinal(bytes,offLen,128);
            }else{
                cache = this.cipher.doFinal(bytes,offLen,inputLen - offLen);
            }
            byteArrayOutputStream.write(cache);
            i++;
            offLen = 128 * i;

        }
        byteArrayOutputStream.close();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return new String(byteArray);
    }

    public static void main(String[] args) throws Exception
     {

        AsymmetricCryptography ac = new AsymmetricCryptography();

        PrivateKey privateKey = ac.getPrivate("KeyPair/privateKey");
        PublicKey publicKey = ac.getPublic("KeyPair/publicKey");

        String msg = "Exemple de message d'envoie d'un utlisateur";
        String encrypted_msg = ac.encryptText(msg, privateKey);
        String decrypted_msg = ac.decryptText(encrypted_msg, publicKey);
        System.out.println("Original Message: " + msg + 
            "\nEncrypted Message: " + encrypted_msg
            + "\nDecrypted Message: " + decrypted_msg);

        
    }
}