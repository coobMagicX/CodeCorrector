private static <T extends Clusterable<T>> List<Cluster<T>>
chooseInitialCenters(final Collection<T> points, final int k, final Random random) {

    final List<T> pointSet = new ArrayList<>(points);
    final List<Cluster<T>> resultSet = new ArrayList<>();

    // Choose one center uniformly at random from among the data points.
    final T firstPoint = pointSet.remove(random.nextInt(pointSet.size()));
    resultSet.add(new Cluster<>(firstPoint));

    final double[] distancesSquared = new double[pointSet.size()];
    while (resultSet.size() < k) {
        // For each data point x, compute D(x), the distance between x and
        // the nearest center that has already been chosen.
        int sum = 0;
        for (int i = 0; i < pointSet.size(); i++) {
            final T p = pointSet.get(i);
            final Cluster<T> nearest = getNearestCluster(resultSet, p);
            final double d = p.distanceFrom(nearest.getCenter());
            sum += d * d;
            distancesSquared[i] = sum;
        }

        // Add one new data point as a center. Each point x is chosen with
        // probability proportional to D(x)^2
        final double r = random.nextDouble() * (distancesSquared.length > 0 ? distancesSquared[distancesSquared.length - 1] : 0);
        for (int i = 0; i < distancesSquared.length; i++) {
            if (distancesSquared[i] >= r) {
                final T p = pointSet.remove(i);
                resultSet.add(new Cluster<>(p));
                break;
            }
        }
    }

    return resultSet;

}