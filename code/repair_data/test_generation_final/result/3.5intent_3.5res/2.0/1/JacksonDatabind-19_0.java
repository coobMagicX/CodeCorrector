private JavaType _mapType(Class<?> rawClass) {
    // 28-May-2015, tatu: Properties are special, as per [databind#810]
    JavaType[] typeParams = findTypeParameters(rawClass, Map.class);
    // ok to have no types ("raw")
    if (typeParams == null) {
        return MapType.construct(rawClass, _unknownType(), _unknownType());
    }
    // but exactly 2 types if any found
    if (typeParams.length != 2) {
        throw new IllegalArgumentException("Strange Map type "+rawClass.getName()+": can not determine type parameters");
    }
    // Fix: Convert the type parameters to String if they are not already String
    JavaType keyType = (typeParams[0].getRawClass() == String.class) ? typeParams[0] : _constructType(String.class);
    JavaType valueType = (typeParams[1].getRawClass() == String.class) ? typeParams[1] : _constructType(String.class);
    return MapType.construct(rawClass, keyType, valueType);
}