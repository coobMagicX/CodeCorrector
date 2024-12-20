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
        if (size != null && size.width > w[0]) {
            throw new RuntimeException("Invalid width for top block");
        }
        h[0] = size.height;
    }
    else {
        h[0] = 0.0; // default height when no topBlock
    }
    w[1] = w[0];
    if (this.bottomBlock != null) {
        RectangleConstraint c2 = new RectangleConstraint(w[0], null,
                LengthConstraintType.FIXED, 0.0, new Range(0.0,
                constraint.getHeight() - h[0]), LengthConstraintType.RANGE);
        Size2D size = this.bottomBlock.arrange(g2, c2);
        if (size != null && size.width > w[1]) {
            throw new RuntimeException("Invalid width for bottom block");
        }
        h[1] = size.height;
    }
    else {
        h[1] = 0.0; // default height when no bottomBlock
    }
    h[2] = constraint.getHeight() - h[1] - h[0];
    if (this.leftBlock != null) {
        RectangleConstraint c3 = new RectangleConstraint(0.0,
                new Range(0.0, constraint.getWidth()),
                LengthConstraintType.RANGE, h[2], null,
                LengthConstraintType.FIXED);
        Size2D size = this.leftBlock.arrange(g2, c3);
        if (size != null && size.height > h[2]) {
            throw new RuntimeException("Invalid height for left block");
        }
        w[2] = size.width;
    }
    else {
        w[2] = 0.0; // default width when no leftBlock
    }
    if (this.rightBlock != null) {
        RectangleConstraint c4 = new RectangleConstraint(w[2] + w[4], h[0],
                LengthConstraintType.FIXED, h[2], new Range(0.0,
                constraint.getWidth() - w[2] - w[4]), LengthConstraintType.RANGE);
        Size2D size = this.rightBlock.arrange(g2, c4);
        if (size != null && size.height > h[3]) {
            throw new RuntimeException("Invalid height for right block");
        }
        w[3] = size.width;
    }
    else {
        w[3] = 0.0; // default width when no rightBlock
    }
    if (this.centerBlock != null) {
        RectangleConstraint c5 = new RectangleConstraint(w[2], h[0],
                LengthConstraintType.FIXED, h[4], new Range(0.0,
                constraint.getWidth() - w[2] - w[4]), LengthConstraintType.RANGE);
        Size2D size = this.centerBlock.arrange(g2, c5);
        if (size != null && size.height > h[4]) {
            throw new RuntimeException("Invalid height for center block");
        }
        w[4] = size.width;
    }
    else {
        w[4] = 0.0; // default width when no centerBlock
    }

    if (this.topBlock != null) {
        this.topBlock.setBounds(new Rectangle2D.Double(0, 0, w[0], h[0]));
    }
    if (this.bottomBlock != null) {
        this.bottomBlock.setBounds(new Rectangle2D.Double(0, h[0] + h[2],
                w[1], h[1]));
    }
    if (this.leftBlock != null) {
        this.leftBlock.setBounds(new Rectangle2D.Double(0, h[0], w[2], h[2]));
    }
    if (this.rightBlock != null) {
        this.rightBlock.setBounds(new Rectangle2D.Double(w[2] + w[4], h[0],
                w[3], h[3]));
    }
    if (this.centerBlock != null) {
        this.centerBlock.setBounds(new Rectangle2D.Double(w[2], h[0], w[4],
                h[4]));
    }

    return new Size2D(constraint.getWidth(), constraint.getHeight());
}