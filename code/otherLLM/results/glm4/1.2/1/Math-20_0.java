public double[] repairAndDecode(final double[] x) {
    double[] decoded = decode(x);

    // Assuming an upper bound is defined somewhere in your code, e.g., as UPPPER_BOUND
    final double UPPER_BOUND = 100.0;

    for (int i = 0; i < decoded.length; i++) {
        if (decoded[i] > UPPER_BOUND) {
            decoded[i] = UPPER_BOUND;
        }
    }

    return decoded;
}