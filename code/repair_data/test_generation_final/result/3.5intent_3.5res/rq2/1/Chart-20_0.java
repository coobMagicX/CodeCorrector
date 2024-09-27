public ValueMarker(double value, Paint paint, Stroke stroke, 
                   Paint outlinePaint, Stroke outlineStroke, float alpha) {
    super(paint, stroke, outlinePaint, outlineStroke, alpha);
    setValue(value);
}

public void setValue(double value) { 
    this.value = value;
    notifyListeners(new MarkerChangeEvent(this));
}