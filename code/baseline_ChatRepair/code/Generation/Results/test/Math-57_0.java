private static <T extends Clusterable<T>> List<Cluster<T>>
    chooseInitialCenters(final Collection<T> points, final int k, final Random random) {

    final List<T> pointSet = new ArrayList<T>(points);
    final List<Cluster<T>> resultSet = new ArrayList<Cluster<T>>();

    
    final T firstPoint = pointSet.remove(random.nextInt(pointSet.size()));
    resultSet.add(new Cluster<T>(firstPoint));

    while (resultSet.size() < k) {
        final double[] dx2 = new double[pointSet.size()];
        double sum = 0;
        for (int i = 0; i < pointSet.size(); i++) {
            final T p = pointSet.get(i);
            final Cluster<T> nearest = getNearestCluster(resultSet, p);
            final double d = p.distanceFrom(nearest.getCenter());
            dx2[i] = d * d;
            sum += dx2[i];
        }

        
        
        final double r = random.nextDouble() * sum;
        boolean found = false;
        for (int i = 0; i < dx2.length && !found; i++) {
            if (dx2[i] >= r) {
                final T newCenter = pointSet.remove(i);
                resultSet.add(new Cluster<T>(newCenter));
                found = true;
            }
        }

        
        if (!found) {
            T fallbackCenter = pointSet.remove(random.nextInt(pointSet.size()));
            resultSet.add(new Cluster<T>(fallbackCenter));
        }
    }

    return resultSet;
}
