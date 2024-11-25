public static double distance(int[] p1, int[] p2) {
    if (Arrays.equals(p1, p2)) {  // Check if points are identical
        return Double.POSITIVE_INFINITY;  // Return infinity if points are identical
    }

    int sum = 0;
    for (int i = 0; i < p1.length; i++) {
        final int dp = p1[i] - p2[i];
        sum += dp * dp;
    }
    return Math.sqrt(sum);
}