private void visitGetProp(NodeTraversal t, Node n, Node parent) {
    // obj.prop or obj.method()
    //Lots of types can appear on the left, a call to a void function can
    //never be on the left. getPropertyType will decide what is acceptable
    //and what isn't.
    Node property = n.getLastChild();
    Node objNode = n.getFirstChild();
    JSType childType = getJSType(objNode);

    if (childType == null) {
        report(t, objNode, TypeValidator.NULL_POINTER_ACCESS, "null object", "Cannot access properties on a null object");
    } else if (childType.isDict()) {
        report(t, property, TypeValidator.ILLEGAL_PROPERTY_ACCESS, "'.'", "dict");
    } else if (n.getJSType() != null && parent.isAssign()) {
        return;
    } else if (!validator.expectNotNullOrUndefined(t, n, childType,
            "No properties on this expression", getNativeType(OBJECT_TYPE))) {
        reportMissingProperties(true); // Report missing properties
    }
    checkPropertyAccess(childType, property.getString(), t, n);
    ensureTyped(t, n);
}