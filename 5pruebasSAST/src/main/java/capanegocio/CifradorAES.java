
package capanegocio;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class CifradorAES {
    
    SecretKeySpec clave = null;
    private final SecureRandom secureRandom = new SecureRandom();
    private final static int GCM_IV_LENGTH = 12;
    
    public CifradorAES(){
        try {
            clave = crearClave("Esto es una clave muy complicada");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CifradorAES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CifradorAES.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//    
//     private SecretKeySpec crearClave(String clave) throws UnsupportedEncodingException, NoSuchAlgorithmException {
//        byte[] claveEncriptacion = clave.getBytes("UTF-8");
//        
//        MessageDigest sha = MessageDigest.getInstance("SHA-256");
//        
//        claveEncriptacion = sha.digest(claveEncriptacion);
//        claveEncriptacion = Arrays.copyOf(claveEncriptacion, 32);
//        
//        SecretKeySpec secretKey = new SecretKeySpec(claveEncriptacion, "AES");
//
//        return secretKey;
//    }
     
     private SecretKeySpec crearClave(String clave) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] claveEncriptacion = clave.getBytes("UTF-8");
        
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        
        claveEncriptacion = sha.digest(claveEncriptacion);
        claveEncriptacion = Arrays.copyOf(claveEncriptacion, 32);
        
        SecretKeySpec secretKey = new SecretKeySpec(claveEncriptacion, "AES");

        return secretKey;
    }

     
    public String cifrar(String plaintext) throws Exception {

        byte[] iv = new byte[GCM_IV_LENGTH]; //NEVER REUSE THIS IV WITH SAME KEY
        secureRandom.nextBytes(iv);
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv); //128 bit auth tag length
        cipher.init(Cipher.ENCRYPT_MODE, clave, parameterSpec);

        byte[] cipherText = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
        byteBuffer.put(iv);
        byteBuffer.put(cipherText);
        
        String textoCifrado = Base64.getEncoder().encodeToString(byteBuffer.array());
            
        return textoCifrado;
    } 
     
   
//    public String cifrar(String datos)  {
//        try {
//            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//            cipher.init(Cipher.ENCRYPT_MODE, clave);
//            
//            byte[] datosCifrar = datos.getBytes("UTF-8");
//            byte[] bytesCifrados = cipher.doFinal(datosCifrar);
//            String textoCifrado = Base64.getEncoder().encodeToString(bytesCifrados);
//            
//            return textoCifrado;
//        } catch (NoSuchAlgorithmException ex) {
//            Logger.getLogger(CifradorAES.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NoSuchPaddingException ex) {
//            Logger.getLogger(CifradorAES.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(CifradorAES.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InvalidKeyException ex) {
//            Logger.getLogger(CifradorAES.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IllegalBlockSizeException ex) {
//            Logger.getLogger(CifradorAES.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (BadPaddingException ex) {
//            Logger.getLogger(CifradorAES.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        return null;
//    }

    
//    public String descifrar(String datosCifrados) {
//        try {
//            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
//            cipher.init(Cipher.DECRYPT_MODE, clave);
//            
//            byte[] bytesCifrados = Base64.getDecoder().decode(datosCifrados);
//            byte[] datosDescifrados = cipher.doFinal(bytesCifrados);
//            String datos = new String(datosDescifrados);
//            
//            return datos;
//        } catch (NoSuchAlgorithmException ex) {
//            Logger.getLogger(CifradorAES.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NoSuchPaddingException ex) {
////            Logger.getLogger(CifradorAES.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InvalidKeyException ex) {
//            Logger.getLogger(CifradorAES.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IllegalBlockSizeException ex) {
////            Logger.getLogger(CifradorAES.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (BadPaddingException ex) {
////            Logger.getLogger(CifradorAES.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        return null;
//    }
    
    public String descifrar(String datosCifrados) throws Exception {
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        
        byte[] encodedBytes = Base64.getDecoder().decode(datosCifrados);
    //use first 12 bytes for iv
        AlgorithmParameterSpec gcmIv = new GCMParameterSpec(128, encodedBytes, 0, GCM_IV_LENGTH);
        cipher.init(Cipher.DECRYPT_MODE, clave, gcmIv);

        //use everything from 12 bytes on as ciphertext
        byte[] plainText = cipher.doFinal(encodedBytes, GCM_IV_LENGTH, encodedBytes.length - GCM_IV_LENGTH);

        return new String(plainText, StandardCharsets.UTF_8);
    }
    
}
