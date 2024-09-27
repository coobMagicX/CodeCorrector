public static double distance(int[] p1, int[] p2) {
  BigInteger sum = BigInteger.ZERO;
  for (int i = 0; i < p1.length; i++) {
      final BigInteger dp = BigInteger.valueOf(p1[i] - p2[i]);
      sum = sum.add(dp.multiply(dp));
  }
  return Math.sqrt(sum.doubleValue());
}