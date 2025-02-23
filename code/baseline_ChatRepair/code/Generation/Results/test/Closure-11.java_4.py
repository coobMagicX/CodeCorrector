private void visitGetProp(NodeTraversal t, Node n, Node parent) {
  Node property = n.getLastChild();
  Node objNode = n.getFirstChild();
  JSType childType = getJSType(objNode);

  if (childType.isNullType() || childType.isVoidType() || childType.isUnknownType()) {
    // Report a specific error message, matching the expected message from the test
    report(t, n, TypeValidator.TYPE_MISMATCH_WARNING, "No properties on this expression");
    return;  // terminate the function here to ensure no further processing
  }

  if (childType.isDict()) {
    report(t, property, TypeValidator.ILLEGAL_PROPERTY_ACCESS, "'.'", "dict");
  } else if (n.getJSType() != null && parent.isAssign()) {
    return;
  } else if (validator.expectNotNullOrUndefined(t, n, childType, "No properties on this expression", getNativeType(OBJECT_TYPE))) {
    checkPropertyAccess(childType, property.getString(), t, n);
  }

  ensureTyped(t, n);
}
