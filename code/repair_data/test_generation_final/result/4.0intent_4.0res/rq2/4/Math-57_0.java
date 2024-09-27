private static <T extends Clusterable<T>> List<Cluster<T>> chooseInitialCenters(final Collection<T> points, final int k, final Random random) {
    final List<T> pointSet = new ArrayList<T>(points);
    final List<Cluster<T>> resultSet = new ArrayList<Cluster<T>>();

    // Choose one center uniformly at random from among the data points.
    final T firstPoint = pointSet.remove(random.nextInt(pointSet.size()));
    resultSet.add(new Cluster<T>(firstPoint));

    final double[] dx2 = new double[pointSet.size()];

    // Reset sum for each round of selecting new centers
    double sum = 0;
    while (resultSet.size() < k) {
        sum = 0; // Ensure sum is reset for each new center selection
        // For each data point x, compute D(x), the distance between x and
        // the nearest center that has already been chosen.
        for (int i = 0; i < pointSet.size(); i++) {
            final T p = pointSet.get(i);
            final Cluster<T> nearest = getNearestCluster(resultSet, p);
            final double d = p.distanceFrom(nearest.getCenter());
            dx2[i] = d * d;
            sum += dx2[i];
        }

        // Add one new data point as a center. Each point x is chosen with
        // probability proportional to D(x)^2
        final double r = random.nextDouble() * sum;
        double cumulativeProbability = 0.0;
        for (int i = 0 ; i < dx2.length; i++) {
            cumulativeProbability += dx2[i];
            if (cumulativeProbability >= r) {
                final T p = pointSet.remove(i);
                resultSet.add(new Cluster<T>(p));
                break;
            }
        }
    }

    return resultSet;
}