private void visitGetProp(NodeTraversal t, Node n, Node parent) {
  Node property = n.getLastChild();
  Node objNode = n.getFirstChild();
  JSType childType = getJSType(objNode);

  if (childType.isNullType() || childType.isVoidType()) {
    // Report error when trying to access properties on null or undefined
    report(t, property, TypeValidator.ILLEGAL_PROPERTY_ACCESS, "'.'", childType.toString());
    return; // early return to prevent further evaluations and possible exceptions
  }

  if (childType.isDict()) {
    report(t, property, TypeValidator.ILLEGAL_PROPERTY_ACCESS, "'.'", "dict");
  } else if (n.getJSType() != null && parent.isAssign()) {
    return;
  } else if (validator.expectNotNullOrUndefined(t, n, childType,
      "No properties on this expression", getNativeType(OBJECT_TYPE))) {
    checkPropertyAccess(childType, property.getString(), t, n);
  }
  ensureTyped(t, n);
}
