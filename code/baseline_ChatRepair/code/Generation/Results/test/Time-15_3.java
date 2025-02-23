public static long safeMultiply(long val1, int val2) {
    switch (val2) {
        case -1:
            if (val1 == Long.MIN_VALUE) { 
                throw new ArithmeticException("Multiplication overflows a long: " + val1 + " * " + val2);
            }
            return -val1;
        case 0:
            return 0L;
        case 1:
            return val1;
        default:
            
            BigInteger bigVal1 = BigInteger.valueOf(val1);
            BigInteger bigVal2 = BigInteger.valueOf(val2);
            BigInteger bigProduct = bigVal1.multiply(bigVal2);
            
           