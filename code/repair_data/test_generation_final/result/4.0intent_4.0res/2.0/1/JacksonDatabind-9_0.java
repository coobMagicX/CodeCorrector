public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
    String str;

    if (value instanceof Date) {
        provider.defaultSerializeDateKey((Date) value, jgen);
        return;
    } else if (value instanceof Class) {
        str = ((Class<?>) value).getName();
    } else if (value instanceof InetAddress) {
        str = ((InetAddress) value).getHostAddress();
    } else {
        str = value.toString();
    }
    jgen.writeFieldName(str);
}