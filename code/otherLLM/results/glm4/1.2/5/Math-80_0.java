private boolean flipIfWarranted(final int n, final int step) {
    // Ensure the work array is non-null and within bounds before proceeding.
    if (work == null || pingPong < 0 || pingPong >= work.length) {
        throw new IllegalArgumentException("Invalid index 'pingPong'.");
    }

    if (1.5 * work[pingPong] < work[4 * (n - 1) + pingPong]) {
        // flip array
        int j = 4 * n - 1;
        for (int i = 0; i <= j; i += 4) { // Corrected the condition to include the last element of each block
            for (int k = 0; k < 4 && (i + k) < (j + 1); k += step) { // Check if k is within the bounds after incrementing by step
                final double tmp = work[i + k];
                work[i + k] = work[j - k];
                work[j - k] = tmp;
            }
            j -= 4; // Decrease 'j' to maintain correct array boundaries
        }
        return true;
    }
    return false;
}