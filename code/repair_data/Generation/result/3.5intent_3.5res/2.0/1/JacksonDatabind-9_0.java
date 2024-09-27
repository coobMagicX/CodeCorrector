public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
    if (value instanceof Date) {
        provider.defaultSerializeDateKey((Date) value, jgen);
        return;
    }

    // Serialize class key as a string
    String str = value.getClass().getName();
    provider.defaultSerializeValue(str, jgen);
}