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
    h[2] = constraint.getHeight() - h[1] - h[0];
    if (this.leftBlock != null) {
        RectangleConstraint c3 = new RectangleConstraint(0.0,
                new Range(0.0, constraint.getWidth()),
                LengthConstraintType.RANGE, h[2], null,
                LengthConstraintType.FIXED);
        Size2D size = this.leftBlock.arrange(g2, c3);
        w[2] = size.width;
    }
    h[3] = h[2];
    if (this.rightBlock != null) {
        RectangleConstraint c4 = new RectangleConstraint(0.0,
                new Range(0.0, constraint.getWidth() - w[2]),
                LengthConstraintType.RANGE, h[2], null,
                LengthConstraintType.FIXED);
        Size2D size = this.rightBlock.arrange(g2, c4);
        w[3] = size.width;
    }
    h[4] = h[2];
    w[4] = constraint.getWidth() - w[3] - w[2];

    // Add the following code to arrange items in a horizontal line
    int x = 0;
    int y = 0;

    for (Item item : container.getItems()) {
        if (x + item.getWidth() > constraint.getMaxWidth()) {
            x = 0; // Reset x position
            y += item.getHeight(); // Move to the next line
        }
        RectangleConstraint c5 = new RectangleConstraint(x, y,
                LengthConstraintType.FIXED, 0.0,
                new Range(0.0, constraint.getHeight()),
                LengthConstraintType.RANGE);
        Size2D size = item.arrange(g2, c5);
        x += size.getWidth();
    }

    if (this.centerBlock != null) {
        RectangleConstraint c6 = new RectangleConstraint(w[2], h[0],
                LengthConstraintType.FIXED, 0.0,
                new Range(0.0, constraint.getHeight()),
                LengthConstraintType.RANGE);
        Size2D size = this.centerBlock.arrange(g2, c6);
    }

    if (this.rightBlock != null) {
        RectangleConstraint c7 = new RectangleConstraint(w[2] + w[4], h[0],
                LengthConstraintType.FIXED, 0.0,
                new Range(0.0, constraint.getHeight()),
                LengthConstraintType.RANGE);
        Size2D size = this.rightBlock.arrange(g2, c7);
    }

    if (this.leftBlock != null) {
        RectangleConstraint c8 = new RectangleConstraint(w[4], h[0],
                LengthConstraintType.FIXED, 0.0,
                new Range(0.0, constraint.getHeight()),
                LengthConstraintType.RANGE);
        Size2D size = this.leftBlock.arrange(g2, c8);
    }

    return new Size2D(constraint.getWidth(), constraint.getHeight());
}