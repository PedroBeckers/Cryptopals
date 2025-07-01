/*
Challenge 8: Detect AES in ECB mode
In 8.txt file are a bunch of hex-encoded ciphertexts. One of them has been encrypted with ECB. Detect it.
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Ex8 {

    // Returns a byte array to the respective hex string
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

    // Returns a matrix[numBlocks][Size] to the respective byte array and size
    static int[][] byteArray_to_blocks(int[] ba, int size){

        int len = ba.length;
        int numBlocks = len / size;

        int[][] ret = new int[numBlocks][];
        for(int i = 0; i < numBlocks; i++){
            
            ret[i] = new int[size];
            for(int j = 0; j < size; j++){

                ret[i][j] = ba[i * size + j];

            }

        }
        return ret;
    }

    // Returns the index of the most likely to be the ECB encoded string and how much repeated blocks it contains
    static int[] readFile(String file) throws IOException{

        BufferedReader reader = new BufferedReader(new FileReader(file));

        int[] ba;
        int[][] blocks;
        int duplicateNum;

        int[] ret = new int[2];

        int lineIndex = 0;
        String line;
        while((line = reader.readLine()) != null){

            ba = hexString_to_byteArray(line);
            blocks = byteArray_to_blocks(ba, 16);

            duplicateNum = countDuplicates(blocks);

            if(duplicateNum > ret[1]){
                ret[1] = duplicateNum;
                ret[0] = lineIndex;
            }
            lineIndex++;
        }
        
        return ret;

    }

    // Returns the max number of duplicate blocks 
    static int countDuplicates(int[][] blocks){
        int blocksNum = blocks.length;
        int max = -1;
        int counter;
        for(int i = 0; i < blocksNum; i++){
            counter = 0;
            for(int j = 0; j < blocksNum; j++){
                if (j == i) continue;
                if(Arrays.equals(blocks[i], blocks[j])){
                    counter++;
                }
            }
            if(counter > max){
                max = counter;
            }
        }
        return max;
    }


    
    public static void main(String[] args) throws Exception {
        
        // Getting relative file path
        String file = System.getProperty("user.dir") + "/8.txt";

        // Finding the encrypted string
        int[] ret = readFile(file);
        int index = ret[0];
        int duplicatesNum = ret[1];

        // Printing results
        System.out.println("\n\nThe encrypted string is at the line " + (index+1) + " (index = " + index + ") of 8.txt file.");
        System.out.println("It counts up to " + duplicatesNum + " repeated blocks.\n\n");

    }

}
