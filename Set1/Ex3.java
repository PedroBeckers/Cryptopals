/*
Challenge 3: Single-byte XOR cipher
Find the key, decrypt the message.
*/

public class Ex3 {

    // Returns a string from a byte array
    static String byteArray_to_string(int[] ba){

        int len = ba.length;
        char[] ca = new char[len];

        for(int i = 0; i < len; i++){
            ca[i] = (char) (ba[i]);
        }

        return new String(ca);

    }

    // Same as used in Exercise 1. From a hexadecimal string, return a byte array. Byte array represented in a int array, as explained before.
    static int[] hexString_to_byteArray(String h){

        int len = h.length() / 2;

        int[] ret = new int[len];

        char c1;
        char c2;
        int v1;
        int v2;
        int b;

        for(int i = 0; i < len; i++){
            c1 = h.charAt(2 * i);
            c2 = h.charAt(2 * i + 1);

            v1 = hexChar_to_value(c1) << 4; // shift left 4 digits
            v2 = hexChar_to_value(c2);

            b = v1 | v2; // concat vectors

            ret[i] = b;
        }

        return ret;

    }

    // Same as used in Exercise 1. Returns the integer value of a hexadecimal character
    static int hexChar_to_value(char c){

        int num;

        if(c >= 48 && c <= 57){ // ['0', '9'] 
            num = c - 48;
        }else if(c >= 97 && c <= 102){ // ['a', 'f']
            num = c - 97 + 10;
        }else{
            throw new IllegalArgumentException("Invalid hexadecimal character.");
        }

        return num;

    }

    // Recieves a byte array of a encoded string and returns a byte array with the key and the decoded string byte array
    static int[][] singleCharXorDecode(int[] ba){

        int[][] ret = new int[2][];

        double min = -1;
        int key = -1;
        int[] decodedStringByteArray = new int[ba.length];
        
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
                decodedStringByteArray = temp_xordArray;
                key = i;
            }

        }

        ret[0] = new int[1];
        ret[1] = decodedStringByteArray;
        ret[0][0] = key;
        
        return ret;
    
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

    
    public static void main(String[] args) {
        
        // Hex encoded string input
        String hexEncoded = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736";
        
        // Encoded hex byte array
        int[] byteArray = hexString_to_byteArray(hexEncoded);

        // Decoding by letter frequency
        int[][] decodeResult = singleCharXorDecode(byteArray);
        int key = decodeResult[0][0];
        int[] decodedStringByteArray = decodeResult[1];

        // Result byte array
        String result = byteArray_to_string(decodedStringByteArray);

        // Printing input
        System.out.println("\n\nEncoded hex string: " + hexEncoded);

        // Printing result
        System.out.println("\nKey (char): " + (char) key);
        System.out.println("Decoded string: " + result + "\n\n");


    }
    
}
