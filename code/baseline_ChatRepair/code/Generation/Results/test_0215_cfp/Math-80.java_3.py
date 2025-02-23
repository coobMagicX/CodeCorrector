private boolean flipIfWarranted(final int n, final int step) {
    if (1.5 * work[pingPong] < work[4 * (n - 1) + pingPong]) {
        // flip array
        int j = 4 * n - 4;  // Initialize j directly before the end index of 4-element group.
        for (int i = 0; i < j; i += 4) {
            for (int k = 0; k < 4; k++) {
                final double tmp = work[i + k];
                work[i + k] = work[j + k];  // Correct the + k for accessing the paired element to swap.
                work[j + k] = tmp;
            }
            j -= 4;  // Decrement j by 4 after full 4-element swap.
        }
        return true;
    }
    return false;
}
