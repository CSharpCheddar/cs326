/*****************************************************
   CS 326 - Spring 2020 - Assignment #3

   Your name(s): Martin Mueller

 *****************************************************/

import java.util.*;

class Avalanche extends AES {

    /* Given a string of 0s and 1s, return a string of exactly 8 digits,
     * namely the input string preceded by the necessary number of 0s,
     * i.e., between 0 and 8, if the input string contains 8 and 0 digits,
     * respectively: For example:
     *      leftPad("")         returns "00000000"
     *      leftPad("101")      returns "00000101"
     *      leftPad("11110101") returns "11110101"
     */
    static String leftPad(String s) {
      String string = new String(s);
      for (int i = string.length(); i < 8; i++) {
        string = "0" + string;
      }
      return string;
    }// leftPad method
    
    /* given an AES state array and a column number, return the string
     * obtained by concatenating the 8-bit binary representation of
     * the values (from top to bottom) in the selected column of the
     * input array. For example, given the following state array:
     *      255 3 2 1  and the column number 0
     *      254 6 5 4
     *       15 9 8 7
     *       10 0 2 4
     * this function must return the string:
     *  "11111111111111100000111100001010"
     */
    static String intArrayToBinString(int[][] data,int col) {
      return Integer.toBinaryString(
                                     (data[0][col] << 24)
                                     ^ (data[1][col] << 16)
                                     ^ (data[2][col] << 8)
                                     ^ (data[3][col])
                                   );
    }// intArrayToBinString method

    /* Given a round number and two AES state arrays, send to the standard
     * output stream the string representation of the input data in the
     * format for each round specified in problem 1 of the handout. For 
     * example: 

Round 00 00001110001101100011010010101110 11001110011100100010010110110110
         00001111001101100011010010101110 11001110011100100010010110110110
                *                                                         
         11110010011010110001011101001110 11011001001010110101010110001000
         11110010011010110001011101001110 11011001001010110101010110001000
                                                                             1
     */
    static void printRound(int num, int[][] s1, int[][] s2) {
      // print round info
      System.out.printf("Round %02d ", num);
      // convert state arrays to binary strings
      String first = "";
      String second = "";
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
          first += String.format("%8s", Integer.toBinaryString(s1[j][i]))
                     .replace(' ', '0');
          second += String.format("%8s", Integer.toBinaryString(s2[j][i]))
                     .replace(' ', '0');
        }
      }
      // first halves of state arrays
      System.out.printf("%s %s\n", first.substring(0, 32),
                        first.substring(32, 64));
      System.out.printf("%41s %s\n%9s", second.substring(0, 32),
                        second.substring(32, 64), "");
      int n = 0;
      for (int i = 0; i < 65; i++) {
        if (i == 32) {
          System.out.print(" ");
        } else if (i < 32) {
          if (first.charAt(i) == second.charAt(i)) {
            System.out.print(" ");
          } else {
            System.out.print("*");
            n++;
          }
        } else {
          if (first.charAt(i - 1) == second.charAt(i - 1)) {
            System.out.print(" ");
          } else {
            System.out.print("*");
            n++;
          }
        }
      }
      System.out.printf(" %d\n", n);
      // second halves of state arrays
      System.out.printf("%41s %s\n", first.substring(64, 96),
                        first.substring(96));
      System.out.printf("%41s %s\n%9s", second.substring(64, 96),
                        second.substring(96), "");
      n = 0;
      for (int i = 64; i < 129; i++) {
        if (i == 96) {
          System.out.print(" ");
        } else if (i < 96) {
          if (first.charAt(i) == second.charAt(i)) {
            System.out.print(" ");
          } else {
            System.out.print("*");
            n++;
          }
        } else {
          if (first.charAt(i - 1) == second.charAt(i - 1)) {
            System.out.print(" ");
          } else {
            System.out.print("*");
            n++;
          }
        }
      }
      System.out.printf(" %d", n);
    }// printRound method

    /* Given a 128-bit AES key (as a 32-digit hexadecimal number) and two
     * 128-bit plaintext blocks (in the same representation), print
     * the complete trace of the AES algorithm when encrypting the two
     * blocks "in parallel" with the same key. The precise (character
     * by character) format of the output is given in the handout for
     * A3.
     */
    static void testEffect(String keyStr, String block1, String block2) {
      int[][] first = hexStringToByteArray(block1);
      int[][] second = hexStringToByteArray(block2);
      int[] key = expandKey(hexStringToByteArray(keyStr));
      addRoundKey(first, key, 0);
      addRoundKey(second, key, 0);
      for (int i = 1; i <= 10; i++) {
        forwardSubstituteBytes(first);
        forwardSubstituteBytes(second);
        shiftRows(first);
        shiftRows(second);
        if (i != 10) {
          mixColumns(first);
          mixColumns(second);
        }
        addRoundKey(first, key, i);
        addRoundKey(second, key, i);
        printRound(i, first, second);
        if (i != 10) {
          System.out.println();
        }
      }
    }// testEffect method

    /* This method will be used for testing. 
     * Do NOT modify it. 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println( leftPad("") );
            System.out.println( leftPad("1111") );
            System.out.println( leftPad("11111111") );
            System.out.println( intArrayToBinString(
                             new int[][] { { 255, 3, 2, 1 },
                                           { 254, 6, 5, 4 },
                                           {  15, 9, 8, 7 },
                                           {  10, 0, 2, 4 } } , 0 ) );

        } else if (args.length != 3) {
            System.out.println("Usage:  java Avalanche <key> <P1> <P2>");
            System.exit(1);
        }

        
        String key = args[0];
        String plaintext1 = args[1];
        String plaintext2 = args[2];
        testEffect(key, plaintext1, plaintext2);
    }// main method
}// Avalanche class
