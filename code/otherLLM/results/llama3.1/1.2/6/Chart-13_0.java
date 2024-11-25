protected Size arrangeFF(BlockContainer container, Graphics2D g2,
                         RectangleConstraint constraint) {
    double[] w = new double[5];
    double[] h = new double[5];

    // Calculate width and height of each block
    if (this.topBlock != null) {
        RectangleConstraint c1 = new RectangleConstraint(constraint.getWidth(), 
                LengthConstraintType.FIXED, 0.0, new Range(0.0, constraint.getHeight()), 
                LengthConstraintType.RANGE);
        Size2D size = this.topBlock.arrange(g2, c1);
        h[0] = size.height;
    }

    if (this.bottomBlock != null) {
        RectangleConstraint c2 = new RectangleConstraint(constraint.getWidth(), 
                LengthConstraintType.FIXED, 0.0, new Range(0.0, constraint.getHeight() - h[0]), 
                LengthConstraintType.RANGE);
        Size2D size = this.bottomBlock.arrange(g2, c2);
        h[1] = size.height;
    }

    h[2] = constraint.getHeight() - h[0] - h[1];
    if (this.leftBlock != null) {
        RectangleConstraint c3 = new RectangleConstraint(LengthConstraintType.RANGE, 
                0.0, new Range(0.0, constraint.getWidth()), LengthConstraintType.FIXED, h[2], 
                LengthConstraintType.RANGE);
        Size2D size = this.leftBlock.arrange(g2, c3);
        w[2] = size.width;
    }

    if (this.rightBlock != null) {
        RectangleConstraint c4 = new RectangleConstraint(LengthConstraintType.RANGE, 
                0.0, new Range(0.0, constraint.getWidth() - w[2]), LengthConstraintType.FIXED, h[2], 
                LengthConstraintType.RANGE);
        Size2D size = this.rightBlock.arrange(g2, c4);
        w[3] = size.width;
    }

    w[4] = constraint.getWidth() - w[2] - w[3];
    RectangleConstraint c5 = new RectangleConstraint(w[4], h[2]);
    if (this.centerBlock != null) {
        this.centerBlock.arrange(g2, c5);
    }

    // Set bounds of each block
    if (this.topBlock != null) {
        this.topBlock.setBounds(new Rectangle2D.Double(0.0, 0.0, constraint.getWidth(), h[0]));
    }
    if (this.bottomBlock != null) {
        this.bottomBlock.setBounds(new Rectangle2D.Double(0.0, h[0] + h[1], 
                constraint.getWidth(), h[1]));
    }
    if (this.leftBlock != null) {
        this.leftBlock.setBounds(new Rectangle2D.Double(0.0, 0.0, w[2], h[2]));
    }
    if (this.rightBlock != null) {
        this.rightBlock.setBounds(new Rectangle2D.Double(constraint.getWidth() - w[3], 0.0, 
                w[3], h[2]));
    }
    if (this.centerBlock != null) {
        this.centerBlock.setBounds(new Rectangle2D.Double(w[2], 0.0, w[4], h[2]));
    }

    return new Size(constraint.getWidth(), constraint.getHeight());
}