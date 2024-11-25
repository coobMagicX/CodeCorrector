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
  } else if (parent.isAssign() && validator.expectNotNullOrUndefined(t, n, 
      getNativeType(OBJECT_TYPE), "No properties on this expression")) {
    // If the object being accessed is assigned to, it's a valid property access
    checkPropertyAccess(childType, property.getString(), t, n);
  } else if (validator.expectNotNullOrUndefined(t, n, childType, 
      "No properties on this expression", getNativeType(OBJECT_TYPE))) {
    // If the object being accessed is not assigned to, perform normal property access validation
    checkPropertyAccess(childType, property.getString(), t, n);
  }
  ensureTyped(t, n);
}