private void visitGetProp(NodeTraversal t, Node n, Node parent) {
  // obj.prop or obj.method()
  // Lots of types can appear on the left, a call to a void function can
  // never be on the left. getPropertyType will decide what is acceptable
  // and what isn't.
  Node property = n.getLastChild();
  Node objNode = n.getFirstChild();
  JSType childType = getJSType(objNode);

  if (childType.isDict()) {
    report(t, property, TypeValidator.ILLEGAL_PROPERTY_ACCESS, "'.'", "dict");
  } else if (validator.expectNotNullOrUndefined(t, n, childType,
      "No properties on this expression", getNativeType(OBJECT_TYPE))) {
    // Check for null expressions before attempting to access properties
    if (objNode == null) {
      report(t, property, TypeValidator.ILLEGAL_PROPERTY_ACCESS, "'.'", "null");
      return;
    }
    checkPropertyAccess(childType, property.getString(), t, n);
  } else {
    // Verify object existence before attempting to access its properties
    report(t, property, TypeValidator.ILLEGAL_PROPERTY_ACCESS, "'.'", "object does not exist");
  }
  ensureTyped(t, n);
}