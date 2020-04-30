/*****************************************************
   CS 326 - Spring 2020 - Assignment #6a
   Student's full name(s): Martin Mueller
*****************************************************/

class Driver {

    /* complete the following method to simulate the protocol given in
     * the handout and match the output listed in the handout. You
     * must modify ALL and ONLY the lines marked with a "modify"
     * comment.
     */
    public static void main(String[] args) throws Exception {

        if (args.length != 7) {
            System.out.println("Usage: java Driver BBS_p BBS_q RSA_Alice_p " +
                               "RSA_Alice_q RSA_Bob_p RSA_Bob_q DH_q");
            System.exit(1);
        }

        Primes.loadPrimes();

        /* inputs for BBS computations */
        /* (in this simulation, the BBS instance is shared by Alice and Bob) */
        int BBS_p = Integer.parseInt(args[0]);
        int BBS_q = Integer.parseInt(args[1]);
        BBS bbs = new BBS(BBS_p,BBS_q);

        /* inputs for public-private key pair computations */
        int RSAa_p = Integer.parseInt(args[2]);
        int RSAa_q = Integer.parseInt(args[3]);
        int RSAb_p = Integer.parseInt(args[4]);
        int RSAb_q = Integer.parseInt(args[5]);

        /* input for Diffie-Hellman computations */
        int DH_q = Integer.parseInt(args[6]);
        System.out.println("Setting up DH with q = " + DH_q);
        int alpha = Primes.pickAlpha(DH_q);
        System.out.println("alpha = " + alpha);

        System.out.println("***** Alice's side *****");

        RSA RSAa = new RSA(RSAa_p, RSAa_q);
        int Ra = bbs.getRandomInt();
        System.out.println("Ra = " + Ra);
        int Xa = DH_q / 3;
        System.out.println("Xa = " + Xa);
        int Ya = RSA.modularExponent(alpha, Xa, DH_q);
        System.out.println("Ya = " + Ya);

        System.out.println("****** Bob's side ******");

        RSA RSAb = new RSA(RSAb_p, RSAb_q);
        int Rb = bbs.getRandomInt();
        System.out.println("Rb = " + Rb);
        int Xb = DH_q / 2;
        System.out.println("Xb = " + Xb);
        int Yb = RSA.modularExponent(alpha, Xb, DH_q);
        System.out.println("Yb = " + Yb);

        /* encrypt and sign Ra */
        int EPUa_Ra = RSAa.encrypt(Ra);
        int EPRb_Ra = RSAb.decrypt(EPUa_Ra);
        System.out.println("E(PUa,Ra) = " + EPUa_Ra);
        System.out.println("E(PRb,E(PUa,Ra)) = " + EPRb_Ra);
        /* encrypt and sign Yb */
        int EPUa_Yb = RSAa.encrypt(Yb);
        int EPRb_Yb = RSAb.decrypt(EPUa_Yb);
        System.out.println("E(PUa,Yb) = " + EPUa_Yb);
        System.out.println("E(PRb,E(PUa,Yb)) = " + EPRb_Yb);

        System.out.println("***** Alice's side *****");

        int RaDec = RSAa.decrypt(RSAb.encrypt(EPRb_Ra));
        System.out.println("Decrypted Ra = " + RaDec);
        int YbDec = RSAa.decrypt(RSAb.encrypt(EPRb_Yb));
        System.out.println("Decrypted Yb = " + YbDec);
        int K = RSA.modularExponent(YbDec, Xa, DH_q);
        System.out.println("K = " + K);

        /* encrypt and sign RB */
        int EPUb_Rb = RSAb.encrypt(Rb);
        int EPRa_Rb = RSAa.decrypt(EPUb_Rb);
        System.out.println("E(PUb,Rb) = " + EPUb_Rb);
        System.out.println("E(PRa,E(PUb,Rb)) = " + EPRa_Rb);
        /* encrypt and sign Ya */
        int EPUb_Ya = RSAb.encrypt(Ya);
        int EPRa_Ya = RSAa.decrypt(EPUb_Ya);
        System.out.println("E(PUb,Ya) = " + EPUb_Ya);
        System.out.println("E(PRa,E(PUb,Ya)) = " + EPRa_Ya);

        System.out.println("****** Bob's side ******");

        int RbDec = RSAb.decrypt(RSAa.encrypt(EPRa_Rb));
        System.out.println("Decrypted Rb = " + RbDec);
        int YaDec = RSAb.decrypt(RSAa.encrypt(EPRa_Ya));
        System.out.println("Decrypted Ya = " + YaDec);
        K = RSA.modularExponent(YaDec, Xb, DH_q);
        System.out.println("K = " + K);

    }// main method
}// Driver class
