private boolean flipIfWarranted(final int n, final int step) {
    if (1.5 * work[pingPong] < work[4 * (n - 1) + pingPong]) {
        // flip array
        int end = 4 * (n - 1);
        for (int start = 0; start < end; start += 4, end -= 4) {
            for (int k = 0; k < 4; k++) { // Ensure every element is swapped in a group of 4
                double tmp = work[start + k];
                work[start + k] = work[end + k];
                work[end + k] = tmp;
            }
        }
        return true;
    }
    return false;
}
