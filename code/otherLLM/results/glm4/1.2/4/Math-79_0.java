public static double distance(int[] p1, int[] p2) {
  if (p1 == null || p2 == null || p1.length != p2.length) {
    throw new IllegalArgumentException("Arrays must not be null and of the same length.");
  }
  
  int sum = 0;
  for (int i = 0; i < p1.length; i++) {
      final int dp = p1[i] - p2[i];
      // Ensure that each element difference is squared to avoid floating point issues
      sum += Math.abs(dp) * Math.abs(dp);
  }
  
  return Math.sqrt(sum);
}