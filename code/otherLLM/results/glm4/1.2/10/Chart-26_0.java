protected AxisState drawLabel(String label, Graphics2D g2,
        Rectangle2D plotArea, Rectangle2D dataArea, RectangleEdge edge,
        AxisState state, PlotRenderingInfo plotState) {

    // it is unlikely that 'state' will be null, but check anyway...
    if (state == null) {
        throw new IllegalArgumentException("Null 'state' argument.");
    }
    
    // Check for null or empty label and skip drawing
    if (label == null || label.isEmpty()) {
        return state;
    }

    Font font = getLabelFont();
    RectangleInsets insets = getLabelInsets();
    g2.setFont(font);
    g2.setPaint(getLabelPaint());
    FontMetrics fm = g2.getFontMetrics();
    Rectangle2D labelBounds = TextUtilities.getTextBounds(label, g2, fm);

    // Initialize hotspot before any operations that might throw exceptions
    Shape hotspot = null;
    
    if (edge == RectangleEdge.TOP || edge == RectangleEdge.BOTTOM) {
        AffineTransform t = AffineTransform.getRotateInstance(
                getLabelAngle(), labelBounds.getCenterX(),
                labelBounds.getCenterY());
        Shape rotatedLabelBounds = t.createTransformedShape(labelBounds);
        labelBounds = rotatedLabelBounds.getBounds2D();
    }

    // Calculate positions based on edge and avoid NullPointerException
    float w = (float) labelBounds.getWidth();
    float h = (float) labelBounds.getHeight();
    float labelx, labely;

    if (edge == RectangleEdge.TOP) {
        labelx = (float) dataArea.getCenterX();
        labely = (float) (state.getCursor() - insets.getBottom() - h / 2.0);
    } else if (edge == RectangleEdge.BOTTOM) {
        labelx = (float) dataArea.getCenterX();
        labely = (float) (state.getCursor() + insets.getTop() + h / 2.0);
    } else if (edge == RectangleEdge.LEFT) {
        labelx = (float) (state.getCursor() - insets.getRight() - w / 2.0);
        labely = (float) dataArea.getCenterY();
    } else if (edge == RectangleEdge.RIGHT) {
        labelx = (float) (state.getCursor() + insets.getLeft() + w / 2.0);
        labely = (float) (dataArea.getY() + dataArea.getHeight() / 2.0);
    }

    // Only draw and add hotspot if it's not null
    if (hotspot != null) {
        TextUtilities.drawRotatedString(label, g2, labelx, labely,
                TextAnchor.CENTER, getLabelAngle(), TextAnchor.CENTER);
        hotspot = new Rectangle2D.Float(labelx - w / 2.0f, labely - h / 2.0f, w, h);
    }

    // Add entity collection if plotState and hotspot are not null
    if (plotState != null && hotspot != null) {
        ChartRenderingInfo owner = plotState.getOwner();
        EntityCollection entities = owner.getEntityCollection();
        if (entities != null) {
            entities.add(new AxisLabelEntity(this, hotspot, this.labelToolTip, this.labelURL));
        }
    }

    return state;
}