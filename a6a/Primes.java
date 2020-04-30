/*****************************************************
   CS 326 - Spring 2020 - Assignment #6a
   Student's full name(s): Martin Mueller
*****************************************************/

import java.util.*;
import java.io.*;

class Primes {

    /* in this assignment, we will only work with the first 1000 primes */
    static int NUM_PRIMES = 1000;

    /* the square of the value of the largest prime number used in this
       assignment */
    static int SKY = -1;

    /* the values of the first NUM_PRIMES prime numbers */
    static int[] primes =  new int[NUM_PRIMES];

    /* load the first NUM_PRIMES prime numbers from the file "primes.txt"
     * into the static array 'primes'. This method must also
     * initialize the 'SKY' static variable.
     */
    public static void loadPrimes() {
      try {
        Scanner s = new Scanner(new File("primes.txt"));
        for (int i = 0; i < NUM_PRIMES; i++) {
          primes[i] = s.nextInt();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      SKY = primes[NUM_PRIMES - 1] * primes[NUM_PRIMES - 1];
    }// loadPrimes method

    /* return true if and only if its input is one of the first NUM_PRIMES
     * prime numbers.
     */
    public static boolean isPrime(int n) {
      for (int i : primes) {
        if (n == i) {
          return true;
        }
      }
      return false;
    }// isPrime method

    /* given an integer, return a 2-element array containing its two
     * prime factors if the input is the product of two primes (with
     * the smallest prime in the first array location); otherwise
     * return an empty (0-element) array.
     */
    public static int[] factor(int n) {
      for (int i = 0; i < NUM_PRIMES; i++) {
        for (int j = i; j < NUM_PRIMES; j++) {
          if (primes[i] * primes[j] == n) {
            return new int[]{primes[i], primes[j]};
          }
        }
      }
      return new int[]{};
    }// factor method

    /* return the greatest common divisor of the two input integers,
     * which are assumed to be positive. The implementation of this
     * method must be iterative. No recursion allowed!  You are also
     * not allowed to use any 'gcd' method in any of the Java API
     * classes. You may not use any helper methods. You must implement
     * this method from first principles.
    */
    public static int gcd(int m, int n){
      int i = 0;
      while (n != 0) {
        i = n;
        n = m % n;
        m = i;
      }
      return m;
    }// gcd method

    /* return true if and only if alpha is a primitive root modulo q
     */
    public static boolean isPrimitiveRoot(int alpha, int q) {
      HashMap<Integer, Boolean> hashMap = new HashMap<>();
      for (int i = 1; i < q; i++) {
        int key = RSA.modularExponent(alpha, i, q);
        if (hashMap.containsKey(key)) {
          return false;
        } else {
          hashMap.put(key, null);
        }
      }
      return true;
    }// isPrimitiveRoot method

    /* return the largest primitive root of q (or -1, if q does not have
     * a primitive root).
     */
    public static int pickAlpha(int q) {
      for (int alpha = q - 1; alpha > 1; alpha--) {
        if (isPrimitiveRoot(alpha, q)) {
          return alpha;
        }
      }
      return -1;
    }// pickAlpha method


    /* This method is used for testing.
     * Do NOT modify it
     */
    public static void main(String[] args) {
        if (args.length == 1) {
            loadPrimes();
            if (args[0].equals("load")) {
                System.out.println("Testing loadPrimes()");
                loadPrimes();
                System.out.println(primes[0]);
                System.out.println(primes[NUM_PRIMES-1]);
                System.out.println(SKY);
            } else if (args[0].equals("isPrime")) {
                for(int i = 0; i < 20; i++) {
                    System.out.println(isPrime(i));
                }
            } else if (args[0].equals("factor")) {
                for(int i = 0; i < 1000; i++) {
                    int[] result = factor(i);
                    if (result.length == 2) {
                        System.out.format("%d is the product of %d and %d\n",
            i, result[0], result[0]);
                    }
                }
            } else if (args[0].equals("gcd")) {
                for(int i = 0; i < 300; i += 3) {
                    for(int j = 5; j < 200; j += 13) {
                        System.out.println("gcd(" + i + "," + j + ") = "  +
                                           gcd(i,j));
                    }
                }
            } else if (args[0].equals("primRoot")) {
                for(int i = 0; i < 20; i++) {
                    int n = primes[i];
                    System.out.print(n + ": ");
                    for(int j = 1; j < n; j++) {
                        if (isPrimitiveRoot(j,n)) {
                            System.out.print( j + " ");
                        }
                    }
                    System.out.println();
                }
            } else if (args[0].equals("pickAlpha")) {
                for(int i = 0; i < 20; i++) {
                    int q = primes[i];
                    System.out.println(q + ": " + pickAlpha(q));
                }
            }
        }
    }
}// Primes class
