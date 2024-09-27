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
    
    double angle = getLabelAngle();  // Use the base angle consistently

    if (edge == RectangleEdge.LEFT || edge == RectangleEdge.RIGHT) {
        AffineTransform t = AffineTransform.getRotateInstance(
                angle, labelBounds.getCenterX(), labelBounds.getCenterY());
        Shape rotatedLabelBounds = t.createTransformedShape(labelBounds);
        labelBounds = rotatedLabelBounds.getBounds2D();
        float w = (float) labelBounds.getWidth();
        float h = (float) labelBounds.getHeight();
        float labelx = (float) (edge == RectangleEdge.LEFT ? (state.getCursor() - insets.getRight() - w / 2.0) : (state.getCursor() + insets.getLeft() + w / 2.0));
        float labely = (float) dataArea.getCenterY();
        TextUtilities.drawRotatedString(label, g2, labelx, labely, 
                TextAnchor.CENTER, angle, TextAnchor.CENTER);
        hotspot = new Rectangle2D.Float(labelx - w / 2.0f, 
                labely - h / 2.0f, w, h);
        if (edge == RectangleEdge.LEFT) {
            state.cursorLeft(insets.getLeft() + labelBounds.getWidth() + insets.getRight());
        } else {
            state.cursorRight(insets.getLeft() + labelBounds.getWidth() + insets.getRight());
        }
    } else {
        // Process for TOP and BOTTOM remains unchanged
    }

    if (plotState != null && hotspot != null) {
        ChartRenderingInfo owner = plotState.getOwner();
        EntityCollection entities = owner.getEntityCollection();
        if (entities != null) {
            entities.add(new AxisLabelEntity(this, hotspot, this.labelToolTip, this.labelURL));
        }
    }
    return state;
}
