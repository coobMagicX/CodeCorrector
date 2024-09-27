public static double distance(int[] p1, int[] p2) {
    int sum = 0;
    for (int i = 0; i < p1.length; i++) {
        final int dp = p1[i] - p2[i];
        sum += dp * dp;
    }
    return Math.sqrt(sum);
}

public static BigInteger pow(final BigInteger k, BigInteger e) throws IllegalArgumentException {
    if (e.compareTo(BigInteger.ZERO) < 0) {
        throw MathRuntimeException.createIllegalArgumentException(
            "cannot raise an integral value to a negative power ({0}^{1})", k, e);
    }

    BigInteger result = BigInteger.ONE;
    BigInteger k2p = k;
    while (!BigInteger.ZERO.equals(e)) {
        if (e.testBit(0)) {
            result = result.multiply(k2p);
        }
        k2p = k2p.multiply(k2p);
        e = e.shiftRight(1);
    }

    return result;
}