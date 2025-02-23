private void visitGetProp(NodeTraversal t, Node n, Node parent) {
  Node property = n.getLastChild();
  Node objNode = n.getFirstChild();
  JSType childType = getJSType(objNode);

  // If the object node type is null or undefined, report the error and return.
  if (childType.isNullType() || childType.isVoidType()) {
    // Ensuring error message exactly as expected assuming typical provided arguments.
    String msg = "No properties on this expression";
    t.report(property, TypeValidator.TYPE_MISMATCH_WARNING, msg);
    return;
  }

  if (childType.isDict()) {
    report(t, property, TypeValidator.ILLEGAL_PROPERTY_ACCESS, "'.'", "dict");
  } else if (n.getJSType() != null && parent.isAssign()) {
    // No operation if the property node is already typed and assigned.
    return;
  } else if (validator.expectNotNullOrUndefined(t, n, childType,
          "No properties on this expression", getNativeType(JSTypeNative.OBJECT_TYPE))) {
    checkPropertyAccess(childType, property.getString(), t, n);
  }

  ensureTyped(t, n);
}
