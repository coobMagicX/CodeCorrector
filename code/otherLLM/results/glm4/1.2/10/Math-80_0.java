private boolean flipIfWarranted(final int n, final int step) {
    if (work[pingPong] < work[4 * (n - 1) + pingPong]) { // Corrected comparison
        // flip array
        int j = 4 * n; // Change to n instead of (n - 1)
        for (int i = 0; i < j; i += 4) { // Start from 0 instead of 1
            for (int k = 0; k < 4; k += step) {
                final double tmp = work[i + k];
                work[i + k] = work[j - k]; // Corrected the index for swapping
                work[j - k] = tmp;
            }
            j -= 4;
        }
        return true;
    }
    return false;
}