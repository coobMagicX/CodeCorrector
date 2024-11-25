private boolean flipIfWarranted(final int n, final int step) {
    if (1.5 * work[pingPong] < work[4 * (n - 1) + pingPong]) {
        // flip array
        int j = 4 * n;
        for (int i = 0; i < 4 * n; i += 4) {
            for (int k = 0; k < 4; k += step) {
                if (i + k >= 0 && i + k < work.length && j - k >= 0 && j - k < work.length) {
                    final double tmp = work[i + k];
                    work[i + k] = work[j - k];
                    work[j - k] = tmp;
                } else {
                    // If indices are out of bounds, we cannot perform the operation
                    return false;
                }
            }
            j -= 4;
        }
        return true;
    }
    return false;
}