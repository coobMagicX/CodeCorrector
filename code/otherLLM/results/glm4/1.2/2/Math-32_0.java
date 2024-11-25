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
            if (loop.length == 0) continue; // Skip empty loops
            double x1 = loop[loop.length - 1].getX();
            double y1 = loop[loop.length - 1].getY();
            for (final Vector2D point : loop) {
                final double x0 = x1;
                final double y0 = y1;
                x1 = point.getX();
                y1 = point.getY();
                final double factor = (x0 * y1 - y0 * x1);
                sum  += factor; // Sum of signed area elements
            }
        }

        if (sum == 0) {
            setSize(0); // The polygon has zero area, so its size is also zero
            setBarycenter(new Vector2D(0, 0));
        } else {
            setSize(Math.abs(sum / 2)); // Correct sign of the area for non-empty polygons
            double perimeter = computePerimeter(v);
            setBarycenter(new Vector2D(perimeter == 0 ? 0 : (sumX / (3 * sum)), 
                                       perimeter == 0 ? 0 : (sumY / (3 * sum))));
        }
    }

}

private double computePerimeter(Vector2D[][] v) {
    if (v.length == 0 || v[0].length == 0) return 0;
    double perimeter = 0;
    for (Vector2D[] loop : v) {
        for (int i = 0; i < loop.length; i++) {
            final Vector2D point1 = loop[i];
            final Vector2D point2 = i + 1 < loop.length ? loop[i + 1] : loop[0];
            perimeter += point1.distance(point2);
        }
    }
    return perimeter;
}