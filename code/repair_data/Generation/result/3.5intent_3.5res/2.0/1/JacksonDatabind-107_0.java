protected final JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
    JsonDeserializer<Object> deser = _deserializers.get(typeId);
    if (deser == null) {
        JavaType type = _idResolver.typeFromId(ctxt, typeId);
        if (type == null) {
            deser = _findDefaultImplDeserializer(ctxt);
            if (deser == null) {
                JavaType actual = _handleUnknownTypeId(ctxt, typeId);
                if (actual == null) {
                    // 17-Jan-2019, tatu: As per [databind#2221], better NOT return `null` but...
                    return _handleUnknownTypeIdFailure(ctxt, typeId);
                }
                deser = ctxt.findContextualValueDeserializer(actual, _property);
            }
        } else {
            if ((_baseType != null) && _baseType.getClass() == type.getClass()) {
                if (!type.hasGenericTypes()) {
                    type = ctxt.getTypeFactory().constructSpecializedType(_baseType, type.getRawClass());
                }
            }
            deser = ctxt.findContextualValueDeserializer(type, _property);
        }
        _deserializers.put(typeId, deser);
    }
    return deser;
}

protected JavaType _handleUnknownTypeIdFailure(DeserializationContext ctxt, String typeId) throws IOException {
    throw ctxt.unknownTypeIdException(_baseType, typeId,
            "Could not resolve type id '"+typeId+"' into a subtype of "+_baseType);
}

protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId) throws IOException {
    DeserializationProblemHandler handler = ctxt.getProblemHandlers().handleUnknownTypeId(ctxt, typeId, _idResolver, _baseType);
    if (handler == null) {
        return null;
    }
    return handler.handleUnknownTypeId(ctxt, typeId, _idResolver, _baseType);
}