private void handleTypeCycle(ErrorReporter t) {
    setReferencedType(
            registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE));
    t.warning("Cycle detected in inheritance chain of type " + reference.getName(),
            sourceName, lineno, charno);
    setResolvedTypeInternal(getReferencedType());
}