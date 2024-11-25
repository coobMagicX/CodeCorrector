private static <T extends Clusterable<T>> List<Cluster<T>>
chooseInitialCenters(final Collection<T> points, final int k, final Random random) {

    final List<T> pointSet = new ArrayList<T>(points);
    final List<Cluster<T>> resultSet = new ArrayList<Cluster<T>>();

    // Choose one center uniformly at random from among the data points.
    final T firstPoint = pointSet.remove(random.nextInt(pointSet.size()));
    resultSet.add(new Cluster<T>(firstPoint));

    while (resultSet.size() < k) {
        // For each data point x, compute D(x), the distance between x and
        // the nearest center that has already been chosen.
        double[] dx2 = new double[pointSet.size()];
        for (int i = 0; i < pointSet.size(); i++) {
            final T p = pointSet.get(i);
            final Cluster<T> nearest = getNearestCluster(resultSet, p);
            final double d = p.distanceFrom(nearest.getCenter());
            dx2[i] = d * d;
        }

        // Calculate the total distance squared to ensure that the probability
        // distribution is correct.
        double totalDistanceSquared = 0.0;
        for (double dist : dx2) {
            totalDistanceSquared += dist;
        }

        // Add one new data point as a center. Each point x is chosen with
        // probability proportional to D(x)^2, but ensure it's not the same as the nearest center.
        double r = random.nextDouble() * totalDistanceSquared;
        for (int i = 0 ; i < dx2.length; i++) {
            if (dx2[i] >= r) {
                final T p = pointSet.remove(i);
                Cluster<T> nearestCluster = getNearestCluster(resultSet, p);
                // Ensure the selected point is not already a center or the closest to an existing center.
                if (!resultSet.contains(new Cluster<>(p)) && !p.equals(nearestCluster.getCenter())) {
                    resultSet.add(new Cluster<T>(p));
                    break;
                }
            } else {
                r -= dx2[i];
            }
        }
    }

    return resultSet;

}