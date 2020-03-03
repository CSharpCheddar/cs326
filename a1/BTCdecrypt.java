/*****************************************************
   CS 326 - Spring 2020 - Assignment #1
   Student's full name: Martin Alexander Mueller
*****************************************************/

import java.util.*;

class BTCdecrypt {

    /*************************************************************************
                       define up to 2 helper methods below 
     *************************************************************************/

    /* This method splits a given string in half, then returns the two halves.
     * If the string has an odd number of characters, the middle character
     * will be put in the left half. If the string contains fewer than 3
     * characters, it will instad not be split, and the method will return
     * an array containing the string that it was given.
     */
    public static String[] split(String s) {
      if (s.length() < 3) {
        String[] array = {s};
        return array;
      }
      if (s.length() % 2 == 0) {
        String[] array = {s.substring(0, s.length() / 2),
                          s.substring(s.length() / 2)};
        return array;
      }
      String[] array = {s.substring(0, s.length() / 2 + 1),
                        s.substring(s.length() / 2 + 1)};
      return array;
    }

    /* This method takes two strings and combines them in an alternating
     * pattern. The resulting string consists of the first character of the
     * first string followed by the first character of the second string
     * followed by the second character of the first string and so on.
     */
    public static String alternate(String left, String right) {
      char[] output = new char[left.length() + right.length()];
      for (int i = 0; i < output.length; i++) {
        if (i % 2 == 0) {
          output[i] = left.charAt(i / 2);
        } else {
          output[i] = right.charAt(i / 2);
        }
      }
      return new String(output);
    }

    /* This method takes in a ciphertext string produced by the Binary Tree
     * Cipher as described in the handout and returns the plaintext that
     * the BTC was fed as input to produce this ciphertext.
     * Example: input:  "TMMGEATNNIPAMFSOIAHUENASTTWSNEUIBSR"
     *          output: "THISASSIGNMENTISMEANTTOBEAFUNWARMUP"
     * This method may call one or two helper methods to be defined above.
     */
    static String decrypt(String ciphertext) {
      String[] array = split(ciphertext);
      if (array.length == 1) {
        return array[0];
      }
      return alternate(decrypt(array[0]), decrypt(array[1]));
    }// decrypt method
    
    /* This method will be used for testing purposes. 
     * You may NOT modify it.
     */
    public static void main(String[] args) {    
        System.out.println(decrypt(args[0]));
    }// main method
    
}// BTCdecrypt class
