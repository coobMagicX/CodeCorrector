public ValueMarker(double value, Paint paint, Stroke stroke, 
   Paint outlinePaint, Stroke outlineStroke, float alpha) {
    super(paint, stroke);
    this.setValue(value); // Set the value using setValue method
    this.outlinePaint = outlinePaint;
}