/*****************************************************
   CS 326 - Spring 2020 - Assignment #1
   Student's full name: Martin Alexander Mueller
*****************************************************/

import java.util.*;

class BTCencrypt {

    /* This method takes in a plaintext string and returns the ciphertext
     * string produced by the Binary Tree Cipher as described in the handout.
     * Example: input:  "THISASSIGNMENTISMEANTTOBEAFUNWARMUP"
     *          output: "TMMGEATNNIPAMFSOIAHUENASTTWSNEUIBSR"
     */
    static String encrypt(String plaintext) {
      plaintext = plaintext.replaceAll("[^A-Za-z0-9]", "");
      String[] parts = split(plaintext);
      if (parts.length == 1) {
        return parts[0];
      }
      return encrypt(parts[0]) + encrypt(parts[1]);
    }// encrypt method

    /* This method takes in one string. If the input string contains
     * fewer than 3 characters, it returns a single-element array
     * containing the input string. Otherwise, it returns a
     * two-element array whose first element is the string made up of
     * the first, third, fifth, etc., letters of the input string, and
     * whose second element is the string made up of the second,
     * fourth, sixth, etc., letters of the input string.
     * Examples:  "hi"  ==> [ "hi" ]
     *            "hello" ==> [ "hlo", "el" ]
     */
    public static String[] split(String s) {
      // base case: s is less than 3 characters long
      if (s.length() < 3) {
        String[] array = {s};
        return array;
      }
      // if not, split up s by its characters in even and odd spots
      char[] left;
      char[] right;
      if (s.length() % 2 == 0) {
        left = new char[s.length() / 2];
      } else {
        left = new char[s.length() / 2 + 1];
      }
      right = new char[s.length() / 2];
      for (int i = 0; i < s.length(); i++) {
        if (i % 2 == 0) {
          left[i / 2] = s.charAt(i);
        } else {
          right[i / 2] = s.charAt(i);
        }
      }
      String[] array = {new String(left), new String(right)};
      return array;
    }// split method

    /* This method will be used for testing purposes. 
     * You may NOT modify it.
     */
    public static void main(String[] args) {
        System.out.println(encrypt(args[0]));
    }// main method
    
}// BTCencrypt class
