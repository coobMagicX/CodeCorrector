protected JavaType _narrow(Class<?> subclass) {
    if (_class == subclass) {
        return this;
    }
    // Reintroducing the subclass check to ensure type safety during deserialization
    if (!_class.isAssignableFrom(subclass)) {
        throw new IllegalArgumentException("Class " + subclass.getName() + " not sub-type of " + _class.getName());
    }
    return new SimpleType(subclass, _bindings, _superClass, _superInterfaces, _valueHandler, _typeHandler, _asStatic);
}