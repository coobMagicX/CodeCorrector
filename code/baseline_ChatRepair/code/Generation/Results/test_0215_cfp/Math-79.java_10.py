public static double distance(int[] p1, int[] p2) {
  if (p1 == null || p2 == null) {
    throw new NullPointerException("One of the input arrays is null");
  }
  if (p1.length != p2.length) {
    throw new IllegalArgumentException("