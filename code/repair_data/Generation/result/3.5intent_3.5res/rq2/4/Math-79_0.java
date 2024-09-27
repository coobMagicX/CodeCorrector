public static double distance(int[] p1, int[] p2) {
    int sum = 0;
    for (int i = 0; i < p1.length; i++) {
        final int dp = p1[i] - p2[i];
        sum += dp * dp;
    }
    return Math.sqrt(sum);
}

public static void main(String[] args) {
    int[][] points = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
    
    KMeansPlusPlusClusterer<int[]> clusterer = new KMeansPlusPlusClusterer<>(1);
    List<CentroidCluster<int[]>> clusters = clusterer.cluster(points);
    
    for (CentroidCluster<int[]> cluster : clusters) {
        System.out.println("Cluster center: " + Arrays.toString(cluster.getCenter().getPoint()));
        System.out.println("Points in cluster: " + cluster.getPoints().size());
        System.out.println("----------------------------");
    }
}