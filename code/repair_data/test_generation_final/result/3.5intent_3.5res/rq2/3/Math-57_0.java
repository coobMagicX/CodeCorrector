private static <T extends Clusterable<T>> List<Cluster<T>> chooseInitialCenters(final Collection<T> points, final int k, final Random random) {
    final List<T> pointSet = new ArrayList<T>(points);
    final List<Cluster<T>> resultSet = new ArrayList<Cluster<T>>();

    // Choose one center uniformly at random from among the data points.
    final T firstPoint = pointSet.remove(random.nextInt(pointSet.size()));
    resultSet.add(new Cluster<T>(firstPoint));

    while (resultSet.size() < k) {
        // For each data point x, compute D(x), the distance between x and
        // the nearest center that has already been chosen.
        double maxDist = 0;
        T nextPoint = null;
        for (T p : pointSet) {
            double minDist = Double.MAX_VALUE;
            for (Cluster<T> cluster : resultSet) {
                double dist = p.distanceFrom(cluster.getCenter());
                if (dist < minDist) {
                    minDist = dist;
                }
            }
            if (minDist > maxDist) {
                maxDist = minDist;
                nextPoint = p;
            }
        }

        // Add the next point as a center.
        pointSet.remove(nextPoint);
        resultSet.add(new Cluster<T>(nextPoint));
    }

    return resultSet;
}