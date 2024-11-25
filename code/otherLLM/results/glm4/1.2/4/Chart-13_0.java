protected Size2D arrangeFF(BlockContainer container, Graphics2D g2,
                           RectangleConstraint constraint) {
    double[] w = new double[5];
    double[] h = new double[5];
    // First, we assume the width is fixed.
    w[0] = constraint.getWidth();
    w[1] = w[0]; // Copy for bottom block since it's the same width as top
    w[2] = constraint.getWidth(); // Left and right blocks share the same width
    w[3] = w[2];
    w[4] = w[2];

    h[0] = this.topBlock != null ? this.topBlock.arrange(g2, new RectangleConstraint(w[0], null,
                LengthConstraintType.FIXED, 0.0,
                new Range(0.0, constraint.getHeight()),
                LengthConstraintType.RANGE)).height : 0;
    h[1] = this.bottomBlock != null ? this.bottomBlock.arrange(g2, new RectangleConstraint(w[0], null,
                LengthConstraintType.FIXED, 0.0,
                new Range(0.0, constraint.getHeight() - h[0]),
                LengthConstraintType.RANGE)).height : 0;
    h[2] = this.leftBlock != null ? this.leftBlock.arrange(g2, new RectangleConstraint(0.0, 0.0,
                w[2], Double.MAX_VALUE)).height : 0;
    h[3] = this.rightBlock != null ? this.rightBlock.arrange(g2, new RectangleConstraint(0.0, 0.0,
                w[3], Double.MAX_VALUE)).height : 0;
    h[4] = constraint.getHeight() - h[1] - h[2]; // Center height is the remaining space

    if (this.centerBlock != null) {
        this.centerBlock.arrange(g2, new RectangleConstraint(w[4], h[4]));
    }

    // Set bounds for each block
    if (this.topBlock != null) {
        this.topBlock.setBounds(new Rectangle2D.Double(0.0, 0.0, w[0], h[0]));
    }
    if (this.bottomBlock != null) {
        this.bottomBlock.setBounds(new Rectangle2D.Double(0.0, constraint.getHeight() - h[1],
                w[1], h[1]));
    }
    if (this.leftBlock != null) {
        this.leftBlock.setBounds(new Rectangle2D.Double(0.0, 0.0, w[2], h[2]));
    }
    if (this.rightBlock != null) {
        this.rightBlock.setBounds(new Rectangle2D.Double(w[2], 0.0, w[3], h[3]));
    }
    if (this.centerBlock != null) {
        this.centerBlock.setBounds(new Rectangle2D.Double(w[2], constraint.getHeight() - h[1] - h[2],
                w[4], h[4]));
    }

    return new Size2D(constraint.getWidth(), constraint.getHeight());
}