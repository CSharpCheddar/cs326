/*****************************************************
   CS 326 - Spring 2020 - Assignment #3

   Your name(s): ____________________

                 ____________________
 *****************************************************/

import java.util.*;

class DoubleAES extends AES {

    /* Given an AES state array, return a String representation of the array
     * that is, the values in the array listed in column-major order, where
     * each value is a two-digit hexadecimal number. For example, if the state
     * array is:              1 5 10 252
     *                        2 6 11 253
     *                        3 7 12 254
     *                        4 8 13 255
     * then the return value must be: "01020304050607080A0B0C0DFCFDFEFF"
     */     
    static String stateToString(int[][] state) {

        /* To be completed */

        return "";  // here to please the compiler in the code handout

    }// stateToString method
    
    /* Given a pair of 128-bit keys (each represented by a 32-digit hexadecimal 
     * string) and a similar plaintext block, return the resulting ciphertext 
     * block obtained by applying AES encryption twice, namely:
     *                    AES( AES(block,key1), key2)
     * Note that the resulting ciphertext block is returned as a state array,
     * not as a string.
     */
    static int[][] encryptDAES(String key1, String key2, String block) {

        /* To be completed */

        return null;  // here to please the compiler in the code handout

    }// encryptDAES method

    /* Given a pair of 128-bit keys (each represented by a 32-digit hexadecimal 
     * string) and a similar ciphertext block, return the resulting plaintext 
     * block obtained by applying AES decryption twice in the reverse order
     * than the one used for encryption (so that this method undoes what the
     * previous method did to the plaintext block).
     * Note that the resulting plaintext block is returned as a state array,
     * not as a string.
     */
    static int[][] decryptDAES(String key1, String key2, String block) {

        /* To be completed */

        return null;  // here to please the compiler in the code handout

    }// decryptDAES method

    /* This method will be used for testing. 
     * Do NOT modify it. 
     */
    public static void main(String[] args) {        
        if (args.length != 3) {
            System.out.println("Usage: java DoubleAES <key1> <key2> <P>");
            System.exit(1);
        }

        String key1 = args[0].toUpperCase();
        String key2 = args[1].toUpperCase();
        String plaintext = args[2].toUpperCase();
        int[][] ciphertext = encryptDAES(key1,key2,plaintext);
        int[][] decrypted = decryptDAES(key1,key2,stateToString(ciphertext));
        System.out.format("plaintext  = %s\nciphertext = %s\ndecrypted  = %s\n",
                          plaintext,
                          stateToString(ciphertext),
                          stateToString(decrypted));
    }// main method
}// DoubleAES class
