public static int cluster(int[] points, double[] centroids, int k) {
    // Assuming the existence of a method to find the nearest centroid for each point
    int[] assignments = new int[points.length];
    
    for (int i = 0; i < points.length; i++) {
        double minDistance = Double.MAX_VALUE;
        int closestCentroidIndex = -1;

        // Loop over all centroids and find the closest one
        for (int j = 0; j < k; j++) {
            double distance = distance(points, centroids[j]);

            if (distance < minDistance) {
                minDistance = distance;
                closestCentroidIndex = j;
            }
        }

        // Assign the point to the cluster of the nearest centroid
        assignments[i] = closestCentroidIndex;
    }

    return assignments; // Return array containing indices of clusters for each point
}