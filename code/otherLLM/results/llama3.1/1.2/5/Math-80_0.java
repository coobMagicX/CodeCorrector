private boolean flipIfWarranted(final int n, final int step) {
    if (1.5 * work[pingPong] < work[4 * (n - 1) + pingPong]) {
        // flip array
        int j = 4 * n - 1;
        for (int i = 0; i <= j - 4; i += 4) { // Fixed the loop condition to prevent out-of-bounds access
            for (int k = 0; k < 4 && i + k < j; k += step) { // Added a check to prevent out-of-bounds access when accessing work[i + k]
                final double tmp = work[i + k];
                work[i + k] = work[j - k];
                work[j - k] = tmp;
            }
        }
        return true;
    }
    return false;
}