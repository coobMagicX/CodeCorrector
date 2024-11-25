protected Size2D arrangeFF(BlockContainer container, Graphics2D g2,
                           RectangleConstraint constraint) {
    double[] w = new double[5];
    double[] h = new double[5];
    w[0] = constraint.getWidth();
    if (this.topBlock != null) {
        RectangleConstraint c1 = new RectangleConstraint(w[0], null,
                LengthConstraintType.FIXED, 0.0,
                new Range(0.0, constraint.getHeight()),
                LengthConstraintType.RANGE);
        Size2D sizeTop = this.topBlock.arrange(g2, c1);
        h[0] = sizeTop.height;
    }
    
    // Calculate the height for bottom block
    double bottomHeight = (this.bottomBlock != null) ? constraint.getHeight() - h[0] : 0.0;

    if (this.bottomBlock != null) {
        RectangleConstraint c2 = new RectangleConstraint(w[0], null,
                LengthConstraintType.FIXED, 0.0, new Range(0.0, bottomHeight),
                LengthConstraintType.RANGE);
        Size2D sizeBottom = this.bottomBlock.arrange(g2, c2);
        h[1] = sizeBottom.height;
    }
    
    // Combine heights to get the height for left and right blocks
    double totalLeftRightHeight = (this.leftBlock != null) ? h[1] : 0.0;

    if (this.leftBlock != null) {
        RectangleConstraint c3 = new RectangleConstraint(0.0,
                new Range(0.0, constraint.getWidth() - w[0]),
                LengthConstraintType.RANGE, totalLeftRightHeight, null,
                LengthConstraintType.FIXED);
        Size2D sizeLeft = this.leftBlock.arrange(g2, c3);
        w[2] = sizeLeft.width;
    }
    
    if (this.rightBlock != null) {
        RectangleConstraint c4 = new RectangleConstraint(0.0,
                new Range(0.0, constraint.getWidth() - w[2]),
                LengthConstraintType.RANGE, totalLeftRightHeight, null,
                LengthConstraintType.FIXED);
        Size2D sizeRight = this.rightBlock.arrange(g2, c4);
        w[3] = sizeRight.width;
    }
    
    // Calculate the width for center block
    double centerWidth = constraint.getWidth() - w[2] - w[3];

    h[4] = (this.centerBlock != null) ? constraint.getHeight() : 0.0;
    if (this.centerBlock != null) {
        RectangleConstraint c5 = new RectangleConstraint(centerWidth, h[4]);
        this.centerBlock.arrange(g2, c5);
        // Center block width calculation (if necessary)
        w[4] = centerWidth;
    }

    return new Size2D(constraint.getWidth(), constraint.getHeight());
}