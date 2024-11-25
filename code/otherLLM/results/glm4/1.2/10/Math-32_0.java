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
            if (loop.length < 3) {
                // A polygon must have at least 3 vertices
                continue;
            }
            
            double x1 = loop[loop.length - 1].getX();
            double y1 = loop[loop.length - 1].getY();
            for (int i = 0; i < loop.length; ++i) {
                final Vector2D point = loop[i];
                final double x0 = x1;
                final double y0 = y1;
                x1 = point.getX();
                y1 = point.getY();
                
                // Correct the calculation of the signed area factor
                final double factor = (x0 * y1 - y0 * x1) / 2.0; // Divide by 2 for the correct half-area
                
                sum += factor;
                sumX += factor * (x0 + x1);
                sumY += factor * (y0 + y1);
            }
        }

        if (sum < 0) {
            // The signed area is negative, which means the polygon is on the wrong side of some plane
            setSize(Double.POSITIVE_INFINITY);
            setBarycenter(Vector2D.NaN);
        } else {
            setSize(sum); // Use the absolute value of sum to get the area
            double barycenterX = (sumY * sumX) / (6.0 * Math.abs(sum));
            double barycenterY = (sumX * sumY) / (6.0 * Math.abs(sum));
            setBarycenter(new Vector2D(barycenterX, barycenterY));
        }

    }

}