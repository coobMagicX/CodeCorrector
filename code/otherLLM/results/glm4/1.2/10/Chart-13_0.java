protected Size2D arrangeFF(BlockContainer container, Graphics2D g2,
                           RectangleConstraint constraint) {
    double[] w = new double[5];
    double[] h = new double[5];
    w[0] = constraint.getWidth();
    if (this.topBlock != null) {
        RectangleConstraint c1 = new RectangleConstraint(w[0], null,
                LengthConstraintType.FIXED, 0.0, null, LengthConstraintType.FIXED);
        Size2D size = this.topBlock.arrange(g2, c1);
        h[0] = size.getHeight();
    }
    
    if (this.bottomBlock != null) {
        RectangleConstraint c2 = new RectangleConstraint(w[0], null,
                LengthConstraintType.FIXED, 0.0, new Range(h[0], Double.MAX_VALUE), LengthConstraintType.RANGE);
        Size2D size = this.bottomBlock.arrange(g2, c2);
        h[1] = size.getHeight();
    }

    if (this.leftBlock != null) {
        RectangleConstraint c3 = new RectangleConstraint(0.0,
                new Range(0.0, w[0]), LengthConstraintType.RANGE, h[0], null,
                LengthConstraintType.FIXED);
        Size2D size = this.leftBlock.arrange(g2, c3);
        w[2] = size.getWidth();
    }
    
    if (this.rightBlock != null) {
        RectangleConstraint c4 = new RectangleConstraint(0.0,
                new Range(0.0, w[0] - w[2]), LengthConstraintType.RANGE, h[0], null,
                LengthConstraintType.FIXED);
        Size2D size = this.rightBlock.arrange(g2, c4);
        w[3] = size.getWidth();
    }

    h[4] = constraint.getHeight() - (this.topBlock != null ? h[0] : 0) - (this.bottomBlock != null ? h[1] : 0);
    w[4] = constraint.getWidth() - (this.leftBlock != null ? w[2] : 0) - (this.rightBlock != null ? w[3] : 0);

    if (this.centerBlock != null) {
        RectangleConstraint c5 = new RectangleConstraint(w[4], h[4]);
        this.centerBlock.arrange(g2, c5);
        // Adjust center block bounds to reflect the actual space available
        double centerX = (w[2] + w[4]) / 2.0;
        double centerY = constraint.getHeight() / 2.0 - h[4] / 2.0;
        this.centerBlock.setBounds(new Rectangle2D.Double(centerX, centerY, w[4], h[4]));
    }

    // Set bounds for top, bottom, left, right blocks
    if (this.topBlock != null) {
        this.topBlock.setBounds(new Rectangle2D.Double(0.0, 0.0, w[0],
                h[0]));
    }
    if (this.bottomBlock != null) {
        this.bottomBlock.setBounds(new Rectangle2D.Double(0.0, h[0] + (h[4] == 0 ? h[1] : 0), w[1], h[1]));
    }
    if (this.leftBlock != null) {
        this.leftBlock.setBounds(new Rectangle2D.Double(0.0, h[0], w[2],
                h[2]));
    }
    if (this.rightBlock != null) {
        this.rightBlock.setBounds(new Rectangle2D.Double(w[2] + w[4], h[0],
                w[3], h[3]));
    }

    // Update total width and height
    double totalWidth = w[0];
    double totalHeight = constraint.getHeight();
    
    return new Size2D(totalWidth, totalHeight);
}