/*****************************************************
   CS 326 - Spring 2020 - Assignment #1
   Student's full name: Martin Alexander Mueller
 *****************************************************/

import java.util.*;

class PCUtils {

    /* Note that the letter J was removed from this alphabet */
    private static String[] alphabet = {
        "A","B","C","D","E","F","G","H","I","K","L","M","N","O","P",
        "Q","R","S","T","U","V","W","X","Y","Z" };
    
    /* Take in a key and return the matrix built according to the Playfair 
     * Cipher specification.
     * Your output matrix must contain the letter 'I' but not the letter 'J'.
     * You can assume that the input key only contains uppercase
     * letters excluding 'J'.
     */
    static String[][] computeMatrix(String key) {
      // get rid of repeating characters
      String temp = new String(key);
      for (int i = 1; i < temp.length(); i++) {
        for (int j = i; j < temp.length(); j++) {
          if (temp.charAt(i - 1) == temp.charAt(j)) {
            temp = j == temp.length() - 1 ? temp.substring(0, j) : temp.substring(0, j) + temp.substring(j + 1);
          }
        }
      }
      // create the Playfair matrix
      String[][] matrix = new String[5][5];
      int alpha = 0;
      for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix[0].length; j++) {
          if (5 * i + j < temp.length()) {
            matrix[i][j] = Character.toString(temp.charAt(5 * i + j));
          } else {
            while (temp.contains(alphabet[alpha])) {
              alpha++;
            }
            matrix[i][j] = alphabet[alpha];
            alpha++;
          }
        }
      }
      return matrix;
    }// computeMatrix method

    /* Take *any* 2D array of Strings and send it to the standard output
     * stream with no space before or after any elements and a single newline
     * character at the end of each row. The handout contains a sample output
     * of this method.
     */
    static void printMatrix(String[][] matrix) {
      for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix[0].length; j++) {
          System.out.print(matrix[i][j]);
        }
        System.out.println();
      }
    }// printMatrix method

    /* Take in a Playfair Cipher matrix of single characters and
     * return a mapping from each character to the index of the row in
     * which this character appears in the matrix. In this mapping,
     * the first row has index 0 and the last row has index 4. Note
     * that all characters in the alphabet (excluding 'J') must appear
     * in the output mapping.
     */
    static HashMap<String,Integer> precomputeRows(String[][] matrix) {
      HashMap<String, Integer> map = new HashMap<>();
      for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix[0].length; j++) {
          map.put(matrix[i][j], i);
        }
      }
      return map;
    }// precomputeRows method

    /* Take in a Playfair Cipher matrix of single characters and
     * return a mapping from each character to the index of the column
     * in which this character appears in the matrix. In this mapping,
     * the first column has index 0 and the last column has index
     * 4. Note that all characters in the alphabet (excluding 'J')
     * must appear in the output mapping.
     */
    static HashMap<String,Integer> precomputeColumns(String[][] matrix) {
      HashMap<String, Integer> map = new HashMap<>();
      for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix[0].length; j++) {
          map.put(matrix[i][j], j);
        }
      }
      return map;
    }// precomputeColumns method

    /* Take in a plaintext string and return it after applying
     * the following modifications to it:
     * 0) Convert the string to all uppercase letters (just in case)
     * 1) insert an uppercase 'X' to break any repeating letters that would
     * otherwise end up in the same pair in the output string (step 1 of 
     * the algorithm's description),
     * 2) add one extra 'X' at the end of the output string if needed to
     * make sure it has an even length, and
     * 3) replace all occurrences of the letter 'J' with an 'I'.
     */
    static String preprocess(String plaintext) {
      // convert string to upper case
      plaintext = plaintext.toUpperCase();
      // break up repeating letters with uppercase X
      for (int i = 0; i < plaintext.length() - 1; i += 2) {
        if (plaintext.charAt(i) == plaintext.charAt(i + 1)) {
          plaintext = plaintext.substring(0, i + 1) + "X" + plaintext.substring(i + 1);
        }
      }
      // add an extra 'X' at the end if necessary
      if (plaintext.length() % 2 == 1) {
        plaintext += "X";
      }
      // replace all 'J's with 'I's
      plaintext = plaintext.replace('J', 'I');
      return plaintext;
    }// preprocess method
    
    /* This method will be used for testing purposes. 
     * You may NOT modify it.
     */
    public static void main(String[] args) {
        
        // to be called with: java PCUtils <key> <plaintext>
        
        System.out.println("Key: " + args[0].toUpperCase());
        String[][] matrix = computeMatrix(args[0].toUpperCase());
        printMatrix(matrix);
        System.out.println(precomputeRows(matrix));
        System.out.println(precomputeColumns(matrix));
        System.out.println("Plaintext: " + args[1]);
        System.out.println("Processed: " + preprocess(args[1]));
    }// main method
}// PCUtils class
