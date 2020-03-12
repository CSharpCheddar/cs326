/*****************************************************
   CS 326 - Spring 2020 - Assignment #3

   Your name(s): Martin Mueller

 *****************************************************/

import java.util.*;

class MITM extends BruteForce {

    /* Given two plaintext-ciphertext pairs and the number numBits of
     * significant bits in each key, perform a MITM attack of
     * DoubleAES (refer to your class notes about the algorithmic
     * details of this attack) and output the key pair that produces
     * the two given ciphertexts given the respective plaintexts. This
     * method also records the time in seconds to find the key pair.
     * This method sends to the standard output stream some output
     * whose format is specified in the handout.
    */
    static void meetInTheMiddle(String plaintext1, String ciphertext1, 
                                String plaintext2, String ciphertext2,
                                int numBits)  {

        long startTime, elapsedTime;
        startTime = System.currentTimeMillis();
        String pk1 = "";
        String pk2 = "";
        HashMap<String, String> hm = new HashMap<>();
        for (int key1 = 0; key1 < Math.pow(2, numBits); key1++) {
          pk1 = String.format("%32S",
                              Integer.toHexString(key1)).replace(' ', '0');
          hm.put(stateToString(encrypt(plaintext1, pk1)), pk1);
        }
        for (int key2 = 0; key2 < Math.pow(2, numBits); key2++) {
          pk2 = String.format("%32S",
                              Integer.toHexString(key2)).replace(' ', '0');
          String possible = stateToString(decrypt(ciphertext1, pk2));
          if (hm.containsKey(possible)) {
            pk1 = hm.get(possible);
            String possible2 = stateToString(encryptDAES(pk1, pk2, plaintext2));
            if (possible2.equals(ciphertext2)) {
              break;
            }
          }
        }
        pk1 = pk1.toLowerCase();
        pk2 = pk2.toLowerCase();
        System.out.printf("%45s %s  time = ", pk1, pk2);
        elapsedTime = System.currentTimeMillis() - startTime;
        System.out.print( (elapsedTime/1000.0) + "s\n" );
    }// meetInTheMiddle method
    
    /* This code is used for testing purposes.  This driver code
     * invokes the MIMT method repeatedly with numBits equal to 2,
     * then to 3, then to 4, etc. You will have to terminate this
     * program (e.g.,with a CTRL-C) as soon as the TOTAL runtime of
     * the last meet-in-the-middle search has exceeded 60 seconds.
     *
     * Do NOT modify this method.
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java MITM [test1 or test2] " +
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
            meetInTheMiddle(P1,C1,P2,C2,numBits);            
        }// loop on numBits        
    }// main method
}// MITM class
