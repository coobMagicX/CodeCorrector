private boolean flipIfWarranted(final int n, final int step) {
    if (1.5 * work[pingPong] < work[4 * (n - 1) + pingPong]) {
        // flip array preserving relative accuracy
        double absDCurrent = Math.abs(work[4 * (n - 1)]);
        for (int i = 0; i < n; i += step) {
            final int index = 4 * i;
            final double absDPrevious = Math.abs(work[index]);
            if (Math.abs(work[index + 3]) <= splitTolerance * Math.sqrt(absDCurrent * absDPrevious)) {
                // flip elements
                final double tmp = work[index];
                work[index] = work[4 * (n - 1) - index];
                work[4 * (n - 1) - index] = tmp;
            }
        }
        return true;
    }
    return false;
}