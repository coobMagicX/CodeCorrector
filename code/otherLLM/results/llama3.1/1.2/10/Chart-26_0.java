private void drawRotatedString(ChartRenderingInfo plotState,
                                 double x, double y,
                                 TextAnchor textAnchor,
                                 double angle) {
    String label = getText();
    AffineTransform t = getTransform(x, y, angle);
    Shape rotatedLabelBounds = t.createTransformedShape(getTextBounds(label));
    float w = (float) rotatedLabelBounds.getWidth();
    float h = (float) rotatedLabelBounds.getHeight();

    TextUtilities.drawRotatedString(plotState.getGraphics(),
                                      x - w / 2.0f, y - h / 2.0f,
                                      label, textAnchor, angle);
}

private AffineTransform getTransform(double x, double y, double angle) {
    // implementation of the transform calculation
}

// ...