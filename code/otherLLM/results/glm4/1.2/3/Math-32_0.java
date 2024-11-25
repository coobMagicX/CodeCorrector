protected void computeGeometricalProperties() {

    final Vector2D[][] v = getVertices();

    if (v.length == 0) {
        final BSPTree<Euclidean2D> tree = getTree(false);
        if ((Boolean) tree.getAttribute()) {
            // the instance covers the whole space
            setSize(Double.POSITIVE_INFINITY);
            setBarycenter(Vector2D.NaN);
        } else {
            setSize(0);
            setBarycenter(new Vector2D(0, 0));
        }
    } else if (v[0][0] == null) {
        // there is at least one open-loop: the polygon is infinite
        setSize(Double.POSITIVE_INFINITY);
        setBarycenter(Vector2D.NaN);
    } else {
        // all loops are closed, we compute some integrals around the shape

        double sum = 0.0;
        double sumX = 0.0;
        double sumY = 0.0;

        for (Vector2D[] loop : v) {
            double x1 = loop[loop.length - 1].getX();
            double y1 = loop[loop.length - 1].getY();
            for (final Vector2D point : loop) {
                final double x0 = x1;
                final double y0 = y1;
                x1 = point.getX();
                y1 = point.getY();
                // Use the Shoelace formula to calculate the signed area of the polygon
                final double factor = x0 * y1 - y0 * x1;
                sum += Math.abs(factor);
            }
        }

        if (sum < 0) {
            // The sum is negative, which means the polygon has a finite outside surrounded by an infinite inside
            setSize(Double.POSITIVE_INFINITY);
            setBarycenter(Vector2D.NaN);
        } else {
            // Calculate the area using the Shoelace formula result
            setSize(sum / 2.0);
            // Since we are dealing with a simple polygon, the barycenter (centroid) is at the centroid of the vertices
            double xSum = 0.0;
            double ySum = 0.0;
            int count = 0;
            for (Vector2D[] loop : v) {
                for (final Vector2D point : loop) {
                    xSum += point.getX();
                    ySum += point.getY();
                    ++count;
                }
            }
            setBarycenter(new Vector2D(xSum / count, ySum / count));
        }

    }

}