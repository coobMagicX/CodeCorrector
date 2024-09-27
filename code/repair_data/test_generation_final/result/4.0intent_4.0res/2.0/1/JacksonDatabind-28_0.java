public ObjectNode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    if (p.getCurrentToken() == JsonToken.START_OBJECT) {
        p.nextToken();
        if (p.getCurrentToken() == JsonToken.END_OBJECT) {
            return ctxt.getNodeFactory().objectNode(); // Handle empty JSON object
        }
        return deserializeObject(p, ctxt, ctxt.getNodeFactory());
    }
    if (p.getCurrentToken() == JsonToken.END_OBJECT) {
        return ctxt.getNodeFactory().objectNode(); // Handle empty JSON object correctly when first token is END_OBJECT
    }
    throw ctxt.mappingException(ObjectNode.class);
}