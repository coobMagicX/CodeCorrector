protected Object _deserializeTypedUsingDefaultImpl(JsonParser p, DeserializationContext ctxt, TokenBuffer tb) throws IOException {
    // As per [JACKSON-614], may have default implementation to use
    JsonDeserializer<Object> deser = _findDefaultImplDeserializer(ctxt);
    if (deser != null) {
        if (tb != null) {
            tb.writeEndObject();
            p = tb.asParser(p);
            // must move to point to the first token:
            p.nextToken();
        }
        return deser.deserialize(p, ctxt);
    }
    // or, perhaps we just bumped into a "natural" value (boolean/int/double/String)?
    Object result = TypeDeserializer.deserializeIfNatural(p, ctxt, _baseType);
    if (result != null) {
        return result;
    }
    // or, something for which "as-property" won't work, changed into "wrapper-array" type:
    if (p.getCurrentToken() == JsonToken.START_ARRAY) {
        return super.deserializeTypedFromAny(p, ctxt);
    }
    // check if property value is empty string and deserialize it as null object
    if (p.getCurrentToken() == JsonToken.VALUE_STRING && p.getText().isEmpty()) {
        return null;
    }
    // handle special case for InetAddress deserialization
    if (_baseType.getRawClass() == InetAddress.class) {
        return InetAddress.getByName(p.getText());
    }
    ctxt.reportWrongTokenException(p, JsonToken.FIELD_NAME, "missing property '"+_typePropertyName+"' that is to contain type id  (for class "+baseTypeName()+")");
    return null;
}