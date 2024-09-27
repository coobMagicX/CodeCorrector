public boolean isCachable() {
    /* As per [databind#735], existence of value or key deserializer (only passed
     * if annotated to use non-standard one) should also prevent caching.
     */
    return (_valueTypeDeserializer == null)
            && (_ignorableProperties == null);
}

public Map<Object,Object> deserialize(JsonParser jp, DeserializationContext ctxt,
        Map<Object,Object> result)
        throws IOException, JsonProcessingException
{
    // Ok: must point to START_OBJECT or FIELD_NAME
    JsonToken t = jp.getCurrentToken();
    if (t != JsonToken.START_OBJECT && t != JsonToken.FIELD_NAME) {
        throw ctxt.mappingException(getMapClass());
    }
    if (_standardStringKey) {
        _readAndBindStringMap(jp, ctxt, result);
        return result;
    }
    _readAndBind(jp, ctxt, result);
    return result;
}

public void _readAndBind(JsonParser jp, DeserializationContext ctxt,
        Map<Object,Object> result)
        throws IOException, JsonProcessingException
{
    JsonToken t;
    if ((t = jp.nextToken()) != JsonToken.END_OBJECT) {
        final KeyDeserializer keyDes = _keyDeserializer;
        final JsonDeserializer<Object> valueDes = _valueDeserializer;
        // No much choice: must try to map key/value pairs
        String key1;
        Object value1;
        if (t == JsonToken.FIELD_NAME) {
            key1 = jp.getCurrentName();
            value1 = jp.nextToken();
            // and deserialize value (skip-colon handled by nextFieldName())
            try {
                Object key = keyDes.deserializeKey(key1, ctxt);
                Object value = (value1 == JsonToken.VALUE_NULL) ? null : valueDes.deserialize(jp, ctxt);
                result.put(key, value);
            } catch (UnrecognizedPropertyException e) {
                handleUnrecognizedKey(e, ctxt, result, key1);
            }
        } else {
            throw ctxt.mappingException(getMapClass());
        }
    }
}

public void _readAndBindStringMap(JsonParser jp, DeserializationContext ctxt,
        Map<Object,Object> result)
        throws IOException, JsonProcessingException
{
    JsonToken t;
    if ((t = jp.nextToken()) != JsonToken.END_OBJECT) {
        final JsonDeserializer<Object> valueDes = _valueDeserializer;
        // No much choice: must try to map key/value pairs
        String key1;
        Object value1;
        if (t == JsonToken.FIELD_NAME) {
            key1 = jp.getCurrentName();
            value1 = jp.nextToken();
            // and deserialize value (skip-colon handled by nextFieldName())
            try {
                Object value = (value1 == JsonToken.VALUE_NULL) ? null : valueDes.deserialize(jp, ctxt);
                result.put(key1, value);
            } catch (UnrecognizedPropertyException e) {
                handleUnrecognizedKey(e, ctxt, result, key1);
            }
        } else {
            throw ctxt.mappingException(getMapClass());
        }
    }
}