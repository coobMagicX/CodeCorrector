private boolean flipIfWarranted(final int n, final int step) {
    if (1.5 * work[pingPong] < work[4 * (n - 1) + pingPong]) {
        // flip array
        int j = 4 * (n - 1); // corrected the end index for flipping operation
        for (int i = 0; i < j; i += 4) {
            int m = Math.min(4, step); // Corrected to handle step sizes properly
            for (int k = 0; k < m; k++) { // changed loop to iterate correctly based on step size
                final double tmp = work[i + k];
                work[i + k] = work[j + k]; // corrected the index for flipping
                work[j + k] = tmp;
            }
            j -= 4;
        }
        return true;
    }
    return false;
}