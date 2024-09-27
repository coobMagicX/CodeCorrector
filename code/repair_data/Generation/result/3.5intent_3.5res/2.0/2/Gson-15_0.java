public JsonWriter value(double value) throws IOException {
  writeDeferredName();
  if (Double.isNaN(value)) {
    out.append("\"NaN\"");
  } else if (Double.isInfinite(value)) {
    if (value < 0) {
      out.append("\"-Infinity\"");
    } else {
      out.append("\"Infinity\"");
    }
  } else if (!lenient) {
    throw new IllegalArgumentException("Numeric values must be finite, but was " + value);
  } else {
    beforeValue();
    out.append(Double.toString(value));
  }
  return this;
}