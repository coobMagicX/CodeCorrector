protected JavaType _typeFromId(String id, DatabindContext ctxt) throws IOException {
    TypeFactory tf = ctxt.getTypeFactory();
    if (id.indexOf('<') > 0) {
        JavaType t = tf.constructFromCanonical(id);
        if (!tf.constructSpecializedType(_baseType, t.getRawClass()).isSubTypeOf(_baseType)) {
            throw new IllegalArgumentException("Constructed type " + t + " is not a subtype of the base type " + _baseType);
        }
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
    JavaType specializedType = tf.constructSpecializedType(_baseType, cls);
    if (!specializedType.isSubTypeOf(_baseType)) {
        throw new IllegalArgumentException("Specialized type " + specializedType + " is not a subtype of the base type " + _baseType);
    }
    return specializedType;
}