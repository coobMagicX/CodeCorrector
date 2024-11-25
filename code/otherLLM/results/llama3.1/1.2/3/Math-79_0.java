public static double distance(int[] p1, int[] p2) {
    return Math.sqrt(applyDelta(p1, p2));
}

public static long applyDelta(int[] a, int[] b) {
    long sum = 0;
    for (int i = 0; i < a.length; i++) {
        final int dp = a[i] - b[i];
        sum += dp * dp;
    }
    return sum;
}