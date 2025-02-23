protected Size2D arrangeFF(BlockContainer container, Graphics2D g2, RectangleConstraint constraint) {
    double[] w = new double[5];
    double[] h = new double[5];
    w[0] = constraint.getWidth();
    if (this.topBlock != null) {
        RectangleConstraint c1 = new RectangleConstraint(w[0], null,
                LengthConstraintType.FIXED, 0.0,
                new Range(0.0, Math.max(0.0, constraint.getHeight())),
                LengthConstraintType.RANGE);
        Size2D size = this.topBlock.arrange(g2, c1);
        h[0] = size.height;
    }
    w[1] = w[0];
    if (this.bottomBlock != null) {
        double remainingHeight = constraint.getHeight() - h[0];
        RectangleConstraint c2 = new RectangleConstraint(w[0], null,
                LengthConstraintType.FIXED, 0.0, 
                new Range(0.0, Math.max(0.0, remainingHeight)),
                LengthConstraintType.RANGE);
        Size2D size = this.bottomBlock.arrange(g2, c2);
        h[1] = size.height;
    }
    h[2] = Math.max(0.0, constraint.getHeight() - h[1] - h[0]); 
    if (this.leftBlock != null) {
        RectangleConstraint c3 = new RectangleConstraint(0.0,
                new Range(0.0, Math.max(0.0, constraint.getWidth())),
                LengthConstraintType.RANGE, h[2], null,
                LengthConstraintType.FIXED);
        Size2D size = this.leftBlock.arrange(g2, c3);
        w[2] = size.width;
    }
    h[3] = h[2];
    if (this.rightBlock != null) {
        double remainingWidth = constraint.getWidth() - w[2];
        RectangleConstraint c4 = new RectangleConstraint(0.0,
                new Range(0.0, Math.max(0.0, remainingWidth)),
                LengthConstraintType.RANGE, h[2], null,
                LengthConstraintType.FIXED);
        Size2D size = this.rightBlock.arrange(g2, c4);
        w[3] = size.width;
    }
    h[4] = h[2];
    w[4] = Math.max(0.0, constraint.getWidth() - w[3] - w[2]);
    RectangleConstraint c5 = new RectangleConstraint(w[4], h[4]);
    if (this.centerBlock != null) {
        this.centerBlock.arrange(g2, c5);
    }

    if (this.topBlock != null) {
        this.topBlock.setBounds(new Rectangle2D.Double(0.0, 0.0, w[0], h[0]));
    }
    if (this.bottomBlock != null) {
        this.bottomBlock.setBounds(new Rectangle2D.Double(0.0, h[0] + h[2], w[1], h[1]));
    }
    if (this.leftBlock != null) {
        this.leftBlock.setBounds(new Rectangle2D.Double(0.0, h[0], w[2], h[2]));
    }
    if (this.rightBlock != null) {
        this.rightBlock.setBounds(new Rectangle2D.Double(w[2] + w[4], h[0], w[3], h[3]));
    }
    if (this.centerBlock != null) {
        this.centerBlock.setBounds(new Rectangle2D.Double(w[2], h[0], w[4], h[4]));
    }
    return new Size2D(constraint.getWidth(), constraint.getHeight());
}
