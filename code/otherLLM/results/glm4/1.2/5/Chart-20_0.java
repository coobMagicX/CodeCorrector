public ValueMarker(double value, Paint paint, Stroke stroke, 
                   Paint outlinePaint, Stroke outlineStroke, float alpha) {
    super(paint, stroke, paint, stroke, alpha); // Assuming super class constructor does not need to be changed.
    this.value = value;
    this.outlinePaint = outlinePaint; // Set the correct outline paint directly in the constructor.
}