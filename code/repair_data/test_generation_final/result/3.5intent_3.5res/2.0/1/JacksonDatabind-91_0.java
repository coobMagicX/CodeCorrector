protected JsonDeserializer<Object> _createAndCacheValueDeserializer(DeserializationContext ctxt,
        DeserializerFactory factory, JavaType type) throws JsonMappingException {
    synchronized (_incompleteDeserializers) {
        // Ok, then: could it be that due to a race condition, deserializer can now be found?
        JsonDeserializer<Object> deser = _findCachedDeserializer(type);
        if (deser != null) {
            return deser;
        }

        // Check if the deserializer is being resolved right now
        int count = _incompleteDeserializers.size();
        if (count > 0) {
            deser = _incompleteDeserializers.get(type);
            if (deser != null) {
                return deser;
            }
        }

        // Nope: need to create and possibly cache
        try {
            deser = _createAndCache2(ctxt, factory, type);
            if (type.isContainerType()) {
                // Cache the deserializer for both value and key types
                _incompleteDeserializers.put(type, deser);
                JavaType valueType = type.getContentType();
                if (valueType != null) {
                    _incompleteDeserializers.put(valueType, deser);
                }
                JavaType keyType = type.getKeyType();
                if (keyType != null) {
                    _incompleteDeserializers.put(keyType, deser);
                }
            } else {
                // Cache the deserializer for the type
                _incompleteDeserializers.put(type, deser);
            }
            return deser;
        } finally {
            // Also, any deserializers that have been created are complete by now
            if (count == 0 && _incompleteDeserializers.size() > 0) {
                _incompleteDeserializers.clear();
            }
        }
    }
}