/*****************************************************
   CS 326 - Spring 2020 - Assignment #6a
   Student's full name(s): Martin Mueller
*****************************************************/

class RSA {

    /* the numerical components of the RSA algorithm */
    int p, q, n, phiOfN, e, d;

    /* this constructor takes the value of p and q and initializes all of the
     * instance variables of the RSA object with the following constraints on
     * e and d:
     * 1. e and d are both prime numbers and relatively prime to phi(n),
     * 2. 1 < e < phi(n) AND e <= d AND 0 < e * d <= Primes.SKY,
     * 3. e * d = 1 + k * phi(n), for some positive integer k, and
     * 4. the values of e and d are such that k is the smallest value that
     *    satisfies the previous condition.
     * This constructor must print the instance variables with the
     * following format (only one line of output allowed):
     *
     * p=3 q=5 n=15 phi(n)=8 e=3 d=3
     *
     * If it is not possible to satisfy all of the conditions above, then this
     * constructor must throw an exception with the following message:
     * "Cannot handle this case."
     */
    RSA(int p, int q) throws Exception {
      this.p = p;
      this.q = q;
      n = p * q;
      phiOfN = (p - 1) * (q - 1);
      int product = Primes.SKY + 1;
      for (int i = 0; i < Primes.NUM_PRIMES &&
           Primes.primes[i] < phiOfN; i++) {
        for (int j = i; j < Primes.NUM_PRIMES; j++) {
          int m = Primes.primes[i];
          int n = Primes.primes[j];
          int temp = m * n;
          if (Primes.gcd(phiOfN, m) == 1 &&
              Primes.gcd(phiOfN, n) == 1 &&
              (m * n) % phiOfN == 1 &&
              temp < product) {
            e = m;
            d = n;
            product = temp;
          }
        }
      }
      if (product == Primes.SKY + 1) {
        throw new Exception("Cannot handle this case.");
      } else {
        System.out.printf("p=%d q=%d n=%d phi(n)=%d e=%d d=%d\n",
                          p, q, n, phiOfN, e, d);
      }
    }// constructor

    /* compute and return:  a^e mod m
     * The implementation of this method MUST follow the pseudocode on
     * Slide 9-10 as closely as possible with the addition of "modulo
     * operations" wherever needed.
     * Hint: Watch out for integer overflow situations. You must handle those
     * without any try/catch blocks and without any additional tests.
     * This method may not send anything to standard output.
     */
    public static int modularExponent(int a, int e, int m) {
      if (e == 0) {
        return 1;
      }
      int b = 1;
      while (e > 1) {
        if (e % 2 == 0) {
          a = (a * a) % m;
          e /= 2;
        } else {
          b = (b * a) % m;
          a = (a * a) % m;
          e = (e - 1) / 2;
        }
      }
      return (a * b) % m;
    }// modularExponent method

    /* return the ciphertext produced by this RSA instance for the input
     * plaintext m
     * This method may not send anything to standard output.
     */
    public int encrypt(int m) {
      return modularExponent(m, e, n);
    }// encrypt method

    /* return the plaintext produced by this RSA instance for the input
     * ciphertext c
     * This method may not send anything to standard output.
     */
    public int decrypt(int c) {
      return modularExponent(c, d, n);
    }// decrypt method

    /* This method is used for testing.
     * Do NOT modify it.
     */
    public static void main(String[] args) throws Exception {
        Primes.loadPrimes();
        if (args.length == 1) {
            Primes.loadPrimes();
            if (args[0].equals("constructor")) {
                new RSA(5,7);
                new RSA(17,11);
                new RSA(11,3);
                new RSA(7,13);
            } else if (args[0].equals("modExp")) {
                System.out.format("%d^%d mod %d = %d\n",
                                  23,20,29,modularExponent(23,20,29));
                System.out.format("%d^%d mod %d = %d\n",
                                  23,391,55,modularExponent(23,291,55));
                System.out.format("%d^%d mod %d = %d\n",
                                  31,397,55,modularExponent(31,397,55));
            } else if (args[0].equals("encrypt")) {
                System.out.format("RSA(%d,%d): encrypt(%d) = %d\n",
                                  5,7,3,(new RSA(5,7)).encrypt(3));
                System.out.format("RSA(%d,%d): encrypt(%d) = %d\n",
                                  7,13,10,(new RSA(7,13)).encrypt(10));
                System.out.format("RSA(%d,%d): encrypt(%d) = %d\n",
                                  11,17,88,(new RSA(11,17)).encrypt(88));
            } else if (args[0].equals("decrypt")) {
                System.out.format("RSA(%d,%d): decrypt(%d) = %d\n",
                                  5,7,33,(new RSA(5,7)).decrypt(33));

                System.out.format("RSA(%d,%d): decrypt(%d) = %d\n",
                                  7,13,82,(new RSA(7,13)).decrypt(82));
                System.out.format("RSA(%d,%d): decrypt(%d) = %d\n",
                                  11,17,11,(new RSA(11,17)).decrypt(11));
            }
        }
    }// main method
}// RSA class
