protected AxisState drawLabel(String label, Graphics2D g2, 
        Rectangle2D plotArea, Rectangle2D dataArea, RectangleEdge edge, 
        AxisState state, PlotRenderingInfo plotState) {

    // Check for null 'state' before proceeding
    if (state == null) {
        throw new IllegalArgumentException("Null 'state' argument.");
    }
    
    // Check for null or empty label before proceeding
    if (label == null || label.isEmpty()) {
        return state;
    }

    Font font = getLabelFont();
    RectangleInsets insets = getLabelInsets();
    g2.setFont(font);
    g2.setPaint(getLabelPaint());
    FontMetrics fm = g2.getFontMetrics();
    Rectangle2D labelBounds = TextUtilities.getTextBounds(label, g2, fm);
    Shape hotspot = null;
    
    if (edge == RectangleEdge.TOP) {
        // ... existing code for TOP edge ...
    }
    else if (edge == RectangleEdge.BOTTOM) {
        // ... existing code for BOTTOM edge ...
    }
    else if (edge == RectangleEdge.LEFT) {
        // ... existing code for LEFT edge ...
    }
    else if (edge == RectangleEdge.RIGHT) {
        // ... existing code for RIGHT edge ...
    }

    // Check 'plotState' and 'hotspot' before accessing methods on them
    if (plotState != null && hotspot != null) {
        ChartRenderingInfo owner = plotState.getOwner();
        EntityCollection entities = owner.getEntityCollection();
        if (entities != null) {
            entities.add(new AxisLabelEntity(this, hotspot, this.labelToolTip, this.labelURL));
        }
    }

    return state;

}