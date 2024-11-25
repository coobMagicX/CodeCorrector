protected Size2D arrangeFF(BlockContainer container, Graphics2D g2, RectangleConstraint constraint) {
    double[] widths = new double[5];
    double[] heights = new double[5];

    // Calculate the width and height for each block considering spacing and overlaps
    if (this.topBlock != null) {
        RectangleConstraint c1 = new RectangleConstraint(widths[0], null, LengthConstraintType.FIXED, 0.0, new Range(0.0, constraint.getHeight()), LengthConstraintType.RANGE);
        Size2D size = this.topBlock.arrange(g2, c1);
        heights[0] = size.height;
    }
    
    // ... Similar calculations for other blocks

    if (this.centerBlock != null) {
        widths[4] = constraint.getWidth() - widths[2] - widths[3];
        RectangleConstraint c5 = new RectangleConstraint(widths[4], heights[4]);
        this.centerBlock.arrange(g2, c5);
        heights[4] = this.centerBlock.getSize().height; // Update height with the actual arranged size
    }

    // Set bounds for each block using calculated widths and heights
    if (this.topBlock != null) {
        this.topBlock.setBounds(new Rectangle2D.Double(0.0, 0.0, widths[0], heights[0]));
    }
    
    // ... Similar setting of bounds for other blocks

    // Calculate total size of the container
    double totalWidth = widths[0] + widths[1] + widths[2] + widths[3] + widths[4];
    double totalHeight = Math.max(heights[0], heights[1]); // Consider the tallest block

    return new Size2D(totalWidth, totalHeight);
}