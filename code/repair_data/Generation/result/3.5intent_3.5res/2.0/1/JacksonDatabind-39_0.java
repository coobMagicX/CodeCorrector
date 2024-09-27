public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    // 29-Jan-2016, tatu: Simple skipping for all other tokens, but FIELD_NAME bit
    //    special unfortunately
    switch (p.getCurrentTokenId()) {
        case JsonTokenId.ID_START_ARRAY:
        case JsonTokenId.ID_START_OBJECT:
        case JsonTokenId.ID_FIELD_NAME:
            // Not sure if we need to bother but:
            return null;
        default:
            p.skipChildren();
            throw ctxt.unknownTypeException(p, ctxt.getTypeFactory().constructType(Object.class));
    }
}