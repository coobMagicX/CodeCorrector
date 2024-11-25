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
            if (loop.length < 3) {
                continue; // Skip loops that cannot form a valid polygon
            }

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

        if (sum == 0) {
            // The polygon has zero area, which could mean it's degenerate or a single point
            setSize(0);
            setBarycenter(new Vector2D(0, 0));
        } else if (sum < 0) {
            // the polygon as a finite outside surrounded by an infinite inside
            setSize(Double.POSITIVE_INFINITY);
            setBarycenter(Vector2D.NaN);
        } else {
            // All loops are closed, and the polygon has a positive area
            double area = Math.abs(sum / 2);
            setSize(area);
            setBarycenter(new Vector2D(sumX / (6 * area), sumY / (6 * area)));
        }
    }

}