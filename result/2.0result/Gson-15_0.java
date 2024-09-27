public JsonWriter value(double value) throws IOException {
    writeDeferredName();
    if (!lenient && (Double.isNaN(value) || Double.isInfinite(value))) {
        throw new IllegalArgumentException("Numeric values must be finite, but was " + value);
    }
    beforeValue();
    if (Double.isNaN(value) || Double.isInfinite(value)) {
        out.append(Double.toString(value));
    } else {
        out.append(Double.toString(value));
    }
    return this;
}