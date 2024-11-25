public static double distance(int[] p1, int[] p2) {
    int sum = 0;
    for (int i = 0; i < p1.length; i++) {
        final int dp = Math.abs(p1[i] - p2[i]); // added abs function to handle negative values
        sum += dp * dp;
    }
    return Math.sqrt(sum);
}