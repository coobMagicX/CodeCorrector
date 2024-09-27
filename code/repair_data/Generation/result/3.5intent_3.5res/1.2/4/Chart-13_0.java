protected Size2D arrangeFF(BlockContainer container, Graphics2D g2, RectangleConstraint constraint) {
    double[] w = new double[5];
    double[] h = new double[5];
    w[0] = constraint.getWidth();
    if (this.topBlock != null) {
        RectangleConstraint c1 = new RectangleConstraint(w[0], null, LengthConstraintType.FIXED, 0.0, new Range(0.0, constraint.getHeight()), LengthConstraintType.RANGE);
        Size2D size = this.topBlock.arrange(g2, c1);
        h[0] = size.getHeight();
    }
    w[1] = w[0];
    if (this.bottomBlock != null) {
        RectangleConstraint c2 = new RectangleConstraint(w[0], null, LengthConstraintType.FIXED, 0.0, new Range(0.0, constraint.getHeight() - h[0]), LengthConstraintType.RANGE);
        Size2D size = this.bottomBlock.arrange(g2, c2);
        h[1] = size.getHeight();
    }
    h[2] = constraint.getHeight() - h[1] - h[0];
    if (this.leftBlock != null) {
        RectangleConstraint c3 = new RectangleConstraint(0.0, new Range(0.0, constraint.getWidth()), LengthConstraintType.RANGE, h[2], null, LengthConstraintType.FIXED);
        Size2D size = this.leftBlock.arrange(g2, c3);
        w[2] = size.getWidth();
    }
    h[3] = h[2];
    if (this.rightBlock != null) {
        RectangleConstraint c4 = new RectangleConstraint(0.0, new Range(0.0, constraint.getWidth() - w[2]), LengthConstraintType.RANGE, h[2], null, LengthConstraintType.FIXED);
        Size2D size = this.rightBlock.arrange(g2, c4);
        w[3] = size.getWidth();
    }
    h[4] = h[2];
    w[4] = constraint.getWidth() - w[3] - w[2];
    RectangleConstraint c5 = new RectangleConstraint(w[4], h[4]);

    // Calculate the size of the center block
    if (this.centerBlock != null) {
        Size2D centerSize = this.centerBlock.arrange(g2, c5);
        double centerWidth = Math.max(centerSize.getWidth(), constraint.getWidth() - w[2] - w[3]);
        double centerHeight = Math.max(centerSize.getHeight(), constraint.getHeight() - h[0] - h[2] - h[1]);
        w[4] = centerWidth;
        h[4] = centerHeight;
    }

    // Update the width and height of the blocks
    if (this.topBlock != null) {
        w[0] = Math.max(w[0], w[4]);
        h[0] = Math.max(h[0], constraint.getHeight() - h[1] - h[2] - h[4]);
        RectangleConstraint c1 = new RectangleConstraint(w[0], null, LengthConstraintType.FIXED, 0.0, new Range(0.0, h[0]), LengthConstraintType.RANGE);
        Size2D size = this.topBlock.arrange(g2, c1);
        h[0] = size.getHeight();
    }
    if (this.bottomBlock != null) {
        w[1] = Math.max(w[1], w[4]);
        h[1] = Math.max(h[1], constraint.getHeight() - h[0] - h[2] - h[4]);
        RectangleConstraint c2 = new RectangleConstraint(w[1], null, LengthConstraintType.FIXED, 0.0, new Range(0.0, h[1]), LengthConstraintType.RANGE);
        Size2D size = this.bottomBlock.arrange(g2, c2);
        h[1] = size.getHeight();
    }
    if (this.leftBlock != null) {
        h[2] = Math.max(h[2], constraint.getHeight() - h[0] - h[1] - h[4]);
        RectangleConstraint c3 = new RectangleConstraint(0.0, new Range(0.0, w[2]), LengthConstraintType.RANGE, h[2], null, LengthConstraintType.FIXED);
        Size2D size = this.leftBlock.arrange(g2, c3);
        w[2] = size.getWidth();
    }
    if (this.rightBlock != null) {
        h[3] = Math.max(h[3], constraint.getHeight() - h[0] - h[1] - h[4]);
        RectangleConstraint c4 = new RectangleConstraint(0.0, new Range(0.0, w[3]), LengthConstraintType.RANGE, h[3], null, LengthConstraintType.FIXED);
        Size2D size = this.rightBlock.arrange(g2, c4);
        w[3] = size.getWidth();
    }

    // Update the bounds of the blocks
    if (this.topBlock != null) {
        this.topBlock.setBounds(new Rectangle2D.Double(0.0, 0.0, w[0], h[0]));
    }
    if (this.bottomBlock != null) {
        this.bottomBlock.setBounds(new Rectangle2D.Double(0.0, constraint.getHeight() - h[1], w[1], h[1]));
    }
    if (this.leftBlock != null) {
        this.leftBlock.setBounds(new Rectangle2D.Double(0.0, h[0], w[2], h[2]));
    }
    if (this.rightBlock != null) {
        this.rightBlock.setBounds(new Rectangle2D.Double(constraint.getWidth() - w[3], h[0], w[3], h[3]));
    }
    if (this.centerBlock != null) {
        this.centerBlock.setBounds(new Rectangle2D.Double((constraint.getWidth() - w[4]) / 2.0, (constraint.getHeight() - h[4]) / 2.0, w[4], h[4]));
    }

    return new Size2D(constraint.getWidth(), constraint.getHeight());
}