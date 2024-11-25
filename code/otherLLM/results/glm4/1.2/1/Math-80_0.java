private boolean flipIfWarranted(final int n, final int step) {
    // Assuming pingPong is meant to be an index within the work array and should be a positive integer.
    if (n < 0 || step <= 0) {
        throw new IllegalArgumentException("Invalid parameters: n must be non-negative and step must be positive.");
    }
    
    // Calculate the midpoint of the section to be flipped.
    final int midpoint = n - (n % 4 == 0 ? 1 : 2);
    if (1.5 * work[midpoint] < work[4 * midpoint]) {
        // flip array
        for (int i = 0; i < midpoint; i += 4) {
            final int j = 4 * n - 1 - (i % 4 == 0 ? 0 : 2); // Adjust j to flip correctly around the midpoint.
            for (int k = 0; k < 4; k += step) {
                final double tmp = work[i + k];
                work[i + k] = work[j - k];
                work[j - k] = tmp;
            }
        }
        return true;
    }
    return false;
}