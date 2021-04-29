package com.examples.encryption.RSA;

import org.junit.Test;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class RSATest {

    private SecretKey keyFromBytes;

    @Test
    public void testLoadPublicKey() {

        assertDoesNotThrow(
                () -> {

                    RSA.loadPublicKey();
                }
        );
    }