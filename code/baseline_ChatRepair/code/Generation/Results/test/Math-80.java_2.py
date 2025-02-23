private boolean flipIfWarranted(final int n, final int step) {
    if (1.5 * work[pingPong] < work[4 * (n - 1) + pingPong]) {
        // flip array
        int left = 0;
        int right = 4 * (n - 1);
        while (left < right) {
            for (int i = 0; i < 4; i++) {
                double tmp = work[left + i];
                work[left + i] = work[right + i];
                work[right + i] = tmp;
            }
            left += 4;
            right -= 4;
        }
        return true;
    }
    return false;
}
