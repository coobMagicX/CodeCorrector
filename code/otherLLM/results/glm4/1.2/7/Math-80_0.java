private boolean flipIfWarranted(final int n, final double[] work, final int pingPong) {
    // Assuming 'work' is an array and 'pingPong' represents an index within that array.
    if (1.5 * work[pingPong] < work[4 * (n - 1) + pingPong]) {
        // flip array
        int j = 4 * n - 1;
        for (int i = 0; i < j; i += 4) {
            // It's not clear what 'step' should be here, so it has been commented out or removed.
            // if (k + step < j - k) { // This line was commented out
                final double tmp = work[i]; // Assuming we flip the element at index i with the one at index j-k
                work[i] = work[j - 4];
                work[j - 4] = tmp;
            // }
            j -= 4;
        }
        return true;
    }
    return false;
}