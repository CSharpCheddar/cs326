/*****************************************************
   CS 326 - Spring 2020 - Assignment #4
   Student's full name(s): Martin Mueller
*****************************************************/

import java.util.*;
import java.io.*;
import java.math.*;

class ImageCipher {

    // used to store the next block read from or written to the image file
    static int[] block =  new int[128];

    // used to store the next block read from the image file
    static int[] cipherBlock =  new int[128];

    // file extension for all image files in this assignment (you MUST use
    // this constant everywhere instead of the string itself)
    static String EXT = ".pgm";

    /* Given an array of 128 bits (stored as ints), return the uppercase hex
     * string representing the input bit string. For example, given:
     *   [ 1,0,1,0,0,0,0,0,
     *     1,0,1,0,0,0,0,1,
     *     1,0,1,0,0,0,1,0,
     *     ...,
     *     1,0,1,0,1,1,1,1 ]
     *  return "A0A1A2A3A4A5A6A7A8A9AAABACADAEAF".
     */
    static String bitsToHexString(int[] bits) {
      StringBuilder sb = new StringBuilder("");
      for (int i = 0; i < 32; i++) {
        int n = 0;
        for (int j = 0; j < 4; j++) {
          n = n | (bits[4 * i + j] << (3 - j));
        }
        sb.append(Integer.toHexString(n).toUpperCase());
      }
      return sb.toString();
    }// bitsToHexString method

    /* Given a 2D AES state array, return its representation as a 1D array
     * of 128-bit values (0 or 1) stored as ints. For example, given:
     *    { { 0xA0, 0xA4, 0xA8, 0xAC },
     *      { 0xA1, 0xA5, 0xA9, 0xAD },
     *      { 0xA2, 0xA6, 0xAA, 0xAE },
     *      { 0xA3, 0xA7, 0xAB, 0xAF } }
     * return:
     *   [ 1,0,1,0,0,0,0,0,
     *     1,0,1,0,0,0,0,1,
     *     1,0,1,0,0,0,1,0,
     *     ...,
     *     1,0,1,0,1,1,1,1 ]
     */
    static int[] stateToBits(int[][] state) {
      int[] array = new int[128];
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
          for (int k = 0; k < 8; k++) {
            array[32 * i + 8 * j + k] = (state[j][i] >> (7 - k)) & 1;
          }
        }
      }
      return array;
    }// stateToBits method

    /* Given a scanner object, read the next 16 integers from it and
     * store the 16 corresponding 8-bit patterns into the 128-bit
     * instance variable called 'block'
     */
    static void readBlock(Scanner s) throws Exception {
      for (int i = 0; i < 16; i++) {
        int n = s.nextInt();
        for (int j = 0; j < 8; j++) {
          block[8 * i + j] = (n >> (7 - j)) & 1;
        }
      }
    }// readBlock method

    /* Given a PrintWriter object, write to it the 16 integers stored
     * as the 16 corresponding 8-bit patterns in the 128-bit instance
     * variable called 'block'
     */
    static void writeBlock(PrintWriter w) throws Exception {
      for (int i = 0; i < 16; i++) {
        int n = 0;
        for (int j = 0; j < 8; j++) {
          n = n | (block[8 * i + j] << (7 - j));
        }
        w.printf("%3d\n", n);
      }
    }// writeBlock method

    /* Given a Scanner object and a PrintWriter object, copy to the latter
     * the first four lines of the former.
     */
    static void processHeader(Scanner s, PrintWriter w) throws Exception {
      for (int i = 0; i < 4; i++) {
        w.println(s.nextLine());
      }
    }// processHeader method

    /* Given a file name (with no extension) for a PGM image and an
     * AES key (in hex format), encrypt the image using AES in ECB
     * mode and store the result in a file whose name is obtained by
     * adding to the input file name the string "_ECB" + EXT.
     */
    static void encryptECB(String filename, String key) {
      try {
        Scanner s = new Scanner(new File(filename + EXT), "UTF-8");
        File newFile = new File(filename + "_ECB" + EXT);
        newFile.delete();
        newFile.createNewFile();
        PrintWriter w = new PrintWriter(newFile);
        processHeader(s, w);
        while (s.hasNext()) {
          readBlock(s);
          block = stateToBits(AES.encrypt(bitsToHexString(block), key));
          writeBlock(w);
        }
        w.flush();
        w.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }// encryptECB method

    /* Given a file name (with no extension) of an encrypted PGM image
     * and an AES key (in hex format), decrypt the image using AES in
     * ECB mode and store the result in a file whose name is obtained
     * by adding to the input file name the string "_dec" + EXT.
    */
    static void decryptECB(String filename, String key) {
      try {
        Scanner s = new Scanner(new File(filename + EXT), "UTF-8");
        File newFile = new File(filename + "_dec" + EXT);
        newFile.delete();
        newFile.createNewFile();
        PrintWriter w = new PrintWriter(newFile);
        processHeader(s, w);
        while (s.hasNext()) {
          readBlock(s);
          block = stateToBits(AES.decrypt(bitsToHexString(block), key));
          writeBlock(w);
        }
        w.flush();
        w.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }// decryptECB method

    /* given two bit strings of the same length (represented as int
     * arrays whose elements are 0 or 1), return the bit string containing
     * the bitwise XOR of the inputs.
     */
    static int[] xor(int[] s1, int[] s2) {
      int[] array = new int[s1.length];
      for (int i = 0; i < s1.length; i++) {
        array[i] = s1[i] ^ s2[i];
      }
      return array;
    }// xor method


    /* Given a 32-character hex string, return the corresponding
     * 128-bit string represented as an int array in which each
     * element is either 0 or 1.
     */
    static int[] hexStringToBits(String hex) {
      int[] bits = new int[128];
      for (int i = 0; i < hex.length(); i++) {
        int n = (int)hex.charAt(i);
        for (int j = 0; j < 4; j++) {
          bits[4 * i + j] = (n >> (3 - j)) & 1;
        }
      }
      return bits;
    }// hexStringToBits method

    /* Given a file name (with no extension) for a PGM image, an AES
     * key and an initialization vector (both in hex format), encrypt
     * the image using AES in CBC mode and store the result in a file
     * whose name is obtained by adding to the input file name the
     * string "_CBC" + EXT.
     */
    static void encryptCBC(String filename, String key, String IV) {
      try {
        Scanner s = new Scanner(new File(filename + EXT), "UTF-8");
        File newFile = new File(filename + "_CBC" + EXT);
        newFile.delete();
        newFile.createNewFile();
        PrintWriter w = new PrintWriter(newFile);
        processHeader(s, w);
        cipherBlock = hexStringToBits(IV);
        while (s.hasNext()) {
          readBlock(s);
          block = xor(block, cipherBlock);
          block = stateToBits(AES.encrypt(bitsToHexString(block), key));
          for (int i = 0; i < block.length; i++) {
            cipherBlock[i] = block[i];
          }
          writeBlock(w);
        }
        w.flush();
        w.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }// encryptCBC method

    /* Given a file name (with no extension) for an AES-encrypted PGM
     * image, an AES key and an initialization vector (both in hex
     * format), decrypt the image using AES in CBC mode and store the
     * result in a file whose name is obtained by adding to the input
     * file name the string "_dec" + EXT.
     */
    static void decryptCBC(String filename, String key, String IV) {
      try {
        Scanner s = new Scanner(new File(filename + EXT), "UTF-8");
        File newFile = new File(filename + "_dec" + EXT);
        newFile.delete();
        newFile.createNewFile();
        PrintWriter w = new PrintWriter(newFile);
        processHeader(s, w);
        cipherBlock = hexStringToBits(IV);
        while (s.hasNext()) {
          readBlock(s);
          int[] temp = new int[128];
          for (int i = 0; i < block.length; i++) {
            temp[i] = block[i];
          }
          block = stateToBits(AES.decrypt(bitsToHexString(block), key));
          block = xor(block, cipherBlock);
          cipherBlock = temp;
          writeBlock(w);
        }
        w.flush();
        w.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }// decryptCBC method

    /* Given a file name (with no extension) for a PGM image, an AES
     * key and an initial counter value (both in hex format), encrypt
     * the image using AES in CTR mode and store the result in a file
     * whose name is obtained by adding to the input file name the
     * string "_CTR" + EXT.
     */
    static void encryptCTR(String filename, String key, String counter) {
      try {
        Scanner s = new Scanner(new File(filename + EXT), "UTF-8");
        File newFile = new File(filename + "_CTR" + EXT);
        newFile.delete();
        newFile.createNewFile();
        PrintWriter w = new PrintWriter(newFile);
        processHeader(s, w);
        BigInteger b = new BigInteger(counter, 16);
        while (s.hasNext()) {
          readBlock(s);
          String n = b.toString(16).toUpperCase();
          if (n.length() < 32) {
            for (int i = n.length(); i < 32; i++) {
              n = "0" + n;
            }
          } else if (n.length() > 32) {
            n = n.substring(0, 32);
          }
          cipherBlock = stateToBits(AES.encrypt(n, key));
          block = xor(block, cipherBlock);
          writeBlock(w);
          b.add(new BigInteger("1"));
        }
        w.flush();
        w.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }// encryptCTR method

    /* Given a file name (with no extension) for an AES-encrypted PGM
     * image, an AES key and an initialization vector (both in hex
     * format), decrypt the image using AES in CTR mode and store the
     * result in a file whose name is obtained by adding to the input
     * file name the string "_dec" + EXT.
     */
    static void decryptCTR(String filename, String key, String counter) {
      try {
        Scanner s = new Scanner(new File(filename + EXT), "UTF-8");
        File newFile = new File(filename + "_dec" + EXT);
        newFile.delete();
        newFile.createNewFile();
        PrintWriter w = new PrintWriter(newFile);
        processHeader(s, w);
        BigInteger b = new BigInteger(counter, 16);
        while (s.hasNext()) {
          readBlock(s);
          String n = b.toString(16);
          if (n.length() < 32) {
            for (int i = n.length(); i < 32; i++) {
              n = "0" + n;
            }
          } else if (n.length() > 32) {
            n = n.substring(0, 32);
          }
          cipherBlock = stateToBits(AES.encrypt(n, key));
          block = xor(block, cipherBlock);
          writeBlock(w);
          b.add(new BigInteger("1"));
        }
        w.flush();
        w.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }// decryptCTR method

    /* This is the driver code used for testing purposes.
     * Do NOT modify it.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("The first argument must be -e or -d");
            System.out.println(
                   "while the second argument must be -ECB, -CBC. or -CTR");
            System.exit(1);
        }
        if (args[1].equals("-ECB")) {
            if (args.length == 4) {
                String filename = args[2];
                String key = args[3];
                if (args[0].equals("-e")) {
                    encryptECB(filename,key);
                }
                else if (args[0].equals("-d")) {
                    decryptECB(filename,key);
                } else {
                    System.out.println("The first argument must be -e or -d");
                    System.exit(1);
                }
            } else {
                System.out.println("Usage: java ImageCipher [-e or -d] -ECB " +
                                   "<image file name without .pgm> <key>");
                System.exit(1);
            }
        } else if (args[1].equals("-CBC")) {
            if (args.length == 5) {
                String filename = args[2];
                String key = args[3];
                String IV = args[4];
                if (args[0].equals("-e")) {
                    encryptCBC(filename,key,IV);
                }
                else if (args[0].equals("-d")) {
                    decryptCBC(filename,key,IV);
                } else {
                    System.out.println("The first argument must be -e or -d");
                    System.exit(1);
                }
            } else {
                System.out.println("Usage: java ImageCipher [-e or -d] -CBC " +
                                   "<image file name without .pgm> <key> <IV>");
                System.exit(1);
            }
        } else if (args[1].equals("-CTR")) {
            if (args.length == 5) {
                String filename = args[2];
                String key = args[3];
                String counter = args[4];
                if (args[0].equals("-e")) {
                    encryptCTR(filename,key,counter);
                }
                else if (args[0].equals("-d")) {
                    decryptCTR(filename,key,counter);
                } else {
                    System.out.println("The first argument must be -e or -d");
                    System.exit(1);
                }
            } else {
                System.out.println("Usage: java ImageCipher [-e or -d] -CTR " +
                                   "<image file name without .pgm> <key> <IV>");
                System.exit(1);
            }
        } else {
            System.out.println("The second argument must be -ECB, -CBC, or " +
                               "-CTR");
            System.exit(1);
        }

    }// main method
}// ImageCipher class
