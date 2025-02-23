private static <T extends Clusterable<T>> List<Cluster<T>>
    chooseInitialCenters(final Collection<T> points, final int k, final Random random) {

    final List<T> pointSet = new ArrayList<T>(points);
    final List<Cluster<T>> resultSet = new ArrayList<Cluster<T>>();

    
    final T firstPoint = pointSet.remove(random.nextInt(pointSet.size()));
    resultSet.add(new Cluster<T>(firstPoint));

    final double[] dx2 = new double[pointSet.size()];
    while (resultSet.size() < k) {
        double sum = 0.0;
        for (int i = 0; i < pointSet.size(); i++) {
            final T p = pointSet.get(i);
            final Cluster<T> nearest = getNearestCluster(resultSet, p);
            final double d = p.distanceFrom(nearest.getCenter());
            dx2[i] = d * d;
            sum += dx2[i];
        }

        
        if (sum == 0) {
            continue;  
        }

        
        
        final double r = random.nextDouble() * sum;
        for (int i = 0 ; i < dx2.length; i++) {
            if ((i == 0 && r <= dx2[i]) || (i > 0 && r > dx2[i - 1] && r <= dx2[i])) {
                final T p = pointSet.remove(i);
                resultSet.add(new Cluster<T>(p));
                break;  
            }
        }
    }

    return resultSet;
}
