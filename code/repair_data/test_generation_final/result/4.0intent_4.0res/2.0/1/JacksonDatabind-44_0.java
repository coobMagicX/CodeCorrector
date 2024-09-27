protected JavaType _narrow(Class<?> subclass) {
    if (_class == subclass || _class.isAssignableFrom(subclass)) {
        return new SimpleType(subclass, _bindings, this, _superInterfaces, _valueHandler, _typeHandler, _asStatic);
    } else {
        throw new IllegalArgumentException("Class " + subclass.getName() + " is not a sub-type of " + _class.getName());
    }
}