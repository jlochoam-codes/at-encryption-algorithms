package com.examples.encryption.AES;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

public class AESTest {

    private static final String PLAIN_TEXT_1 = "You should be able to read me flawlessly";
    private static final String PLAIN_TEXT_2 = "You should be able to read me flawlessly.";

    @Test
    public void testAesImplementation() {

        assertDoesNotThrow(
                () -> {
                    // Stage 0: Generate the encryption key (only done when necessary).
                    AES.generateKey();

                    // Stage 1: Encrypt a string of plain text.
                    byte[] cipherText1 = AES.encrypt(PLAIN_TEXT_1);
                    BufferedWriter cipherText1FileWriter = Files.newBufferedWriter(
                            Paths.get(AES.FILES_PATH + "cipherText1.txt"));
                    cipherText1FileWriter.write(Base64.getEncoder().encodeToString(
                            cipherText1));
                    cipherText1FileWriter.close();

                    // Stage 2: Decrypt the cipher text obtained from stage 1.
                    BufferedReader cipherText1FileReader = Files.newBufferedReader(
                            Paths.get(AES.FILES_PATH + "cipherText1.txt"));
                    byte[] cipherText1FromFile = Base64.getDecoder().decode(
                            cipherText1FileReader.readLine());
                    cipherText1FileReader.close();
                    String decryptedText1 = AES.decrypt(cipherText1FromFile);
                    BufferedWriter decryptedText1FileWriter = Files.newBufferedWriter(
                            Paths.get(AES.FILES_PATH + "decryptedText1.txt"));
                    decryptedText1FileWriter.write(decryptedText1);
                    decryptedText1FileWriter.close();

                    // Stages 1 and 2 will be repeated with PLAIN_TEXT_2, which only
                    // differs from PLAIN_TEXT_1 by 1 character. This is to evaluate
                    // the quality of the cipher considering small changes in the
                    // plain text should still provide quite different cipher texts.
                    byte[] cipherText2 = AES.encrypt(PLAIN_TEXT_2);
                    BufferedWriter cipherText2FileWriter = Files.newBufferedWriter(
                            Paths.get(AES.FILES_PATH + "cipherText2.txt"));
                    cipherText2FileWriter.write(Base64.getEncoder().encodeToString(
                            cipherText2));
                    cipherText2FileWriter.close();

                    BufferedReader cipherText2FileReader = Files.newBufferedReader(
                            Paths.get(AES.FILES_PATH + "cipherText2.txt"));
                    byte[] cipherText2FromFile = Base64.getDecoder().decode(
                            cipherText2FileReader.readLine());
                    cipherText2FileReader.close();
                    String decryptedText2 = AES.decrypt(cipherText2FromFile);
                    BufferedWriter decryptedText2FileWriter = Files.newBufferedWriter(
                            Paths.get(AES.FILES_PATH + "decryptedText2.txt"));
                    decryptedText2FileWriter.write(decryptedText2);
                    decryptedText2FileWriter.close();
                }
        );

    }

}
