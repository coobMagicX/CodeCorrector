private static <T extends Clusterable<T>> List<Cluster<T>>
    chooseInitialCenters(final Collection<T> points, final int k, final Random random) {
    final List<T> pointSet = new ArrayList<T>(points);
    final List<Cluster<T>> resultSet = new ArrayList<Cluster<T>>();

    
    final T firstPoint = pointSet.remove(random.nextInt(pointSet.size()));
    resultSet.add(new Cluster<T>(firstPoint));

    while (resultSet.size() < k) {
        double sum = 0;
        double[] dists = new double[pointSet.size()];
        
        
        for (int i = 0; i < pointSet.size(); i++) {
            T p = pointSet.get(i);
            Cluster<T> nearest = getNearestCluster(resultSet, p);
            double d = p.distanceFrom(nearest.getCenter());
            dists[i] = d * d;
            sum += dists[i];
        }

        
        double[] dx2 = new double[pointSet.size()];
        double cumulativeSum = 0;
        for (int i = 0; i < pointSet.size(); i++) {
            cumulativeSum += dists[i];
            dx2[i] = cumulativeSum / sum;  
        }

        
        double r = random.nextDouble();
        for (int i = 0; i < dx2.length; i++) {
            if (dx2[i] >= r) {
                T p = pointSet.remove(i);
                resultSet.add(new Cluster<T>(p));
                break;
            }
        }
    }

    return resultSet;
}
