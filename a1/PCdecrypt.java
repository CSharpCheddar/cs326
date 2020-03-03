/*****************************************************
   CS 326 - Spring 2020 - Assignment #1
   Student's full name: Martin Alexander Mueller
*****************************************************/

import java.util.*;

class PCdecrypt {

    /* the 5x5 matrix used in the Playfair Cipher */
    private static String[][] matrix;
    
    /* mapping from character to row and column index in the matrix above,
       as computed by two PCUtils methods.
    */
    private static HashMap<String,Integer> rows, cols;
    
    /* Given a ciphertext string, return the (preprocessed) plaintext
       string obtained by the inverse algorithm of the one specified
       in the description of the Playfair Cipher in the handout.
       Note that this method does NOT undo the preprocessing of the plaintext.
    */
    static String decrypt(String ciphertext) {
      char[] array = ciphertext.toCharArray();
      for (int i = 0; i < array.length; i += 2) {
        String left = Character.toString(array[i]);
        String right = Character.toString(array[i + 1]);
        if (rows.get(left) == rows.get(right)) {
          array[i] = matrix[rows.get(left)]
                     [(cols.get(left) + 4) % 5].charAt(0);
          array[i + 1] = matrix[rows.get(right)]
                         [(cols.get(right) + 4) % 5].charAt(0);
        } else if (cols.get(left) == cols.get(right)) {
          array[i] = matrix[(rows.get(left) + 4) % 5]
                     [cols.get(left)].charAt(0);
          array[i + 1] = matrix[(rows.get(right) + 4) % 5]
                         [cols.get(right)].charAt(0);
        } else {
          array[i] = matrix[rows.get(left)]
                     [cols.get(right)].charAt(0);
          array[i + 1] = matrix[rows.get(right)]
                     [cols.get(left)].charAt(0);
        }
      }
      return new String(array);
    }// decrypt method
    
    /* This method will be used for testing purposes. 
       You may NOT modify it.
     */
    public static void main(String[] args) {
        
        /* to be called with:
           java PCdecrypt <KEY> <CIPHERTEXT 1>  ... <CIPHERTEXT n>
        */
        
        matrix = PCUtils.computeMatrix(args[0].toUpperCase());
        PCUtils.printMatrix(matrix);
        rows = PCUtils.precomputeRows(matrix);
        cols = PCUtils.precomputeColumns(matrix);
        
        for(int i=1; i<args.length; i++) {
            String ciphertext = args[i];
            System.out.println("----------------------------------");
            System.out.println("Ciphertext: " + ciphertext);
            System.out.println("PPlaintext: " + decrypt( ciphertext ));
        }        
    }// main method
    
}// PCdecrypt class
