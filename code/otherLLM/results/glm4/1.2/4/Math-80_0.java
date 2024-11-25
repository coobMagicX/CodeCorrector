private boolean flipIfWarranted(final int n, final int step) {
    if (1.5 * work[pingPong] < work[4 * (n - 1) + pingPong]) {
        // flip array
        int j = 4 * n - 1;
        for (int i = 0; i < j; i += 4) {
            if (i + k >= work.length || j - k < 0) {
                // If the indices are out of bounds, the flipping is not possible
                return false;
            }
            for (int k = 0; k < 4; k += step) { // Correctly use 'step' here
                final double tmp = work[i + k];
                work[i + k] = work[j - k];
                work[j - k] = tmp;
            }
            j -= 4;
        }
        return true;
    }
    return false;
}