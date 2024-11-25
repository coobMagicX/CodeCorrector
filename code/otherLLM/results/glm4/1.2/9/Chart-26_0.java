protected AxisState drawLabel(String label, Graphics2D g2, 
        Rectangle2D plotArea, Rectangle2D dataArea, RectangleEdge edge, 
        AxisState state, PlotRenderingInfo plotState) {

    // Check for null 'plotState' and throw an exception if it is null.
    if (plotState == null) {
        throw new IllegalArgumentException("Null 'PlotRenderingInfo' argument.");
    }

    // Check for null or empty 'label' and return the current state if true.
    if ((label == null) || (label.equals(""))) {
        return state;
    }
    
    // Retrieve label font and insets, checking for nulls.
    Font font = getLabelFont();
    RectangleInsets insets = getLabelInsets();
    if (font == null || insets == null) {
        throw new IllegalArgumentException("Null 'font' or 'insets'.");
    }

    g2.setFont(font);
    g2.setPaint(getLabelPaint());
    FontMetrics fm = g2.getFontMetrics();
    Rectangle2D labelBounds = TextUtilities.getTextBounds(label, g2, fm);

    Shape hotspot = null;
    
    // Perform drawing logic for each edge case.
    if (edge == RectangleEdge.TOP) {
        // ... existing code ...
    } else if (edge == RectangleEdge.BOTTOM) {
        // ... existing code ...
    } else if (edge == RectangleEdge.LEFT) {
        // ... existing code ...
    } else if (edge == RectangleEdge.RIGHT) {
        // ... existing code ...
    }
    
    // If 'plotState' and 'hotspot' are not null, add the entity to the collection.
    if (plotState != null && hotspot != null) {
        ChartRenderingInfo owner = plotState.getOwner();
        EntityCollection entities = owner.getEntityCollection();
        if (entities != null) {
            entities.add(new AxisLabelEntity(this, hotspot, 
                    this.labelToolTip, this.labelURL));
        }
    }

    return state;
}