package com.huhoot.host.organize;

import com.google.common.primitives.Bytes;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EncryptUtil {


    public static String encrypt(String value, byte[] KEY) {

        byte[] b1 = value.getBytes();
        byte[] encryptedValue;
        try {
            Cipher ecipher = Cipher.getInstance("AES");
            SecretKeySpec eSpec = new SecretKeySpec(KEY, "AES");
            ecipher.init(Cipher.ENCRYPT_MODE, eSpec);
            encryptedValue = ecipher.doFinal(b1);
            return Base64.encodeBase64String(encryptedValue);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
    public static String decrypt(String encryptedValue, byte[] KEY) {
        byte[] decryptedValue = Base64.decodeBase64(encryptedValue.getBytes());
        byte[] decValue;
        try {
            Cipher dcipher = Cipher.getInstance("AES");
            SecretKeySpec dSpec = new SecretKeySpec(KEY, "AES");
            dcipher.init(Cipher.DECRYPT_MODE, dSpec);

            decValue = dcipher.doFinal(decryptedValue);
            return new String(decValue);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static byte[] generateRandomKeyStore() {

        Byte[] KEY = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P'};

        List<Byte> listByte = Arrays.asList(KEY);

        Collections.shuffle(listByte);

        return Bytes.toArray(listByte);
    }

    public static String genKeyForJsSide(byte[] key) {
        return Base64.encodeBase64String(key);
    }

}