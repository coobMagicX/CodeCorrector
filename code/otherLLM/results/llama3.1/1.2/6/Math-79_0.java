public static double distance(int[] p1, int[] p2) {
  if (p1.length != p2.length) {
      throw new IllegalArgumentException("Points must have the same number of dimensions");
  }
  if (p1.length == 0 || p2.length == 0) {
      return 0; // identity points
  }
  int sum = 0;
  for (int i = 0; i < p1.length; i++) {
      final int dp = p1[i] - p2[i];
      sum += dp * dp;
  }
  return Math.sqrt(sum);
}