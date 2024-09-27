public ValueMarker(double value, Paint paint, Stroke stroke, 
                   Paint outlinePaint, Stroke outlineStroke, float alpha) {
    super(paint, stroke);
    setOutlinePaint(outlinePaint);
    setOutlineStroke(outlineStroke);
    setAlpha(alpha);
    this.value = value;
}