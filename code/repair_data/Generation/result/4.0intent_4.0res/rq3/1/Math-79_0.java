public static double distance(int[] p1, int[] p2) {
    int sum = 0;
    for (int i = 0; i < p1.length; i++) {
        int dp = p1[i] - p2[i];  // Removed the 'final' keyword which is not necessary here
        sum += dp * dp;
    }
    return Math.sqrt(sum);
}