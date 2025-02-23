private static <T extends Clusterable<T>> List<Cluster<T>> 
    chooseInitialCenters(final Collection<T> points, final int k, final Random random) {
    final List<T> pointSet = new ArrayList<T>(points);
    final List<Cluster<T>> resultSet = new ArrayList<Cluster<T>>();

    
    final T firstPoint = pointSet.remove(random.nextInt(pointSet.size()));
    resultSet.add(new Cluster<T>(firstPoint));

    double[] minDistancesSquared = new double[pointSet.size()];
    Arrays.fill(minDistancesSquared, Double.MAX_VALUE);

    
    for (int i = 0; i < pointSet.size(); i++) {
        T point = pointSet.get(i);
        double d = point.distanceFrom(firstPoint);
        minDistancesSquared[i] = d * d;
    }

    double totalDistanceSquared = Arrays.stream(minDistancesSquared).sum();

    while (resultSet.size() < k) {
        
        double r = random.nextDouble() * totalDistanceSquared;
        double sum = 0;
        int newIndex = -1;
        for (int i = 0; i < pointSet.size(); i++) {
            sum += minDistancesSquared[i];
            if (sum >= r) {
                newIndex = i;
                break;
            }
        }

        if (newIndex == -1) {
            newIndex = pointSet.size() - 1; 
        }

        T newCenter = pointSet.remove(newIndex);
        resultSet.add(new Cluster<T>(newCenter));
        Arrays.fill(minDistancesSquared, Double.MAX_VALUE);

        
        for (int i = 0; i < pointSet.size(); i++) {
            T point = pointSet.get(i);
            double distanceToNewCenter = point.distanceFrom(newCenter);
            double distSquared = distanceToNewCenter * distanceToNewCenter;
            if (distSquared < minDistancesSquared[i]) {
                minDistancesSquared[i] = distSquared;
            }
       