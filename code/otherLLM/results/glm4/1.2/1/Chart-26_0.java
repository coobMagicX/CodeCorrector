protected AxisState drawLabel(String label, Graphics2D g2, 
        Rectangle2D plotArea, Rectangle2D dataArea, RectangleEdge edge, 
        AxisState state, PlotRenderingInfo plotState) {

    // it is unlikely that 'state' will be null, but check anyway...
    if (state == null) {
        throw new IllegalArgumentException("Null 'state' argument.");
    }
    
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
    
    // Initialize plotState reference to avoid NPE
    PlotRenderingInfo tempPlotState = (plotState != null) ? plotState : new PlotRenderingInfo(new StandardEntityCollection());

    if (edge == RectangleEdge.TOP || edge == RectangleEdge.BOTTOM || 
        edge == RectangleEdge.LEFT || edge == RectangleEdge.RIGHT) {
        
        AffineTransform t = AffineTransform.getRotateInstance(
                getLabelAngle(), labelBounds.getCenterX(), 
                labelBounds.getCenterY());
        Shape rotatedLabelBounds = t.createTransformedShape(labelBounds);
        labelBounds = rotatedLabelBounds.getBounds2D();
        float w = (float) labelBounds.getWidth();
        float h = (float) labelBounds.getHeight();

        // Position the label based on the edge
        double labelX, labelY;
        if (edge == RectangleEdge.TOP || edge == RectangleEdge.BOTTOM) {
            labelX = (float) dataArea.getCenterX();
            labelY = (edge == RectangleEdge.TOP ? state.getCursor() - insets.getBottom() 
                                               : state.getCursor() + insets.getTop());
        } else {
            labelX = (edge == RectangleEdge.LEFT ? state.getCursor() - insets.getRight() 
                                               : state.getCursor() + insets.getLeft());
            labelY = (float) dataArea.getCenterY();
        }

        TextUtilities.drawRotatedString(label, g2, labelX, labelY, 
                TextAnchor.CENTER, getLabelAngle(), edge);
        
        hotspot = new Rectangle2D.Float((float)(labelX - w / 2.0f), 
                                       (float)(labelY - h / 2.0f), w, h);

        // Update the cursor position based on the edge
        if (edge == RectangleEdge.TOP || edge == RectangleEdge.BOTTOM) {
            state.cursorUp(insets.getTop() + labelBounds.getHeight() 
                           + insets.getBottom());
        } else {
            state.cursorLeft(insets.getLeft() + labelBounds.getWidth() 
                             + insets.getRight());
        }
    }

    // Add entity collection to the plotState if available
    if (hotspot != null) {
        ChartRenderingInfo owner = tempPlotState.getOwner();
        EntityCollection entities = owner.getEntityCollection();
        if (entities != null) {
            entities.add(new AxisLabelEntity(this, hotspot, 
                    this.labelToolTip, this.labelURL));
        }
    }

    return state;
}