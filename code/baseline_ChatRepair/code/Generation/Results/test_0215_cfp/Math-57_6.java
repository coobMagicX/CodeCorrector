private static <T extends Clusterable<T>> List<Cluster<T>>
    chooseInitialCenters(final Collection<T> points, final int k, final Random random) {

    final List<T> pointSet = new ArrayList<>(points);
    final List<Cluster<T>> resultSet = new ArrayList<>();

    
    T firstPoint = pointSet.remove(random.nextInt(pointSet.size()));
    resultSet.add(new Cluster<>(firstPoint));

    
    double[] squaredDistances = new double[pointSet.size()];

    
    while (resultSet.size() < k) {
        double sum = 0;
        for (int i = 0; i < pointSet.size(); i++) {
            T point = pointSet.get(i);
            Cluster<T> nearestCluster = getNearestCluster(resultSet, point);
            double distance = point.distanceFrom(nearestCluster.getCenter());
            squaredDistances[i] = distance * distance;
            sum += squaredDistances[i];
        }

        
        double randomValue = random.nextDouble() * sum;
        double cumulativeSum = 0;
        for (int i = 0; i < pointSet.size(); i++) {
            cumulativeSum += squaredDistances[i];
            if (cumulativeSum >= randomValue) {
                T newCenterPoint = pointSet.remove(i);
                resultSet.add(new Cluster<>(newCenterPoint));
                break; 
            }
        }

        
        if (resultSet.size() < k) {
            squaredDistances = new double[pointSet.size()];
        }
    }

    return resultSet;
}

private static <T extends Clusterable<T>> Cluster<T> getNearestCluster(List<Cluster<T>> clusters, T point) {
    Cluster<T> nearest = null;
    double minDistance = Double.MAX_VALUE;

    for (Cluster<T> cluster : clusters) {
        double distance = point.distanceFrom(cluster.getCenter());
        if (distance < minDistance) {
            minDistance =