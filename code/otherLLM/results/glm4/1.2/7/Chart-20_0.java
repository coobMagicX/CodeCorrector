public ValueMarker(double value, Paint paint, Stroke stroke,
                   Paint outlinePaint, Stroke outlineStroke, float alpha) {
    super(paint, stroke); // Corrected to use only one instance of paint and stroke for the superclass constructor
    this.value = value;
    
    if (outlinePaint != null && outlineStroke != null) {
        this.outlinePaint = outlinePaint; // Assign outlinePaint only if it's not null
        this.outlineStroke = outlineStroke; // Assign outlineStroke only if it's not null
    } else {
        this.outlinePaint = paint; // Fallback to using the main paint if outlinePaint is null
        this.outlineStroke = stroke; // Fallback to using the main stroke if outlineStroke is null
    }
}