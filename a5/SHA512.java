/*****************************************************
   CS 326 - Spring 2020 - Assignment #5
   Student's full name: Martin Mueller

*****************************************************/

import java.math.*;
import java.io.*;
import java.nio.file.*;

/*
    See reading #13a and https://en.wikipedia.org/wiki/SHA-2
*/

class SHA512 {

    // first 80 prime numbers
    static int[] primes = {  2,   3,   5,   7,  11,  13,  17,  19,  23,  29,
                            31,  37,  41,  43,  47,  53,  59,  61,  67,  71,
                            73,  79,  83,  89,  97, 101, 103, 107, 109, 113,
                           127, 131, 137, 139, 149, 151, 157, 163, 167, 173,
                           179, 181, 191, 193, 197, 199, 211, 223, 227, 229,
                           233, 239, 241, 251, 257, 263, 269, 271, 277, 281,
                           283, 293, 307, 311, 313, 317, 331, 337, 347, 349,
                           353, 359, 367, 373, 379, 383, 389, 397, 401, 409 };

    /* the 80 SHA-512 round constants */
    static long[] k = {
                0x428a2f98d728ae22L, 0x7137449123ef65cdL, 0xb5c0fbcfec4d3b2fL,
                0xe9b5dba58189dbbcL, 0x3956c25bf348b538L, 0x59f111f1b605d019L,
                0x923f82a4af194f9bL, 0xab1c5ed5da6d8118L, 0xd807aa98a3030242L,
                0x12835b0145706fbeL, 0x243185be4ee4b28cL, 0x550c7dc3d5ffb4e2L,
                0x72be5d74f27b896fL, 0x80deb1fe3b1696b1L, 0x9bdc06a725c71235L,
                0xc19bf174cf692694L, 0xe49b69c19ef14ad2L, 0xefbe4786384f25e3L,
                0x0fc19dc68b8cd5b5L, 0x240ca1cc77ac9c65L, 0x2de92c6f592b0275L,
                0x4a7484aa6ea6e483L, 0x5cb0a9dcbd41fbd4L, 0x76f988da831153b5L,
                0x983e5152ee66dfabL, 0xa831c66d2db43210L, 0xb00327c898fb213fL,
                0xbf597fc7beef0ee4L, 0xc6e00bf33da88fc2L, 0xd5a79147930aa725L,
                0x06ca6351e003826fL, 0x142929670a0e6e70L, 0x27b70a8546d22ffcL,
                0x2e1b21385c26c926L, 0x4d2c6dfc5ac42aedL, 0x53380d139d95b3dfL,
                0x650a73548baf63deL, 0x766a0abb3c77b2a8L, 0x81c2c92e47edaee6L,
                0x92722c851482353bL, 0xa2bfe8a14cf10364L, 0xa81a664bbc423001L,
                0xc24b8b70d0f89791L, 0xc76c51a30654be30L, 0xd192e819d6ef5218L,
                0xd69906245565a910L, 0xf40e35855771202aL, 0x106aa07032bbd1b8L,
                0x19a4c116b8d2d0c8L, 0x1e376c085141ab53L, 0x2748774cdf8eeb99L,
                0x34b0bcb5e19b48a8L, 0x391c0cb3c5c95a63L, 0x4ed8aa4ae3418acbL,
                0x5b9cca4f7763e373L, 0x682e6ff3d6b2b8a3L, 0x748f82ee5defb2fcL,
                0x78a5636f43172f60L, 0x84c87814a1f0ab72L, 0x8cc702081a6439ecL,
                0x90befffa23631e28L, 0xa4506cebde82bde9L, 0xbef9a3f7b2c67915L,
                0xc67178f2e372532bL, 0xca273eceea26619cL, 0xd186b8c721c0c207L,
                0xeada7dd6cde0eb1eL, 0xf57d4f7fee6ed178L, 0x06f067aa72176fbaL,
                0x0a637dc5a2c898a6L, 0x113f9804bef90daeL, 0x1b710b35131c471bL,
                0x28db77f523047d84L, 0x32caab7b40c72493L, 0x3c9ebe0a15c9bebcL,
                0x431d67c49c100d4cL, 0x4cc5d4becb3e42b6L, 0x597f299cfc657e2aL,
                0x5fcb6fab3ad6faecL, 0x6c44198c4a475817L };

    /* Return the square root of its input with a precision of 20 decimal
     * digits after the decimal point, which is enough to give us at least
     * 64 binary digits for the fractional part.
     *
     * Code adapted from:
     *  http://stackoverflow.com/questions/13649703/square-root-of-
     *                       bigdecimal-in-java
     *
     * Do NOT modify this method.
     */
    public static BigDecimal sqrt(BigDecimal n) {
        BigDecimal two = BigDecimal.valueOf(2);
        BigDecimal x0 = BigDecimal.ZERO;
        BigDecimal x1 = new BigDecimal(Math.sqrt(n.doubleValue()));
        // 20 below is the number of decimal digits in the fractional part
        while ( ! x0.equals(x1) ) {
            x0 = x1;
            x1 = n.divide(x0, 20, RoundingMode.HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(two, 20,RoundingMode.HALF_UP);
        }
        return x1;
    }// sqrt method

    /* Send to the output stream the input byte values, n to a line,
     * separated by a space, with each value formatted as a two-digit
     * hexadecimal string.
     *
     * Do NOT modify this method.
     */
    static void printBytes(byte[] bytes, int n) {
        for(int i = 0; i < bytes.length; i++) {
            if ( i > 0 && i % n == 0) {
                System.out.println();
            }
            System.out.format(" %02X",bytes[i]);
        }
        System.out.println();
    }// printBytes method

    /* Send to the output stream the input long values, 4 to a line,
     * separated by a space, with each value formatted as a
     * sixteen-digit hexadecimal string. Each group of 16 values (or
     * block of 1,024 bits) is also separated from the next one by a
     * blank line. Finally, an additional blank line is added at the end.
     *
     * Do NOT modify this method.
     */
    static void printLongs(long[] longs) {
        for(int i=0; i<longs.length; i++) {
            if (i > 0 && i % 4 == 0) {
                System.out.println();
                if (i % 16 == 0) {
                    System.out.println();
                }
            }
            System.out.format(" %016X",longs[i]);
        }
        System.out.println();
    }// printLongs method

    /* Load and return all of the bytes in the file with the given name.
     */
    static byte[] readMessage(String filename) {
      File file = new File(filename);
      byte[] bytes = new byte[(int) file.length()];
      try {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        for (int i = 0; i < file.length(); i++) {
          bytes[i] = (byte) reader.read();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return bytes;
    }// readMessage method

    /* Return the given bytes with just the right number of zero-valued
     * bytes added at the end to accommodate the padding and message length
     * field required by the SHA-512 algorithm.
     */
    static byte[] makeSpaceForPadding(byte[] bytes) {
      byte[] newbytes = new byte[bytes.length + 128 - (bytes.length % 128)];
      for (int i = 0; i < bytes.length; i++) {
        newbytes[i] = bytes[i];
      }
      return newbytes;
    }// makeSpaceForPadding method

    /* Given a message (byte array) of the given length (in bytes) followed
     * by enough zero-valued bytes to store the padding and message length
     * field required by the SHA-512 algorithm, replace the trailing
     * zero-valued bytes by the padding bit pattern and the value of message
     * length (in bits).
     */
    static void addPadding(byte[] bytes, int messageLength) {
      bytes[messageLength] = (byte) (1 << 7);
      messageLength *= 8;
      for (int i = 1; i <= 4; i++) {
        bytes[bytes.length - i] = (byte) messageLength;
        messageLength = messageLength >>> 8;
      }
    }// addPadding method

    /* Given a byte array whose length is a multiple of eight, build and
     * return a long array that contains the input bytes in the same order
     * with eight bytes stored in big-endian order in each long. For example,
     * if the input bytes (in hex) are:
     *  { 00, 01, 02, 03, 04, 05, 06, 07, 08, 09, 0A, 0B, 0C, 0D, 0E, 0F }
     * the output would be:
     *  { 0x0001020304050607L, 0x08090A0B0C0D0E0FL}
     */
    static long[] toLongs(byte[] bytes) {
      long[] longs = new long[bytes.length / 8];
      for (int i = 0; i < longs.length; i++) {
        long l = 0;
        for (int j = 0; j < 8; j++) {
          l = l | (((long) bytes[8 * i + j]) << (8 * (7 - j)));
        }
        longs[i] = l;
      }
      return longs;
    }// toLongs method

    /* Given a file name, use the six previous methods to read the bytes of the
     * message from the file, add the padding to this message (in two steps),
     * then convert the result to longs, and return them.
     * This method must also send debugging information to the output stream,
     * including a one-line message followed by the byte or long values after
     * each preprocessing step. The format of this output must match EXACTLY the
     * one shown in the handout. Note that the four byte or long arrays may
     * only be output if their length is less than 300. However, the one-line
     * message must always be output.
     */
    static long[] prepareMessage(String filename) {
      byte[] msg = readMessage(filename);
      int len = msg.length;
      System.out.printf("Original message contains %d bytes.\n", msg.length);
      if (msg.length < 300) {
        printBytes(msg, 32);
      }
      msg = makeSpaceForPadding(msg);
      System.out.printf("Extended message contains %d bytes.\n", msg.length);
      if (msg.length < 300) {
        printBytes(msg, 32);
      }
      addPadding(msg, len);
      System.out.printf("Padded message contains %d bytes.\n", msg.length);
      if (msg.length < 300) {
        printBytes(msg, 32);
      }
      long[] longs = toLongs(msg);
      System.out.printf("Padded message contains %d blocks or %d longs.\n",
                        longs.length / 16, longs.length);
      if (longs.length < 300) {
        printLongs(longs);
      }
      return longs;
    }// prepareMessage method

    /* Given a positive integer, return the first 64 bits of the fractional
     * part of its square root. The return values of this method for the
     * first 8 prime numbers are given as the values a through h at the bottom
     * of page 357 of reading #13a
     */
    static long getSquareRootBits(int n) {
      BigDecimal d = sqrt(new BigDecimal(n));
      return d.remainder(BigDecimal.ONE).movePointRight(64).longValue();
    }// getSquareRootBits method

    /* Create, initialize, and return the W array, i.e., W_t in step 1 on
     * page 363 of reading #13a
     */
    static long[] prepareRoundSchedule(long[] block) {
      long[] w = new long[80];
      for (int i = 0; i < 16; i++) {
        w[i] = block[i];
      }
      for (int i = 16; i < 80; i++) {
        w[i] = sigma1(w[i - 2]) + w[i - 7] + sigma0(w[i - 15]) + w[i - 16];
      }
      return w;
    }// prepareRoundSchedule method

    /* The ROTR^n(x) function defined on page 361 of reading #13a
     */
    static long rotr(long x, int n) {
      return Long.rotateRight(x, n);
    }// rotr method

    /* The SHR^n(x) function defined on page 361 of reading #13a
     */
    static long shr(long x, int n) {
      return x >>> n;
    }// shr method

    /* The sigma_0^{512}(x) function defined on page 361 of reading #13a
     */
    static long sigma0(long x) {
      return rotr(x, 1) ^ rotr(x, 8) ^ shr(x, 7);
    }// sigma0 method

    /* The sigma_1^{512}(x) function defined on page 361 of reading #13a
     */
    static long sigma1(long x) {
      return rotr(x, 19) ^ rotr(x, 61) ^ shr(x, 6);
    }// sigma1 method

    /* The Sigma_0^{512} function defined on page 360 of reading #13a
     */
    static long bigSigma0(long a) {
      return rotr(a, 28) ^ rotr(a, 34) ^ rotr(a, 39);
    }// bigSigma0 method

    /* The Sigma_1^{512} function defined on page 360 of reading #13a
     */
    static long bigSigma1(long e) {
      return rotr(e, 14) ^ rotr(e, 18) ^ rotr(e, 41);
    }// bigSigma1 method

    /* The Ch function defined on page 360 of reading #13a
     */
    static long ch(long e, long f, long g) {
      return (e & f) ^ (~e & ~g);
    }// ch method

    /* The Maj function defined on page 360 of reading #13a
     */
    static long maj(long a, long b, long c) {
      return (a & b) ^ (a & c) ^ (b & c);
    }// maj method

    /* Given a padded message, hash it using the algorithm described on
     * page 363 of reading #13a. This method returns an array of 8 longs
     * that together encode the 512-bit message digest produced by SHA-512.
     */
    static long[] hash(long[] message) {
      long[] hash = new long[8];
      for (int i = 0; i < 8; i ++) {
        hash[i] = getSquareRootBits(primes[i]);
      }
      for (int i = 0; i < message.length / 16; i++) {
        long[] m = new long[16];
        for (int j = 0; j < 16; j++) {
          m[j] = message[16 * i + j];
        }
        long[] w = prepareRoundSchedule(m);
        long[] vars = new long[8];
        for (int j = 0; j < 8; j++) {
          vars[j] = hash[j];
        }
        for (int j = 0; j < 80; j++) {
          long t1 = vars[7] + ch(vars[4], vars[5], vars[6])
               + bigSigma1(vars[4]) + w[j] + k[j];
          long t2 = bigSigma0(vars[0]) + maj(vars[0], vars[1], vars[2]);
          vars[7] = vars[6];
          vars[6] = vars[5];
          vars[5] = vars[4];
          vars[4] = vars[3] + t1;
          vars[3] = vars[2];
          vars[2] = vars[1];
          vars[1] = vars[0];
          vars[0] = t1 + t2;
        }
        for (int j = 0; j < 8; j++) {
          hash[j] += vars[j];
        }
      }
      return hash;
    }// hash method

}// SHA512 class
