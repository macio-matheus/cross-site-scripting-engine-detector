package utilsLib.util;

import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.util.ArrayList;

public class Crypt {
    Cipher ecipher;
    Cipher dcipher; // 8-byte Salt
    String key;
    byte[] salt = {(byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
                  (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03};

    // Iteration count
    int iterationCount = 19;
    public Crypt(String key) {
        this.key = key;
        initEncryptor(key);
    }

    public void initEncryptor(String pass) {
        try {
            // Create the key
            KeySpec keySpec = new PBEKeySpec(pass.toCharArray(), salt,
                                             iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").
                            generateSecret(keySpec);
            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());
            // Prepare the parameter to the ciphers
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
                    iterationCount);
            // Create the ciphers
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        } catch (java.security.InvalidAlgorithmParameterException e) {} catch (
                java.security.spec.InvalidKeySpecException e) {} catch (javax.
                crypto.NoSuchPaddingException e) {} catch (java.security.
                NoSuchAlgorithmException e) {} catch (java.security.
                InvalidKeyException e) {}
    }


    /**
     *
     * @param str String para encriptar.
     * @return    O encriptado
     */
    public String encrypt(String str) {
        try {
            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8");
            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);
            // Encode bytes to base64 to get a string

            // Deembaralha
            //return Utils.shuffleLetters(new sun.misc.BASE64Encoder().encode(enc), key);
        } catch (javax.crypto.BadPaddingException e) {
//            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
        } catch (java.io.IOException e) {
//            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param str Strubg oara desubcruotar.
     * @return    O Decriptado ou retorna nulo caso não consiga decriptar.
     */
    public String decrypt(String str) {
        try {
            // Embaralha
            str = Utils.shuffleLetters(str, key);

            // Decode base64 to get bytes
            byte[] dec = null;//new sun.misc.BASE64Decoder().decodeBuffer(str);
            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);
            // Decode using utf-8
            return new String(utf8, "UTF8");
        } catch (javax.crypto.BadPaddingException e) {
//            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
        } catch (java.io.IOException e) {
//            e.printStackTrace();
        }
        return null;
    }

    /** @todo melhorar este teste */
    public static void test(String key, int wordsCount, int lettersCount, String wordsPath,
                            String cryptPath, String decryptPath) throws
            IOException {
        Crypt c = new Crypt(key);

        ArrayList words = new ArrayList();
        ArrayList crypts = new ArrayList();
        ArrayList decrypts = new ArrayList();
        int diff = ('z' - 'a') + 1;

        System.out.println("[1] Criando palavras e criptografando-as");
        for (int i = wordsCount; i > 0 ; i--) {
            String word = "";
            for (int j = 1; j < lettersCount; j++) {
                word += (char) (((int) 'a') + ((i * j + i + j) % diff));
            }
            words.add(word);
            crypts.add(c.encrypt(word));
        }

        System.out.println("[2] Escrevendo nos arquivos");
        Utils.writeStrFile(wordsPath, Utils.toStr(words, "\n"));
        Utils.writeStrFile(cryptPath, Utils.toStr(crypts, "\n"));

        System.out.println("[3] Recriando encriptador");

        c = new Crypt(key);

        System.out.println("[4] Lendo e descriptografando");
        String wordsEncrypted = Utils.readStrFile(
                cryptPath);
        String[] wordsEncryptedA = Utils.getLines(wordsEncrypted, '\n');
        ArrayList wordsEncryptedAl = new ArrayList();

        for (int i = 0; i < wordsEncryptedA.length; i++) {
            wordsEncryptedAl.add(c.decrypt(wordsEncryptedA[i]));
        }

        System.out.println("[5] Escrevendo arquivo");
        Utils.writeStrFile(decryptPath,
                           Utils.toStr(wordsEncryptedAl, "\n"));
    }
}
