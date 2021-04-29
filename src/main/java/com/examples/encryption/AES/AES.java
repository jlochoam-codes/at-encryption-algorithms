package com.examples.encryption.AES;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
public class AES {

    private static final int KEY_SIZE = 128; // In bits
    private static final int GCM_IV_LEN = 12; // In bytes
    private static final int GCM_TAG_LEN = 16; // In bytes
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    public static final String FILES_PATH = "src/main/resources/static/AES/";

    public static void generateKey() throws
            IOException,
            NoSuchAlgorithmException {

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(KEY_SIZE);
        SecretKey key = keyGenerator.generateKey();
        BufferedWriter keyFileWriter = Files.newBufferedWriter(
                Paths.get(FILES_PATH + "keyFile.txt"));
        keyFileWriter.write(Base64.getEncoder().encodeToString(key.getEncoded()));
        keyFileWriter.close();

    }

    public static byte[] encrypt(String plainText)
            throws IOException,
            NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidAlgorithmParameterException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException {

        BufferedReader keyFileReader = Files.newBufferedReader(
                Paths.get(FILES_PATH + "keyFile.txt"));
        byte[] keyBytes = Base64.getDecoder().decode(keyFileReader.readLine());
        keyFileReader.close();
        SecretKeySpec key = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");

        byte[] iv = new byte[GCM_IV_LEN];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LEN * 8, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);
        byte[] cipherOutput = cipher.doFinal(plainText.getBytes());


        // The IV is attached to the beginning of the ciphertext.
        ByteArrayOutputStream cipherTextStream = new ByteArrayOutputStream();
        cipherTextStream.write(iv);
        cipherTextStream.write(cipherOutput);

        byte[] cipherTextBytes = cipherTextStream.toByteArray();
        cipherTextStream.close();
        return cipherTextBytes;

    }

    public static String decrypt(byte[] encryptedBytes)
            throws IOException,
            NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidAlgorithmParameterException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(ALGORITHM);

        BufferedReader keyFileReader = Files.newBufferedReader(
                Paths.get(FILES_PATH + "keyFile.txt"));
        byte[] keyBytes = Base64.getDecoder().decode(keyFileReader.readLine());
        keyFileReader.close();
        SecretKeySpec key = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");

        // We take the IV out of the ciphertext
        byte[] iv = new byte[GCM_IV_LEN];
        System.arraycopy(encryptedBytes, 0, iv, 0, iv.length);
        byte[] cipherText = new byte[encryptedBytes.length - GCM_IV_LEN];
        System.arraycopy(encryptedBytes, GCM_IV_LEN, cipherText, 0, cipherText.length);

        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LEN * 8, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);
        byte[] decryptedText = cipher.doFinal(cipherText);
        return new String(decryptedText);

    }

}
