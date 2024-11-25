protected Size2D arrangeFF(BlockContainer container, Graphics2D g2,
                           RectangleConstraint constraint) {
    double[] w = new double[5];
    double[] h = new double[5];
    w[0] = constraint.getWidth();
    
    if (this.topBlock != null) {
        RectangleConstraint c1 = this.topBlock.toContentConstraint(constraint);
        Size2D size = this.topBlock.arrange(container, g2, c1);
        h[0] = size.height;
    }
    w[1] = constraint.getWidth();
    
    if (this.bottomBlock != null) {
        RectangleConstraint c2 = this.bottomBlock.toContentConstraint(constraint);
        Size2D size = this.bottomBlock.arrange(container, g2, c2);
        h[1] = size.height;
    }
    h[2] = constraint.getHeight() - h[0] - h[1];
    
    if (this.leftBlock != null) {
        RectangleConstraint c3 = this.leftBlock.toContentConstraint(constraint);
        Size2D size = this.leftBlock.arrange(container, g2, c3);
        w[2] = size.width;
    }
    h[3] = h[2];
    
    if (this.rightBlock != null) {
        RectangleConstraint c4 = this.rightBlock.toContentConstraint(constraint);
        Size2D size = this.rightBlock.arrange(container, g2, c4);
        w[3] = size.width;
    }
    w[4] = constraint.getWidth() - w[3] - w[2];
    
    h[4] = h[2];
    
    if (this.centerBlock != null) {
        RectangleConstraint c5 = this.centerBlock.toContentConstraint(constraint);
        Size2D size = this.centerBlock.arrange(container, g2, c5);
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
    
    return new Size2D(constraint.getWidth(), constraint.getHeight());
}