package com.huhoot.encrypt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huhoot.dto.AnswerResponse;
import com.huhoot.organize.AnswerResultResponse;
import com.huhoot.organize.PublishAnswer;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class EncryptUtils {

    @Value("${huhoot.secret-key}")
    private String SECRET_KEY = null;

    public String encrypt(double value, byte[] bytes) {
        Key key = this.genKeyFromByte(bytes);
        return this.encrypt(String.valueOf(value), key);
    }

    public String encrypt(String value, byte[] bytes) {
        Key key = this.genKeyFromByte(bytes);
        return this.encrypt(value, key);
    }

    public String encrypt(double value, Key key) {
        return this.encrypt(String.valueOf(value), key);
    }

    public String encrypt(long value, Key key) {
        return this.encrypt(String.valueOf(value), key);
    }

    public String encrypt(int value, Key key) {
        return this.encrypt(String.valueOf(value), key);
    }

    public String decrypt(String encryptedValue, byte[] bytes) {
        Key key = this.genKeyFromByte(bytes);
        return this.decrypt(encryptedValue, key);
    }

    public Key generateKeyFromString(String s) {
        return this.getKeyFromPassword(s, this.SECRET_KEY);
    }

    public Key generateRandomKey() {
        return this.getKeyFromKeyGenerator("AES", 128);
    }

    public String genKeyForJsSide(byte[] bytes) {
        Key key = new SecretKeySpec(bytes, "AES");
        return Base64.encodeBase64String(key.getEncoded());
    }

    public Key genKeyFromByte(byte[] bytes) {
        return new SecretKeySpec(bytes, "AES");
    }

    public String encrypt(String value, Key key) {

        byte[] b1 = value.getBytes();
        byte[] encryptedValue;
        try {
            Cipher ecipher = Cipher.getInstance("AES");
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            encryptedValue = ecipher.doFinal(b1);
            return Base64.encodeBase64String(encryptedValue);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }


    public String decrypt(String encryptedValue, Key key) {
        byte[] decryptedValue = Base64.decodeBase64(encryptedValue.getBytes());
        byte[] decValue;
        try {
            Cipher dcipher = Cipher.getInstance("AES");
            dcipher.init(Cipher.DECRYPT_MODE, key);

            decValue = dcipher.doFinal(decryptedValue);
            return new String(decValue);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }


    public Key getKeyFromPassword(String password, String salt) {

        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 128);
            SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
                    .getEncoded(), "AES");
            return secret;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {

            return null;
        }
    }

    public String genEncryptedResponse(double pointReceive, int comboCount, byte[] bytesKey) throws JsonProcessingException {
        AnswerResponse ar = new AnswerResponse(pointReceive, comboCount);

        String json = new ObjectMapper().writeValueAsString(ar);


        String encrypt = this.encrypt(json, bytesKey);
        return encrypt;

    }


    private Key getKeyFromKeyGenerator(String cipher, int keySize) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(cipher);
            keyGenerator.init(keySize);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }


    private Key genKeyForComboToken(String username,  int questionOrder) {
        String s = new StringBuilder(username)
                .append("-")
                .append(questionOrder)
                .toString();
        return this.generateKeyFromString(s);
    }

    public String prepareComboToken(String username, int publishedOrderNumber, int comboCount) {
        Key key = this.genKeyForComboToken(username, publishedOrderNumber);
        return this.encrypt(comboCount, key);

    }

    public int extractCombo(String comboToken, String username, int publishedOrderNumber) {
        Key key = this.genKeyForComboToken(username, publishedOrderNumber);
        try {
            String decrypted = this.decrypt(comboToken, key);
            return Integer.parseInt(decrypted);
        } catch (Exception e) {
            return 0;
        }

    }

    // generate key for student sent answer, use answerIds key to decrypt questionToken to get answerTime limit


    public Key createKeyFromAnswerResultResponses(List<PublishAnswer> answers) {

        List<Integer> collect = answers.stream()
                .filter(PublishAnswer::getIsCorrect)
                .map(e -> e.getId())
                .collect(Collectors.toList());

        return this.genKeyFromAnswerIds(collect);

    }

    public Key genKeyFromAnswerIds(List<Integer> collect) {
        String s = collect.stream()
                .sorted(Integer::compareTo)
                .map(String::valueOf)
                .collect(Collectors.joining("-"));
        return this.generateKeyFromString(s);
    }

    public String encryptCorrectAnswer(long answerTimeLimit, Key key) {
        return this.encrypt(answerTimeLimit, key);
    }


    public String generateQuestionToken(List<PublishAnswer> publishAnswers, long askDate, int answerTimeLimit) {
        Key key = this.createKeyFromAnswerResultResponses(publishAnswers);
        return this.encryptCorrectAnswer(askDate + answerTimeLimit * 1000, key);


    }

    public Long decryptQuestionToken(String questionToken, List<Integer> ids) {
        Key key = this.genKeyFromAnswerIds(ids);

        try{
            String decrypted = this.decrypt(questionToken, key);

            return Long.valueOf(decrypted);

        }catch (Exception e){
            log.error(e.getMessage());

            return null;
        }
    }
}