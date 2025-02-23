public static double distance(int[] p1, int[] p2) {
    // Check if the arrays are null
    if (p1 == null || p2 == null) {
        throw new IllegalArgumentException("One of the input points is null");
    }

    // Check if the arrays are empty or have different lengths
    if (p1.length == 0 || p2.length == 0 || p1.length != p2.length) {
        throw new IllegalArgumentException("Input points must be non-empty and have the same number of dimensions");
    }

    int sum = 0;
    for (int i = 0; i < p1.length; i++) {
        final int dp = p1[i] - p2[i];
        sum += dp * dp;
    }
    return Math.sqrt(sum);
}
