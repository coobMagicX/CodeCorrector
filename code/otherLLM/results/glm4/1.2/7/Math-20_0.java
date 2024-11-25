public double[] repairAndDecode(final double[] x) {
    if (boundaries == null) {
        // If there are no boundaries, return the input array as is.
        return x;
    }

    double[] decoded = new double[x.length];
    for (int i = 0; i < x.length; i++) {
        // Ensure that the boundaries are within the expected bounds
        if (boundaries[1][i] > boundaries[0][i]) {
            double diff = boundaries[1][i] - boundaries[0][i];
            decoded[i] = Math.max(0, Math.min(x[i], 1)) * diff + boundaries[0][i]; // Ensure that the result is within [0, 1]
        } else {
            throw new IllegalArgumentException("Upper boundary must be greater than or equal to lower boundary");
        }
    }
    return decoded;
}