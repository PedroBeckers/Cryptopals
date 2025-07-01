/*
Challenge 7: AES in ECB mode
The Base64-encoded content in 7.txt has been encrypted via AES-128 in ECB mode under the key "YELLOW SUBMARINE". Decrypt it.

Note:
As sugested in the exercise statement, I used a external decrypter. I didn't use the sugested OpenSSL, but i used java 
javax.crypto native package.
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Ex7 {

    // Returns a string from a byte array
    static String byteArray_to_string(byte[] ba){

        int len = ba.length;
        char[] ca = new char[len];

        for(int i = 0; i < len; i++){
            ca[i] = (char) (ba[i]);
        }

        return new String(ca);

    }

    // Returns the file full string, without breaking line
    static String readFile(String file) throws IOException{

        BufferedReader reader = new BufferedReader(new FileReader(file));

        int charNum = countCharNum(file);
        char[] ca = new char[charNum];

        int c;
        int i = 0;
        while((c = reader.read()) != -1){
            if(c == 10)
                continue;

            ca[i] = (char) c;
            i++;
        }
        
        return new String(ca);

    }
    
    // Receives a base64 string and returns its respective decoded byte array
    static byte[] b64String_to_byteArray(String s){

        int len = s.length();

        // Ensuring integer bytes quantity
        if(len % 4 != 0){
            throw new IllegalArgumentException("Invalid string length.");
        }

        len = len / 4;
        byte[] ret = new byte[decodedB64ByteArraySize(s)];
        int retIndex = 0;

        char[] c = new char[4];
        byte[] v = new byte[4];
        for(int i = 0; i < len; i++){

            c[0] = s.charAt(4 * i); 
            c[1] = s.charAt(4 * i + 1);
            c[2] = s.charAt(4 * i + 2);
            c[3] = s.charAt(4 * i + 3);
            
            // 6 bit values : 1 b64 character
            v[0] = b64Char_to_value(c[0]);
            v[1] = b64Char_to_value(c[1]);
            v[2] = b64Char_to_value(c[2]);
            v[3] = b64Char_to_value(c[3]);

            // treating each case of padding. 
            if(v[2] == -1 && v[3] == -1){
                ret[retIndex] = (byte) ((v[0] << 2) | (v[1] >> 4)); 
            }else if(v[3] == -1){
                ret[retIndex] = (byte) ((v[0] << 2) | (v[1] >> 4)); 
                ret[retIndex + 1] = (byte) ((v[1] << 4) | (v[2] >> 2));
            }else{
                ret[retIndex] = (byte) ((v[0] << 2) | (v[1] >> 4));
                ret[retIndex + 1] = (byte) ((v[1] << 4) | (v[2] >> 2)); 
                ret[retIndex + 2] = (byte) ((v[2] << 6) | (v[3]));
                retIndex += 3;
            }

        }

        return ret;
    }

    // Receives a base64 string and returns the number of bytes of the decoded string according to the padding system
    static int decodedB64ByteArraySize(String s){
        int len = s.length();
        int discount = 0;
        char c1 = s.charAt(len - 2);
        char c2 = s.charAt(len - 1);
        if(c1 == 61 && c2 == 61){
            discount = 2;
        }else if(c2 == 61){
            discount = 1;
        }
        return ((len * 3) / 4) - discount;
    }

    // Returns a value of the respective base64 char
    static byte b64Char_to_value(char c){

        byte ret;

        if(c >= 65 && c <= 90){
            ret = (byte) (c - 65);
        }else if(c >= 97 && c <= 122){
            ret = (byte) (c - 97 + 26);
        }else if(c >= 48 && c <= 57){
            ret = (byte) (c - 48 + 52);
        }else if(c == 43){
            ret = 62;
        }else if(c == 47){
            ret = 63;
        }else if(c == 61){ // treating padding case
            ret = -1;
        }else{
            throw new IllegalArgumentException("Invalid base64 character.");
        }

        return ret;

    }

    // Returns the number of characters in file, excluding new line characters
    static int countCharNum(String file) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(file)); // Warning. Does not cause any problem
        int total = 0;
        int c;
        while((c = reader.read()) != -1){
            total = c == 10 ? total : total + 1;
        }
        reader.close();
        return total;
    }

    public static void main(String[] args) throws Exception {
        
        // Getting relative file path
        String file = System.getProperty("user.dir") + "/7.txt";

        // Getting the fully concatenated, without new line, string
        String b64EncryptedString = readFile(file);

        // initializing input key and cipher
        String key = "YELLOW SUBMARINE";
        byte[] keyByteArray = key.getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(keyByteArray, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        
        // Decoding base64 into byte array and finding the most likely key size
        byte[] decodedB64 = b64String_to_byteArray(b64EncryptedString);

        // Decoding AES-128-ECB
        byte[] resultByteArray = cipher.doFinal(decodedB64);
        String result = byteArray_to_string(resultByteArray);

        // Printing results
        System.out.println("\n\nDecrypted text: " + result);
        System.out.println("Key: " + key + "\n\n");
        
    }
    
}
