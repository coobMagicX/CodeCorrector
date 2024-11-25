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
        Size2D size = this.topBlock.arrange(g2, c1);
        h[0] = size.height;
    }
    w[1] = w[0];
    if (this.bottomBlock != null) {
        RectangleConstraint c2 = new RectangleConstraint(w[0], null,
                LengthConstraintType.FIXED, 0.0, new Range(0.0,
                constraint.getHeight() - h[0]), LengthConstraintType.RANGE);
        Size2D size = this.bottomBlock.arrange(g2, c2);
        h[1] = size.height;
    }
    h[2] = Math.min(constraint.getHeight(), h[0] + h[1]);
    if (this.leftBlock != null) {
        RectangleConstraint c3 = new RectangleConstraint(0.0,
                new Range(0.0, constraint.getWidth()),
                LengthConstraintType.RANGE, h[2], null,
                LengthConstraintType.FIXED);
        Size2D size = this.leftBlock.arrange(g2, c3);
        w[2] = Math.min(constraint.getWidth(), size.width);
    }
    h[3] = h[2];
    if (this.rightBlock != null) {
        RectangleConstraint c4 = new RectangleConstraint(0.0,
                new Range(0.0, constraint.getWidth()),
                LengthConstraintType.RANGE, h[3], null,
                LengthConstraintType.FIXED);
        Size2D size = this.rightBlock.arrange(g2, c4);
        w[3] = Math.min(constraint.getWidth(), size.width);
    }
    w[4] = w[0] - w[2] - w[3];
    if (this.centerBlock != null) {
        RectangleConstraint c5 = new RectangleConstraint(w[2], 0.0,
                LengthConstraintType.FIXED, h[2], Math.min(h[2], constraint.getHeight()),
                LengthConstraintType.RANGE);
        this.centerBlock.arrange(g2, c5);
    }

    if (this.topBlock != null) {
        this.topBlock.setBounds(new Rectangle2D.Double(0.0, 0.0, w[0],
                h[0]));
    }
    if (this.bottomBlock != null) {
        this.bottomBlock.setBounds(new Rectangle2D.Double(0.0, h[0] + Math.min(h[2], constraint.getHeight() - h[0]), w[1], Math.min(h[1], constraint.getHeight() - (h[0] + Math.min(h[2], constraint.getHeight() - h[0])))));
    }
    if (this.leftBlock != null) {
        this.leftBlock.setBounds(new Rectangle2D.Double(0.0, 0.0, w[2],
                Math.min(h[2], constraint.getHeight())));
    }
    if (this.rightBlock != null) {
        this.rightBlock.setBounds(new Rectangle2D.Double(w[2] + w[4], 0.0, w[3], Math.min(h[3], constraint.getHeight())));
    }
    if (this.centerBlock != null) {
        this.centerBlock.setBounds(new Rectangle2D.Double(w[2], h[0], w[4],
                Math.min(h[2], constraint.getHeight() - h[0])));
    }
    return new Size2D(constraint.getWidth(), Math.max(Math.min(h[0] + Math.min(h[2], constraint.getHeight() - h[0]) + Math.min(h[3], constraint.getHeight() - (h[0] + Math.min(h[2], constraint.getHeight() - h[0]))), constraint.getHeight()), container.calculateTotalHeight(Math.min(h[0] + Math.min(h[2], constraint.getHeight() - h[0]), constraint.getHeight()))));
}