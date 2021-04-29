package com.examples.encryption.RSA;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.*;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSA {

    public static Cipher rsa;
    private static final String ALGORITHM = "RSA/ECB/PKCS1Padding";

    public static SecretKey generateKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        saveKey(publicKey, "publickey.dat");
        publicKey = loadPublicKey("publickey.dat");

        saveKey(privateKey, "privatekey.dat");
        privateKey = loadPrivateKey("privatekey.dat");

        rsa = Cipher.getInstance(ALGORITHM);

        String text = "Text to encrypt";

        rsa.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypted = rsa.doFinal(text.getBytes());

        for (byte b : encrypted) {
            System.out.print(Integer.toHexString(0xFF & b));
        }
        System.out.println();

        rsa.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytesDecrypted = rsa.doFinal(encrypted);
        String textDecrypt = new String(bytesDecrypted);

        System.out.println(textDecrypt);

        return ;
    }

    public static PublicKey loadPublicKey(String fileName) throws Exception {
        FileInputStream fis = new FileInputStream(fileName);
        int numBtyes = fis.available();
        byte[] bytes = new byte[numBtyes];
        fis.read(bytes);
        fis.close();

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        KeySpec keySpec = new X509EncodedKeySpec(bytes);
        PublicKey keyFromBytes = keyFactory.generatePublic(keySpec);
        return keyFromBytes;
    }

    public static PrivateKey loadPrivateKey(String fileName) throws Exception {
        FileInputStream fis = new FileInputStream(fileName);
        int numBtyes = fis.available();
        byte[] bytes = new byte[numBtyes];
        fis.read(bytes);
        fis.close();

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        KeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        PrivateKey keyFromBytes = keyFactory.generatePrivate(keySpec);
        return keyFromBytes;
    }

    public static void saveKey(Key key, String fileName) throws Exception {
        byte[] publicKeyBytes = key.getEncoded();
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(publicKeyBytes);
        fos.close();
    }
}