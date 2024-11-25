public ValueMarker(double value, Paint paint, Stroke stroke, 
                   Paint outlinePaint, Stroke outlineStroke, float alpha) {
    super(paint, stroke, outlinePaint, outlineStroke, alpha); // Corrected super constructor call
    this.value = value; // Set the 'value' property correctly
}