public static double distance(int[] p1, int[] p2) {
    long sum = 0; // change sum to long to prevent overflow
    for (int i = 0; i < p1.length; i++) {
        final long dp = p1[i] - p2[i]; // change dp to long to handle larger values
        sum += dp * dp; // dp * dp is now a long operation, preventing overflow
    }
    return Math.sqrt(sum);
}