protected Size2D arrangeFF(BlockContainer container, Graphics2D g2,
                           RectangleConstraint constraint) {
    double width = constraint.getWidth();
    double height = constraint.getHeight();

    // Calculate dimensions for top block
    if (this.topBlock != null) {
        RectangleConstraint c1 = new RectangleConstraint(width, null,
                LengthConstraintType.FIXED, 0.0,
                new Range(0.0, height),
                LengthConstraintType.RANGE);
        Size2D size = this.topBlock.arrange(g2, c1);
        height -= size.height; // Update the remaining height
    }

    // Calculate dimensions for bottom block
    if (this.bottomBlock != null) {
        RectangleConstraint c2 = new RectangleConstraint(width, null,
                LengthConstraintType.FIXED, 0.0, new Range(0.0, height),
                LengthConstraintType.RANGE);
        Size2D size = this.bottomBlock.arrange(g2, c2);
        height -= size.height; // Update the remaining height
    }

    // Calculate dimensions for left block
    if (this.leftBlock != null) {
        RectangleConstraint c3 = new RectangleConstraint(null, height,
                LengthConstraintType.FIXED, 0.0, width,
                LengthConstraintType.RANGE);
        Size2D size = this.leftBlock.arrange(g2, c3);
        width -= size.width; // Update the remaining width
    }

    // Calculate dimensions for right block
    if (this.rightBlock != null) {
        RectangleConstraint c4 = new RectangleConstraint(width - size.width,
                height, LengthConstraintType.FIXED, 0.0, null,
                LengthConstraintType.RANGE);
        Size2D size = this.rightBlock.arrange(g2, c4);
        width -= size.width; // Update the remaining width
    }

    // Calculate dimensions for center block
    if (this.centerBlock != null) {
        RectangleConstraint c5 = new RectangleConstraint(width - size.width,
                height - size.height, LengthConstraintType.FIXED, 0.0, null,
                LengthConstraintType.RANGE);
        this.centerBlock.arrange(g2, c5);
    }

    // Set the bounds for all blocks
    if (this.topBlock != null) {
        this.topBlock.setBounds(new Rectangle2D.Double(0.0, 0.0, width,
                this.topBlock.arrange(g2, new RectangleConstraint(width, null,
                        LengthConstraintType.FIXED, 0.0, null,
                        LengthConstraintType.RANGE)).height));
    }
    if (this.bottomBlock != null) {
        this.bottomBlock.setBounds(new Rectangle2D.Double(0.0, height - this.bottomBlock.arrange(g2, new RectangleConstraint(width, null,
                LengthConstraintType.FIXED, 0.0, null,
                LengthConstraintType.RANGE)).height,
                width, this.bottomBlock.arrange(g2, new RectangleConstraint(width, null,
                        LengthConstraintType.FIXED, 0.0, null,
                        LengthConstraintType.RANGE)).height));
    }
    if (this.leftBlock != null) {
        this.leftBlock.setBounds(new Rectangle2D.Double(0.0, height - size.height, size.width, size.height));
    }
    if (this.rightBlock != null) {
        this.rightBlock.setBounds(new Rectangle2D.Double(width - size.width, height - size.height, size.width, size.height));
    }
    if (this.centerBlock != null) {
        // Assuming the center block fills the remaining area
        this.centerBlock.setBounds(new Rectangle2D.Double(size.width, height - size.height, width - 2 * size.width, size.height));
    }

    return new Size2D(width, height);
}