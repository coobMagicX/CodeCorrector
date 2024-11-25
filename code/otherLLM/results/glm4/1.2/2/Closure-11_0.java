private void visitGetProp(NodeTraversal t, Node n, Node parent) {
  // obj.prop or obj.method()
  // Lots of types can appear on the left, a call to a void function can
  // never be on the left. getPropertyType will decide what is acceptable
  // and what isn't.
  Node property = n.getLastChild();
  Node objNode = n.getFirstChild();
  
  if (objNode == null) {
    report(t, property, TypeValidator.NON_NULL_ACCESS_ERROR, "No object to access property", "null");
    return;
  }

  JSType childType = getJSType(objNode);

  // If the type of the object is null or undefined and it's an assignment,
  // we should not allow property access.
  if (validator.expectNotNullOrUndefined(t, objNode, childType, "No properties on this expression", getNativeType(OBJECT_TYPE))) {
    checkPropertyAccess(childType, property.getString(), t, n);
  } else if (childType.isDict()) {
    report(t, property, TypeValidator.ILLEGAL_PROPERTY_ACCESS, "'.'", "dict");
  }

  ensureTyped(t, n);
}