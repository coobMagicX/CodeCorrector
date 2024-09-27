private boolean flipIfWarranted(final int n, final int step) {
    if (1.5 * work[pingPong] < work[4 * (n - 1) + pingPong]) {
        // flip array
        int j = 4 * n - 4; // corrected j value
        for (int i = 0; i < n; i++) { // corrected loop condition
            for (int k = 0; k < step; k++) { // corrected loop condition
                final double tmp = work[i + k];
                work[i + k] = work[j - k];
                work[j - k] = tmp;
            }
            j -= 4; // decrease the step by 4 for each iteration
        }
        return true;
    }
    return false;
}