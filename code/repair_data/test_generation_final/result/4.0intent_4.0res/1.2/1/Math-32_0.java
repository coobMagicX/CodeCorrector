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
        double sum = 0;
        double sumX = 0;
        double sumY = 0;

        for (Vector2D[] loop : v) {
            double x1 = loop[loop.length - 1].getX();
            double y1 = loop[loop.length - 1].getY();
            for (final Vector2D point : loop) {
                final double x0 = x1;
                final double y0 = y1;
                x1 = point.getX();
                y1 = point.getY();
                final double factor = x0 * y1 - y0 * x1;
                sum += factor;
                sumX += factor * (x0 + x1);
                sumY += factor * (y0 + y1);
            }
        }

        // Correcting the sign of the sum to ensure it is positive if computed negatively due to order of vertices
        if (sum < 0) {
            sum = -sum;
            sumX = -sumX;
            sumY = -sumY;
        }

        double area = Math.abs(sum) / 2;
        Vector2D barycenter = new Vector2D(sumX / (3 * sum), sumY / (3 * sum));

        setSize(area);
        setBarycenter(barycenter);
    }
}