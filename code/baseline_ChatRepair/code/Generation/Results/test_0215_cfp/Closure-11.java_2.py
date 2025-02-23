private void visitGetProp(NodeTraversal t, Node n, Node parent) {
  Node property = n.getLastChild();
  Node objNode = n.getFirstChild();
  JSType childType = getJSType(objNode);

  // First, correctly handle checks for null or undefined to prevent property access
  if (childType.isNullType() || childType.isVoidType()) {
    report(t, property, TypeValidator.ILLEGAL_PROPERTY_ACCESS, "'.'", "null or undefined");
    return;
  }

  // Report illegal property access on dictionaries
  if (childType.isDict()) {
    report(t, property, TypeValidator.ILLEGAL_PROPERTY_ACCESS, "'.'", "dict");
  }

  // Handle cases where assignments are being made (check done before other conditions)
  if (n.getJSType() != null && parent.isAssign()) {
    ensureTyped(t, n);
    return; // Exit after ensuring type correctness
  }

  // Validator to assert the node type is not null or undefined before accessing properties
  if (validator.expectNotNullOrUndefined(t, n, childType, "No properties on this expression", getNativeType(OBJECT_TYPE))) {
    checkPropertyAccess(childType, property.getString(), t, n);
  }

  // Ensure node typing is maintained
  ensureTyped(t, n);
}
