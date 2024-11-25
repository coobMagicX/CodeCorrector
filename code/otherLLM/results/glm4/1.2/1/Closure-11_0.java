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
    return;
  } else if (validator.expectNotNullOrUndefined(t, n, childType,
      "No properties on this expression", getNativeType(OBJECT_TYPE))) {
    // Check if the object is null or undefined before checking for property access
    JSType objType = validator.getType(t, objNode);
    if (objType == null) {
      reportMissingProperties(true); // Report missing properties when accessing a 'null' or 'undefined' object
    } else {
      checkPropertyAccess(childType, property.getString(), t, n);
    }
  }
  ensureTyped(t, n);
}