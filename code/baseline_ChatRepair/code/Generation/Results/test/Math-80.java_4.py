private boolean flipIfWarranted(final int n, final int step) {
    if (1.5 * work[pingPong] < work[4 * (n - 1) + pingPong]) {
        // flip array
        int j = 4 * (n - 1); // Set j to the 4-begin-index of the last group
        for (int i = 0; i <= j; i += 4) {
            int k = 0;
            // Flip each group of 4 elements, ensuring i+k and j-k pairs correctly.
            while (k < 4) {
                double tmp = work[i + k];
                work[i + k] = work[j + k];
                work[j + k] = tmp;
                k++;
            }
            j -= 4;
        }
        return true;
    }
    return false;
}
