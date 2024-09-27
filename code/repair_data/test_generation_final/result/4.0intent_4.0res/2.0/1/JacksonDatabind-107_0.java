protected final JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt,
String typeId) throws IOException {
    JsonDeserializer<Object> deser = _deserializers.get(typeId);
    if (deser == null) {
        JavaType type = _idResolver.typeFromId(ctxt, typeId);
        if (type == null) {
            deser = _findDefaultImplDeserializer(ctxt);
            if (deser == null) {
                JavaType actual = _handleUnknownTypeId(ctxt, typeId);
                if (actual == null) {
                    throw new JsonMappingException(ctxt, "Type id '" + typeId + "' could not be resolved and no default deserializer was found.");
                }
                deser = ctxt.findContextualValueDeserializer(actual, _property);
                if (deser == null) {
                    throw new JsonMappingException(ctxt, "Could not find deserializer for resolved unknown type id: '" + typeId + "'");
                }
            }
        } else {
            if ((_baseType != null) && _baseType.getClass() == type.getClass()) {
                if (!type.hasGenericTypes()) {
                    type = ctxt.getTypeFactory().constructSpecializedType(_baseType, type.getRawClass());
                }
            }
            deser = ctxt.findContextualValueDeserializer(type, _property);
        }
        if (deser != null) {
            _deserializers.put(typeId, deser);
        }
    }
    return deser;
}