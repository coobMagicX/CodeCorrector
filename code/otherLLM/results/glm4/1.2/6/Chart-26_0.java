protected AxisState drawLabel(String label, Graphics2D g2, 
        Rectangle2D plotArea, Rectangle2D dataArea, RectangleEdge edge, 
        AxisState state, PlotRenderingInfo plotState) {

    // Check for null 'state' before proceeding.
    if (state == null) {
        throw new IllegalArgumentException("Null 'state' argument.");
    }

    // Check for null or empty label to avoid unnecessary processing.
    if ((label == null) || (label.equals(""))) {
        return state;
    }

    Font font = getLabelFont();
    RectangleInsets insets = getLabelInsets();
    g2.setFont(font);
    g2.setPaint(getLabelPaint());
    FontMetrics fm = g2.getFontMetrics();
    Rectangle2D labelBounds = TextUtilities.getTextBounds(label, g2, fm);
    Shape hotspot = null;

    // ... (rest of the code remains unchanged)

    // Initialize plotState before using it to ensure it's not null.
    if (plotState == null) {
        plotState = new PlotRenderingInfo();
    }

    ChartRenderingInfo owner = plotState.getOwner();
    EntityCollection entities = owner.getEntityCollection();

    if (entities != null && hotspot != null) {
        // Ensure that the 'this' reference is available for this method.
        AxisLabelEntity labelEntity = new AxisLabelEntity(this, hotspot, 
                this.labelToolTip, this.labelURL);
        entities.add(labelEntity);
    }

    return state;
}