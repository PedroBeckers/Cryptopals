/*
Challenge 4: Detect single-character XOR
One of the 60-character strings in this file has been encrypted by single-character XOR. Find it.
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Ex4 {

    // Convert int array to double array
    static double[] intArray_to_doubleArray(int[] a){
        int len = a.length;
        double[] ret = new double[len];
        for(int i = 0; i < len; i++){
            ret[i] = (double) (a[i]);
        }
        return ret;
    }

    // Convert double array to int array
    static int[] doubleArray_to_intArray(double[] d){
        int len = d.length;
        int[] ret = new int[len];
        for(int i = 0; i < len; i++){
            ret[i] = (int) (d[i]);
        }
        return ret;
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

    // Recieves a byte array and returns the lowest score, key and decoded byte array. Modified method from Ex3.
    static double[][] findLowestScore(int[] ba){

        double[][] ret = new double[4][];
        ret[0] = new double[1]; // Score
        ret[1] = new double[1]; // Key
        ret[2] = new double[1]; // Line index
        ret[3] = new double[ba.length]; // Byte array

        double temp_sum;
        int[] temp_xordArray;
        double[] temp_freq;

        double menor = -1;
        boolean flag = true;
        for(int i = 0; i < 256; i++){

            temp_xordArray = byteXorByteArray(i, ba);            

            temp_freq = countLetterFreq(temp_xordArray);

            temp_sum = sumOfSqrdDiff(temp_freq);
            
            if(flag || temp_sum < menor){
                flag = false;
                menor = temp_sum;
                ret[0][0] = menor;
                ret[1][0] = (double) (i);
                ret[3] = intArray_to_doubleArray(temp_xordArray);
            }

        }

        return ret;
    
    }

    // Returns a string from a byte array
    static String byteArray_to_string(int[] ba){

        int len = ba.length;
        char[] ca = new char[len];

        for(int i = 0; i < len; i++){
            ca[i] = (char) (ba[i]);
        }

        return new String(ca);

    }

    // Returns the a double array with score, key and byte array of the most likely line to be single-char encrypted 
    static double[][] readFile(String file) throws IOException{

        BufferedReader reader = new BufferedReader(new FileReader(file));

        int[] temp_byteArray;
        double menor_atual;

        double menor = -1;
        double[][] ret = new double[4][];

        String line;
        boolean flag = true;
        int i = 1;
        while((line = reader.readLine()) != null){

            temp_byteArray = hexString_to_byteArray(line);

            double[][] r = findLowestScore(temp_byteArray);
            menor_atual = r[0][0];
        
            if(flag || menor_atual < menor){
                flag = false;
                menor = menor_atual;
                ret = r;
                ret[2][0] = i;
            }

            i++;

        }

        reader.close();

        return ret;

    }

    public static void main(String[] args) throws IOException {

        // Getting relative file path
        String file = System.getProperty("user.dir") + "/4.txt";

        // Finding the single-char encrypted line
        double[][] r = readFile(file);
        int key = (int) (r[1][0]);
        int index = (int) (r[2][0]);
        int[] decodedByteArray = doubleArray_to_intArray(r[3]);

        // Converting result byte array to string
        String result = byteArray_to_string(decodedByteArray);

        // Printing result
        System.out.println("\nLine: " + index);
        System.out.println("Key (char): " + (char) key);
        System.out.println("Result: " + result + "\n");
        
    }

}
