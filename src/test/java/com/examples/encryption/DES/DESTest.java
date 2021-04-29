package com.examples.encryption.DES;

import com.examples.encryption.AES.AES;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class DESTest {

    private static final String PLAIN_TEXT_1 = "You should be able to read me flawlessly";
    private static final String PLAIN_TEXT_2 = "You should be able to read me flawlessly.";

    @Test
    public void testDesImplementation() {

        assertDoesNotThrow(
                () -> {
                    // Stage 0: Generate the encryption key (only done when necessary).
                    DES.generateKey();
    }
}
