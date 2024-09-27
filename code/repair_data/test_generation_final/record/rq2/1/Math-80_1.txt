private boolean flipIfWarranted(final int n, final int step) {
    if (1.5 * work[pingPong] < work[4 * (n - 1) + pingPong]) {
        // flip array
        int j = 4 * n - 1;
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < 4; k++) {
                final double tmp = work[i * 4 + k];
                work[i * 4 + k] = work[j - k];
                work[j - k] = tmp;
            }
            j -= 4;
        }
        return true;
    }
    return false;
}