/*****************************************************
   CS 326 - Spring 2020 - Assignment #4
   Student's full name(s): Martin Mueller
*****************************************************/

import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.math.*;

class Steganography {

    // block size in bits
    static final int BSIZE = 128;

    // used to store the next block read from or written to the image file
    static int[] block =  new int[BSIZE];

    // file extension for all image files in this assignment (you MUST use
    // this constant everywhere instead of the string itself)
    static final String EXT = ".pgm";

    // number of rows and columns of pixels in the current image
    static int numRows, numCols;

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

    /* Given a Scanner object and a PrintWriter object, copy to the
     * latter the first four lines of the former. Also initialize the
     * numCols and numRows static variables to the values read on the
     * third line.
     */
    static void processHeader(Scanner s, PrintWriter w) throws Exception {
      for (int i = 0; i < 4; i++) {
        String line = s.nextLine();
        w.println(line);
        if (i == 2) {
          String[] array = line.split(" ");
          numRows = Integer.parseInt(array[0]);
          numCols = Integer.parseInt(array[1]);
        }
      }
    }// processHeader method

    /* Given a Scanner object, read and ignore the first four lines of
     * the corresponding file.
     */
    static void skipHeader(Scanner s) throws Exception {
      for (int i = 0; i < 4; i++) {
        s.nextLine();
      }
    }// skipHeader method

    /* Given a two-character String (in ASCII), encode its 16 bits in
     * the least significant bit of the 16 bytes of the block static
     * variable.
     */
    static void encode(String message) {
      for (int i = 0; i < message.length(); i++) {
        int n = (int)message.charAt(i);
        for (int j = 0; j < 8; j++) {
          block[64 * i + 8 * j + 7] = (n >> (7 - j)) & 1;
        }
      }
    }// encode method

    /* Read out the least significant bit of the 16 bytes contained in
     * the block static variable and return the string containing the
     * two characters with the corresponding ASCII codes.
     */
    static String decode() {
      char[] c = new char[2];
      for (int i = 0; i < 2; i++) {
        int n = 0;
        for (int j = 0; j < 8; j++) {
          n = n | (char)(block[64 * i + 8 * j + 7] << (7 - j));
        }
        c[i] = (char)n;
      }
      return new String(c);
    }// decode method

    /* Given a message and an image file name (without the extension),
     * encode the bit string representation of the ASCII codes of the
     * message's characters into the first pixel values of the given
     * image. The details of the required encoding are given in the
     * handout. The resulting image must be stored in a file whose
     * name is equal to
     * filename + "_steg" + EXT
     * You can assume that the length of the input message is even.
     * This method must check that the whole message can fit in
     * encoded form in the input image. If that is not the case, it
     * must abort the encoding and output the error message specified
     * in the handout and terminate the program.
     */
    static void writeImage(String message, String filename) {
      try {
        Scanner s = new Scanner(new File(filename + EXT), "UTF-8");
        File newFile = new File(filename + "_steg" + EXT);
        newFile.delete();
        newFile.createNewFile();
        PrintWriter w = new PrintWriter(newFile);
        processHeader(s, w);
        if ((numCols * numRows) < ((message.length() + 6) * 8)) {
          System.out.printf("The message is too long (%d "
              + "characters plus 6 characters for the block count,\n",
              message.length());
          System.out.printf("i.e., %d bits); the image "
              + "contains only %d pixels.\n",
              (message.length() + 6) * 8, numCols * numRows);
        } else {
          String string = String.format("%06d%s",
              message.length() / 2, message);
          while (s.hasNext()) {
            readBlock(s);
            if (string.length() > 0) {
              if (string.length() == 2) {
                encode(string);
                string = "";
              } else {
                encode(string.substring(0, 2));
                string = string.substring(2);
              }
            }
            writeBlock(w);
          }
        }
        w.flush();
        w.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }// write method

    /* Given an image file name (without the extension), read in the
     * image, decode the message it contains, and send this message to
     * the standard output stream followed by a single newline
     * character. The details of the required encoding are given in
     * the handout.
     */
    static void readImage(String filename) {
      try {
        Scanner s = new Scanner(new File(filename + EXT), "UTF-8");
        File newFile = new File(filename + "_steg" + EXT);
        newFile.delete();
        newFile.createNewFile();
        PrintWriter w = new PrintWriter(newFile);
        skipHeader(s);
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < 3; i++) {
          readBlock(s);
          sb.append(decode());
        }
        int n = Integer.parseInt(sb.toString());
        sb = new StringBuilder("");
        for (int i = 0; i < n; i++) {
          readBlock(s);
          sb.append(decode());
        }
        System.out.println(sb.toString());
        w.flush();
        w.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }// read method

    /* This is the driver code used for testing purposes.
     * Do NOT modify it.
     */
    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) {
            System.out.println("Usage: java Steganography -w " +
                               "<image file name without .pgm> <message>");
            System.out.println("or");
            System.out.println("Usage: java Steganography -r " +
                               "<image file name without .pgm>");
            System.exit(1);
        }
        boolean write = args[0].equals("-w");
        String filename = args[1];
        String message = args.length == 3 ? args[2] : "";
        if (write) {
            if (message.endsWith(".txt")) {
                try {
                    message =
                        new String(Files.readAllBytes(Paths.get(message)));
                } catch (IOException e) {
                    System.out.println("Error loading message from file.");
                    System.exit(1);
                }
            }
            writeImage(message,filename);
        } else {
            readImage(filename);
        }
    }// main method
}// Steganography class
