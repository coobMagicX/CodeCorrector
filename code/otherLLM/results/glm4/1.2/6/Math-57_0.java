private static <T extends Clusterable<T>> List<Cluster<T>>
chooseInitialCenters(final Collection<T> points, final int k, final Random random) {

    final List<T> pointSet = new ArrayList<>(points);
    final List<Cluster<T>> resultSet = new ArrayList<>();
    if (k > 0 && !pointSet.isEmpty()) {
        // Choose one center uniformly at random from among the data points.
        final T firstPoint = pointSet.remove(random.nextInt(pointSet.size()));
        resultSet.add(new Cluster<>(firstPoint));

        final double[] distancesToFirstCenter = new double[pointSet.size()];
        for (int i = 0; i < pointSet.size(); i++) {
            distancesToFirstCenter[i] = pointSet.get(i).distanceFrom(resultSet.get(0).getCenter());
        }

        while (resultSet.size() < k) {
            // For each data point x, compute D(x), the distance between x and
            // the nearest center that has already been chosen.
            int sum = 0;
            for (int i = 0; i < distancesToFirstCenter.length; i++) {
                double minDistance = Double.MAX_VALUE;
                for (final Cluster<T> c : resultSet) {
                    final double d = pointSet.get(i).distanceFrom(c.getCenter());
                    if (d < minDistance) {
                        minDistance = d;
                    }
                }
                sum += minDistance * minDistance;
            }

            // Add one new data point as a center. Each point x is chosen with
            // probability proportional to D(x)^2
            final double r = random.nextDouble() * sum;
            int chosenIndex = -1;
            for (int i = 0; i < distancesToFirstCenter.length; i++) {
                sum -= distancesToFirstCenter[i] * distancesToFirstCenter[i];
                if (distancesToFirstCenter[i] >= r) {
                    chosenIndex = i;
                    break;
                }
            }

            if (chosenIndex != -1) {
                final T p = pointSet.remove(chosenIndex);
                resultSet.add(new Cluster<>(p));
                // Update distances to the new center
                for (int j = 0; j < distancesToFirstCenter.length; j++) {
                    if (j != chosenIndex) {
                        distancesToFirstCenter[j] = p.distanceFrom(resultSet.get(resultSet.size() - 1).getCenter());
                    }
                }
            }
        }
    }

    return resultSet;
}