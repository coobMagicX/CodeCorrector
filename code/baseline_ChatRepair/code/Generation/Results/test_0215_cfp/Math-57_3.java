private static <T extends Clusterable<T>> List<Cluster<T>>
    chooseInitialCenters(final Collection<T> points, final int k, final Random random) {
    
    if (points == null || points.isEmpty() || k <= 0) {
        throw new IllegalArgumentException("Invalid input parameters.");
    }
    
    final List<T> pointSet = new ArrayList<T>(points);
    final List<Cluster<T>> resultSet = new ArrayList<Cluster<T>>();

    
    if (k > pointSet.size()) {
        throw new IllegalArgumentException("The number of required centers exceeds the number of data points.");
    }

    
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
        for (int i = 0; i < dx2.length; i++) {
            if ((i == 0 && dx2[i] >= r) || (i > 0 && dx2[i] >= r && dx2[i - 1] < r)) {
                final T p = pointSet.remove(i);
                resultSet.add(new Cluster<T>(p));
                break;
            }
        }
    }

    return resultSet;
}
