public static double distance(int[] p1, int[] p2) {
  if (p1.length == 0 || p2.length == 0) { // Added check for empty points
      throw new IllegalArgumentException("Cannot calculate distance between empty point arrays");
  }
  
  int sum = 0;
  for (int i = 0; i < Math.min(p1.length, p2.length); i++) {
      final int dp = p1[i] - p2[i];
      sum += dp * dp;
  }
  return Math.sqrt(sum);
}