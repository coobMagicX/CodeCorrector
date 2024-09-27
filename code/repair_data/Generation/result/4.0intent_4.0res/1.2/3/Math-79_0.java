public static double distance(int[] p1, int[] p2) {
    long sum = 0L; // Use long to prevent integer overflow
    for (int i = 0; i < p1.length; i++) {
        final long dp = p1[i] - p2[i]; // Cast to long
        sum += dp * dp; // Use long in arithmetic to handle larger values
    }
    return Math.sqrt(sum);
}