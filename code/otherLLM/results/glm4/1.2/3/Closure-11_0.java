private void visitGetProp(NodeTraversal t, Node n, Node parent) {
  // obj.prop or obj.method()
  // Lots of types can appear on the left, a call to a void function can
  // never be on the left. getPropertyType will decide what is acceptable
  // and what isn't.
  Node property = n.getLastChild();
  Node objNode = n.getFirstChild();

  JSType childType = getJSType(objNode);
  if (childType == null) {
    report(t, n, TypeValidator.TYPE_ERROR, "Invalid expression", null);
    return;
  }

  // Check if the object is a valid type that can have properties
  if (!isAcceptableAccessType(childType)) {
    report(t, property, TypeValidator.ILLEGAL_PROPERTY_ACCESS, "'.'", "dict");
    return;
  }

  if (n.getJSType() != null && parent.isAssign()) {
    return; // Continue without further checks as the assignment is valid
  } else if (validator.expectNotNullOrUndefined(t, n, childType,
      "No properties on this expression", getNativeType(OBJECT_TYPE))) {
    checkPropertyAccess(childType, property.getString(), t, n);
  }
  
  ensureTyped(t, n); // Ensure that the node is typed after the property access check
}

// Helper method to determine if the type is acceptable for property access
private boolean isAcceptableAccessType(JSType type) {
  return type.isObject() || type.isArray() || type.isString();
}