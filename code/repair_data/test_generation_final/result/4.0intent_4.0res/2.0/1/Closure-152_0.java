JSType resolveInternal(ErrorReporter t, StaticScope<JSType> scope) {
    setResolvedTypeInternal(this);

    call = (ArrowType) safeResolve(call, t, scope);
    prototype = (FunctionPrototypeType) safeResolve(prototype, t, scope);

    // Resolve typeOfThis and check if it is an object type
    JSType resolvedTypeOfThis = safeResolve(typeOfThis, t, scope);
    if (resolvedTypeOfThis.isNoObjectType()) {
        t.report(JSError.make("Type of 'this' must resolve to an object type when using typedefs."));
        typeOfThis = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    } else {
        typeOfThis = (ObjectType) resolvedTypeOfThis;
    }

    boolean changed = false;
    ImmutableList.Builder<ObjectType> resolvedInterfaces = ImmutableList.builder();
    for (ObjectType iface : implementedInterfaces) {
        ObjectType resolvedIface = (ObjectType) iface.resolve(t, scope);
        resolvedInterfaces.add(resolvedIface);
        changed |= (resolvedIface != iface);
    }
    if (changed) {
        implementedInterfaces = resolvedInterfaces.build();
    }

    if (subTypes != null) {
        for (int i = 0; i < subTypes.size(); i++) {
            subTypes.set(i, (FunctionType) subTypes.get(i).resolve(t, scope));
        }
    }

    return super.resolveInternal(t, scope);
}