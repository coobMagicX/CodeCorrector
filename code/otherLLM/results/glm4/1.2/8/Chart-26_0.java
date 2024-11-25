protected AxisState drawLabel(String label, Graphics2D g2, 
        Rectangle2D plotArea, Rectangle2D dataArea, RectangleEdge edge, 
        AxisState state, PlotRenderingInfo plotState) {

    // Check for null 'state' argument.
    if (state == null) {
        throw new IllegalArgumentException("Null 'state' argument.");
    }
    
    // Check for null or empty label and handle accordingly.
    if (label == null || label.isEmpty()) {
        return state;
    }

    Font font = getLabelFont();
    RectangleInsets insets = getLabelInsets();
    if (font != null && g2 != null) { // Ensure 'g2' is not null before setting font
        g2.setFont(font);
        g2.setPaint(getLabelPaint());
    }
    
    FontMetrics fm;
    if (g2 != null && font != null) { // Ensure 'g2' and 'font' are not null before getting metrics
        fm = g2.getFontMetrics();
        Rectangle2D labelBounds = TextUtilities.getTextBounds(label, g2, fm);
        Shape hotspot = null;
        
        if (edge == RectangleEdge.TOP || edge == RectangleEdge.BOTTOM) {
            AffineTransform t = AffineTransform.getRotateInstance(
                    getLabelAngle(), labelBounds.getCenterX(), 
                    labelBounds.getCenterY());
            Shape rotatedLabelBounds = t.createTransformedShape(labelBounds);
            labelBounds = rotatedLabelBounds.getBounds2D();
        }
        
        // The rest of the code to draw the label based on edge...
        // Make sure that all usages of variables like g2, font, etc., are inside the conditional checks
    }

    if (plotState != null && hotspot != null) {
        ChartRenderingInfo owner = plotState.getOwner();
            EntityCollection entities;
            if (owner != null && (entities = owner.getEntityCollection()) != null) { // Check for null 'owner' and 'entities'
                entities.add(new AxisLabelEntity(this, hotspot, 
                        this.labelToolTip, this.labelURL));
            }
    }
    
    return state;

}