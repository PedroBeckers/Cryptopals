/*
Challenge 2: Fixed XOR
Write a function that takes two equal-length buffers and produces their XOR combination.
*/

public class Ex2 {

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

    // Recieves a byte array and returns the respective hex string
    static String byteArray_to_hexString(int[] ba){

        int len = ba.length;
        char[] ret = new char[2 * len];

        char[] temp;
        int b;
        for(int i = 0; i < len; i++){

            b = ba[i];

            temp = byte_to_hexChar(b);

            ret[2 * i] = temp[0];
            ret[2 * i + 1] = temp[1]; 

        }

        return new String(ret);

    }

    // Recieves a byte and returns an array of 2 characters
    static char[] byte_to_hexChar(int b){

        char[] ca = new char[2];
        char[] hex_chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        int b1 = b >> 4;
        int b2 = b & 15;

        ca[0] = hex_chars[b1];
        ca[1] = hex_chars[b2];

        return ca;
    }

    // Recieves 2 buffers and returns the xor'd buffer
    static int[] xorBuffers(int[] b1, int[] b2){

        int len = b1.length;

        if(len != b2.length){
            throw new IllegalArgumentException("Buffers should have the same lenght.");
        }

        int[] r = new int[len];

        for(int i = 0; i < len; i++){
            r[i] = b1[i] ^ b2[i];
        }

        return r;

    }

    // Return True, if the result matches to the expected output
    static boolean confirm_result(String result){
        String expected_output = "746865206b696420646f6e277420706c6179";
        return expected_output.equals(result);
    }

    public static void main(String[] args) {

        // Input Strings
        String s1 = "1c0111001f010100061a024b53535009181c";
        String s2 = "686974207468652062756c6c277320657965";

        // Initializing buffers
        int[] buffer1 = hexString_to_byteArray(s1);
        int[] buffer2 = hexString_to_byteArray(s2);

        // Creating xor'd buffer
        int[] xordbuffer = xorBuffers(buffer1, buffer2);

        String result = byteArray_to_hexString(xordbuffer);

        // Printing inputs
        System.out.print("\n\nString 1: ");
        System.out.println(s1);
        System.out.print("String 2: ");
        System.out.println(s2);

        // Printing result
        System.out.print("\nResult: ");
        System.out.println(result);

        // Comparing result with expected output
        if(confirm_result(result)){
            System.out.println("\nSuccessful operation. Result matches the expected output.\n\n");
        }else{
            System.out.println("\nUnsuccessful operation. Result does not match the expected output.\n\n");
        }

    }

}