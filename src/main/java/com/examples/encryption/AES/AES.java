package com.examples.encryption.AES;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AES {

    private static final int KEY_SIZE = 128; // In bits
    private static final int GCM_IV_LEN = 12; // In bytes
    private static final int GCM_TAG_LEN = 16; // In bytes
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final String FILES_PATH = "src/main/resources/static/";

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(KEY_SIZE);
        SecretKey key = keyGenerator.generateKey();
        try (
                FileOutputStream keyFileStream = new FileOutputStream(
                        FILES_PATH + "keyFile.txt"
                )
        ) {
            keyFileStream.write((key.getEncoded()));
        }
        catch(Exception e) {
            System.out.println("Key file could not be created.");
        }
        return key;
    }

    public static byte[] encrypt(String input, SecretKey key)
            throws IOException,
            NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidAlgorithmParameterException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException {

        byte[] iv = new byte[GCM_IV_LEN];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LEN * 8, iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);
        byte[] cipherOutput = cipher.doFinal(input.getBytes());


        // The IV is attached to the beginning of the ciphertext.
        ByteArrayOutputStream cipherText = new ByteArrayOutputStream();
        cipherText.write(iv);
        cipherText.write(cipherOutput);

        return cipherText.toByteArray();

    }

    public static String decrypt(byte[] cipherText, SecretKey key)
            throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidAlgorithmParameterException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

        // We take the IV out of the ciphertext
        byte[] iv = new byte[GCM_IV_LEN];
        System.arraycopy(cipherText, 0, iv, 0, GCM_IV_LEN);
        System.arraycopy(cipherText, GCM_IV_LEN, cipherText, 0, cipherText.length);

        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LEN * 8, iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);
        byte[] decryptedText = cipher.doFinal(cipherText);
        return new String(decryptedText);

    }

}
