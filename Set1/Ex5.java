/*
Challenge 5: Implement repeating-key XOR
In repeating-key XOR, you'll sequentially apply each byte of the key; the first byte of plaintext will 
be XOR'd against I, the next C, the next E, then I again for the 4th byte, and so on.
*/

public class Ex5 {

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

    // Recieves a byte and returns an array of 2 characters. Considering "/n" in the string.
    static char[] byte_to_hexChar(int b){

        char[] ca = new char[2];
        char[] hex_chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        int b1 = b >> 4;
        int b2 = b & 15;

        ca[0] = hex_chars[b1];
        ca[1] = hex_chars[b2];

        return ca;
    }

    // Recieve a ascii string and returns respective byte array
    static int[] string_to_byteArray(String s){

        int len = s.length();
        int[] ba = new int[len];

        for(int i = 0; i < len; i++){
            ba[i] = s.charAt(i);
        }

        return ba;

    }

    // Recieve the string byte array and the key byte array for repeating key xor encryption and returns the encrypted byte array
    static int[] repeatingKeyXorEncryption(int[] stringByteArray, int[]keyByteArray){

        int stringLen = stringByteArray.length;
        int keyLen = keyByteArray.length;

        // i iterates over String and j iterates over key
        int j = 0;
        for(int i = 0; i < stringLen; i++){

            stringByteArray[i] = stringByteArray[i] ^ keyByteArray[j];
            j = (j + 1) % keyLen;

        }

        return stringByteArray;

    }

    // Return True, if the result matches to the expected output
    static boolean confirm_result(String result){
        String expected_output = "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f";
        return expected_output.equals(result);
    }

    public static void main(String[] args) {

        // Defining inputs
        String input_string = "Burning 'em, if you ain't quick and nimble\nI go crazy when I hear a cymbal";
        String input_key = "ICE";

        // Converting inputs to byte arrays
        int[] stringByteArray = string_to_byteArray(input_string);
        int[] keyByteArray = string_to_byteArray(input_key);

        // Encrypting the string
        int[] encryptedStringByteArray = repeatingKeyXorEncryption(stringByteArray, keyByteArray);

        // Converting encrypted string byte array to a hex string
        String encryptedHexString = byteArray_to_hexString(encryptedStringByteArray);

        // Printing inputs
        System.out.println("\n\nString: " + input_string);
        System.out.println("Key: " + input_key);

        // Printing results
        System.out.println("\nEcrypted string: " + encryptedHexString);

        // Comparing result with expected output
        if(confirm_result(encryptedHexString)){
            System.out.println("\nSuccessful operation. Result matches the expected output.\n\n");
        }else{
            System.out.println("\nUnsuccessful operation. Result does not match the expected output.\n\n");
        }

    }


}
