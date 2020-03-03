/*****************************************************
   CS 326 - Spring 2020 - Assignment #1
   Student's full name: Martin Alexander Mueller
 *****************************************************/

import java.util.*;

class PCencrypt {

    /* the 5x5 matrix used in the Playfair Cipher */
    private static String[][] matrix;

    /* mapping from character to row and column index in the matrix above,
       as computed by two PCUtils methods.
     */
    private static HashMap<String,Integer> rows, cols;

    /* Given a preprocessed plaintext string, return the ciphertext
       string as specified in the description of the Playfair cipher
       in the handout.
     */
    static String encrypt(String plaintext) {
      char[] array = plaintext.toCharArray();
      for (int i = 0; i < array.length; i += 2) {
        String left = Character.toString(array[i]);
        String right = Character.toString(array[i + 1]);
        if (rows.get(left) == rows.get(right)) {
          array[i] = matrix[rows.get(left)]
                     [(cols.get(left) + 1) % 5].charAt(0);
          array[i + 1] = matrix[rows.get(right)]
                         [(cols.get(right) + 1) % 5].charAt(0);
        } else if (cols.get(left) == cols.get(right)) {
          array[i] = matrix[(rows.get(left) + 1) % 5]
                     [cols.get(left)].charAt(0);
          array[i + 1] = matrix[(rows.get(right) + 1) % 5]
                         [cols.get(right)].charAt(0);
        } else {
          array[i] = matrix[rows.get(left)]
                     [cols.get(right)].charAt(0);
          array[i + 1] = matrix[rows.get(right)]
                         [cols.get(left)].charAt(0);
        }
      }
      return new String(array);
    }// encrypt method
    
    /* This method will be used for testing purposes. 
       You may NOT modify it.
     */
    public static void main(String[] args) {
        
        /* to be called with:
            java PCencrypt <KEY> <PLAINTEXT 1> <PLAINTEXT 2> ... <PLAINTEXT n>
        */
        
        matrix = PCUtils.computeMatrix(args[0].toUpperCase());
        PCUtils.printMatrix(matrix);
        rows = PCUtils.precomputeRows(matrix);
        cols = PCUtils.precomputeColumns(matrix);
        
        for(int i = 1; i < args.length; i++) {
            String plaintext = PCUtils.preprocess(args[i]);
            System.out.println("----------------------------------");
            System.out.println("Plaintext:  " + plaintext);
            System.out.println("Ciphertext: " +  encrypt( plaintext ) );
        }        
    }// main method
    
}// PCencrypt class
