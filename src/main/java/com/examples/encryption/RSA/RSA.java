package com.examples.encryption.RSA;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSA {

    private static final int KEY_SIZE = 4096;
    private static final String ALGORITHM = "RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING";
    public static final String FILES_PATH = "src/main/resources/static/RSA/";

    public static void generateKeyPair(String owner) throws
            IOException,
            NoSuchAlgorithmException {

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(KEY_SIZE);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        BufferedWriter privateKeyFileWriter = Files.newBufferedWriter(
                Paths.get(FILES_PATH + owner + "/privateKey.txt"));
        privateKeyFileWriter.write(Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        privateKeyFileWriter.close();

        BufferedWriter publicKeyFileWriter = Files.newBufferedWriter(
                Paths.get(FILES_PATH + owner + "/publicKey.txt"));
        publicKeyFileWriter.write(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        publicKeyFileWriter.close();

    }

    // Information is encrypted by front end before sending it.
    public static byte[] encrypt(String plainText) throws
            IOException,
            InvalidKeySpecException,
            InvalidKeyException,
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            IllegalBlockSizeException,
            BadPaddingException {

        BufferedReader publicKeyBackEndFileReader = Files.newBufferedReader(
                Paths.get(FILES_PATH + "BackEnd/publicKey.txt"));
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyBackEndFileReader.readLine());
        publicKeyBackEndFileReader.close();
        PublicKey publicKeyBackEnd = KeyFactory.getInstance("RSA").generatePublic(
                new X509EncodedKeySpec(keyBytes));

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKeyBackEnd);
        return cipher.doFinal(plainText.getBytes());

    }

    // Information is decrypted by back end after receiving it.
    public static String decrypt(byte[] encryptedBytes) throws
            IOException,
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeySpecException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException {

        BufferedReader privateKeyBackEndFileReader = Files.newBufferedReader(
                Paths.get(FILES_PATH + "BackEnd/privateKey.txt"));
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyBackEndFileReader.readLine());
        privateKeyBackEndFileReader.close();
        PrivateKey privateKeyBackEnd = KeyFactory.getInstance("RSA").generatePrivate(
                new PKCS8EncodedKeySpec(keyBytes));

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKeyBackEnd);
        byte[] decryptedText = cipher.doFinal(encryptedBytes);
        return new String(decryptedText);

    }

}