public TokenBuffer deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    JsonToken t = jp.getCurrentToken();

    // Let's handle field-name separately first
    if (t == JsonToken.FIELD_NAME) {
        if (_mayHaveNativeIds) {
            _checkNativeIds(jp);
        }
        writeFieldName(jp.getCurrentName());
        t = jp.nextToken();
        // fall-through to copy the associated value
    }

    if (_mayHaveNativeIds) {
        _checkNativeIds(jp);
    }

    if (t == JsonToken.FIELD_NAME) {
        writeStartObject();
        writeFieldName(jp.getCurrentName());
        t = jp.nextToken();
    }

    switch (t) {
        case START_ARRAY:
            writeStartArray();
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                copyCurrentStructure(jp);
            }
            writeEndArray();
            break;
        case START_OBJECT:
            writeStartObject();
            while (jp.nextToken() != JsonToken.END_OBJECT) {
                copyCurrentStructure(jp);
            }
            writeEndObject();
            break;
        default: // others are simple:
            copyCurrentEvent(jp);
    }

    // Fix: If the token is END_OBJECT, writeEndObject() should be called
    if (t == JsonToken.END_OBJECT) {
        writeEndObject();
    }

    return this;
}