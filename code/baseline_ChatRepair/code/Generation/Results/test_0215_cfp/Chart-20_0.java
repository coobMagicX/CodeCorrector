public ValueMarker(double value, Paint paint, Stroke stroke, 
                   Paint outlinePaint, Stroke outlineStroke, float alpha) {
    super(paint, stroke, outlinePaint == null ? paint : outlinePaint, 
          outlineStroke == null ? stroke : outlineStroke, alpha);
    this.value = value;
}
