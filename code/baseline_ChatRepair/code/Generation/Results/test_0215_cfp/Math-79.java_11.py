public static double distance(int[] p1, int[] p2) {
    // Check if either array is null
    if (p1 == null || p2 == null) {
        throw new IllegalArgumentException("Neither of the input arrays should be null");
    }
    
    // Ensure both arrays are of the same length
    if (p1.length != p2.length) {
        throw new IllegalArgumentException("Input arrays must have the same length");
    }

    int sum = 0;
    for (int i = 0; i < p1.length; i++) {
        final int dp = p1[i] - p2[i];
        sum += dp * dp;
    }
    return Math.sqrt(sum);
}
