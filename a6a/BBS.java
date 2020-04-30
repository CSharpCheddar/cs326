/*****************************************************
   CS 326 - Spring 2020 - Assignment #6a
   Student's full name(s): Martin Mueller
*****************************************************/

class BBS {

    /* the numerical components of the BBS algorithm */
    int p, q, n, s, X;

    /* Check that the p and q values are appropriate for the BBS algorithm.
     * If either one is not appropriate, throw an exception with a meaningful
     * message. Otherwise, compute and assign the values of n, s, and X_0 (the
     * latter is stored in X).
     * Must produce the EXACT output shown in the handout, such as:
     *   Setting up BBS with p = 7 and q = 199
     *   n = 1393
     *   s = 696
     */
    BBS(int p, int q) throws Exception {

        System.out.println("Setting up BBS with p = " + p + " and q = " + q);

        if (!Primes.isPrime(p) || !Primes.isPrime(q) ||
            (p % 4 != 3) || (q % 4 != 3)) {
          throw new Exception("p and q must each be prime" +
                              " and equal to 3 (mod 4)");
        } else {
          this.p = p;
          this.q = q;
          n = p * q;
          s = chooseS();
          X = (s * s) % n;
          System.out.printf("n = %d\ns = %d\n", n, s);
        }

    }// constructor

    /* return the largest integer less than or equal to n/2 that is
     * relatively prime to n.
     */
    private int chooseS() {
      int guess = n / 2;
      while (Primes.gcd(n, guess) != 1) {
        guess--;
      }
      return guess;
    }// chooseS method

    /* use the BBS algorithm to update X and return the next random
     * bit (with true representing a 1 bit and false representing a 0
     * bit.
     */
    boolean getNextRandomBit() {
      X = (X * X) % n;
      return X % 2 == 1;
    }// getNextRandomBit method

    /* use the BBS algorithm to return the next random integer value;
     * to keep values small, the 20 most significant bits must be equal
     * to 0; the remaining bits are produced by BBS from most to least
     * significant (i.e., from left to right)
     */
    int getRandomInt() {
      int rand = getNextRandomBit() ? 1 : 0;
      for (int i = 0; i < 11; i++) {
        rand = rand << 1;
        rand = rand | (getNextRandomBit() ? 1 : 0);
      }
      return rand;
    }// getRandomInt method

    /* This method is used for testing.
     * Do NOT modify it.
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: java BBS <p> <q>");
            System.exit(1);
        }
        Primes.loadPrimes();
        BBS bbs = new BBS(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
        for(int i = 0; i < 10; i++) {
            System.out.print( bbs.getNextRandomBit() ? "1" : "0");
        }
        System.out.println();
        System.out.println( bbs.getRandomInt());
        System.out.println( bbs.getRandomInt());
    }// main method

}// BBS class
