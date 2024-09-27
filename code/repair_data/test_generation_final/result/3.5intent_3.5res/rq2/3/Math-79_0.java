public static double distance(int[] p1, int[] p2) {
  int sum = 0;
  for (int i = 0; i < p1.length; i++) {
      final int dp = p1[i] - p2[i];
      sum += dp * dp;
  }
  return Math.sqrt(sum);
}

public static void main(String[] args) {
  int[][] points = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}; // Example points array
  KMeansPlusPlusClusterer<double[]> clusterer = new KMeansPlusPlusClusterer<>(1); // Clusterer with 1 cluster
  List<CentroidCluster<double[]>> clusters = clusterer.cluster(points); // Cluster the points
  for (CentroidCluster<double[]> cluster : clusters) {
      for (double[] point : cluster.getPoints()) {
          System.out.println(Arrays.toString(point));
      }
  }
}