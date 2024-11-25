public ValueMarker(double value, Paint paint, Stroke stroke, 
                   Paint outlinePaint, Stroke outlineStroke, float alpha) {
    super(paint, stroke, outlinePaint, outlineStroke, alpha); // Corrected the second and third parameters to match outline paint/stroke
    this.value = value;
    this.outlinePaint = outlinePaint; // Assigning outlinePaint to a member variable
}