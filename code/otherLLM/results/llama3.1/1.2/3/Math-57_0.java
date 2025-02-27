private static <T extends Clusterable<T>> List<Cluster<T>>
chooseInitialCenters(final Collection<T> points, final int k, final Random random) {

final List<T> pointSet = new ArrayList<T>(points);
final List<Cluster<T>> resultSet = new ArrayList<Cluster<T>>();

// Choose one center uniformly at random from among the data points.
final T firstPoint = pointSet.remove(random.nextInt(pointSet.size()));
resultSet.add(new Cluster<T>(firstPoint));

final double[] dx2 = new double[pointSet.size()];
while (resultSet.size() < k) {
    // For each data point x, compute D(x), the distance between x and
    // the nearest center that has already been chosen.
    int sum = 0;
    for (int i = 0; i < pointSet.size(); i++) {
        final T p = pointSet.get(i);
        final Cluster<T> nearest = getNearestCluster(resultSet, p);
        final double d = p.distanceFrom(nearest.getCenter());
        sum += d * d;
        dx2[i] = sum;
    }

    // Add one new data point as a center. Each point x is chosen with
    // probability proportional to D(x)2
    final double r = random.nextDouble() * sum;
    boolean uniquePointAdded = false;
    for (int i = 0 ; i < dx2.length; i++) {
        if (!uniquePointAdded && !pointSet.get(i).equals(firstPoint)) { // Check if point is unique before adding it as center
            final T p = pointSet.remove(i);
            resultSet.add(new Cluster<T>(p));
            uniquePointAdded = true;
            break;
        } else if (dx2[i] >= r) {
            final T p = pointSet.remove(i);
            resultSet.add(new Cluster<T>(p));
            break;
        }
    }

    // If no unique point is found, add any remaining data points as centers
    while(resultSet.size() < k && pointSet.size() > 0){
        final T p = pointSet.remove(0);
        resultSet.add(new Cluster<T>(p));
    }
}

return resultSet;

}