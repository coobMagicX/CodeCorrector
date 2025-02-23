private boolean flipIfWarranted(final int n, final int step) {
    if (1.5 * work[pingPong] < work[4 * (n - 1) + pingPong]) {
        // flip array
        int j = 4 * n - 4;  // correctly point at the start of the last chunk
        for (int i = 0; i < j; i += 4) {
            for (int k = 0; k < 4; k++) {
                if (i + k < j - k) { // ensure elements are within bounds and not overlapping
                    double tmp = work[i + k];
                    work[i + k] = work[j - k];
                    work[j - k] = tmp;
                }
            }
            j -= 4;
        }
        return true;
    }
    return false;
}
