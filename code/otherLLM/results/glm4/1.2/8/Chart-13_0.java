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
    if (this.bottomBlock != null) {
        RectangleConstraint c2 = new RectangleConstraint(w[0], null,
                LengthConstraintType.FIXED, 0.0, new Range(0.0,
                constraint.getHeight() - (h[0] == Double.NaN ? 0 : h[0])),
                LengthConstraintType.RANGE);
        Size2D size = this.bottomBlock.arrange(g2, c2);
        h[1] = size.height;
    }
    h[2] = constraint.getHeight() - (h[0] == Double.NaN ? 0 : h[0]) - (h[1] == Double.NaN ? 0 : h[1]);
    if (this.leftBlock != null) {
        RectangleConstraint c3 = new RectangleConstraint(0.0,
                new Range(0.0, constraint.getWidth()),
                LengthConstraintType.RANGE, h[2], null,
                LengthConstraintType.FIXED);
        Size2D size = this.leftBlock.arrange(g2, c3);
        w[2] = size.width;
    }
    if (this.rightBlock != null) {
        RectangleConstraint c4 = new RectangleConstraint(0.0,
                new Range(0.0, constraint.getWidth() - (w[2] == Double.NaN ? 0 : w[2])),
                LengthConstraintType.RANGE, h[2], null,
                LengthConstraintType.FIXED);
        Size2D size = this.rightBlock.arrange(g2, c4);
        w[3] = size.width;
    }
    if (this.centerBlock != null) {
        double centerWidth = constraint.getWidth() - (w[2] == Double.NaN ? 0 : w[2]) - (w[3] == Double.NaN ? 0 : w[3]);
        RectangleConstraint c5 = new RectangleConstraint(centerWidth / 2,
                new Range(0.0, centerWidth),
                LengthConstraintType.FIXED, h[2], null,
                LengthConstraintType.FIXED);
        Size2D size = this.centerBlock.arrange(g2, c5);
        w[4] = centerWidth;
    }

    if (this.topBlock != null) {
        this.topBlock.setBounds(new Rectangle2D.Double(0.0, 0.0, w[0],
                h[0]));
    }
    if (this.bottomBlock != null) {
        this.bottomBlock.setBounds(new Rectangle2D.Double(0.0, h[0] + h[2],
                w[1], h[1]));
    }
    if (this.leftBlock != null) {
        this.leftBlock.setBounds(new Rectangle2D.Double(0.0, h[0], w[2],
                h[2]));
    }
    if (this.rightBlock != null) {
        this.rightBlock.setBounds(new Rectangle2D.Double(w[2] + w[4], h[0],
                w[3], h[3]));
    }
    if (this.centerBlock != null) {
        this.centerBlock.setBounds(new Rectangle2D.Double(w[2], h[0], w[4],
                h[4]));
    }

    // Ensure that the width and height returned are correct after setting bounds
    double totalWidth = 0.0;
    double totalHeight = 0.0;

    if (this.topBlock != null) {
        totalWidth += this.topBlock.getBounds().getWidth();
        totalHeight += this.topBlock.getBounds().getHeight();
    }
    if (this.bottomBlock != null) {
        totalWidth += this.bottomBlock.getBounds().getWidth();
        totalHeight += this.bottomBlock.getBounds().getHeight();
    }
    if (this.leftBlock != null) {
        totalWidth += this.leftBlock.getBounds().getWidth();
        totalHeight += this.leftBlock.getBounds().getHeight();
    }
    if (this.rightBlock != null) {
        totalWidth += this.rightBlock.getBounds().getWidth();
        totalHeight += this.rightBlock.getBounds().getHeight();
    }
    if (this.centerBlock != null) {
        totalWidth += this.centerBlock.getBounds().getWidth();
        totalHeight += this.centerBlock.getBounds().getHeight();
    }

    return new Size2D(totalWidth, totalHeight);
}