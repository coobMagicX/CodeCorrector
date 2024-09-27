import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

class Cluster<T extends Clusterable<T>> {
    private T center;

    public Cluster(T center) {
        this.center = center;
    }

    public T getCenter() {
        return center;
    }
}

interface Clusterable<T> {
    double distanceFrom(T other);
}

public class ClusteringAlgorithm {

    private static <T extends Clusterable<T>> List<Cluster<T>>
    chooseInitialCenters(final Collection<T> points, final int k, final Random random) {

        final List<T> pointSet = new ArrayList<T>(points);
        final List<Cluster<T>> resultSet = new ArrayList<Cluster<T>>();

        // Choose one center uniformly at random from among the data points.
        final T firstPoint = pointSet.remove(random.nextInt(pointSet.size()));
        resultSet.add(new Cluster<T>(firstPoint));

        final double[] dx2 = new double[pointSet.size()];
        double sum = 0;
        while (resultSet.size() < k) {
            // Reset sum for each iteration over pointSet.
            sum = 0;
            for (int i = 0; i < pointSet.size(); i++) {
                final T p = pointSet.get(i);
                final Cluster<T> nearest = getNearestCluster(resultSet, p);
                final double d = p.distanceFrom(nearest.getCenter());
                dx2[i] = d * d;
                sum += dx2[i];
            }

            // Add one new data point as a center. Each point x is chosen with
            // probability proportional to D(x)^2
            final double r = random.nextDouble() * sum;
            for (int i = 0 ; i < dx2.length; i++) {
                if (dx2[i] >= r) {
                    final T p = pointSet.remove(i);
                    resultSet.add(new Cluster<T>(p));
                    break;
                }
            }
        }

        return resultSet;
    }

    private static <T extends Clusterable<T>> Cluster<T> getNearestCluster(List<Cluster<T>> clusters, T point) {
        Cluster<T> nearest = null;
        double minDistance = Double.MAXIMUM;
        for (Cluster<T> cluster : clusters) {
            double distance = point.distanceFrom(cluster.getCenter());
            if (nearest == null || distance < minDistance) {
                minDistance = distance;
                nearest = cluster;
            }
        }
        return nearest;
    }

    public static void main(String[] args) {
        // Example usage, provided you have implementations for Clusterable.
    }
}