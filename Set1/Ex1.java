/*
Challenge 1: Convert hex to base64
"Always operate on raw bytes, never on encoded strings. Only use hex and base64 for pretty-printing."
following the rule, input must lenght a multiple of 6 for complete b64 output. 
1 - multiple of 2, because we are operating in bytes, and 2 hex digits equals 1 byte
2 - multiple of 3, because 3 Hex digits equals 2 b64 digit

3 Hex digits : 2 b64 digits (12 bits, 1.5 byte)
6 Hex digits : 4 b64 digits (24 bits, 3 bytes)

Note:
In reason of java bytes assuming negative values, I used int arrays instead of byte arrays for faster coding. This isn't the highest
performance solution. For higher performance it should be represented as byte arrays and, when needed, converted to int and masked by 
0xFF byte, in order to obtain unsigned byte values.
*/

public class Ex1{

    // Returns the integer value of a hexadecimal character
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

    // From a hexadecimal string, return a byte array. Byte array represented in a int array, as explained before.
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

    // Recieves a 6 bit value and returns its respective character in base64
    static char value_to_b64Char(int v){

        char c;
         
        if(v >= 0 && v <= 25){ // ['A', 'Z']
            c = (char) (v + 65);
        }else if(v >= 26 && v <= 51){ // ['a', 'z']
            c = (char) (v + 97 - 26);
        }else if(v >= 52 && v <= 61){ // ['0', '1']
            c = (char) (v + 48 - 52);
        }else if(v == 62){ // '+'
            c = (char) (43);
        }else if(v == 63){ // '/'
            c = (char) (47);
        }else{
            throw new IllegalArgumentException("Invalid value for base64 conversion.");
        }

        return c;

    }

    // Reads a byte array and returns a base64 string
    static String byteArray_to_b64String(int[] b){

        int len = b.length / 3;
        char[] a = new char[4 * len];

        int mask = 63;

        int b1;
        int b2;
        int b3;
        int v1;
        int v2;
        int v3;
        int v4;

        for(int i = 0; i < len; i++){

            b1 = b[3 * i];     
            b2 = b[3 * i + 1]; 
            b3 = b[3 * i + 2];
            
            v1 = b1 >> 2;
            v2 = ((b1 << 4) & mask) | b2 >> 4;
            v3 = ((b2 << 2) & mask) | b3 >> 6;
            v4 = b3 & mask;

            a[4 * i] = value_to_b64Char(v1);
            a[4 * i + 1] = value_to_b64Char(v2);
            a[4 * i + 2] = value_to_b64Char(v3);
            a[4 * i + 3] = value_to_b64Char(v4);

        }

        return new String(a);

    }

    // Return True, if the result matches to the expected output
    static boolean confirm_result(String result){
        String expected_output = "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t";
        return expected_output.equals(result);
    }

    public static void main(String[] args){
        
        // Hexadecimal input
        String hex_input = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";

        // Byte array
        int[] byte_array = hexString_to_byteArray(hex_input);

        // Base64 string
        String b64_result = byteArray_to_b64String(byte_array);

        // Input and result printing
        System.out.print("\n\nhex: ");
        System.out.println(hex_input);
        System.out.print("base64: ");
        System.out.println(b64_result);

        // Confirming result
        if(confirm_result(b64_result)){
            System.out.println("\nSuccessful convertion. Result matches the expected output.\n\n");
        }else{
            System.out.println("\nUnsuccessful convertion. Result does not match the expected output.\n\n");
        }

    }

}