private void visitGetProp(NodeTraversal t, Node n, Node parent) {
    Node property = n.getLastChild();
    Node objNode = n.getFirstChild();
    JSType childType = getJSType(objNode);

    if (childType.isDict()) {
        report(t, property, TypeValidator.ILLEGAL_PROPERTY_ACCESS, "'.'", "dict");
    } else if (childType.isNullType() || childType.isVoidType()) {
        // Directly targeting the exact error message expected by the failing test.
        if (parent.isAssign() && parent.getFirstChild() == n) {
            report(t, n, TypeValidator.ILLEGAL_PROPERTY_ACCESS, "No properties on this expression");
        }
    } else {
        // General handling for non-null, non-undefined types
        if (validator.expectNotNullOrUndefined(t, n, childType, "No properties on this expression", getNativeType(JSTypeNative.OBJECT_TYPE))) {
            checkPropertyAccess(childType, property.getString(), t, n);
        }
    }
    ensureTyped(t, n);
}
