public static KMeansPlusPlusClusterer cluster(EuclideanIntegerPoint[] points) {
    if (points == null || points.length == 0) {
        throw new IllegalArgumentException("Input points array is empty or null.");
    }

    // Select an initial center at random
    EuclideanIntegerPoint initialCenter = points[new Random().nextInt(points.length)];
    Cluster firstCluster = new Cluster(initialCenter);

    // Add the first point to the first cluster
    firstCluster.addPoint(initialCenter);
    
    for (int i = 1; i < points.length; i++) {
        double minDistance = Double.MAX_VALUE;
        Cluster closestCluster = null;

        // Find the closest existing cluster to the current point
        for (Cluster c : clusters) {
            double distance = distance(points[i].getCoordinates(), c.getCenter().getCoordinates());
            if (distance < minDistance) {
                minDistance = distance;
                closestCluster = c;
            }
        }

        // If no closer cluster is found, create a new one
        if (closestCluster == null) {
            Cluster newCluster = new Cluster(points[i]);
            clusters.add(newCluster);
        } else {
            // Otherwise, add the point to the closest cluster
            closestCluster.addPoint(points[i]);
        }
    }

    return this;
}