package com.examples.encryption.AES;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class AESTest {

    private SecretKey key;

    private static final String PLAIN_TEXT_1 = "You should be able to read me flawlessly";
    private static final String PLAIN_TEXT_2 = "You should be able to read me flawlessly.";

    @Test
    public void testGenerateKey() {

        assertDoesNotThrow(
                () -> {
                    this.key = AES.generateKey();
                }
        );

    }

}
