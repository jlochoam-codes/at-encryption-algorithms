package com.examples.encryption.RSA;

import com.examples.encryption.AES.AES;
import org.junit.Test;

import javax.crypto.SecretKey;

import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class RSATest {

    private SecretKey keyFromBytes;

    private static final String PLAIN_TEXT_1 = "You should be able to read me flawlessly";
    private static final String PLAIN_TEXT_2 = "You should be able to read me flawlessly.";

    @Test
    public void testLoadPublicKey() {

        assertDoesNotThrow(
                () -> {

                }
        );
    }

    @Test
    public void testLoadPrivateKey() {

        assertDoesNotThrow(
                () -> {

                }
        );
    }

    @Test
    public void testSaveKey() {

        assertDoesNotThrow(
                () -> {

                }
        );
    }
}
