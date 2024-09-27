public TokenBuffer deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    // Ensure the parser starts with an object (START_OBJECT)
    if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
        writeStartObject(); // Manually insert START_OBJECT if missing
        copyCurrentStructure(jp);
        writeEndObject(); // Manually insert END_OBJECT to balance the structure
    } else {
        copyCurrentStructure(jp);
    }
    return this;
}