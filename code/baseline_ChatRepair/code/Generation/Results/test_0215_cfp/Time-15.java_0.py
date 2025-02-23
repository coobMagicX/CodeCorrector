public static long safeMultiply(long val1, int val2) {
    switch (val2) {
        case -1:
            if (val1 == Long.MIN_VALUE) { // Special case where multiplying MIN_VALUE with -1 causes overflow
                throw new ArithmeticException("Multiplication overflows a long: " + val1 + " * " + val2);
            }
            return -val1;
        case 0:
            return 0L;
        case 1:
            return val1;
    }

    // Check for overflow by comparing the result of a division with the original value
    long total = val1 * val2;
    if (val1 != 0 && total / val1 != val2) {
        throw new ArithmeticException("Multiplication overflows a long: " + val1 + " * " + val2);
    }
    return total;
}
