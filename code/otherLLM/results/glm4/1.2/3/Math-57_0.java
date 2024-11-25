private static <T extends Clusterable<T>> List<Cluster<T>>
chooseInitialCenters(final Collection<T> points, final int k, final Random random) {

    final List<T> pointSet = new ArrayList<T>(points);
    final List<Cluster<T>> resultSet = new ArrayList<Cluster<T>>();

    // Choose one center uniformly at random from among the data points.
    final T firstPoint = pointSet.remove(random.nextInt(pointSet.size()));
    resultSet.add(new Cluster<T>(firstPoint));

    while (resultSet.size() < k) {
        final double[] dx2 = calculateDistancesSquared(pointSet, resultSet);
        final double r = random.nextDouble();
        double cumulativeProbability = 0.0;

        for (int i = 0; i < dx2.length; i++) {
            cumulativeProbability += Math.sqrt(dx2[i]);
            if (cumulativeProbability >= r) {
                final T p = pointSet.remove(i);
                resultSet.add(new Cluster<T>(p));
                break;
            }
        }
    }

    return resultSet;
}

private static double[] calculateDistancesSquared(final List<T> pointSet, final List<Cluster<T>> clusters) {
    final double[] dx2 = new double[pointSet.size()];
    for (int i = 0; i < pointSet.size(); i++) {
        T p = pointSet.get(i);
        Cluster<T> nearest = getNearestCluster(clusters, p);
        double d = p.distanceFrom(nearest.getCenter());
        dx2[i] = d * d;
    }
    return dx2;
}