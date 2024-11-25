import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

// Assuming 'Clusterable' and 'T' are defined elsewhere with necessary functionality.

private static <T extends Clusterable<T>> List<Cluster<T>>
chooseInitialCenters(final Collection<T> points, final int k, final Random random) {

    final List<T> pointSet = new ArrayList<>(points);
    final List<Cluster<T>> resultSet = new ArrayList<>();

    // Choose one center uniformly at random from among the data points.
    final T firstPoint = pointSet.remove(random.nextInt(pointSet.size()));
    resultSet.add(new Cluster<>(firstPoint));

    double[] distancesSquared = new double[pointSet.size()];
    while (resultSet.size() < k) {
        // For each data point x, compute D(x), the distance between x and
        // the nearest center that has already been chosen.
        int sum = 0;
        for (int i = 0; i < pointSet.size(); i++) {
            final T p = pointSet.get(i);
            final Cluster<T> nearest = getNearestCluster(resultSet, p);
            final double d = p.distanceFrom(nearest.getCenter());
            sum += d * d;
            distancesSquared[i] = sum;
        }

        // Add one new data point as a center. Each point x is chosen with
        // probability proportional to D(x)^2.
        final double r = random.nextDouble() * (sum / k);  // Adjust the range based on the size of resultSet

        int selectedIndex = -1;
        for (int i = 0; i < distancesSquared.length; i++) {
            if (distancesSquared[i] >= r) {
                selectedIndex = i;
                break;
            }
        }

        if (selectedIndex != -1) {
            final T p = pointSet.remove(selectedIndex);
            resultSet.add(new Cluster<>(p));
        } else { // In case there was a mistake in the selection
            System.out.println("Error: Center selection failed");
            return resultSet; // Optionally, you can throw an exception or handle it as needed.
        }
    }

    return resultSet;
}

// Assuming 'getNearestCluster', 'Clusterable', and other necessary classes are defined elsewhere.