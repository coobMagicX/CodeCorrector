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

        double sum  = 0;
        double sumX = 0;
        double sumY = 0;

        for (Vector2D[] loop : v) {
            if (loop == null || loop.length <= 1) continue; // Skip invalid loops
            double x1 = loop[loop.length - 1].getX();
            double y1 = loop[loop.length - 1].getY();
            for (final Vector2D point : loop) {
                final double x0 = x1;
                final double y0 = y1;
                x1 = point.getX();
                y1 = point.getY();
                final double factor = x0 * y1 - y0 * x1;
                sum  += factor;
                sumX += factor * (x0 + x1);
                sumY += factor * (y0 + y1);
            }
        }

        // Correcting the sign of area calculation
        if (sum < 0) {
            // The polygon has an inside opposite to the outside
            sum = -sum;
        }

        // Use a tolerance for setting size and barycenter calculations
        final double TOLERANCE = 3.0e-6;

        setSize(sum / 2);
        setBarycenter(new Vector2D(sumX / (3 * sum), sumY / (3 * sum)));

        // Adjusting the size to account for numerical precision issues
        if (Math.abs(getSize() - (sum / 2)) > TOLERANCE) {
            setSize((int)Math.round((sum / 2) + TOLERANCE));
        }

    }

}