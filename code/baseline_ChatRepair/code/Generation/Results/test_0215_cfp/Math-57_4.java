private static <T extends Clusterable<T>> List<Cluster<T>>
    chooseInitialCenters(final Collection<T> points, final int k, final Random random) {

    final List<T> pointSet = new ArrayList<T>(points);
    final List<Cluster<T>> resultSet = new ArrayList<Cluster<T>>();

    
    T firstPoint = pointSet.remove(random.nextInt(pointSet.size()));
    resultSet.add(new Cluster<T>(firstPoint));

    while (resultSet.size() < k) {
        double[] dx2 = new double[pointSet.size()];
        double sum = 0;

        
        for (int i = 0; i < pointSet.size(); i++) {
            T p = pointSet.get(i);
            Cluster<T> nearest = getNearestCluster(resultSet, p);
            double d = p.distanceFrom(nearest.getCenter());
            double d2 = d * d;
            dx2[i] = d2;
            sum += d2;
        }

        
        double r = random.nextDouble() * sum;
        double cumulative = 0;
        for (int i = 0; i < dx2.length; i++) {
            cumulative += dx2[i];
            if (cumulative >= r) {
                T newCenter = pointSet.remove(i);
                resultSet.add(new Cluster<T>(newCenter));
                break;
            }
        }
    }

    return resultSet;
}
