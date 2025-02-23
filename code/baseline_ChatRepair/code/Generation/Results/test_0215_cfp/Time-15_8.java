public static long safeMultiply(long val1, int val2) {
    if (val1 == 0 || val2 == 0) {
        return 0L;
    }
    if (val1 == Long.MIN_VALUE && val2 == -1) {
        
        throw new ArithmeticException("Multiplication overflows a long: " + val1 + " * " + val2);
    }

    long total = val1 * val2;

    
    if (val2 > 0 && (total < val1 || total