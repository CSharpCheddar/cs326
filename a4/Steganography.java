/*****************************************************
   CS 326 - Spring 2020 - Assignment #4
   Student's full name: <Your name goes here>
                        <Your name goes here> 
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

        /* To be completed */
        
    }// readBlock method

    /* Given a PrintWriter object, write to it the 16 integers stored
     * as the 16 corresponding 8-bit patterns in the 128-bit instance
     * variable called 'block'
     */
    static void writeBlock(PrintWriter w) throws Exception {

        /* To be completed */
        
    }// writeBlock method

    /* Given a Scanner object and a PrintWriter object, copy to the
     * latter the first four lines of the former. Also initialize the
     * numCols and numRows static variables to the values read on the
     * third line.
     */
    static void processHeader(Scanner s, PrintWriter w) throws Exception {

        /* To be completed */
        
    }// processHeader method

    /* Given a Scanner object, read and ignore the first four lines of
     * the corresponding file.
     */
    static void skipHeader(Scanner s) throws Exception {

        /* To be completed */
        
    }// skipHeader method

    /* Given a two-character String (in ASCII), encode its 16 bits in
     * the least significant bit of the 16 bytes of the block static
     * variable.
     */
    static void encode(String message) {

        /* To be completed */
        
    }// encode method

    /* Read out the least significant bit of the 16 bytes contained in
     * the block static variable and return the string containing the
     * two characters with the corresponding ASCII codes.
     */
    static String decode() {

        /* To be completed */

        return "";   //  delete this line after completing this method

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

        /* To be completed */
        
    }// write method

    /* Given an image file name (without the extension), read in the
     * image, decode the message it contains, and send this message to
     * the standard output stream followed by a single newline
     * character. The details of the required encoding are given in
     * the handout.
     */
    static void readImage(String filename) {

        /* To be completed */
        
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
