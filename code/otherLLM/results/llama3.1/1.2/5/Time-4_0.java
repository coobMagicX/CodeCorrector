public Partial with(DateTimeFieldType fieldType, int value) {
    if (fieldType == null || !fieldType.isSupported()) {
        throw new IllegalArgumentException("The field type must not be null and supported");
    }
    // rest of your code remains the same...
}