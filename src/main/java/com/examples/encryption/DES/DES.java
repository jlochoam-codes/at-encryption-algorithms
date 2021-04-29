package com.examples.encryption.DES;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.*;

public class DES {

    public static final String FILES_PATH = "src/main/resources/static/";

    public static void generateKey(String [] args) throws Exception{

        String comand1 = "-c";
        String comand2 = "-d";

        //COMAND 1 o COMAND 2
        if ((comand1.equals(args[0]))||(comand2.equals(args[0]))){
            //leer clave por teclado
            try{
                InputStreamReader read_key = new InputStreamReader(System.in);
                BufferedReader buff_key = new BufferedReader(read_key);
                System.out.print("Write a key: ");
                String key = buff_key.readLine();

                //pasar clave a la clase SecretKey
                try{
                    SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
                    DESKeySpec kspec = new DESKeySpec(key.getBytes());
                    SecretKey ks = skf.generateSecret(kspec);

                    //Inicializar el cifrado
                    try{
                        Cipher encryption = Cipher.getInstance("DES");

                        //Escojo modo cifrado o descifrado segun sea el caso

                        if (comand1.equals(args[0])){
                            encryption.init(Cipher.ENCRYPT_MODE, ks);}//MODO CIFRAR
                        if (comand2.equals(args[0])){
                            encryption.init(Cipher.DECRYPT_MODE, ks);}//MODO DESCIFRAR


                        //Leer fichero

                        InputStream file = new FileInputStream( args[1] );
                        OutputStream fich_out = new FileOutputStream ( args[2] );

                        byte[] buffer = new byte[1024];
                        byte[] bloque_cifrado;
                        String textCrypted = new String();
                        int fin_file = -1;
                        int read;//numero de bytes leidos

                        read = file.read(buffer);

                        while( read != fin_file ) {
                            bloque_cifrado = encryption.update(buffer,0,read);
                            textCrypted = textCrypted + new String(bloque_cifrado,"ISO-8859-1");
                            read = file.read(buffer);
                        }

                        file.close();

                        bloque_cifrado = encryption.doFinal();
                        textCrypted = textCrypted + new String(bloque_cifrado,"ISO-8859-1");
                        //ISO-8859-1 es ISO-Latin-1

                        fich_out.write(textCrypted.getBytes("ISO-8859-1"));//escribir fichero

                    }
                    //Inicializacion de cifrado
                    catch(javax.crypto.NoSuchPaddingException nspe) {} //Instanciacion DES
                    catch(javax.crypto.IllegalBlockSizeException ibse) {}//metodo doFinal
                    catch(javax.crypto.BadPaddingException bpe) {}//metodo doFinal
                }
                //pasar clave a la clase SecretKey
                catch(java.security.InvalidKeyException ike) {}
                catch(java.security.spec.InvalidKeySpecException ikse) {}
                catch(java.security.NoSuchAlgorithmException nsae) {}
            }
            //leer del teclado la clave como String
            catch(java.io.IOException ioex) {}
        }
    }
}
