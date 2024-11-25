private boolean flipIfWarranted(final int n, final int step) {
    if (1.5 * work[pingPong] < work[4 * (n - 1) + pingPong]) {
        // flip array
        int j = 4 * n - 1;
        for (int i = 0; i < j; i += 4) {
            for (int k = 0; k < 4; k += step) {
                final double tmp = work[i + k];
                work[i + k] = work[j - k];
                work[j - k] = tmp;
            }
            j -= 4;
        }
        return true;
    } else if (work[pingPong] > work[4 * (n - 1) + pingPong]) {
        // If the condition is not met but it's still necessary to flip, do so here
        int j = 4 * n - 1;
        for (int i = 0; i < j; i += 4) {
            for (int k = 0; k < 4; k += step) {
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