public double[] repairAndDecode(final double[] x) {
    double[] decoded = decode(x);
    
    // Check if any decoded value exceeds the upper bound and adjust if necessary.
    for (int i = 0; i < decoded.length; i++) {
        if (decoded[i] > upper[0]) {
            decoded[i] = upper[0];
        }
    }

    return decoded;
}