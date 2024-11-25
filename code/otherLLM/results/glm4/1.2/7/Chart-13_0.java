protected Size2D arrangeFF(BlockContainer container, Graphics2D g2, RectangleConstraint constraint) {
    double[] w = new double[5];
    double[] h = new double[5];
    w[0] = Math.max(0, constraint.getWidth()); // Ensure no negative width.
    if (this.topBlock != null) {
        RectangleConstraint c1 = new RectangleConstraint(w[0], null,
                LengthConstraintType.FIXED, 0.0,
                new Range(0.0, constraint.getHeight()),
                LengthConstraintType.RANGE);
        Size2D sizeTop = this.topBlock.arrange(g2, c1);
        h[0] = Math.max(0, sizeTop.height); // Ensure no negative height.
    }
    
    w[1] = constraint.getWidth() - (this.leftBlock != null ? w[0] : 0) - (this.rightBlock != null ? w[3] : 0);
    if (this.bottomBlock != null) {
        RectangleConstraint c2 = new RectangleConstraint(w[1], null,
                LengthConstraintType.FIXED, 0.0,
                new Range(0.0, constraint.getHeight() - h[0]),
                LengthConstraintType.RANGE);
        Size2D sizeBottom = this.bottomBlock.arrange(g2, c2);
        h[1] = Math.max(0, sizeBottom.height);
    }

    h[2] = Math.max(0, constraint.getHeight() - (h[0] + h[1]));
    
    if (this.leftBlock != null) {
        RectangleConstraint c3 = new RectangleConstraint(0.0,
                new Range(0.0, constraint.getWidth()),
                LengthConstraintType.RANGE, h[2], null,
                LengthConstraintType.FIXED);
        Size2D sizeLeft = this.leftBlock.arrange(g2, c3);
        w[2] = Math.max(0, sizeLeft.width);
    }

    if (this.rightBlock != null) {
        RectangleConstraint c4 = new RectangleConstraint(0.0,
                new Range(0.0, constraint.getWidth() - (w[2] + (this.centerBlock != null ? w[4] : 0))),
                LengthConstraintType.RANGE, h[2], null,
                LengthConstraintType.FIXED);
        Size2D sizeRight = this.rightBlock.arrange(g2, c4);
        w[3] = Math.max(0, sizeRight.width);
    }

    if (this.centerBlock != null) {
        w[4] = constraint.getWidth() - w[2] - w[3];
        RectangleConstraint c5 = new RectangleConstraint(w[4], h[4]);
        this.centerBlock.arrange(g2, c5);
    }

    // Set bounds for each block after all calculations
    if (this.topBlock != null) {
        this.topBlock.setBounds(new Rectangle2D.Double(0.0, 0.0, w[0], h[0]));
    }
    if (this.bottomBlock != null) {
        this.bottomBlock.setBounds(new Rectangle2D.Double(0.0, h[0] + h[2],
                w[1], h[1]));
    }
    if (this.leftBlock != null) {
        this.leftBlock.setBounds(new Rectangle2D.Double(0.0, 0.0, w[2], h[2]));
    }
    if (this.rightBlock != null) {
        this.rightBlock.setBounds(new Rectangle2D.Double(w[2] + (this.centerBlock != null ? w[4] : 0),
                0.0, w[3], h[2]));
    }
    
    // Return the final size of the container
    return new Size2D(constraint.getWidth(), constraint.getHeight());
}