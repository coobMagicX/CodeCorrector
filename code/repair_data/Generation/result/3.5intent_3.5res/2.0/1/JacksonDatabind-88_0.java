protected JavaType _typeFromId(String id, DatabindContext ctxt) throws IOException {
    TypeFactory tf = ctxt.getTypeFactory();
    if (id.indexOf('<') > 0) {
        JavaType t = tf.constructFromCanonical(id);
        return t;
    }

    Class<?> cls;
    try {
        cls = tf.findClass(id);
    } catch (ClassNotFoundException e) {
        if (ctxt instanceof DeserializationContext) {
            DeserializationContext dctxt = (DeserializationContext) ctxt;
            return dctxt.handleUnknownTypeId(_baseType, id, this, "no such class found");
        }
        return null;
    } catch (Exception e) {
        throw new IllegalArgumentException("Invalid type id '" + id + "' (for id type 'Id.class'): " + e.getMessage(), e);
    }

    // Handle nested generic types
    JavaType baseType = tf.constructType(cls);
    if (_baseType.hasRawClass(baseType.getRawClass())) {
        // If the base type and the constructed type have the same raw class,
        // update the base type with the constructed type's generic parameters
        return _baseType.withContentType(baseType.getContentType());
    } else if (_baseType.isContainerType() && _baseType.getKeyType() != null) {
        // If the base type is a container type with a key type, update the base type's key type
        JavaType newKeyType = tf.constructSpecializedType(_baseType.getKeyType(), cls);
        return _baseType.withKeyType(newKeyType);
    } else {
        // Otherwise, construct specialized type as before
        return tf.constructSpecializedType(_baseType, cls);
    }
}