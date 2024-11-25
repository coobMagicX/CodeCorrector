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
  } else if (n.getJSType() != null && parent.isAssign()) {
    // Add a check for null or undefined values
    JSType objType = getJSType(objNode);
    if (objType == null || objType.isEquivalentTo(typeRegistry.getNativeType(UNKNOWN_TYPE))) {
      report(t, n, TypeValidator.UNDEFINED_OBJECT_ACCESS, "'.'");
      return;
    }
  } else if (validator.expectNotNullOrUndefined(t, n, childType,
      "No properties on this expression", getNativeType(OBJECT_TYPE)) ||
             validator.expectNull(t, n)) {
    checkPropertyAccess(childType, property.getString(), t, n);
  }
  ensureTyped(t, n);
}