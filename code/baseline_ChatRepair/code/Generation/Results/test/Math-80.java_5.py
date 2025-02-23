private boolean flipIfWarranted(final int n, final int step) {
    if (1.5 * work[pingPong] < work[4 * (n - 1) + pingPong]) {
        // flip array
        int end = 4 * n; // compute the end boundary of the relevant array part
        for (int i = 0; i < end / 2; i += step) { // iterate over first half, incrementing by step
            int opposingIndex = end - i - step; // calculate the opposing element's index
            for (int k = 0; k < step; k++) { // swap elements in sub-steps within the step range
                double temp = work[i + k];
                work[i + k] = work[opposingIndex + k];
                work[opposingIndex + k] = temp;
            }
        }
        return true;
    }
    return false;
}
