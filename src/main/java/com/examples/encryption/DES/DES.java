package com.examples.encryption.DES;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class DES {

    public static final String FILES_PATH = "src/main/resources/static/";
    private static final int KEY_SIZE = 56;
    private static final String ALGORITHM = "DES/ECB/PKCS5Padding";

    public static void generateKey(String [] args) throws Exception{

        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(KEY_SIZE);
        SecretKey key = keyGen.generateKey();
        BufferedWriter keyFileWriter = Files.newBufferedWriter(
                Paths.get(FILES_PATH + "keyFile.txt"));
        keyFileWriter.write(Base64.getEncoder().encodeToString(key.getEncoded()));
        keyFileWriter.close();

        /* PASO 2: Crear cifrador */
        Cipher encryptor= Cipher.getInstance(ALGORITHM);

        /* PASO 3a: Inicializar cifrador en modo CIFRADO */
        encryptor.init(Cipher.ENCRYPT_MODE, key);

        /* Leer fichero de 1k en 1k y pasar fragmentos leidos al cifrador */
        byte[] bufferPlano = new byte[1000];
        byte[] bufferCifrado;

        String textoCifradoTotal = new String();
        FileInputStream in = new FileInputStream(args[0]);
        int bytesLeidos = in.read(bufferPlano,0, 1000);
        while(bytesLeidos != -1) {  // Mientras no se llegue al final del fichero
            bufferCifrado = encryptor.update(bufferPlano, 0 , bytesLeidos);  // Pasa texto claro leido al cifrador
            textoCifradoTotal = textoCifradoTotal + new String(bufferCifrado); // Acumular texto cifrado
            bytesLeidos = in.read(bufferPlano,0, 1000);
        }
        in.close();

        bufferCifrado = encryptor.doFinal(); // Completar cifrado (puede devolver texto)
        textoCifradoTotal = textoCifradoTotal + new String(bufferCifrado);


        System.out.println("--------------- TEXTO CIFRADO ---------------");
        System.out.println(textoCifradoTotal);   // Mostrar texto cifrado
        System.out.println("---------------------------------------------");

        System.out.println("--------------- TEXTO DESCIFRADO -------------");
        /* PASO 3b: Poner cifrador en modo DESCIFRADO */
        encryptor.init(Cipher.DECRYPT_MODE, key);
        byte[] textoDescifrado = encryptor.update(textoCifradoTotal.getBytes()); // Pasar texto al descifrador
        System.out.print( new String(textoDescifrado) );
        textoDescifrado = encryptor.doFinal(); // Completar descifrado (puede devolver texto)
        System.out.print( new String(textoDescifrado) );
        System.out.println("----------------------------------------------");
    }
}