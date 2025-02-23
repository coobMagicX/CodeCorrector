private boolean flipIfWarranted(final int n, final int step) {
    if (1.5 * work[pingPong] < work[4 * (n - 1) + pingPong]) {
        // flip array
        int j = 4 * (n - 1);  // adjusted to correctly point at the last block of 4 at the array's end
        for (int i = 0; i < j; i += 4) {
            for (int k = 0; k < 4; k++) {  // ensure all four elements of each block are swapped
                final double tmp = work[i + k];
                work[i + k] = work[j + k];
                work[j + k] = tmp;
            }
            j -= 4;  // decrement j to move to the next block
        }
        return true;
    }
    return false;
}
