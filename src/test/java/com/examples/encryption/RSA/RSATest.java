package com.examples.encryption.RSA;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class RSATest {

    private static final String PLAIN_TEXT_1 = "You should be able to read me flawlessly";
    private static final String PLAIN_TEXT_2 = "You should be able to read me flawlessly.";

    @Test
    public void test1_RsaKeyGeneration() {

        assertDoesNotThrow(
                () -> {
                    RSA.generateKeyPair("FrontEnd");
                    RSA.generateKeyPair("BackEnd");
                }
        );

    }

    @Test
    public void test2_RsaImplementation() {

        assertDoesNotThrow(
                () -> {
                    byte[] cipherText1 = RSA.encrypt(PLAIN_TEXT_1);
                    BufferedWriter cipherText1FileWriter = Files.newBufferedWriter(
                            Paths.get(RSA.FILES_PATH + "cipherText1.txt"));
                    cipherText1FileWriter.write(Base64.getEncoder().encodeToString(
                            cipherText1));
                    cipherText1FileWriter.close();

                    BufferedReader cipherText1FileReader = Files.newBufferedReader(
                            Paths.get(RSA.FILES_PATH + "cipherText1.txt"));
                    byte[] cipherText1FromFile = Base64.getDecoder().decode(
                            cipherText1FileReader.readLine());
                    cipherText1FileReader.close();
                    String decryptedText1 = RSA.decrypt(cipherText1FromFile);
                    BufferedWriter decryptedText1FileWriter = Files.newBufferedWriter(
                            Paths.get(RSA.FILES_PATH + "decryptedText1.txt"));
                    decryptedText1FileWriter.write(decryptedText1);
                    decryptedText1FileWriter.close();

                    byte[] cipherText2 = RSA.encrypt(PLAIN_TEXT_2);
                    BufferedWriter cipherText2FileWriter = Files.newBufferedWriter(
                            Paths.get(RSA.FILES_PATH + "cipherText2.txt"));
                    cipherText2FileWriter.write(Base64.getEncoder().encodeToString(
                            cipherText2));
                    cipherText2FileWriter.close();

                    BufferedReader cipherText2FileReader = Files.newBufferedReader(
                            Paths.get(RSA.FILES_PATH + "cipherText2.txt"));
                    byte[] cipherText2FromFile = Base64.getDecoder().decode(
                            cipherText2FileReader.readLine());
                    cipherText2FileReader.close();
                    String decryptedText2 = RSA.decrypt(cipherText2FromFile);
                    BufferedWriter decryptedText2FileWriter = Files.newBufferedWriter(
                            Paths.get(RSA.FILES_PATH + "decryptedText2.txt"));
                    decryptedText2FileWriter.write(decryptedText2);
                    decryptedText2FileWriter.close();
                }
        );

    }

}