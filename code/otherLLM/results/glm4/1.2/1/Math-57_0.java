private static <T extends Clusterable<T>> List<Cluster<T>>
chooseInitialCenters(final Collection<T> points, final int k, final Random random) {

    final List<T> pointSet = new ArrayList<>(points);
    final List<Cluster<T>> resultSet = new ArrayList<>();

    // Choose one center uniformly at random from among the data points.
    final T firstPoint = pointSet.remove(random.nextInt(pointSet.size()));
    resultSet.add(new Cluster<>(firstPoint));

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
        // probability proportional to D(x)^2
        final double r = random.nextDouble() * sum;
        
        // Use binary search to find the index of the first element that is greater than r
        int low = 0, high = dx2.length - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            if (dx2[mid] < r)
                low = mid + 1;
            else
                high = mid - 1;
        }

        // If the sum is zero, all points have been considered and no new center has been selected.
        if (sum == 0 && resultSet.size() < k - 1) {
            throw new IllegalArgumentException("All points are identical or very close in distance.");
        }
        
        // The point at index 'high' should be the next cluster center if it is greater than r
        final T p = pointSet.remove(high);
        resultSet.add(new Cluster<>(p));
    }

    return resultSet;
}