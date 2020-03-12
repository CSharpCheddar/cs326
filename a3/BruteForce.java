/*****************************************************
   CS 326 - Spring 2020 - Assignment #3

   Your name(s): Martin Mueller

 *****************************************************/

class BruteForce extends DoubleAES {

    /* given a binary string, return the hexadecimal representation of the
     * input as a String. Sample input/output:
     *      input: "01101110"           output: "6E"
     * Note: you can assume that the length of the input is a multiple of 4.
     */
    static String binStringToHex(String bits) {
      return Integer.toString(Integer.parseInt(bits, 2), 16);
    }// binStringToHex method

    /* Given two plaintext-ciphertext pairs and the number numBits of
     * significant bits in each key, perform a brute-force search of
     * DoubleAES, that is, try all key pairs (in which each key is
     * numBits long) and output the key pair that produces the two
     * given ciphertexts given the respective plaintexts. This method
     * also records the time in seconds to find the key pair.  This
     * method sends to the standard output steam some output whose format is
     * specified in the handout.
    */
    static void bruteForce(String plaintext1, String ciphertext1, 
                           String plaintext2, String ciphertext2, int numBits) 
    {
        long startTime, elapsedTime;
        startTime = System.currentTimeMillis();
        String pk1 = "";
        String pk2 = "";
        String possible1 = "";
        String possible2 = "";
        for (int key1 = 0; key1 < Math.pow(2, numBits); key1++) {
          for (int key2 = 0; key2 < Math.pow(2, numBits); key2++) {
            pk1 = String.format("%32S",
                                Integer.toHexString(key1)).replace(' ', '0');
            pk2 = String.format("%32S",
                                Integer.toHexString(key2)).replace(' ', '0');
            possible1 = stateToString(encryptDAES(pk1, pk2, plaintext1));
            possible2 = stateToString(encryptDAES(pk1, pk2, plaintext2));
            if (possible1.equals(ciphertext1)
                && possible2.equals(ciphertext2)) {
              break;
            }
          }
          if (possible1.equals(ciphertext1)
              && possible2.equals(ciphertext2)) {
            break;
          }
        }
        pk1 = pk1.toLowerCase();
        pk2 = pk2.toLowerCase();
        System.out.printf("%45s %s  time = ", pk1, pk2);
        elapsedTime = System.currentTimeMillis() - startTime;
        System.out.print( (elapsedTime/1000.0) + "s\n" );
    }// bruteForce method

    /* Given a test number and a number of bits, this method returns an
       array of two keys, each of which is numBits long and has a specific
       value hardcoded in the test case number. This method also outputs to
       the console window the value of the two keys it returns. 
        
       This method will be called to produce the two keys that
       DoubleAES will use to encrypt two plaintext blocks and before
       invoking the bruteForce method.  Therefore, the key pair that
       the brute force search is trying to find is printed just above
       the output of the search.

       Do NOT modify this method.
    */
    static String[] getKeyPair(int testNumber,int numBits) {
        String key1 = "",  key2 = "";
        switch (testNumber) {
        case 1:
            key1 = "1";
            key2 = "1";
            for(int i = 1; i <= numBits - 1; i++) {
                key1 += "1";
                key2 += "0";
            }
            break;
        case 2:
            key1 = "1";
            key2 = "1";
            for(int i = 1; i <= numBits - 1; i++) {
                key1 += "1";
                key2 += (i % 2) +"";
            }
            break;
        }
        while (key1.length() % 4 != 0) {
            key1 = "0" + key1;
            key2 = "0" + key2;
        }
        key1 = binStringToHex(key1);
        key2 = binStringToHex(key2);
        while (key1.length() < 32) {
            key1 = "0" + key1;
            key2 = "0" + key2;
        }
        System.out.println("Actual keys: " + key1 + " " + key2 + 
                           "  numBits = " + numBits);
        
        return new String[] { key1, key2 };
    }// getKeyPair method

    /* This code is used for testing purposes.  This driver code
     * invokes the bruteForce method repeatedly with numBits equal to
     * 2, then to 3, then to 4, etc. You will have to terminate this
     * program (e.g.,with a CTRL-C) as soon as the TOTAL runtime of
     * the last brute-force search has exceeded 60 seconds.
     *
     * Do NOT modify this method.
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java BruteForce [test1 or test2] " +
                               "<P1> <P2>");
            System.exit(1);
        }
        String testCase = args[0];
        String P1 = args[1];
        String P2 = args[2];    
        String key1 = "", key2 = "";
        int numBits;
        String[] keys = null;
        for(numBits = 2; true; numBits++) {
            // generate a key pair
            if (testCase.equals("test1")) {
                keys = getKeyPair(1,numBits);
            } else if (testCase.equals("test2")) {
                keys = getKeyPair(2,numBits);
            } else {
                System.out.println("This test case is not implemented.");
                System.exit(1);
            }
            key1 = keys[0];
            key2 = keys[1];
            // encrypt the plaintexts
            String C1 = stateToString(encryptDAES(key1,key2,P1));
            String C2 = stateToString(encryptDAES(key1,key2,P2));
            bruteForce(P1,C1,P2,C2,numBits);            
        }// loop on numBits        
    }// main method
}// BruteForce class
