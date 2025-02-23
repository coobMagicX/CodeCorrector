private void visitGetProp(NodeTraversal t, Node n, Node parent) {
  Node property = n.getLastChild();
  Node objNode = n.getFirstChild();
  JSType childType = getJSType(objNode);

  if (childType.isDict()) {
    report(t, property, TypeValidator.ILLEGAL_PROPERTY_ACCESS, "'.'", "dict");
  } else if (childType.isNullType() || childType.isVoidType()) {
    // Check for null or undefined usage in property access
    report(t, property, TypeValidator.ILLEGAL_PROPERTY_ACCESS, "'.'", "null or undefined");
  } else if (n.getJSType() != null && parent.isAssign()) {
    // Handle when there's an assignment to the property
    return;
  } else if (validator.expectNotNullOrUndefined(t, n, childType, "No properties on this expression", getNativeType(OBJECT_TYPE))) {
    // Ensures property access only when the expression should not be null or undefined
    checkPropertyAccess(childType, property.getString(), t, n);
  }
  ensureTyped(t, n);
}
