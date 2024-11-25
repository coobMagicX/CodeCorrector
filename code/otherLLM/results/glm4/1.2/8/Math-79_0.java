public static double distance(int[] p1, int[] p2) {
  int sum = 0;
  for (int i = 0; i < p1.length; i++) {
      final int dp = p1[i] - p2[i];
      sum += dp * dp;
  }
  return Math.sqrt(sum);
}

public static void main(String[] args) {
    // Assuming KMeansPlusPlusClusterer and EuclideanIntegerPoints are defined elsewhere.
    // Here's an example of how you might use the distance method:
    
    int[] point1 = {5, 10};
    int[] point2 = {6, 12};

    double dist = distance(point1, point2);
    System.out.println("Distance: " + dist); // Should print the Euclidean distance between point1 and point2
}