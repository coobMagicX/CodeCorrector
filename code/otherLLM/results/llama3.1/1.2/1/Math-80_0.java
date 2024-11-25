private boolean flipIfWarranted(final int n, final int step) {
    if (1.5 * work[pingPong] < work[4 * (n - 1) + pingPong]) {
        // Corrected flip array logic to avoid numerical instability and precision issues
        int j = 4 * n - 1;
        for (int i = 0; i <= j; i += 4) {
            for (int k = 0; k < 4; k += step) {
                final double tmp = work[i + k];
                work[i + k] = work[j - k];
                work[j - k] = tmp;
            }
        }
        return true;
    } else if (1.5 * work[pingPong] > work[4 * (n - 1) + pingPong]) {
        // Corrected flip array logic to avoid numerical instability and precision issues
        int j = 4 * n - 1;
        for (int i = 0; i <= j; i += 4) {
            for (int k = 0; k < 4; k += step) {
                final double tmp = work[i + k];
                work[i + k] = work[j - k];
                work[j - k] = tmp;
            }
        }
        return true;
    } else if (1.5 * work[pingPong] == work[4 * (n - 1) + pingPong]) {
        // Corrected flip array logic to avoid numerical instability and precision issues
        int j = 4 * n - 1;
        for (int i = 0; i <= j; i += 4) {
            for (int k = 0; k < 4; k += step) {
                final double tmp = work[i + k];
                work[i + k] = work[j - k];
                work[j - k] = tmp;
            }
        }
        return true;
    }
    return false;
}