/*
 * Test harness for AES.java.
 */

class Test extends AES {
  public static void main(String[] args) {
    String plaintext = "000102030405060708090A0B0C0D0E0F";
    String key = "000102030405060708090A0B0C0D0E0F";
    System.out.println("\nEncrypting string " + plaintext + "...\n");
    printMatrix(hexStringToByteArray(plaintext));
    System.out.println();
    int[][] ciphermatrix = encrypt(plaintext, key);
    printMatrix(ciphermatrix);
    System.out.println();
    String ciphertext = "";
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if (ciphermatrix[j][i] < 0x10) {
          ciphertext += "0";
        }
        ciphertext += Integer.toHexString(ciphermatrix[j][i]);
      }
    }
    ciphertext = ciphertext.toUpperCase();
    System.out.println("Decrypting string " + ciphertext + "...\n");
    printMatrix(hexStringToByteArray(ciphertext));
    System.out.println();
    int[][] plainmatrix = decrypt(ciphertext, key);
    String newplaintext = "";
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if (plainmatrix[j][i] < 0x10) {
          newplaintext += "0";
        }
        newplaintext += Integer.toHexString(plainmatrix[j][i]);
      }
    }
    newplaintext = newplaintext.toUpperCase();
    printMatrix(hexStringToByteArray(newplaintext));
    System.out.println();
    System.out.println("Final result: " + newplaintext);
    System.out.println("Result " + (newplaintext.equals(plaintext)
                       ? "equal" : "not equal") + ".\n");
  }
}
