public static double distance(int[] p1, int[] p2) {
  if (p1.length != p2.length) {
    throw new IllegalArgumentException("Points have different dimensions");
  }

  int sum = 0;
  for (int i = 0; i < p1.length; i++) {
      final int dp = Math.abs(p1[i] - p2[i]);
      sum += dp * dp;
  }
  
  // check if points are identical
  return (sum == 0) ? 0 : Math.sqrt(sum);
}