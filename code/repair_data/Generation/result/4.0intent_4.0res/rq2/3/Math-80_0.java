private boolean flipIfWarranted(final int n, final int step) {
    if (1.5 * work[pingPong] < work[4 * (n - 1) + pingPong]) {
        // flip array
        int j = 4 * (n - 1); // Adjusted the index to start from the last group of 4 elements
        for (int i = 0; i < j; i += 4) {
            for (int k = 0; k < 4; k += step) {
                final double tmp = work[i + k];
                work[i + k] = work[j + 3 - k]; // Corrected to j + 3 - k to correctly address the mirrored element
                work[j + 3 - k] = tmp;
            }
            j -= 4;
        }
        return true;
    }
    return false;
}