/*
Challenge 6: Break repeating-key XOR
It's been base64'd after being encrypted with repeating-key XOR. Decrypt it.
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Ex6 {

    // Returns a string from a byte array
    static String byteArray_to_string(int[] ba){

        int len = ba.length;
        char[] ca = new char[len];

        for(int i = 0; i < len; i++){
            ca[i] = (char) (ba[i]);
        }

        return new String(ca);

    }

    // Recieves a base64 string and returns its respective decoded byte array
    static int[] b64String_to_byteArray(String s){

        int len = s.length();

        // Ensuring integer bytes quantity
        if(len % 4 != 0){
            throw new IllegalArgumentException("Invalid string length.");
        }

        len = len / 4;
        int[] ret = new int[decodedB64ByteArraySize(s)];
        int retIndex = 0;

        char[] c = new char[4];
        int[] v = new int[4];
        int byteMask = 255;
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
                ret[retIndex] = (v[0] << 2) | (v[1] >> 4); 
            }else if(v[3] == -1){
                ret[retIndex] = (v[0] << 2) | (v[1] >> 4); 
                ret[retIndex + 1] = ((v[1] << 4) & byteMask) | (v[2] >> 2);
            }else{
                ret[retIndex] = (v[0] << 2) | (v[1] >> 4);
                ret[retIndex + 1] = ((v[1] << 4) & byteMask) | (v[2] >> 2); 
                ret[retIndex + 2] = ((v[2] << 6) & byteMask) | (v[3]);
                retIndex += 3;
            }

        }

        return ret;
    }

    // Recieves a base64 string and returns the number of bytes of the decoded string according to the padding system
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
    static int b64Char_to_value(char c){

        int ret;

        if(c >= 65 && c <= 90){
            ret = c - 65;
        }else if(c >= 97 && c <= 122){
            ret = c - 97 + 26;
        }else if(c >= 48 && c <= 57){
            ret = c - 48 + 52;
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

    // Recieves an encoded byte array and returns the most likely key size
    static int findKeySize(int[] ba) {

        int keySize = 0;

        int[][] ret;
        double actualSum;

        double min = -1;
        boolean flag = true;
        for(int i = 2; i < 41; i++){

            actualSum = 0;
            ret = getByteArraySubsections(ba, i);

            // Avegerage of the sum of all combinations
            actualSum += calculeHammingDistance(ret[0], ret[1]);
            actualSum += calculeHammingDistance(ret[0], ret[2]);
            actualSum += calculeHammingDistance(ret[0], ret[3]);
            actualSum += calculeHammingDistance(ret[1], ret[2]);
            actualSum += calculeHammingDistance(ret[1], ret[3]);
            actualSum += calculeHammingDistance(ret[2], ret[3]);
            actualSum /= 6;

            // Normalizing the result
            actualSum /= i;

            if(flag || actualSum < min){
                flag = false;
                min = actualSum;
                keySize = i;
            } 

        }

        return keySize;

    }

    // Returns the sum of different bits between byte arrays
    static int calculeHammingDistance(int[] ba1, int[] ba2){

        int b1;
        int b2;
        int len = ba1.length;
        int[] mask = {1, 2, 4, 8, 16, 32, 64, 128};

        int sum = 0;
        for(int i = 0; i < len; i++){

            for(int j = 0; j < 8; j++){

                b1 = ba1[i] & mask[j];
                b2 = ba2[i] & mask[j];
                b1 = b1 >> j;
                b2 = b2 >> j;

                sum += b1 ^ b2;

            }
        }

        return sum;
    }

    // Recievies an array and returns a 'z' padding copy of it
    static int[] addByteArrayPositions(int[] a, int n){
        int len = a.length;
        int[] ret = new int[len + n];
        for(int i = 0; i < len; i++){ // Warning. does not cause any problem
            ret[i] = a[i];
        }
        for(int i = 0; i < n; i++){
            ret[len + i] = 122;
        }
        return ret;
    }

    // Recieve byte array and size of the chunks and returns 4 consecutive byte chunks
    static int[][] getByteArraySubsections(int[] ba, int size){
        int[][] ret = new int[4][];
        for(int i = 0; i < 4; i++){
            ret[i] = new int[size];
            for(int j = 0; j < size; j++){
                ret[i][j] = ba[size * i + j];
            }
        }
        return ret;
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

    // Returns a matrix[numBlocks][keySize] to the respective byte array and keySize
    static int[][] byteArray_to_blocks(int[] ba, int keySize){

        int len = ba.length;
        int numBlocks;
        
        int remainder = len % keySize; 
        if(remainder == 0){
            numBlocks = len / keySize;
        }else{
            numBlocks = len / keySize + 1;
            ba = addByteArrayPositions(ba, getPaddingNumber(ba, keySize));
        }

        int[][] ret = new int[numBlocks][];
        for(int i = 0; i < numBlocks; i++){
            
            ret[i] = new int[keySize];
            for(int j = 0; j < keySize; j++){

                ret[i][j] = ba[i * keySize + j];

            }

        }
        return ret;
    }

    // Returns the number of padding characters
    static int getPaddingNumber(int[] ba, int keySize){
        return keySize - (ba.length % keySize);
    }

    // Returns the respective transposed matrix
    static int[][] transposeMatrix(int[][] a){
        int x = a.length;
        int y = a[0].length;
        int[][] ret = new int[y][];
        for(int i = 0; i < y; i++){
            ret[i] = new int[x];
        }
        for(int i = 0; i < y; i++){
            for(int j = 0; j < x; j++){
                ret[i][j] = a[j][i];
            }
        }
        return ret;
    }

    // Recieves a byte array of a encoded string and returns the key and the decoded string byte array
    static int singleCharXorDecode(int[] ba){

        double min = -1;
        int key = -1;
        
        double temp_sum;
        int[] temp_xordArray;
        double[] temp_freq;
        boolean flag = true;
        for(int i = 0; i < 256; i++){

            temp_xordArray = byteXorByteArray(i, ba);            
            temp_freq = countLetterFreq(temp_xordArray);
            temp_sum = sumOfSqrdDiff(temp_freq);
            
            if(flag || temp_sum < min){
                flag = false;
                min = temp_sum;
                key = i;
            }

        }
        
        return key;
    
    }

    // Return a xor with a byte and byte array
    static int[] byteXorByteArray(int b, int[] ba){

        int len = ba.length;
        int[] ret = new int[len];
        
        for(int i = 0; i < len; i++){
            ret[i] = ba[i] ^ b;
        }

        return ret;

    }

    // Returns the sum of squared differences between actual frequency and expected frequency
    static double sumOfSqrdDiff(double[] freq){

        // expected frequencies from a to z and space
        double[] expected_freq = {0.08167, 0.01492, 0.02202, 0.04253, 0.12702, 0.02228, 0.02015, 0.06094, 0.06966, 0.00153, 0.00772, 0.04025, 0.02406, 0.06749, 0.07507, 0.01929, 0.00095, 0.05987, 0.06327, 0.09056, 0.02758, 0.00978, 0.02360, 0.00150, 0.01974, 0.00074, 0.190};

        double sum = 0;
        for(int i = 0; i < 27; i++){
            sum += (expected_freq[i] - freq[i]) * (expected_freq[i] - freq[i]);
        }
       
        return sum;
    
    }

    // Recieve a byte array and return an array of each character freq from a to z and space
    static double[] countLetterFreq(int[] ba){

        double[] freq = new double[27];
        int total = ba.length;

        int b;
        for(int i = 0; i < total; i++){

            b = ba[i];

            if(b >= 97 && b <= 122){ // Lowercase
                freq[b - 97]++;
            }else if(b >= 65 && b <= 90){ // Uppercase
                freq[b - 65]++;
            }else if(b == 32){
                freq[26]++;
            }

        }

        for(int i = 0; i < 27; i++){
            freq[i] /= total;
        }

        return freq;

    }

    // Returns the respective key associated to the block
    static int[] findKey(int[][] blocks){

        int x = blocks.length;
        int[] keySet = new int[x];

        for(int i = 0; i < x; i++){
            keySet[i] = singleCharXorDecode(blocks[i]);
        }

        return keySet;

    }

    // Recieves encrypted byte array, key and number of padding characters and returns the decrypted file text
    static int[] decryptByteArray(int[] key, int[][] blocks, int paddingNum){
    
        int x = blocks.length;
        int y = blocks[0].length;

        int[] ret = new int[x * y];
        int[] ba;

        int paddingDiscount;
        for(int i = 0; i < x; i++){
            ba = xorArrays(blocks[i], key);
            paddingDiscount = i == x - 1 ? paddingNum : 0;
            for(int j = 0; j < y - paddingDiscount; j++){ // Warning. Does not cause any problem
                ret[i * y + j] = ba[j];
            }
        }
        return ret;
    }

    // Recieves 2 arrays and returns the xor'd array
    static int[] xorArrays(int[] b1, int[] b2){

        int len = b1.length;

        if(len != b2.length){
            throw new IllegalArgumentException("Byte arrays should have the same lenght.");
        }

        int[] r = new int[len];

        for(int i = 0; i < len; i++){
            r[i] = b1[i] ^ b2[i];
        }

        return r;

    }
    
    public static void main(String[] args) throws IOException {
        
        // Getting relative file path
        String file = System.getProperty("user.dir") + "/6.txt";

        // Getting the fully concatenated, without new line, string
        String b64EncryptedString = readFile(file);
        
        // Decoding base64 into byte array and findind the most likely key size
        int[] decodedB64 = b64String_to_byteArray(b64EncryptedString);
        int keySize = findKeySize(decodedB64);

        // Breaking decoded byte array into blocks and transposing it
        int[][] blocks = byteArray_to_blocks(decodedB64, keySize);
        int[][] transposedBlocks = transposeMatrix(blocks);

        // Decoding text. Observation: padding characters were removed for decrypting
        int[] keyByteArray = findKey(transposedBlocks);
        int[] decryptedTextByteArray = decryptByteArray(keyByteArray, blocks, getPaddingNumber(decodedB64, keySize));
        String key = byteArray_to_string(keyByteArray);
        String text = byteArray_to_string(decryptedTextByteArray);

        // Printing results. 
        System.out.println("\n\nDecrypted text: " + text);
        System.out.println("Key: " + key + "\n\n");

    }
    
}
