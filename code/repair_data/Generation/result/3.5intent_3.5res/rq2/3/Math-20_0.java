public double[] repairAndDecode(final double[] x) {
    // Check if any value in the input array exceeds the upper bound
    for (int i = 0; i < x.length; i++) {
        if (x[i] > UPPER_BOUND) {
            x[i] = UPPER_BOUND;
        }
    }
    
    return decode(x);
}