public JsonWriter value(double value) throws IOException {
  writeDeferredName();
  if (Double.isNaN(value) || Double.isInfinite(value)) {
    // Handle non-finite values
    if (Double.isNaN(value)) {
      out.append("\"NaN\"");
    } else if (value == Double.POSITIVE_INFINITY) {
      out.append("\"Infinity\"");
    } else if (value == Double.NEGATIVE_INFINITY) {
      out.append("\"-Infinity\"");
    }
  } else {
    beforeValue();
    out.append(Double.toString(value));
  }
  return this;
}