// This would be part of a larger method that calls drawRotatedString
public void drawAxisLabel(AxisLabel label, Rectangle2D dataArea) {
    // Calculate the text bounds for the label
    TextUtilities.getTextBounds(label.getText(), g2, fm);

    // Determine edge and angle for rotation
    double angle = getLabelAngle();
    if (edge == RectangleEdge.TOP || edge == RectangleEdge.BOTTOM) {
        angle += Math.PI / 2;
    }

    // Create AffineTransform to rotate the text
    AffineTransform t = AffineTransform.getRotateInstance(angle, ...);

    // Compute text position based on current cursor and label size
    Point2D.Double labelPosition = new Point2D.Double(...);
    
    // Draw the rotated string at the computed position
    TextUtilities.drawRotatedString(label.getText(), g2, labelPosition.x, labelPosition.y, angle, ...);

    // Update cursor position for subsequent draws
    state.cursorUp(...);
}