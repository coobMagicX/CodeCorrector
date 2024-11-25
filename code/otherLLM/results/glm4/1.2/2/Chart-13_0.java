protected Size2D arrangeFF(BlockContainer container, Graphics2D g2,
                           RectangleConstraint constraint) {
    double[] w = new double[5];
    double[] h = new double[5];
    w[0] = constraint.getWidth();
    h[0] = 0; // Initialize to zero if there is no topBlock

    // Set top block bounds
    if (this.topBlock != null) {
        RectangleConstraint c1 = new RectangleConstraint(w[0], null,
                LengthConstraintType.FIXED, 0.0,
                new Range(0.0, constraint.getHeight()),
                LengthConstraintType.RANGE);
        Size2D sizeTop = this.topBlock.arrange(g2, c1);
        h[0] = sizeTop.getHeight(); // Update height with actual top block size
    }

    w[1] = 0; // Initialize to zero if there is no bottomBlock

    // Set bottom block bounds
    if (this.bottomBlock != null) {
        RectangleConstraint c2 = new RectangleConstraint(w[0], h[0],
                LengthConstraintType.FIXED, 0.0,
                new Range(0.0, constraint.getHeight() - h[0]),
                LengthConstraintType.RANGE);
        Size2D sizeBottom = this.bottomBlock.arrange(g2, c2);
        h[1] = sizeBottom.getHeight(); // Update height with actual bottom block size
    }

    w[2] = 0; // Initialize to zero if there are no left or right blocks

    // Set left and right block bounds
    if (this.leftBlock != null) {
        RectangleConstraint c3 = new RectangleConstraint(0, 0,
                LengthConstraintType.FIXED, 0.0,
                new Range(0.0, constraint.getWidth() - w[0]),
                LengthConstraintType.RANGE);
        Size2D sizeLeft = this.leftBlock.arrange(g2, c3);
        w[2] = sizeLeft.getWidth(); // Update width with actual left block size
    }
    if (this.rightBlock != null) {
        RectangleConstraint c4 = new RectangleConstraint(w[0] + w[2], 0,
                LengthConstraintType.FIXED, 0.0,
                new Range(0.0, constraint.getWidth() - w[0]),
                LengthConstraintType.RANGE);
        Size2D sizeRight = this.rightBlock.arrange(g2, c4);
        w[2] += sizeRight.getWidth(); // Update width with actual right block size
    }

    // Set center block bounds
    if (this.centerBlock != null) {
        RectangleConstraint c5 = new RectangleConstraint(w[0] + w[2], 0,
                LengthConstraintType.FIXED, 0.0,
                new Range(0.0, constraint.getWidth() - w[2]),
                LengthConstraintType.RANGE);
        this.centerBlock.arrange(g2, c5); // Arrange the center block
    }

    // Calculate total width and height
    double totalWidth = w[0] + (this.leftBlock != null ? w[2] : 0) +
                        (this.rightBlock != null ? w[2] : 0);
    double totalHeight = Math.max(h[0], h[1]) + (this.centerBlock != null ? Math.max(0, h[0] - h[1]) : 0);

    return new Size2D(totalWidth, totalHeight);
}