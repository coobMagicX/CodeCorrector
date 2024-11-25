public ValueMarker(double value, Paint paint, Stroke stroke, 
                   Paint outlinePaint, Stroke outlineStroke, float alpha) {
    super(paint, stroke, outlinePaint, outlineStroke, alpha);
    this.value = value;
    this.outlinePaint = outlinePaint; // Added this line to assign the outlinePaint correctly
}