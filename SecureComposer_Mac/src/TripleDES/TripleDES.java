/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TripleDES;

/**
 *
 * @author Jenn
 */

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;


public class TripleDES {
        private static Key key = null;
        public static Cipher cipher = null;
        private static String encText = "";
        private static String encodingType = "UTF-8";

    public TripleDES()
    {
        try
        {
            cipher = Cipher.getInstance("DESede");//DESede

        } catch (Exception e) {
            System.err.println("Installing SunJCE provider. " + e);
            Provider sunjce = new com.sun.crypto.provider.SunJCE();
            Security.addProvider(sunjce);
        }
    }
   
    public void doKey(String keyStr) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException
    {
        byte[] encryptKey = (keyStr).getBytes();
        //byte[] rawkey = new byte[(int) f.length()];
        DESedeKeySpec keyspec = new DESedeKeySpec(encryptKey);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
        key = keyfactory.generateSecret(keyspec);
        //System.out.println("secret key: " + key.toString());
    }
    public byte[] encrypt(String input, String keyStr)
    {
        byte[] ciphertext = null;
        try {
            doKey(keyStr);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] inputBytes = input.getBytes(encodingType);
            ciphertext = cipher.doFinal(inputBytes);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(TripleDES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(TripleDES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(TripleDES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TripleDES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(TripleDES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(TripleDES.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ciphertext;
    }
    public String decrypt(byte[] encryptionBytes, String keyStr) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {
        try {
            doKey(keyStr);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(TripleDES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(TripleDES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TripleDES.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] recoveredBytes = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            recoveredBytes = cipher.doFinal(encryptionBytes);
        }catch(BadPaddingException ex) {
            return "";
        }
        String recovered = new String(recoveredBytes);
        return recovered;
    }
    public char[] hexChar = {
	        '0' , '1' , '2' , '3' ,
	        '4' , '5' , '6' , '7' ,
	        '8' , '9' , 'A' , 'B' ,
	        'C' , 'D' , 'E' , 'F'
   };


  public String toHexString ( byte[] b ) {

	    StringBuffer sb = new StringBuffer( b.length * 2 );
	    for ( int i=0; i<b.length; i++ ) {
	        // look up high nibble char
	        sb.append( hexChar [( b[i] & 0xf0 ) >>> 4] ); // fill left with zero bits

	        // look up low nibble char
	        sb.append( hexChar [b[i] & 0x0f] );
	    }
	    return sb.toString();
   }
  public byte[] toBinArray( String hexStr ){
	    byte bArray[] = new byte[hexStr.length()/2];
	    for(int i=0; i<(hexStr.length()/2); i++){
	    	byte firstNibble  = Byte.parseByte(hexStr.substring(2*i,2*i+1),16); // [x,y)
	    	byte secondNibble = Byte.parseByte(hexStr.substring(2*i+1,2*i+2),16);
	    	int finalByte = (secondNibble) | (firstNibble << 4 ); // bit-operations only with numbers, not bytes.
	    	bArray[i] = (byte) finalByte;
	    }
	    return bArray;
	}
}