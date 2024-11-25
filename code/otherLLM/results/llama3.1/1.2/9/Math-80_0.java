private boolean flipIfWarranted(final int n, final int step) {
    if (1.5 * work[pingPong] < work[4 * (n - 1) + pingPong]) {
        // flip array
        int j = 4 * n - 1;
        for (int i = 0; i <= j; i += 4) { // Change: <= instead of <
            for (int k = 0; k < 4 && i + k <= j; k += step) {
                final double tmp = work[i + k];
                work[i + k] = work[j - k];
                work[j - k] = tmp;
            }
            if (i + 3 == j) { // Change: Add condition to prevent index out of bounds
                break;
            }
            j -= 4;
        }
        return true;
    }
    return false;
}