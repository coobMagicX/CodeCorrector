public ObjectNode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    if (p.getCurrentToken() == JsonToken.START_OBJECT) {
        p.nextToken();
        return deserializeObject(p, ctxt, ctxt.getNodeFactory());
    }
    // 23-Sep-2015, tatu: Ugh. We may also be given END_OBJECT (similar to FIELD_NAME),
    //    if caller has advanced to the first token of Object, but for empty Object
    if (p.getCurrentToken() == JsonToken.FIELD_NAME || p.getCurrentToken() == JsonToken.END_OBJECT) {
        return deserializeObject(p, ctxt, ctxt.getNodeFactory());
    }
    throw ctxt.mappingException(ObjectNode.class);
}