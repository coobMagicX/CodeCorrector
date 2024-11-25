public enum EmptyClusterStrategy {
    ERROR;

    public static void main(String[] args) {
        KMeansPlusPlus kmeans = new KMeansPlusPlus(new Random(), EmptyClusterStrategy.ERROR);
        // Use kmeans to perform clustering
        kmeans.cluster(points, k, maxIterations);
    }
}

class KMeansPlusPlus {

    private final Random random;
    private final EmptyClusterStrategy emptyStrategy;

    public KMeansPlusPlus(Random random) {
        this(random, EmptyClusterStrategy.LARGEST_DISTANCE_VARIANCE);
    }

    public KMeansPlusPlus(Random random, EmptyClusterStrategy emptyStrategy) {
        this.random = random;
        this.emptyStrategy = emptyStrategy;
    }

    public List<Cluster<T>> cluster(Collection<T> points, int k, int maxIterations) {
        // Perform clustering using the selected strategy
        switch (emptyStrategy) {
            case LARGEST_DISTANCE_VARIANCE:
                // Use K-means++ to choose initial centers based on largest distance variance
                break;
            case LARGEST_POINTS_NUMBER:
                // Use K-means++ to choose initial centers based on largest number of points
                break;
            case FARTHEST_POINT:
                // Use K-means++ to choose initial centers based on the point farthest from its centroid
                break;
            case ERROR:
                throw new UnsupportedOperationException("ERROR strategy is not implemented");
        }

        return clusters;
    }
}