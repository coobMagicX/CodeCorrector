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

        double sumArea = 0;
        double sumX = 0;
        double sumY = 0;

        for (Vector2D[] loop : v) {
            if (loop.length < 3) { // A valid polygon must have at least 3 vertices
                continue; // Skip invalid polygons that cannot form a closed loop
            }
            
            double x1 = loop[loop.length - 1].getX();
            double y1 = loop[loop.length - 1].getY();
            for (final Vector2D point : loop) {
                final double x0 = x1;
                final double y0 = y1;
                x1 = point.getX();
                y1 = point.getY();
                final double factor = x0 * y1 - y0 * x1;
                sumArea += factor;
                sumX += factor * (x0 + x1);
                sumY += factor * (y0 + y1);
            }
        }

        if (sumArea == 0) {
            // The polygon might be degenerate or non-closed
            setSize(0);
            setBarycenter(new Vector2D(0, 0));
        } else {
            // Calculate area and barycenter coordinates
            double area = Math.abs(sumArea / 2);
            double xMean = (sumX != 0) ? 2 * sumY / sumArea : 0;
            double yMean = (sumY != 0) ? 2 * sumX / sumArea : 0;
            setSize(area);
            setBarycenter(new Vector2D(xMean, yMean));
        }

    }

}