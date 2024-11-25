public ValueMarker(double value, Paint paint, Stroke stroke, 
                   Paint outlinePaint, Stroke outlineStroke, float alpha) {
    super(paint, stroke, outlinePaint, outlineStroke, alpha); // corrected superclass constructor call
    setValue(value); // use existing method to set value property
}