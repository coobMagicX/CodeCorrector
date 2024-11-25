protected AxisState drawLabel(String label, Graphics2D g2, 
        Rectangle2D plotArea, Rectangle2D dataArea, RectangleEdge edge, 
        AxisState state, PlotRenderingInfo plotState) {

    // Input validation
    if (label == null || label.equals("")) {
        throw new IllegalArgumentException("Null or empty 'label' argument.");
    }
    if ((state == null) || (plotArea == null) || (dataArea == null)) {
        throw new IllegalArgumentException("Null 'state', 'plotArea', or 'dataArea' arguments.");
    }

    // Edge case handling
    if (!(edge.equals(RectangleEdge.TOP) || edge.equals(RectangleEdge.BOTTOM) 
            || edge.equals(RectangleEdge.LEFT) || edge.equals(RectangleEdge.RIGHT))) {
        throw new IllegalArgumentException("Invalid 'edge' argument. It should be one of TOP, BOTTOM, LEFT, or RIGHT.");
    }

    // State updates
    Font font = getLabelFont();
    RectangleInsets insets = getLabelInsets();
    g2.setFont(font);
    g2.setPaint(getLabelPaint());
    FontMetrics fm = g2.getFontMetrics();

    // Rest of the original code...
}