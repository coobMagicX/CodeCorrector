protected JavaType _narrow(Class<?> subclass) {
    if (_class == subclass) {
        return this;
    }
    
    JavaType refinedType = refine(subclass, _bindings, _superClass, _superInterfaces);
    if (refinedType != null) {
        return refinedType;
    }
    
    throw new IllegalArgumentException("Class " + subclass.getName() + " not sub-type of " + _class.getName());
}